package com.example.mqttbasic.ui.scenes.connection

import android.content.Context
import android.net.Uri
import com.example.mqttbasic.base.UiEvent
import com.example.mqttbasic.data.model.database.entities.ConnectionTopic

sealed class ConnectionInfoEvent:UiEvent {
    data class EnterFetchingDbDataScreen(val brokerId: Int): ConnectionInfoEvent()
    data object EnterConnectionScreen: ConnectionInfoEvent()
    data class ImageSelected(val uri:Uri): ConnectionInfoEvent()
    data class TopicFieldChange(val value:String): ConnectionInfoEvent()
    data class SubscribeButtonClicked(val context:Context): ConnectionInfoEvent()
    data class SendMessageToBroker(val topicValue:String, val messageValue:String, val context: Context):ConnectionInfoEvent()
    data class CreateNewTopicButtonClicked(val newTopicName: String, val context: Context): ConnectionInfoEvent()
    data class DeleteTopic(val topic:ConnectionTopic, val context: Context): ConnectionInfoEvent()
    //data class SwitchModalSheetVisibility(val isVisible:Boolean): ConnectionInfoEvent()
}