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
fun MqttFilledTextField(
    modifier: Modifier = Modifier,
    value:String,
    enabled:Boolean = true,
    labelText:String? = null,
    onValueChange:(String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val singleLine = true

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(top = 5.dp)
            .background(
                color = if (enabled) LightPurple else DisableGrey,
                shape = RoundedCornerShape(15.dp)
            ),
        cursorBrush = SolidColor(DarkPurple),
        visualTransformation = VisualTransformation.None,
        textStyle = TextStyle(
            color = if (enabled) DarkPurple else DisableGrey,
            fontSize = 18.sp
        ),
        // internal implementation of the BasicTextField will dispatch focus events
        interactionSource = interactionSource,
        enabled = enabled,
        singleLine = singleLine
    ) {
        TextFieldDefaults.TextFieldDecorationBox(
            value = value,
            label = {
                if (!labelText.isNullOrBlank()) {
                    Text(
                        text = labelText,
                        color = if (enabled) DarkPurple else DisableGrey
                    )
                }
            },
            visualTransformation = VisualTransformation.None,
            innerTextField = it,
            singleLine = singleLine,
            enabled = enabled,
            // same interaction source as the one passed to BasicTextField to read focus state
            // for text field styling
            interactionSource = interactionSource,
            // keep vertical paddings but change the horizontal
            contentPadding =
            if (!labelText.isNullOrBlank())
                TextFieldDefaults.textFieldWithLabelPadding(start = 10.dp, end = 10.dp, top = 8.dp)
            else
                TextFieldDefaults.textFieldWithoutLabelPadding(start = 10.dp, end = 10.dp, top = 8.dp),
            container = {
//                Box(
//                    modifier = Modifier
//                        .border(
//                            2.dp,
//                            if (enabled) DarkPurple else DisableGrey,
//                            RoundedCornerShape(15.dp)
//                        )
//                )
            }
        )
    }

}

@Preview
@Composable
private fun MqttFilledTextFieldPreview() {
    var text by remember { mutableStateOf("Текст") }

    MqttFilledTextField(
        value = text,
        onValueChange = {text = it},
        labelText = "Text",
        enabled = true
    )
}