package com.example.mqttbasic.ui.scenes.createconnection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mqttbasic.ui.components.FloatingButton
import com.example.mqttbasic.ui.components.MqttBasicTextField
import com.example.mqttbasic.ui.components.MqttButton
import com.example.mqttbasic.ui.components.TopBar
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.LightGrey

@Composable
fun CreateConnection(navController:NavHostController, viewModel:CreateConnectionViewModel = hiltViewModel<CreateConnectionViewModel>()) {
    val state = viewModel.uiState.collectAsState().value

    when (state) {
        is CreateConnectionState.MainState -> CreateConnectionContent(state = state, viewModel::invokeEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateConnectionContent(state:CreateConnectionState.MainState, onEvent:(CreateConnectionEvent) -> Unit) {
    Scaffold(
        containerColor = LightGrey,
        topBar = { TopBar(name = "Новое подключение") },
        contentWindowInsets = WindowInsets(10.dp, 10.dp, 10.dp, 20.dp),
        floatingActionButtonPosition = FabPosition.End
    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrimaryBlock( modifier = Modifier.weight(1f), onEvent , state = state)
            AuthBlock(modifier = Modifier.weight(1f), onEvent ,state = state)
            MqttButton(text = "Подключиться") {
                
            }
        }
    }
}

@Composable
private fun PrimaryBlock(modifier:Modifier = Modifier, onEvent:(CreateConnectionEvent) -> Unit, state: CreateConnectionState.MainState) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkGrey, RoundedCornerShape(20.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Обязательные поля:",
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        )
        MqttBasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            value = state.name,
            onValueChange = {value -> onEvent(CreateConnectionEvent.NameFieldChanged(value))},
            labelText = "Название подключения"
        )
        MqttBasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            value = state.address,
            onValueChange = {value -> onEvent(CreateConnectionEvent.AddressFieldChanged(value))},
            labelText = "Адрес"
        )
        MqttBasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            value = state.port.toString(),
            onValueChange = {value -> onEvent(CreateConnectionEvent.PortFieldChanged(value))},
            labelText = "Порт"
        )
    }
}

@Composable
private fun AuthBlock(modifier:Modifier = Modifier, onEvent:(CreateConnectionEvent) -> Unit, state: CreateConnectionState.MainState) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkGrey, RoundedCornerShape(20.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "Авторизация:",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
        }
        MqttBasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            value = state.userName ?: "",
            onValueChange = {value -> onEvent(CreateConnectionEvent.UserNameFieldChanged(value))},
            labelText = "Имя пользователя",
            enabled = state.authChecked
        )
        MqttBasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            value = state.userPassword ?: "",
            onValueChange = {value -> onEvent(CreateConnectionEvent.UserPasswordFieldChanged(value))},
            labelText = "Пароль пользователя",
            enabled = state.authChecked
        )
    }
}

@Preview
@Composable
fun CreateConnectionPreview() {
    val state = CreateConnectionState.MainState()

    CreateConnectionContent(state = state, {})
}