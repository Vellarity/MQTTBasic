package com.example.mqttbasic.ui.scenes.main

sealed class MainPageEvent {
    data object ClickOnConnectionWidget:MainPageEvent()
    data object ClickOnAddConnectionButton:MainPageEvent()
}