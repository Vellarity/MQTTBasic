package com.example.mqttbasic.ui.scenes.connection

import androidx.lifecycle.ViewModel
import com.example.mqttbasic.base.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ConnectionInfoViewModel @Inject constructor(

): ViewModel(){

    var _uiState = MutableStateFlow(ConnectionInfoState.Connecting)
    val uiState = _uiState.asStateFlow()

    fun invokeEvent(event: UiEvent) {
        when (event) {
            is ConnectionInfoEvent -> {}
        }
    }

}