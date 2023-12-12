package com.example.mqttbasic.ui.scenes.createconnection

import com.example.mqttbasic.base.UiEvent

sealed class CreateConnectionEvent:UiEvent {
    data object CreateConnectionClicked: CreateConnectionEvent()
}