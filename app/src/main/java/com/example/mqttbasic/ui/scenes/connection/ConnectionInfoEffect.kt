package com.example.mqttbasic.ui.scenes.connection

import com.example.mqttbasic.base.UiEffect

sealed class ConnectionInfoEffect:UiEffect {
    data class ShowToast(val value:String):ConnectionInfoEffect()
}