package com.example.mqttbasic.ui.scenes.connection

import com.example.mqttbasic.base.UiEvent

sealed class ConnectionInfoEvent:UiEvent {
    data object EnterScreen: ConnectionInfoEvent()
    data class TopicFieldChange(val value:String): ConnectionInfoEvent()
}