package com.example.mqttbasic.ui.scenes.listofbrokers

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.mqttbasic.base.UiEvent
import com.example.mqttbasic.data.model.database.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListOfBrokersViewModel(): ViewModel() {

    private var _uiState:MutableStateFlow<ListOfBrokersState> = MutableStateFlow(ListOfBrokersState.Loading)
    val uiState = _uiState.asStateFlow()

    fun invokeEvent(event:UiEvent) {
        when (val currentState = _uiState.value) {
            is ListOfBrokersState.Loading -> reduceEvent(event, currentState)
            is ListOfBrokersState.Success -> reduceEvent(event, currentState)
            is ListOfBrokersState.Error -> reduceEvent(event, currentState)
            is ListOfBrokersState.NoData -> reduceEvent(event, currentState)
        }
    }

    private fun reduceEvent(event: UiEvent, state:ListOfBrokersState.Loading) {
        when (event) {
            ListOfBrokersEvent.EnterScreen -> getConnectionsList()
        }
    }

    private fun reduceEvent(event:UiEvent, state: ListOfBrokersState.Success) {
        when (event) {
            is ListOfBrokersEvent.CreateConnectionClicked -> {event.navController.navigate("create_connection")}
            is ListOfBrokersEvent.WidgetClicked -> {event.navController.navigate("connection/1")}
        }
    }

    private fun reduceEvent(event:UiEvent, state:ListOfBrokersState.Error) {
        when (event) {
            is ListOfBrokersEvent.CreateConnectionClicked -> {event.navController.navigate("create_connection")}
        }
    }

    private fun reduceEvent(event:UiEvent, state:ListOfBrokersState.NoData) {
        when (event) {
            is ListOfBrokersEvent.CreateConnectionClicked -> {event.navController.navigate("create_connection")}
        }
    }

    private fun getConnectionsList() {

    }

}