package com.example.mqttbasic.ui.components


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.ui.theme.effects.shimmerEffect
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.LightGreen
import com.example.mqttbasic.ui.theme.LightRed
import java.util.UUID

@Composable
fun BrokerInfoWidget(modifier:Modifier = Modifier, broker: Connection, onImageSelected:(Uri) -> Unit) {
    val imageUri = remember { mutableStateOf(broker.imageSource?.toUri()) }
    val context = LocalContext.current
    val photoPicker =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {

                if (!broker.imageSource.isNullOrBlank()){
                    try {
                        context.filesDir.resolve(broker.imageSource).delete()
                    } catch (_:Exception) {}
                }

                val input = context.contentResolver.openInputStream(it) ?: return@rememberLauncherForActivityResult
                val output = context.filesDir.resolve("${broker.name}_${UUID.randomUUID()}_image.jpg")
                input.copyTo(output.outputStream())
                input.close()

                imageUri.value = output.toUri()
                onImageSelected(output.toUri())
            }
        }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(DarkGrey, RoundedCornerShape(20.dp)),
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp, end = 10.dp, bottom = 10.dp, start = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            if (imageUri.value != null)
                AsyncImage(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            photoPicker.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                    model = ImageRequest.Builder(context).data(broker.imageSource).crossfade(true).build(),
                    contentDescription = "123",
                    contentScale = ContentScale.Crop
                )
            else
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(Color.White, RoundedCornerShape(15.dp))
                        .clickable {
                            photoPicker.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет изображения",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(
                        text = "Название: ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                    Text(
                        text = broker.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Row {
                    Text(
                        text = "Адрес: ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                    Text(
                        text = broker.address,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Row {
                    Text(
                        text = "Порт: ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                    Text(
                        text = broker.port.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Row {
                    Text(
                        text = "Пользователь: ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                    Text(
                        text = broker.userName ?: "Не задан",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
        Box(
            modifier= Modifier
                .padding(5.dp)
                .size(20.dp)
                .background( if (broker.establishConnection) LightGreen else LightRed, RoundedCornerShape(100))
                .zIndex(100f)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun BrokerInfoWidgetBlank() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(DarkGrey, RoundedCornerShape(20.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(15.dp))
                .shimmerEffect(tween(3000))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top=4.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(tween(3200, 400))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(tween(3200, 400))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(tween(3200, 400))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(tween(3200, 400))
            )
        }
    }
}

@Preview
@Composable
private fun BrokerInfoWidgetPreview(){
    BrokerInfoWidget(
        broker = Connection(
            name = "Подключение капец длинное",
            address = "lol.com.res",
            port = 1883,
            userName = "userName",
            userPassword = "userPassword",
            actualTopic = "#/abs/topic",
            establishConnection = true
        ),
        onImageSelected = {}
    )

    //BrokerInfoWidgetBlank()
}

