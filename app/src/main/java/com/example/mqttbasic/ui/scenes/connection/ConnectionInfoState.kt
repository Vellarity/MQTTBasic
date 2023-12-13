package com.example.mqttbasic.ui.scenes.connection

import com.example.mqttbasic.base.UiState
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.data.model.database.entities.Message

sealed class ConnectionInfoState:UiState {
    data object FetchingDbData: ConnectionInfoState()
    data class ConnectingToBroker(
        val connectionInfo: Connection,
    ): ConnectionInfoState()
    data class MainState(
        val connectionInfo: Connection,
        val connectionClass: Int, //Заглушка для хранения класса подключения из Paho. Возможно плохой вариант.
        val listOfMessages: List<Message>,
        val topicField: String,
    ): ConnectionInfoState()
}