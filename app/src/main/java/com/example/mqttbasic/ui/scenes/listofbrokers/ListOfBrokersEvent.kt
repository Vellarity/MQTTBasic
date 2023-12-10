package com.example.mqttbasic.ui.scenes.listofbrokers

import androidx.navigation.NavHostController
import com.example.mqttbasic.base.UiEvent

sealed class ListOfBrokersEvent: UiEvent {
    data object EnterScreen: ListOfBrokersEvent()
    data class WidgetClicked(val navController: NavHostController, val widgetId: Int) :
        ListOfBrokersEvent()
    data class CreateConnectionClicked(val navController: NavHostController) : ListOfBrokersEvent()
}
