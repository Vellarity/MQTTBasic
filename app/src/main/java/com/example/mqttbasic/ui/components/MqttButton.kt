package com.example.mqttbasic.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mqttbasic.ui.theme.effects.pulsateClick

@Composable
fun MqttButton(modifier:Modifier = Modifier,text:String,onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .pulsateClick(),
        colors = ButtonDefaults.buttonColors(Color(0xFFD1CEEE)),
        interactionSource = MutableInteractionSource()
    ) {
        Text(
            modifier = Modifier.padding(0.dp, 5.dp),
            text = text,
            color = Color(0xFF4B3755),
            fontSize = 16.sp
        )
    }
}

@Preview
@Composable
private fun MqttButtonPreview() {
    MqttButton(text = "Текст") {
        
    }
}