package com.example.mqttbasic.ui.scenes.connection

import android.net.Uri
import com.example.mqttbasic.base.UiEvent

sealed class ConnectionInfoEvent:UiEvent {
    data class EnterFetchingDbDataScreen(val brokerId: Int): ConnectionInfoEvent()
    data object EnterConnectionScreen: ConnectionInfoEvent()
    data class ImageSelected(val uri:Uri): ConnectionInfoEvent()
    data class TopicFieldChange(val value:String): ConnectionInfoEvent()
    data object SubscribeButtonClicked: ConnectionInfoEvent()
}