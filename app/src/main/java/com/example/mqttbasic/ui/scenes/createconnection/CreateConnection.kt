package com.example.mqttbasic.ui.scenes.createconnection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.mqttbasic.ui.components.MqttButton
import com.example.mqttbasic.ui.components.TopBar
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.LightGrey

@Composable
fun CreateConnection(navController:NavHostController, viewModel:CreateConnectionViewModel = hiltViewModel<CreateConnectionViewModel>()) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateConnectionContent(state:CreateConnectionState) {
    Scaffold(
        containerColor = LightGrey,
        topBar = { TopBar(name = "Новое подключение") },
        contentWindowInsets = WindowInsets(10.dp, 10.dp, 10.dp, 20.dp),
//        floatingActionButton = { FloatingButton() },
        floatingActionButtonPosition = FabPosition.End
    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGrey, RoundedCornerShape(20.dp))
                    .padding(10.dp)
                    .weight(1F)
            ) {
                Text(
                    text = "Обязательные поля:",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGrey, RoundedCornerShape(20.dp))
                    .padding(10.dp)
                    .weight(1F)
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
//                Checkbox(
//                    checked = false,
//                    onCheckedChange = {}
//                ) //Чекбокс контуженный, буду думать

            }
            MqttButton(text = "Подключиться") {
                
            }
        }
    }
}

@Preview
@Composable
fun CreateConnectionPreview() {
    val state = CreateConnectionState.MainState()

    CreateConnectionContent(state = state)
}