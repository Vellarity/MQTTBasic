package com.example.mqttbasic.ui.scenes.connection

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mqttbasic.base.UiEvent
import com.example.mqttbasic.data.model.database.AppDatabase
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.data.model.database.entities.Message
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import com.hivemq.client.mqtt.mqtt3.message.unsubscribe.Mqtt3Unsubscribe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ConnectionInfoViewModel @Inject constructor(
    val db: AppDatabase
): ViewModel(){

    private var _uiState:MutableStateFlow<ConnectionInfoState> = MutableStateFlow(ConnectionInfoState.FetchingDbData)
    val uiState = _uiState.asStateFlow()

    fun invokeEvent(event: ConnectionInfoEvent) {
        when (val currentState = _uiState.value) {
            is ConnectionInfoState.FetchingDbData -> {reduceEvent(event, currentState)}
            is ConnectionInfoState.ConnectingToBroker -> {reduceEvent(event, currentState)}
            is ConnectionInfoState.MainState -> { reduceEvent(event, currentState) }
        }
    }

    private fun reduceEvent(event:ConnectionInfoEvent, state:ConnectionInfoState.FetchingDbData) {
        when (event) {
            is ConnectionInfoEvent.EnterFetchingDbDataScreen -> { getBrokerFromDb(event.brokerId) }
            else -> {}
        }
    }

    private fun reduceEvent(event: ConnectionInfoEvent, state:ConnectionInfoState.ConnectingToBroker) {
        when (event) {
            is ConnectionInfoEvent.EnterConnectionScreen -> { connectToBroker(state) }
            is ConnectionInfoEvent.ImageSelected -> {updateConnectionImage(event.uri, state)}
            else -> {}
        }
    }

    private fun reduceEvent(event: ConnectionInfoEvent, state:ConnectionInfoState.MainState) {
        when (event) {
            is ConnectionInfoEvent.ImageSelected -> {updateConnectionImage(event.uri, state)}
            is ConnectionInfoEvent.TopicFieldChange -> {updateTopicFieldValue(event.value, state)}
            is ConnectionInfoEvent.SubscribeButtonClicked -> { updateSubscription(event.context, state) }
            is ConnectionInfoEvent.SendMessageToBroker -> { sendMessage(event.topicValue, event.messageValue, event.context, state) }
            //is ConnectionInfoEvent.SwitchModalSheetVisibility -> { switchModalSheetVisibility(event.isVisible, state) }
            else -> {}
        }
    }

    private fun getBrokerFromDb(brokerId: Int) {
        viewModelScope.launch {
            val broker:Connection = db.connectionDao().getConnectionById(brokerId)

            _uiState.value = ConnectionInfoState.ConnectingToBroker(broker)
        }
    }

    private fun updateConnectionImage(uri: Uri, state:ConnectionInfoState.ConnectingToBroker) {
        viewModelScope.launch {
            val id = db.connectionDao()
                .insertConnections(state.connectionInfo.copy(imageSource = uri.toString()))

            if (id[0] > 0) {
                _uiState.value =
                    state.copy(connectionInfo = state.connectionInfo.copy(imageSource = uri.toString()))
            }
        }
    }
    private fun updateConnectionImage(uri: Uri, state:ConnectionInfoState.MainState) {
        viewModelScope.launch {
            val id = db.connectionDao()
                .insertConnections(state.connectionInfo.copy(imageSource = uri.toString()))

            if (id[0] > 0) {
                _uiState.value =
                    state.copy(connectionInfo = state.connectionInfo.copy(imageSource = uri.toString()))
            }
        }
    }

    private fun connectToBroker(state: ConnectionInfoState.ConnectingToBroker) {
        viewModelScope.launch {
            val clientBuild = MqttClient.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(state.connectionInfo.address)
                .serverPort(state.connectionInfo.port)
                .useMqttVersion3()
                .buildBlocking()

            var connected:Boolean = false

            var newConnection:Connection? = null

            try {
                clientBuild.connectWith().let {
                    if (!state.connectionInfo.userName.isNullOrBlank() and !state.connectionInfo.userPassword.isNullOrBlank())
                        it.simpleAuth()
                            .username(state.connectionInfo.userName!!)
                            .password(state.connectionInfo.userPassword!!.toByteArray())
                            .applySimpleAuth()
                    else
                        it
                }.send().let { _ -> connected = true }
            } catch (e:Exception) {
                connected = false
                newConnection = state.connectionInfo.copy(establishConnection = false)
                db.connectionDao().insertConnections(newConnection)
                //_uiState.value = state.copy(connectionInfo = newConnection)
            }

            if (connected)
                try {
                    clientBuild.subscribeWith()
                        .topicFilter(state.connectionInfo.actualTopic ?: "#")
                        .send()
                    clientBuild.toAsync().publishes(MqttGlobalPublishFilter.ALL) { publish ->
                        onGetMessage(
                            publish
                        )
                    }
                } catch (e:Exception) {
                    println(e)
                }

            _uiState.value = ConnectionInfoState.MainState(
                connectionClass =  if (connected) clientBuild else null,
                connectionInfo =  newConnection ?: state.connectionInfo,
                listOfMessages = db.messageDao().getMessagesByBrokerId(state.connectionInfo.id!!),
                topicField = state.connectionInfo.actualTopic ?: "#",
                //modalSheetVisibility = false
            )
        }
    }

    private fun onGetMessage(callbackData:Mqtt3Publish) {
        viewModelScope.launch {
            val state = (_uiState.value as ConnectionInfoState.MainState)

            val message = Message(
                payload = callbackData.payloadAsBytes.toString(Charsets.UTF_8),
                topic = callbackData.topic.toString(),
                qos = callbackData.qos.code,
                connectionId = (_uiState.value as ConnectionInfoState.MainState).connectionInfo.id!!,
                timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            )

            val newID = db.messageDao().insertMessage(message)

            val list = state.listOfMessages.toMutableList()
            list.add(0, message.copy(id = newID))

            _uiState.value = state.copy(listOfMessages = list)
        }
    }

    private fun updateTopicFieldValue(value:String, state: ConnectionInfoState.MainState) {
        _uiState.value = state.copy(
            topicField = value
        )
    }

    private fun updateSubscription(context:Context, state: ConnectionInfoState.MainState) {
        viewModelScope.launch {
            try {
                state.connectionClass
                    ?.subscribeWith()
                    ?.topicFilter(state.topicField)
                    ?.send()
                state.connectionClass?.unsubscribe(Mqtt3Unsubscribe.builder().topicFilter(state.connectionInfo.actualTopic!!).build())
                val newConnection = state.connectionInfo.copy(actualTopic = state.topicField)
                db.connectionDao().insertConnections(newConnection)
                _uiState.value = state.copy(connectionInfo = newConnection)
                Toast.makeText(
                    context,
                    "Топик обновлён",
                    Toast.LENGTH_SHORT,
                ).show()
            } catch (e:Exception) {
                Toast.makeText(
                    context,
                    "Не удалось подключиться к новому топику",
                    Toast.LENGTH_SHORT,
                ).show()
                return@launch
            }
        }
    }

    private fun sendMessage(topic:String, message:String, context: Context, state:ConnectionInfoState.MainState) {
        viewModelScope.launch {
            val client = state.connectionClass!!

            try {
                client.publishWith().topic(topic).payload(message.toByteArray()).send()
            } catch (e:Exception) {
                Toast.makeText(
                    context,
                    "Не удалось отправить сообщение",
                    Toast.LENGTH_SHORT,
                ).show()
            }

            return@launch
        }
    }

}