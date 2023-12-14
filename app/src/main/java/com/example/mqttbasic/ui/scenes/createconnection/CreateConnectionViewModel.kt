package com.example.mqttbasic.ui.scenes.createconnection

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.mqttbasic.base.UiEvent
import com.example.mqttbasic.data.model.database.AppDatabase
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.ui.scenes.connection.ConnectionInfoEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class CreateConnectionViewModel @Inject constructor(
    val db: AppDatabase
) :ViewModel() {

    private var _uiState: MutableStateFlow<CreateConnectionState> = MutableStateFlow(CreateConnectionState.MainState())
    val uiState = _uiState.asStateFlow()

    fun invokeEvent(event: UiEvent) {
        when (val currentState = _uiState.value) {
            is CreateConnectionState.MainState -> reduceEvent(event, currentState)
        }
    }

    private fun reduceEvent(event: UiEvent, currentState:CreateConnectionState.MainState) {
        when (event) {
            is CreateConnectionEvent.NameFieldChanged -> {updateNameField(event.value, currentState)}
            is CreateConnectionEvent.AddressFieldChanged -> {updateAddressField(event.value, currentState)}
            is CreateConnectionEvent.PortFieldChanged -> {updatePortField(event.value, currentState)}

            is CreateConnectionEvent.AuthCheckboxClicked -> {updateAuthClicked(event.checked, currentState)}
            is CreateConnectionEvent.UserNameFieldChanged -> {updateUserNameField(event.value, currentState)}
            is CreateConnectionEvent.UserPasswordFieldChanged -> {updateUserPasswordField(event.value, currentState)}

            is CreateConnectionEvent.CreateConnectionClicked -> {tryToConnectAndWriteData(currentState, event.navController, event.context)}
        }
    }

    private fun updateNameField(value: String, state:CreateConnectionState.MainState) {
        _uiState.value = state.copy(
            name = value
        )
    }

    private fun updateAddressField(value: String, state:CreateConnectionState.MainState) {
        _uiState.value = state.copy(
            address = value
        )
    }

    private fun updatePortField(value: String, state:CreateConnectionState.MainState) {
        try {
            val parsedValue = value.toInt(10)
            _uiState.value = state.copy(
                address = value
            )
        } catch (_:Exception) {}
    }

    private fun updateAuthClicked(value:Boolean, state:CreateConnectionState.MainState) {
        _uiState.value = state.copy(
            authChecked = value
        )
    }

    private fun updateUserNameField(value:String, state:CreateConnectionState.MainState) {
        _uiState.value = state.copy(
            userName = value
        )
    }

    private fun updateUserPasswordField(value:String, state:CreateConnectionState.MainState) {
        _uiState.value = state.copy(
            userPassword = value
        )
    }

    private fun tryToConnectAndWriteData(state:CreateConnectionState.MainState, navController:NavHostController, context:Context) {
        viewModelScope.launch {
            var establishConnection = false
            val mqttClient = MqttAndroidClient(null, "tcp://${state.address}:${state.port}", state.name)

            try {
                mqttClient.connect(MqttConnectOptions(), null, object: IMqttActionListener{
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        establishConnection = true
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        establishConnection = false
                    }
                })
            } catch (_:Exception) {}

            var newID:Long = 0
            if (establishConnection) {
                val connection = Connection(
                    name = state.name,
                    address = state.address,
                    port = state.port,
                    userName = if (state.authChecked) state.userName else null,
                    userPassword = if (state.authChecked) state.userPassword else null,
                    establishConnection = establishConnection
                )
                newID = db.connectionDao().insertConnections(connection)[0]
            }

            Toast.makeText(
                context,
                if (state.connected) "Успешное соединение" else "Не удалось подключиться",
                Toast.LENGTH_SHORT,
            ).show()

            if (newID > 0) {
                navController.navigate("list_of_brokers")
            }
        }
    }
}