package com.example.mqttbasic.ui.components

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
fun ConnectionWidget(modifier:Modifier = Modifier ,broker: Connection) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                if (broker.establishConnection) LightGreen else LightRed,
                RoundedCornerShape(15.dp)
            )
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,

        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (broker.imageSource.isNullOrBlank())
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.White, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет изображения",
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        else
            AsyncImage(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(15.dp)),
                model = ImageRequest.Builder(LocalContext.current).data(broker.imageSource).crossfade(true).build(),
                contentDescription = "123",
                contentScale = ContentScale.Crop
            )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = broker.name,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
            )
            Text(
                text = broker.address,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
        }
    }
}

@Preview
@Composable
private fun ConnectionWidgetPreview() {
    ConnectionWidget(broker = Connection(1,"Подключение", "sample.com", 1883, null, null, true, "#"))
}