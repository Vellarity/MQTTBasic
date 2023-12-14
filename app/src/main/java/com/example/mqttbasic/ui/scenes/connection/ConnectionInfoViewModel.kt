package com.example.mqttbasic.ui.scenes.connection

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mqttbasic.base.UiEvent
import com.example.mqttbasic.data.model.database.AppDatabase
import com.example.mqttbasic.data.model.database.entities.Connection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionInfoViewModel @Inject constructor(
    val db: AppDatabase
): ViewModel(){

    private var _uiState:MutableStateFlow<ConnectionInfoState> = MutableStateFlow(ConnectionInfoState.FetchingDbData)
    val uiState = _uiState.asStateFlow()

    fun invokeEvent(event: ConnectionInfoEvent) {
        when (val currentState = _uiState.value) {
            is ConnectionInfoState.FetchingDbData -> {reduceEvent(event, currentState)}
            is ConnectionInfoState.ConnectingToBroker -> {reduceEvent(event, currentState)}
            is ConnectionInfoState.MainState -> {}
        }
    }

    private fun reduceEvent(event:ConnectionInfoEvent, state:ConnectionInfoState.FetchingDbData) {
        when (event) {
            is ConnectionInfoEvent.EnterFetchingDbDataScreen -> { getBrokerFromDb(event.brokerId) }
            else -> {}
        }
    }

    private fun reduceEvent(event: ConnectionInfoEvent, state:ConnectionInfoState.ConnectingToBroker) {
        when (event) {
            is ConnectionInfoEvent.EnterConnectionScreen -> {}
            is ConnectionInfoEvent.ImageSelected -> {updateConnectionImage(event.uri, state)}
            else -> {}
        }
    }

    private fun getBrokerFromDb(brokerId: Int) {
        viewModelScope.launch {
            val broker:Connection = db.connectionDao().getConnectionById(brokerId)

            _uiState.value = ConnectionInfoState.ConnectingToBroker(broker)
        }
    }

    private fun updateConnectionImage(uri: Uri, state:ConnectionInfoState.ConnectingToBroker) {
        viewModelScope.launch {
            val id = db.connectionDao()
                .insertConnections(state.connectionInfo.copy(imageSource = uri.toString()))

            if (id[0] > 0) {
                _uiState.value =
                    state.copy(connectionInfo = state.connectionInfo.copy(imageSource = uri.toString()))
            }
        }
    }

}