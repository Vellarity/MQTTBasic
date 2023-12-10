package com.example.mqttbasic.ui.scenes.main

data class MainPageState(
    val loading: Boolean,
    val connections: List<Int>,
) {
    companion object {
        val initial = MainPageState(
            false,
            listOf()
        )
    }
}
