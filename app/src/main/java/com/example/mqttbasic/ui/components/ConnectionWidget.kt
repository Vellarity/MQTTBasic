package com.example.mqttbasic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.ui.theme.LightGreen
import com.example.mqttbasic.ui.theme.LightRed

//data class Connection(
//    val name:String,
//    val address:String,
//    val port:Int,
//    val userName:String?,
//    val password: String?,
//    val establishConnection:Boolean = false
//)

@Composable
fun ConnectionWidget(modifier:Modifier = Modifier ,connection: Connection) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                if (connection.establishConnection) LightGreen else LightRed,
                RoundedCornerShape(15.dp)
            )
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.White, RoundedCornerShape(10.dp))
        ) {}
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = connection.name,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
            )
            Text(
                text = connection.address,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
        }
    }
}

@Preview
@Composable
private fun ConnectionWidgetPreview() {
    ConnectionWidget(connection = Connection(1,"Подключение", "sample.com", 1883, null, null, true, "#"))
}