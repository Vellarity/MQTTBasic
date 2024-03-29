package com.example.mqttbasic.ui.scenes.connection

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mqttbasic.R
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.data.model.database.entities.Message
import com.example.mqttbasic.ui.components.BrokerInfoWidget
import com.example.mqttbasic.ui.components.BrokerInfoWidgetBlank
import com.example.mqttbasic.ui.components.MessageWidget
import com.example.mqttbasic.ui.components.MqttBasicTextField
import com.example.mqttbasic.ui.components.MqttButton
import com.example.mqttbasic.ui.components.TopBar
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.DarkPurple
import com.example.mqttbasic.ui.theme.LightGrey
import com.example.mqttbasic.ui.theme.LightPurple
import com.example.mqttbasic.ui.theme.effects.shimmerEffect
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import kotlinx.coroutines.launch

@Composable
fun ConnectionInfo(
    brokerId:Int,
    navController:NavHostController,
    viewModel:ConnectionInfoViewModel = hiltViewModel<ConnectionInfoViewModel>()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    ConnectionInfoContent(state = state, brokerId = brokerId, onEvent = viewModel::invokeEvent, { navController.popBackStack() } )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionInfoContent(state:ConnectionInfoState, brokerId: Int, onEvent:(ConnectionInfoEvent) -> Unit, onBackClicked:() -> Unit) {
    Scaffold(
        containerColor = LightGrey,
        topBar = { TopBar(name = "Подключение", onBackClicked) },
        contentWindowInsets = WindowInsets(10.dp, 10.dp, 10.dp, 10.dp),
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (state) {
                is ConnectionInfoState.FetchingDbData -> { FetchingDbBLock( brokerId, onEvent ) }
                is ConnectionInfoState.ConnectingToBroker -> { ConnectingToBrokerBlock( state, onEvent ) }
                is ConnectionInfoState.MainState -> { MainStateBlock(state, onEvent) }
            }
        }
    }
}

@Composable
private fun FetchingDbBLock(brokerId: Int, onEvent:(ConnectionInfoEvent) -> Unit) {
    LaunchedEffect(brokerId){
        onEvent(ConnectionInfoEvent.EnterFetchingDbDataScreen(brokerId))
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        BrokerInfoWidgetBlank()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGrey, RoundedCornerShape(20.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(5) { Box(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(
                    RoundedCornerShape(15.dp)
                )
                .shimmerEffect(tween = tween(3000)), ) }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ConnectingToBrokerBlock(state:ConnectionInfoState.ConnectingToBroker, onEvent:(ConnectionInfoEvent) -> Unit) {
    LaunchedEffect(Unit) {
        onEvent(ConnectionInfoEvent.EnterConnectionScreen)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        BrokerInfoWidget(broker = state.connectionInfo, onImageSelected = {uri -> onEvent(ConnectionInfoEvent.ImageSelected(uri))})
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGrey, RoundedCornerShape(20.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(5) { Box(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(
                    RoundedCornerShape(15.dp)
                )
                .shimmerEffect(tween = tween(3000)), ) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStateBlock(state:ConnectionInfoState.MainState, onEvent: (ConnectionInfoEvent) -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        var topicValue by remember { mutableStateOf(state.topicField) }
        var messageValue by remember { mutableStateOf("") }

        ModalBottomSheet(
            //windowInsets = WindowInsets(5.dp, 0.dp, 5.dp, 0.dp),
            containerColor = LightGrey,
            dragHandle = {BottomSheetDefaults.DragHandle(color = Color.White)},
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Surface(
                color = LightGrey
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MqttBasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(top = 0.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        value = topicValue,
                        labelText = "Топик",
                        onValueChange = { topicValue = it }
                    )
                    MqttBasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .padding(top = 0.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        singleLine = false,
                        value = messageValue,
                        labelText = "Сообщение",
                        onValueChange = {messageValue = it}
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightPurple
                        ),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            onEvent(
                                ConnectionInfoEvent.SendMessageToBroker(
                                    topicValue,
                                    messageValue,
                                    context
                                )
                            );
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.send),
                            contentDescription = null,
                            tint = DarkPurple
                        )
                    }
                    
                }
            }


        }
    }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        BrokerInfoWidget(broker = state.connectionInfo, onImageSelected = {uri -> onEvent(ConnectionInfoEvent.ImageSelected(uri))})
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(DarkGrey, RoundedCornerShape(20.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (state.listOfMessages.isNotEmpty())
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(DarkGrey, RoundedCornerShape(20.dp)),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.listOfMessages, {message -> message.id!!}) {message ->
                        MessageWidget(message = message)
                    }
                }
            else
                Column (
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        text = "Список сообщений пуст"
                    )
                }
            if (state.connectionInfo.establishConnection) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonColors(
                        containerColor = LightPurple,
                        contentColor = DarkPurple,
                        disabledContainerColor = LightGrey,
                        disabledContentColor = DarkGrey
                    ),
                    onClick = { showBottomSheet = true }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.plus),
                        contentDescription = null,
                        tint = DarkPurple
                    )
                }
            }
        }
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
                value = state.topicField,
                labelText = "Топик",
                enabled = state.connectionInfo.establishConnection,
                onValueChange = {
                    value ->
                    onEvent(ConnectionInfoEvent.TopicFieldChange(value))
                }
            )
            if (state.connectionInfo.establishConnection) {
                Button(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightPurple
                    ),
                    shape = RoundedCornerShape(15.dp),
                    onClick = { onEvent(ConnectionInfoEvent.SubscribeButtonClicked(context)) },
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
    }
}



@Preview
@Composable
private fun ConnectionInfoPreview() {
    val state = ConnectionInfoState.MainState(
        connectionInfo = Connection(
            name = "Подключение капец длинное",
            address = "lol.com.res",
            port = 1883,
            userName = "userName",
            userPassword = "userPassword",
            actualTopic = "#/abs/topic",
            establishConnection = false
        ),
        listOfMessages = listOf(),
        topicField = "/asd/conf",
        connectionClass = null,
        //modalSheetVisibility = true
    )

    ConnectionInfoContent(state = state,1, onEvent = {}, {})
}