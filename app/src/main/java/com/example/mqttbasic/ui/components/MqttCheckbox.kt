package com.example.mqttbasic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mqttbasic.R
import com.example.mqttbasic.ui.theme.DarkPurple
import com.example.mqttbasic.ui.theme.LightPurple
import com.example.mqttbasic.ui.theme.pulsateClick

@Composable
fun MqttCheckbox(modifier:Modifier = Modifier ,checked:Boolean, onCheckedChange:(Boolean) -> Unit) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(LightPurple, RoundedCornerShape(10.dp))
            .pulsateClick()
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked)
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.check),
            contentDescription = null,
            tint = DarkPurple
        )
    }
}
@Preview
@Composable
private fun MqttCheckboxPreview() {
    var checked by remember{ mutableStateOf(true) }

    MqttCheckbox(checked = checked, onCheckedChange = {checked = it})
}