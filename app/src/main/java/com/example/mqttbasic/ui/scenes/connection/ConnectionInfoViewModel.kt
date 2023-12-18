package com.example.mqttbasic.ui.scenes.connection

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mqttbasic.base.UiEvent
import com.example.mqttbasic.data.model.database.AppDatabase
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.data.model.database.entities.Message
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
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
            } catch (e:Exception) { connected = false }

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
                connectionClass = clientBuild,
                connectionInfo = state.connectionInfo,
                listOfMessages = db.messageDao().getMessagesByBrokerId(state.connectionInfo.id!!),
                topicField = state.connectionInfo.actualTopic ?: "#"
            )
        }
    }

    private fun onGetMessage(callbackData:Mqtt3Publish) {
        viewModelScope.launch {
            val state = (_uiState.value as ConnectionInfoState.MainState)

            val message = Message(
                id = (_uiState.value as ConnectionInfoState.MainState).listOfMessages.size.toLong(),
                payload = callbackData.payloadAsBytes.toString(Charsets.UTF_8),
                topic = callbackData.topic.toString(),
                qos = callbackData.qos.code,
                connectionId = (_uiState.value as ConnectionInfoState.MainState).connectionInfo.id!!,
                timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            )

            db.messageDao().insertMessage(message)

            val list = state.listOfMessages.toMutableList()
            list.add(0,message)

            _uiState.value = state.copy(listOfMessages = list)
        }
    }

    private fun updateTopicFieldValue(value:String, state: ConnectionInfoState.MainState) {
        _uiState.value = state.copy(
            topicField = value
        )
    }
}