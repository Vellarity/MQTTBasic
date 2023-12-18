package com.example.mqttbasic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mqttbasic.data.model.database.entities.Message
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.DarkPurple
import com.example.mqttbasic.ui.theme.LightPurple
import java.text.DateFormat
import java.text.DateFormat.getDateTimeInstance
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun MessageWidget(modifier:Modifier = Modifier, message: Message) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LightPurple, RoundedCornerShape(15.dp))
            .padding(10.dp, 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "Данные:",
                fontSize = 14.sp,
                color = DarkPurple,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = message.payload,
                fontSize = 14.sp,
                color = DarkPurple,
                fontWeight = FontWeight.Medium
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "Время:",
                fontSize = 14.sp,
                color = DarkPurple,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = message.timestamp.let {
                    val actual = OffsetDateTime.parse(message.timestamp, DateTimeFormatter.ISO_DATE_TIME)
                    actual.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss"))
                },
                fontSize = 14.sp,
                color = DarkPurple,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "QoS:",
                fontSize = 14.sp,
                color = DarkPurple,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = message.qos.toString(),
                fontSize = 14.sp,
                color = DarkPurple,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Spacer(modifier=Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(DarkGrey, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.topic,
                    fontSize = 16.sp,
                    color = LightPurple,
                    fontWeight = FontWeight.Medium
                )
            }
        }


    }
}

@Preview
@Composable
private fun MessageWidgetPreview() {
    val message = Message(
        id=1,
        payload = "{\n\t\"data\":\"Тест\" \n}",
        connectionId = 1,
        qos = 1,
        topic = "/asd/conf",
        timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    )

    MessageWidget(message = message)
}