package com.example.mqttbasic.ui.components

import android.graphics.Color
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mqttbasic.ui.theme.LightGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MqttBasicTextField(
    value:String,
    onValueChange:(String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = MutableInteractionSource()

}

@Preview
@Composable
fun MqttBasicTextFieldPreview() {
    MqttBasicTextField(value = "", onValueChange = {})
}