package com.example.mqttbasic.ui.scenes.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mqttbasic.R

import com.example.mqttbasic.ui.components.MqttButton
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.paralines

@Composable
fun MainPage(navController: NavHostController, viewModel: MainPageViewModel = MainPageViewModel()) {
    Column(
        modifier = Modifier
            .padding(10.dp, 40.dp, 10.dp, 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGrey, RoundedCornerShape(20.dp))
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(10.dp, 0.dp),
                text = "MQTT",
                color = Color.White,
                fontFamily = paralines,
                fontSize = 96.sp
            )
            Text(
                modifier = Modifier.padding(10.dp, 0.dp),
                text = "BASIC",
                color = Color.White,
                fontFamily = paralines,
                fontSize = 48.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGrey, RoundedCornerShape(20.dp)),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            Alignment.CenterHorizontally
        ) {
            MqttButton(
                modifier = Modifier.padding(10.dp, 0.dp),
                text = stringResource(R.string.list_of_brokers),
                onClick = {navController.navigate("list_of_brokers")}
            )
            MqttButton(
                modifier = Modifier.padding(10.dp, 0.dp),
                text = stringResource(R.string.create_connection),
                onClick = {navController.navigate("create_connection")}
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    MainPage(rememberNavController())
}