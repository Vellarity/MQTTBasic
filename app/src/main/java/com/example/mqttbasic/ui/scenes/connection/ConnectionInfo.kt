package com.example.mqttbasic.ui.scenes.connection

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mqttbasic.ui.components.BrokerInfoWidget
import com.example.mqttbasic.ui.components.BrokerInfoWidgetBlank
import com.example.mqttbasic.ui.components.TopBar
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.LightGrey
import com.example.mqttbasic.ui.theme.effects.shimmerEffect

@Composable
fun ConnectionInfo(
    brokerId:Int,
    navController:NavHostController,
    viewModel:ConnectionInfoViewModel = hiltViewModel<ConnectionInfoViewModel>()
) {
    BackHandler { navController.navigate("list_of_brokers") }

    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    ConnectionInfoContent(state = state, brokerId = brokerId, onEvent = viewModel::invokeEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionInfoContent(state:ConnectionInfoState, brokerId: Int, onEvent:(ConnectionInfoEvent) -> Unit) {
    Scaffold(
        containerColor = LightGrey,
        topBar = { TopBar(name = "Подключение") },
        contentWindowInsets = WindowInsets(10.dp, 10.dp, 10.dp, 20.dp),
    ) {innerPadding ->
        Column(
            modifier= Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (state) {
                is ConnectionInfoState.FetchingDbData -> { FetchingDbBLock( brokerId, onEvent ) }
                is ConnectionInfoState.ConnectingToBroker -> { ConnectingToBrokerBlock( state, onEvent ) }
                is ConnectionInfoState.MainState -> {}
            }
        }
    }
}

@Composable
private fun FetchingDbBLock(brokerId: Int, onEvent:(ConnectionInfoEvent) -> Unit) {
    LaunchedEffect(brokerId){
        onEvent(ConnectionInfoEvent.EnterFetchingDbDataScreen(brokerId))
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        BrokerInfoWidgetBlank()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGrey, RoundedCornerShape(20.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(5) { Box(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(
                    RoundedCornerShape(15.dp)
                )
                .shimmerEffect(tween = tween(3000)), ) }
        }
    }
}

@Composable
private fun ConnectingToBrokerBlock(state:ConnectionInfoState.ConnectingToBroker, onEvent:(ConnectionInfoEvent) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        BrokerInfoWidget(broker = state.connectionInfo, onImageSelected = {uri -> onEvent(ConnectionInfoEvent.ImageSelected(uri))})
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGrey, RoundedCornerShape(20.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(5) { Box(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(
                    RoundedCornerShape(15.dp)
                )
                .shimmerEffect(tween = tween(3000)), ) }
        }
    }
}

@Preview
@Composable
private fun ConnectionInfoPreview() {
    val state = ConnectionInfoState.FetchingDbData

    ConnectionInfoContent(state = state,1, onEvent = {})
}