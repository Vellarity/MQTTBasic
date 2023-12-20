package com.example.mqttbasic.ui.scenes.createconnection

import com.example.mqttbasic.base.UiState

sealed class CreateConnectionState: UiState {
    data class MainState(
        val name:String = "",
        val address:String = "",
        val port:Int = 1883,
        val authChecked:Boolean = false,
        val userName:String? = "",
        val userPassword:String? = "",
        val connected:Boolean = false
    ):CreateConnectionState()
}

/*
    data class MainState(
        val name:String = "test",
        val address:String = "m5.wqtt.ru",
        val port:Int = 9772,
        val authChecked:Boolean = true,
        val userName:String? = "u_AJGK3C",
        val userPassword:String? = "3uzQzpcZ",
        val connected:Boolean = false
    ):CreateConnectionState()
*/