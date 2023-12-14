package com.example.mqttbasic.ui.scenes.createconnection

import androidx.navigation.NavHostController
import com.example.mqttbasic.base.UiEvent

sealed class CreateConnectionEvent:UiEvent {
    data class CreateConnectionClicked(val navController:NavHostController): CreateConnectionEvent()

    data class NameFieldChanged(val value:String): CreateConnectionEvent()
    data class AddressFieldChanged(val value:String): CreateConnectionEvent()
    data class PortFieldChanged(val value:String): CreateConnectionEvent()

    data class AuthCheckboxClicked(val checked:Boolean): CreateConnectionEvent()
    data class UserNameFieldChanged(val value:String): CreateConnectionEvent()
    data class UserPasswordFieldChanged(val value:String): CreateConnectionEvent()
}