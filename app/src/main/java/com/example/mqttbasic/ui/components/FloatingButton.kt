package com.example.mqttbasic.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mqttbasic.R

@Composable
fun FloatingButton() {
    FloatingActionButton(
        modifier = Modifier
            .padding(end = 8.dp)
            .size(60.dp),
        onClick = { /*TODO*/ }
    ) {
        Image(
            modifier = Modifier
                .size(32.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.plus),
            contentDescription = ""
        )
    }

}

@Preview
@Composable
private fun FloatingButtonPreview() {
    FloatingButton()
}