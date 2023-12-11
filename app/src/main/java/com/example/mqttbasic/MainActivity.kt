package com.example.mqttbasic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.mqttbasic.data.model.database.AppDatabase
import com.example.mqttbasic.ui.theme.LightGrey
import com.example.mqttbasic.ui.theme.MQTTBasicTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        val db = AppDatabase.getInstance(applicationContext)
//
//        runBlocking {
//            launch {
//                db.connectionDao().getConnections()
//            }
//        }

        setContent {
            MQTTBasicTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color  = LightGrey
                ) {
                    MqttBasicApp()
                }
            }
        }
    }
}

@Composable
fun MqttBasicApp(){
    val navHostController = rememberNavController()
    MqttBasicNavHost(navController = navHostController)
}

@Preview(showBackground = true)
@Composable
fun MqttAppPreview() {
    MQTTBasicTheme() {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color  = LightGrey
        ) {
            MqttBasicApp()
        }
    }
}