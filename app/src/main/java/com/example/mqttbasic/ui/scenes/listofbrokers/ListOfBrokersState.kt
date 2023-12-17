package com.example.mqttbasic.ui.scenes.listofbrokers

import com.example.mqttbasic.base.UiState
import com.example.mqttbasic.data.model.database.entities.Connection
import kotlinx.coroutines.flow.Flow

sealed class ListOfBrokersState:UiState {
    data object Loading: ListOfBrokersState()
    data object Error: ListOfBrokersState()
    data class Success(val listOfBrokers: List<Connection>): ListOfBrokersState()
    data object NoData: ListOfBrokersState()
}
