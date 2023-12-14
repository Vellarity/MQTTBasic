package com.example.mqttbasic.ui.scenes.createconnection

import com.example.mqttbasic.base.UiState

sealed class CreateConnectionState: UiState {
    data class MainState(
        val name:String = "",
        val address:String = "",
        val port:Int = 1883,
        val authChecked:Boolean = false,
        val userName:String? = null,
        val userPassword:String? = null,
        val connected:Boolean = false
    ):CreateConnectionState()
}