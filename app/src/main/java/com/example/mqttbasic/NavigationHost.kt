package com.example.mqttbasic

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mqttbasic.ui.scenes.connection.ConnectionInfo
import com.example.mqttbasic.ui.scenes.connection.ConnectionInfoViewModel
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
        composable("connectionInfo/{brokerId}", arguments = listOf(navArgument("brokerId") { type = NavType.IntType})) {backStackEntry -> ConnectionInfo(backStackEntry.arguments!!.getInt("brokerId"), navController, hiltViewModel<ConnectionInfoViewModel>())}
    }
}