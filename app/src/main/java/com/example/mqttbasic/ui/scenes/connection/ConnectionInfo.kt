package com.example.mqttbasic.ui.scenes.connection

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun ConnectionInfo(
    navController:NavHostController,
    viewModel:ConnectionInfoViewModel = hiltViewModel<ConnectionInfoViewModel>()
) {

    val state = viewModel.uiState.collectAsState().value
}