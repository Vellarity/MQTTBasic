package com.example.mqttbasic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.mqttbasic.R
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.DarkPurple
import com.example.mqttbasic.ui.theme.LightPurple

@Composable
fun CreateNewTopicWidget(
    onButtonClicked: (String) -> Unit
) {
    var topicName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .background(DarkGrey, RoundedCornerShape(20.dp))
            .padding(10.dp, 5.dp, 10.dp, 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MqttBasicTextField(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(top = 0.dp)
                .clip(RoundedCornerShape(15.dp)),
            value = topicName,
            labelText = "Канал",
            onValueChange = {
                topicName = it
            }
        )
        Button(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightPurple
            ),
            shape = RoundedCornerShape(15.dp),
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                onButtonClicked(topicName)
            },
            contentPadding = PaddingValues()
        ) {
            Icon(
                modifier = Modifier
                    .size(40.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.link),
                contentDescription = null,
                tint = DarkPurple
            )
        }

    }
}