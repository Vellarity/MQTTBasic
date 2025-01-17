package com.example.mqttbasic.ui.scenes.connection

import com.example.mqttbasic.base.UiState
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.data.model.database.entities.ConnectionTopic
import com.example.mqttbasic.data.model.database.entities.Message
import com.example.mqttbasic.data.model.database.tuples.ConnectionWithTopics
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient

sealed class ConnectionInfoState:UiState {
    data object FetchingDbData: ConnectionInfoState()
    data class ConnectingToBroker(
        val connectionInfo: Connection,
        val connectionTopics: MutableList<ConnectionTopic>
    ): ConnectionInfoState()
    data class MainState(
        val connectionInfo: Connection,
        val connectionTopics: MutableList<ConnectionTopic>,
        val connectionClass: Mqtt3BlockingClient?,
        val listOfMessages: List<Message>,
        val topicField: String,
        //val modalSheetVisibility:Boolean,
    ): ConnectionInfoState()
}