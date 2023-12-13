package com.example.mqttbasic.ui.scenes.createconnection

import androidx.lifecycle.ViewModel
import com.example.mqttbasic.base.UiEvent
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.ui.scenes.listofbrokers.ListOfBrokersEvent
import com.example.mqttbasic.ui.scenes.listofbrokers.ListOfBrokersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.Console
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CreateConnectionViewModel @Inject constructor(

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

            is CreateConnectionEvent.CreateConnectionClicked -> {tryToConnectAndWriteData(currentState)}
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

    private fun tryToConnectAndWriteData(state:CreateConnectionState.MainState) {
        var connection = Connection(
            name = state.name,
            address = state.address,
            port = state.port,
            userName = if (state.authChecked) state.userName else null,
            userPassword = if (state.authChecked) state.userPassword else null,
            establishConnection = false
        )
    }
}