package com.example.mqttbasic.ui.components

import android.widget.Button
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mqttbasic.R
import com.example.mqttbasic.data.model.database.entities.ConnectionTopic
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.DarkPurple
import com.example.mqttbasic.ui.theme.LightPurple
import com.example.mqttbasic.ui.theme.LightRed
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun TopicWidget(
    modifier: Modifier = Modifier,
    topic: ConnectionTopic,
    onDelete: () -> Unit
) {
    val menuWidth: Dp = 60.dp
    val offsetX = remember { Animatable(0f) }
    val menuWidthPx = with(LocalDensity.current) { menuWidth.toPx() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(LightPurple, RoundedCornerShape(15.dp))
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (offsetX.value > menuWidthPx / 2) {
                                offsetX.animateTo(menuWidthPx)
                            } else if (offsetX.value < -menuWidthPx / 2) {
                                offsetX.animateTo(-menuWidthPx)
                            } else {
                                offsetX.animateTo(0f)
                            }
                        }
                    },
                    onHorizontalDrag = { _, delta ->
                        val newValue = (offsetX.value + delta).coerceIn(-menuWidthPx, 0f)
                        scope.launch { offsetX.snapTo(newValue) }
                    }
                )
            }
    ){
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .width(menuWidth+20.dp)
                    .fillMaxHeight()
                    .background(LightRed, RoundedCornerShape(0.dp, 15.dp, 15.dp, 0.dp))
                    .padding(horizontal = 15.dp)
                    .clickable {
                        scope.launch { offsetX.animateTo(0f) }
                        onDelete()
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.trash),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .background(LightPurple, RoundedCornerShape(15.dp))
                .padding(5.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp, 0.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                text = topic.name,
                color = DarkGrey
            )
        }

    }
}

@Composable
private fun SwipeableList(items: List<ConnectionTopic>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items = items, key = {item -> item.id}) { item ->
            TopicWidget(
                modifier = Modifier,
                topic = item,
                onDelete = {}
            )
        }
    }
}

@Preview
@Composable
private fun TopicWidgetPreview() {
    SwipeableList(items = listOf(
        ConnectionTopic(id = 0, name = "/1", connectionId = 0),
        ConnectionTopic(id = 1, name = "/2", connectionId = 0),
        ConnectionTopic(id = 2, name = "/3", connectionId = 0),
        ConnectionTopic(id = 3, name = "/4", connectionId = 0),
    ))
    //TopicWidget(topic = ConnectionTopic(id = 0, connectionId = 0, name = "/"))
}