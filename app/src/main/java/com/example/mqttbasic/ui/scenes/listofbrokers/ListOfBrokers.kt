package com.example.mqttbasic.ui.scenes.listofbrokers

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mqttbasic.ui.components.ConnectionWidget
import com.example.mqttbasic.ui.components.FloatingButton
import com.example.mqttbasic.ui.components.TopBar
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.LightGrey

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfBrokers(navController:NavHostController, viewModel: ListOfBrokersViewModel = hiltViewModel<ListOfBrokersViewModel>()) {
    val state = viewModel.uiState.collectAsState().value

    Scaffold(
        containerColor = LightGrey,
        topBar = {TopBar(name = "Подключения")},
        contentWindowInsets = WindowInsets(10.dp, 10.dp, 10.dp, 20.dp),
        floatingActionButton = {FloatingButton()},
        floatingActionButtonPosition = FabPosition.End
    ) {
        innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(DarkGrey, RoundedCornerShape(20.dp))
                .fillMaxSize()
        ) {
            when(state) {
                is ListOfBrokersState.Loading -> { LoadingBlock() }
                is ListOfBrokersState.Success -> { DataBlock(state = state) }
                is ListOfBrokersState.NoData -> { LoadingBlock() }
                is ListOfBrokersState.Error -> { ErrorBlock() }
            }
        }
    }
}

@Composable
fun LoadingBlock() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Нет данных",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun DataBlock(state:ListOfBrokersState.Success) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(state.listOfBrokers) {broker ->
            ConnectionWidget(connection = broker)
        }
    }
}

@Composable
fun ErrorBlock() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Произошла непредвиденная ошибка",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ListOfBrokersPagePreview() {
    val state:ListOfBrokersState = ListOfBrokersState.Error

    Scaffold(
        containerColor = LightGrey,
        topBar = {TopBar(name = "Подключения")},
        contentWindowInsets = WindowInsets(10.dp, 10.dp, 10.dp, 20.dp),
        floatingActionButton = {FloatingButton()},
        floatingActionButtonPosition = FabPosition.End
    ) {
            innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(DarkGrey, RoundedCornerShape(20.dp))
                .fillMaxSize()
        ) {
            when(state) {
                is ListOfBrokersState.Loading -> { LoadingBlock() }
                is ListOfBrokersState.Success -> { DataBlock(state = state) }
                is ListOfBrokersState.NoData -> { LoadingBlock() }
                is ListOfBrokersState.Error -> { ErrorBlock() }
            }
        }
    }
}