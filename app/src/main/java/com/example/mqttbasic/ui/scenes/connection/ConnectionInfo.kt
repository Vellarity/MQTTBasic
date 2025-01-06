package com.example.mqttbasic.ui.scenes.connection

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.mqttbasic.R
import com.example.mqttbasic.data.model.database.entities.Connection
import com.example.mqttbasic.data.model.database.entities.ConnectionTopic
import com.example.mqttbasic.ui.components.BrokerInfoWidget
import com.example.mqttbasic.ui.components.BrokerInfoWidgetBlank
import com.example.mqttbasic.ui.components.CreateNewTopicWidget
import com.example.mqttbasic.ui.components.MessageWidget
import com.example.mqttbasic.ui.components.MqttBasicTextField
import com.example.mqttbasic.ui.components.TopBar
import com.example.mqttbasic.ui.components.TopicWidget
import com.example.mqttbasic.ui.theme.DarkGrey
import com.example.mqttbasic.ui.theme.DarkPurple
import com.example.mqttbasic.ui.theme.DisableGrey
import com.example.mqttbasic.ui.theme.LightGrey
import com.example.mqttbasic.ui.theme.LightPurple
import com.example.mqttbasic.ui.theme.effects.shimmerEffect
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
                is ConnectionInfoState.FetchingDbData -> { FetchingDbBlock( brokerId, onEvent ) }
                is ConnectionInfoState.ConnectingToBroker -> { ConnectingToBrokerBlock( state, onEvent ) }
                is ConnectionInfoState.MainState -> { MainStateBlock(state, onEvent) }
            }
        }
    }
}

@Composable
private fun FetchingDbBlock(brokerId: Int, onEvent:(ConnectionInfoEvent) -> Unit) {
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
    val topicSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showMessageBottomSheet by remember { mutableStateOf(false) }
    var showTopicListBottomSheet by remember { mutableStateOf(false) } // TODO: Лист топиков

    if (showMessageBottomSheet) {
        MessageBottomSheet(
            sheetState = sheetState,
            onEvent = onEvent,
            onDismissRequest = {showMessageBottomSheet = false})
    }
    if (showTopicListBottomSheet) {
        TopicsBottomSheet(
            sheetState = topicSheetState,
            onEvent = onEvent,
            onDismissRequest = { showTopicListBottomSheet = false },
            listOfTopics = state.connectionTopics)
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
        }
        if (state.connectionInfo.establishConnection) {
            Row(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
                    .background(DarkGrey, RoundedCornerShape(20.dp))
                    .padding(10.dp, 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonColors(LightPurple, DarkPurple, LightGrey, DisableGrey),
                    shape = RoundedCornerShape(15.dp),
                    contentPadding = PaddingValues(),
                    onClick = { showTopicListBottomSheet = true }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(80.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.link),
                        contentDescription = null,
                        tint = DarkPurple
                    )
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonColors(LightPurple, DarkPurple, LightGrey, DisableGrey),
                    shape = RoundedCornerShape(15.dp),
                    onClick = { showMessageBottomSheet = true }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBottomSheet(
    sheetState:SheetState,
    onDismissRequest:() -> Unit,
    onEvent: (ConnectionInfoEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var topicValue by remember { mutableStateOf("/") }
    var messageValue by remember { mutableStateOf("") }

    ModalBottomSheet(
        containerColor = LightGrey,
        dragHandle = {BottomSheetDefaults.DragHandle(color = Color.White)},
        onDismissRequest = onDismissRequest,
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
                                onDismissRequest()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsBottomSheet(
    sheetState:SheetState,
    onDismissRequest: () -> Unit,
    listOfTopics: MutableList<ConnectionTopic>,
    onEvent: (ConnectionInfoEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val lazyListState = rememberLazyListState()

    /* TODO: РАЗОБРАТЬСЯ С ЗАКРЫТИЕМ ПРИ ДОЛИСТЫВАНИИ */
    val isAtTop = remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
        }
    }
    
    ModalBottomSheet(
        containerColor = LightGrey,
        dragHandle = {BottomSheetDefaults.DragHandle(color=Color.White)},
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = BottomSheetDefaults.ExpandedShape,
    ) {
        Surface(color = LightGrey) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (listOfTopics.size > 0)
                    LazyColumn(
                        modifier = Modifier
                            .defaultMinSize(minHeight = 100.dp)
                            .fillMaxHeight(0.7f)
                            .background(DarkGrey, RoundedCornerShape(15.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        state = lazyListState
                    ){
                        items(listOfTopics, {topic -> topic.id!!}) {topic ->
                            TopicWidget(topic = topic, modifier = Modifier, onDelete = {})
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
                            text = "Список каналов пуст"
                        )
                    }
                CreateNewTopicWidget(onButtonClicked = {
                    topicName ->
                    onEvent(ConnectionInfoEvent.CreateNewTopicButtonClicked(topicName, context))
                })
            }
        }
    }
}

@Preview
@Composable
private fun ConnectionInfoPreview() {
    val state = ConnectionInfoState.MainState(
        connectionInfo = Connection(
            id = 0,
            name = "Подключение капец длинное",
            address = "lol.com.res",
            port = 1883,
            userName = "userName",
            userPassword = "userPassword",
            actualTopic = "#/abs/topic",
            establishConnection = true
        ),
        connectionTopics = mutableListOf(
            ConnectionTopic(name="/1", connectionId = 0, id = 0),
            ConnectionTopic(name="/2", connectionId = 0, id = 1),
            ConnectionTopic(name="/3", connectionId = 0, id = 2),
            ConnectionTopic(name="/4", connectionId = 0, id = 3),
            ConnectionTopic(name="/5", connectionId = 0, id = 4),
            ConnectionTopic(name="/1", connectionId = 0, id = 5),
            ConnectionTopic(name="/2", connectionId = 0, id = 6),
            ConnectionTopic(name="/3", connectionId = 0, id = 7),
            ConnectionTopic(name="/4", connectionId = 0, id = 8),
            ConnectionTopic(name="/5", connectionId = 0, id = 9),
            ConnectionTopic(name="/1", connectionId = 0, id = 10),
            ConnectionTopic(name="/2", connectionId = 0, id = 11),
            ConnectionTopic(name="/3", connectionId = 0, id = 12),
            ConnectionTopic(name="/4", connectionId = 0, id = 13),
            ConnectionTopic(name="/5", connectionId = 0, id = 14),
        ),
        listOfMessages = listOf(),
        topicField = "/asd/conf",
        connectionClass = null,
        //modalSheetVisibility = true
    )

    ConnectionInfoContent(state = state,1, onEvent = {}, {})
}
