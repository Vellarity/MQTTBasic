package com.example.mqttbasic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mqttbasic.ui.theme.DarkPurple
import com.example.mqttbasic.ui.theme.DisableGrey
import com.example.mqttbasic.ui.theme.LightGrey
import com.example.mqttbasic.ui.theme.LightPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MqttBasicTextField(
    modifier: Modifier = Modifier,
    value:String,
    enabled:Boolean = true,
    singleLine:Boolean = true,
    labelText:String? = null,
    onValueChange:(String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(top = 5.dp),
        cursorBrush = SolidColor(LightPurple),
        visualTransformation = VisualTransformation.None,
        textStyle = TextStyle(
            color = if (enabled) LightPurple else DisableGrey,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        ),
        // internal implementation of the BasicTextField will dispatch focus events
        interactionSource = interactionSource,
        enabled = enabled,
        singleLine = singleLine
    ) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = it,
            enabled = enabled,
            // same interaction source as the one passed to BasicTextField to read focus state
            // for text field styling
            singleLine = singleLine,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            // keep vertical paddings but change the horizontal
            label = {
                if (!labelText.isNullOrBlank()) {
                    Text(
                        text = labelText,
                        color = if (enabled) LightPurple else DisableGrey,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(),
            contentPadding = if (!labelText.isNullOrBlank())
                TextFieldDefaults.contentPaddingWithLabel(
                    start = 20.dp,
                    top = 5.dp,
                    end = 10.dp,
                )
            else
                TextFieldDefaults.contentPaddingWithoutLabel(
                    start = 20.dp,
                    top = 5.dp,
                    end = 10.dp,
                ),
            container = {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .border(
                            2.dp,
                            if (enabled) LightPurple else DisableGrey,
                            RoundedCornerShape(15.dp)
                        )
                )
            },
        )
    }

}

@Preview
@Composable
private fun MqttBasicTextFieldPreview() {
    var text by remember { mutableStateOf("Текст") }

    MqttBasicTextField(
        value = text,
        onValueChange = {text = it},
        labelText = "Лейбл",
        enabled = true
    )
}