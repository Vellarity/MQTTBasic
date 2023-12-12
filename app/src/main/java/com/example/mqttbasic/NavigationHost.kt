package com.example.mqttbasic

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mqttbasic.ui.scenes.createconnection.CreateConnection
import com.example.mqttbasic.ui.scenes.createconnection.CreateConnectionViewModel
import com.example.mqttbasic.ui.scenes.listofbrokers.ListOfBrokers
import com.example.mqttbasic.ui.scenes.listofbrokers.ListOfBrokersViewModel
import com.example.mqttbasic.ui.scenes.main.MainPage


@Composable
fun MqttBasicNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main"){
        composable("main") { MainPage(navController) }
        composable("list_of_brokers") { ListOfBrokers(navController, hiltViewModel<ListOfBrokersViewModel>()) }
        composable("create_connection"){ CreateConnection(navController, hiltViewModel<CreateConnectionViewModel>()) }
    }
}