package com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails

import android.icu.util.Calendar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.isu.common.customcomposables.CardDetailComponent
import com.isu.common.customcomposables.CardDetailTitleComponent
import com.isu.common.customcomposables.CloseTicketDialog
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.OpenTicketDialog
import com.isu.common.customcomposables.StatusColorComponent
import com.isu.common.customcomposables.StatusComponent
import com.isu.common.customcomposables.TicketData
import com.isu.common.customcomposables.TicketStatus
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.ui.theme.ActiveTextBackground
import com.isu.common.ui.theme.TextGreen
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.ticketTextLightColor
import com.isu.common.utils.FontProvider
import com.isu.common.utils.ZonedDateFormatter
import com.isu.profile.R
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.FilterSelection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Represents the type of message in a chat.
 */
sealed interface MessageType {
    data object User : MessageType
    data object CustomerSupport : MessageType
}

/**
 * Holds details of a message.
 */
data class MessageDetails(
    val name: String = "",
    val messageType: MessageType = MessageType.CustomerSupport,
    val message: String = "",
    val time: String = "",
)



/**
 * Returns the current date and time formatted according to the given pattern.
 */
fun getDate(pattern: String = "dd MMM, yyyy hh:mm:ss a"): String? {
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(calendar.time)
}

/**
 * Composable function for displaying the Ticket Details Screen.
 */
@Composable
fun TicketDetailsScreen(state: TicketDetailUIData, onEvent: (CommonScreenEvents) -> Unit) {
    val scrollState = rememberScrollState()
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val oldMessageHeight = remember { mutableStateOf(0) }
    val message = state.userComment
    val ticketData = state
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val showClosedDialog = remember { mutableStateOf(false) }
    val showOpenDialog = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        LoadingErrorEvent.helper.loadingErrorEvent.collectLatest {
            isLoading.value = true
        }
    }

    LaunchedEffect(Unit) {
        onEvent(CommonScreenEvents.ClearFields)
        onEvent(CommonScreenEvents.CallApi<Any>(TicketDetailsApiType.FetchTicketComments, additionalInfo = ticketData.ticketId) {})
    }

    Scaffold(
        topBar = { CustomProfileTopBar(stringResource(R.string.ticket_id, state.ticketId)) },
        containerColor = Color.White
    ) { innerPadding ->
        if (showClosedDialog.value) {
            CloseTicketDialog(
                TicketData(
                    requestTitle = state.requestTitle,
                    category = state.category,
                    status = state.status,
                    description = state.description,
                    priority = state.priority,
                    date = state.date
                ),
                dismiss = { showClosedDialog.value = false },
                proceed = {
                    onEvent(
                        CommonScreenEvents.OnClick<TicketData>(
                            TicketDetailsClickables.TicketStatus,
                            TicketData(
                                ticketData.ticketId,
                                status = ticketData.status,
                                category = ticketData.category,
                                requestTitle = ticketData.requestTitle,
                                priority = ticketData.priority,
                                description = ticketData.description,
                                date = ticketData.date
                            ),
                            onComplete = { showClosedDialog.value = false }
                        )
                    )
                }
            )
        }
        if (showOpenDialog.value) {
            OpenTicketDialog(
                ticketData = TicketData(
                    ticketId = ticketData.ticketId,
                    category = ticketData.category,
                    requestTitle = ticketData.requestTitle,
                    status = ticketData.status,
                    description = state.description,
                    priority = state.priority,
                    date = ticketData.date
                ),
                dismiss = { showOpenDialog.value = false },
                proceed = {
                    onEvent(
                        CommonScreenEvents.OnClick<TicketData>(
                            TicketDetailsClickables.TicketStatus,
                            TicketData(
                                ticketData.ticketId,
                                status = ticketData.status,
                                category = ticketData.category,
                                requestTitle = ticketData.requestTitle,
                                priority = ticketData.priority,
                                description = ticketData.description,
                                date = ticketData.date
                            ),
                            onComplete = { showOpenDialog.value = false }
                        )
                    )
                }
            )
        }

        Box(contentAlignment = Alignment.TopCenter) {
            AnimatedVisibility(
                scrollState.value.toInt() > oldMessageHeight.value,
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()).zIndex(100f)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(appMainColor)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Icon(
                            painter = painterResource(com.isu.common.R.drawable.arrow_up),
                            contentDescription = null,
                            tint = Color.White
                        )
                        CustomText(
                            text = stringResource(R.string.show_older_messages),
                            color = Color.White
                        )
                    }
                }
            }

            KeyBoardAwareScreen(
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
                    ,
                screenScrollState = scrollState,
                shouldScroll = false,
                keyBoardAware = false
            ) {
                TicketDetailCard(ticketData) { status ->
                    when (status) {
                        "Close" -> showClosedDialog.value = true
                        "Re-open" -> showOpenDialog.value = true
                    }
                }
                Column(
                    modifier = Modifier
                        .heightIn(screenHeight.dp - 430.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                            .height(50.dp).padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(
                            text = stringResource(R.string.messages),
                            fontSize = 14.sp.noFontScale(),
                            fontWeight = FontWeight(500),
                            color = authTextColor,
                            fontFamily = FontProvider.INTER
                        )
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                scrollState.animateScrollTo(scrollState.maxValue)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(appMainColor),
                        modifier = Modifier.width(230.dp).onGloballyPositioned {
                            oldMessageHeight.value = it.positionInWindow().y.toInt() + 780
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Icon(
                                painter = painterResource(com.isu.common.R.drawable.arrow_up),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.rotate(180f)
                            )
                            CustomText(
                                text = stringResource(R.string.show_newer_messages),
                                color = Color.White
                            )
                        }
                    }
                    Spacer(Modifier.height(25.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(30.dp), modifier = Modifier.fillMaxSize()) {
                        if (state.showLoader) {
                            repeat(4) {
                                SendersLoadingMessageContent()
                                LoadingMessageContent()
                            }
                        } else {
                            state.comments.forEach {
                                when (it.messageType) {
                                    MessageType.CustomerSupport -> RecieversMessageContent(it)
                                    MessageType.User -> SendersMessageContent(it)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    OutlinedTextField(
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray.copy(0.5f),
                            focusedBorderColor = Color.LightGray.copy(0.5f)
                        ),
                        leadingIcon = {
                            Image(
                                painter = painterResource(com.isu.common.R.drawable.profile_img),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp).clip(CircleShape)
                            )
                        },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier
                            .padding(vertical = 15
                                .dp)
                            .fillMaxWidth(0.95f)
                            .height(50.dp),
                        value = message,
                        onValueChange = { newText ->
                            onEvent(CommonScreenEvents.OnTextChanged(newText,TicketDetailsInputComment.TicketComment,))
                        },
                        placeholder = {
                            CustomText(
                                text = stringResource(R.string.reply_here),
                                color = Color.LightGray
                            )
                        },
                        trailingIcon = {

                                IconButton(onClick = {
                                    onEvent(
                                        CommonScreenEvents.OnClick<String>(
                                            TicketDetailsClickables.AddComment,
                                            message,
                                            onComplete = {
                                                focusManager.clearFocus()
                                            }
                                        )
                                    )
                                    focusManager.clearFocus()
                                }) {
                                    Icon(
                                        painter = painterResource(com.isu.common.R.drawable.ic_send),
                                        contentDescription = stringResource(R.string.send),
                                        tint = Color.DarkGray.copy(0.5f)
                                    )
                                }

                        }
                    )
                }
            }
        }
    }
}
@Composable
fun SendersLoadingMessageContent() {

    val startAnime= remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        startAnime.value=true
    }
    val animateOffset= animateOffsetAsState(if(startAnime.value) Offset(x=100f,y=100f) else Offset.Zero, infiniteRepeatable(tween(1000), repeatMode = RepeatMode.Reverse))
    val brush=Brush.linearGradient(
        colors = listOf(Color.Gray.copy(0.4f), Color.LightGray.copy(0.4f), Color.Gray.copy(0.4f)),
        start = animateOffset.value)

    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max), horizontalArrangement = Arrangement.spacedBy(5.dp)){

        Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
            Column(modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 0.dp), verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.End){
                CustomText(
                    text = "",
                    color = Color.LightGray,
                    fontSize = 14.sp.noFontScale(),
                    fontFamily = FontProvider.INTER,
                    modifier = Modifier.background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
                CustomText(
                    text ="  ",
                    textAlign = TextAlign.Justify,
                    fontSize = 14.sp.noFontScale(),
                    fontWeight = FontWeight(500),
                    fontFamily = FontProvider.INTER,
                    modifier = Modifier.width(100.dp)
                        .background(brush, shape = RoundedCornerShape(10.dp))
                )
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                CustomText(
                    text = "",
                    fontSize = 14.sp.noFontScale(),
                    fontWeight = FontWeight(400),
                    fontFamily = FontProvider.INTER,
                    modifier = Modifier.width(150.dp)
                        .background(brush, shape = RoundedCornerShape(10.dp))
                )
            }
        }
        Column (modifier = Modifier.width(50.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)){
            Box( modifier = Modifier.size(40.dp).background(brush, CircleShape))
            Column(modifier = Modifier.width(50.dp).weight(1f), horizontalAlignment = Alignment.CenterHorizontally ) {
                VerticalDivider(thickness = 1.dp, modifier = Modifier.weight(1f), color = Color.LightGray)
            }
        }
    }
}

/**
 * Composable function for displaying a loading message content.
 */
@Composable
fun LoadingMessageContent() {
    val startAnime= remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        startAnime.value=true
    }
    val animateOffset= animateOffsetAsState(if(startAnime.value) Offset(x=100f,y=100f) else Offset.Zero, infiniteRepeatable(tween(1000), repeatMode = RepeatMode.Reverse))
    val brush=Brush.linearGradient(
        colors = listOf(Color.Gray.copy(0.4f), Color.LightGray.copy(0.4f), Color.Gray.copy(0.4f)),
        start = animateOffset.value)
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max), horizontalArrangement = Arrangement.spacedBy(5.dp)){
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Box( modifier = Modifier.size(40.dp).background(brush, CircleShape))
            Column(modifier = Modifier.width(50.dp).weight(1f), horizontalAlignment = Alignment.CenterHorizontally ) {
                VerticalDivider(thickness = 1.dp, modifier = Modifier.weight(1f), color = Color.LightGray)
            }
        }
        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            Column(modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 0.dp), verticalArrangement = Arrangement.spacedBy(5.dp)){
                CustomText(
                    text = "",
                    color = Color.LightGray,
                    fontSize = 14.sp.noFontScale(),
                    fontFamily = FontProvider.INTER,
                    modifier = Modifier.background(brush = brush, shape = RoundedCornerShape(10.dp))
                )
                CustomText(
                    text ="  ",
                    textAlign = TextAlign.Justify,
                    fontSize = 14.sp.noFontScale(),
                    fontWeight = FontWeight(500),
                    fontFamily = FontProvider.INTER,
                    modifier = Modifier.width(100.dp)
                        .background(brush, shape = RoundedCornerShape(10.dp))
                )
            }
            Column {
                CustomText(
                    text = "",
                    fontSize = 14.sp.noFontScale(),
                    fontWeight = FontWeight(400),
                    fontFamily = FontProvider.INTER,
                    modifier = Modifier.width(150.dp)
                        .background(brush, shape = RoundedCornerShape(10.dp))
                )
            }
        }
    }
}


/**
 * Composable function for displaying a message content from the receiver.
 */
@Composable
fun RecieversMessageContent(message: MessageDetails) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.LightGray.copy(0.2f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            CustomText(
                text = message.message,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            CustomText(
                text = message.time,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

/**
 * Composable function for displaying a message content from the sender.
 */
@Composable
fun SendersMessageContent(messageDetails: MessageDetails) {



    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max), horizontalArrangement = Arrangement.spacedBy(5.dp)){

        Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 0.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.End
            ) {
                CustomText(
                    text = messageDetails.name,
                    color = Color.DarkGray,
                    fontSize = 13.5.sp.noFontScale(),
                    fontWeight = FontWeight(550),
                )
                CustomText(
                    text = "${ZonedDateFormatter.format(messageDetails.time)?: messageDetails.time} ",
                    textAlign = TextAlign.Justify,
                    fontSize = 13.sp.noFontScale(),
                    fontWeight = FontWeight(500)
                )
            }
            Spacer(Modifier.height(3.dp))
            Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End){
                CustomText(
                    text = messageDetails.message,
                    fontWeight = FontWeight(400),
                    fontFamily = FontProvider.INTER,
                    fontSize = 13.sp.noFontScale()
                )
            }
        }
        Column (modifier = Modifier.width(50.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)){
            Image(painter = painterResource(com.isu.common.R.drawable.profile_img),"", modifier = Modifier.size(40.dp).clip(
                CircleShape))
            Column(modifier = Modifier.width(50.dp).weight(1f), horizontalAlignment = Alignment.CenterHorizontally ) {
                VerticalDivider(thickness = 1.dp, modifier = Modifier.weight(1f), color = Color.LightGray)
            }
        }
    }
}



@Composable
fun TicketDetailCard(ticketData: TicketDetailUIData, onDropDownClick:(String)->Unit = {}) {
    val showDialog= remember { mutableStateOf(false) }
    val statusDropDown = remember { mutableStateOf(false) }
    Card(colors = CardDefaults.cardColors(Color.White), modifier = Modifier.wrapContentHeight().border(width=0.5.dp, color = Color.LightGray,shape = RoundedCornerShape(5.dp),).padding(start = 14.dp, end = 10.dp, top = 10.dp, bottom=10.dp)) {
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)){
                    Icon(painter = painterResource(com.isu.common.R.drawable.ticket_flag),"", tint = authTextColor)
                    CustomText(
                        text = stringResource(
                            R.string.ticket_id_detail,
                            ticketData.ticketId
                        ), fontSize = 13.sp.noFontScale(), fontWeight = FontWeight(500)
                    )
                }

                Box(contentAlignment = Alignment.CenterStart){
                    Icon(Icons.Default.MoreVert, contentDescription = "", tint = authTextColor, modifier = Modifier.clickable {
                        showDialog.value=!showDialog.value
                    })
                    DropdownMenu(
                        expanded = showDialog.value,
                        onDismissRequest = { showDialog.value = false },
                        Modifier.background(
                            Color.White
                        )
                    ) {
                        when (ticketData.status) {
                            is TicketStatus.Escalated -> {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            stringResource(R.string.re_open),
                                            fontSize = 14.sp.noFontScale(),
                                            color = TicketStatus.Open().openTextColor
                                        )
                                    },
                                    onClick = { showDialog.value = false
                                        onDropDownClick("Re-open")},
                                    modifier = Modifier.height(30.dp).width(170.dp),
                                )
                            }
                            is TicketStatus.Closed -> {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            stringResource(R.string.re_open),
                                            fontSize = 14.sp.noFontScale(),
                                            color = TicketStatus.Open().openTextColor
                                        )
                                    },
                                    onClick = { showDialog.value = false
                                        onDropDownClick("Re-open")},
                                    modifier = Modifier.height(30.dp).width(170.dp),
                                )
                            }

                            is TicketStatus.InProgress -> {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            stringResource(R.string.close),
                                            fontSize = 14.sp.noFontScale(),
                                            color = Color.Red
                                        )
                                    },
                                    onClick = { showDialog.value = false
                                        onDropDownClick("Close")},
                                    modifier = Modifier.height(30.dp).width(170.dp),
                                )
                            }

                            is TicketStatus.Open -> {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            stringResource(R.string.close),
                                            fontSize = 14.sp.noFontScale(),
                                            color = Color.Red
                                        )
                                    },
                                    onClick = { showDialog.value = false
                                        onDropDownClick("Close")},
                                    modifier = Modifier.height(30.dp).width(170.dp),
                                )
                            }

                            else -> {}
                        }

                    }
                }

            }
            Spacer(modifier = Modifier.height(5.dp))
            CardDetailTitleComponent(
                stringResource(R.string.request_title),
                ticketData.requestTitle
            )
            CardDetailComponent(
                s = stringResource(R.string.category_detail),
                s1 = ticketData.category,
              )
            Row(modifier = Modifier.fillMaxWidth().clickable{
                statusDropDown.value = !statusDropDown.value
            }, horizontalArrangement = Arrangement.SpaceBetween) {
                CustomText(text = "Status", color = authTextColor, fontSize = 15.sp)
                Icon(painter = painterResource(id = com.isu.common.R.drawable.arrow_down),
                    contentDescription = "",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp).rotate(if (statusDropDown.value)180f else 0f))
            }
            AnimatedVisibility(visible = !statusDropDown.value){

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(com.isu.common.R.drawable.success_tick),
                        contentDescription = "",
                        tint = TextGreen
                    )
                    Column {
                        CustomText(text = if (ticketData.status==TicketStatus.Open()){
                            "Sit back & relax, we have received your complaint."
                        }
                            else if(ticketData.status==TicketStatus.InProgress()) {
                                "Your complaint is now being reviewed by our representative."
                        }
                                else{
                                    "Your complaint has been resolved!"
                            },
                            fontWeight = FontWeight(400),
                            fontSize = 14.sp.noFontScale()
                        )
                        StatusColorComponent(
                            withCircle = true,
                            status = if (ticketData.status==TicketStatus.Open()){
                                "Open"
                            }
                            else if(ticketData.status==TicketStatus.InProgress()) {
                                "In Progress"
                            }
                            else{
                                "Closed"
                            },
                            color =  if(ticketData.status==TicketStatus.InProgress())TicketStatus.InProgress().inProgressColor else if(ticketData.status==TicketStatus.Closed())TicketStatus.Closed().closedColor  else ActiveTextBackground,
                            textColor = if(ticketData.status==TicketStatus.InProgress())TicketStatus.InProgress().inProgressTextColor else if(ticketData.status==TicketStatus.Closed())TicketStatus.Closed().closedTextColor else TextGreen
                        )

                    }
                }
            }
            AnimatedVisibility(visible = statusDropDown.value){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(com.isu.common.R.drawable.success_tick),
                            contentDescription = "",
                            tint = TextGreen
                        )
                        Box(
                            modifier = Modifier.padding(top = 10.dp).height(30.dp)
                                .width(1.dp).background(
                                    if(ticketData.status==TicketStatus.InProgress()||ticketData.status==TicketStatus.Closed()){
                                        TextGreen
                                    }else{
                                        Color.LightGray
                                    }
                                )
                        )
                        if(ticketData.status==TicketStatus.InProgress()||ticketData.status==TicketStatus.Closed()){
                            Icon(
                                modifier = Modifier.size(25.dp),
                                painter = painterResource(com.isu.common.R.drawable.success_tick),
                                contentDescription = "",
                                tint = TextGreen
                            )
                        }else{
                            RadioButton(
                                selected = true,
                                onClick = {},
                                colors = RadioButtonDefaults.colors(Color.LightGray)
                            )
                        }



                        Box(
                            modifier = Modifier.height(30.dp).width(1.dp)
                                .background( if(ticketData.status==TicketStatus.Closed()){
                                    TextGreen
                                }else{
                                    Color.LightGray
                                })
                        )
                        if(ticketData.status==TicketStatus.InProgress()||ticketData.status==TicketStatus.Closed()){
                            Icon(
                                modifier = Modifier.size(25.dp),
                                painter = painterResource(com.isu.common.R.drawable.success_tick),
                                contentDescription = "",
                                tint = TextGreen
                            )
                        }else{
                            RadioButton(
                                selected = true,
                                onClick = {},
                                colors = RadioButtonDefaults.colors(Color.LightGray)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Column {
                            CustomText(text = "Sit back & relax, we have received your complaint.",
                                fontWeight = FontWeight(400),
                                fontSize = 14.sp.noFontScale()
                            )
                            StatusColorComponent(
                                withCircle = true,
                                status = "Open",
                                color = ActiveTextBackground,
                                textColor = TextGreen
                            )
                        }
                        Column {
                            CustomText(
                                text = "Your complaint is now being reviewed by our representative.",
                                fontSize = 14.sp.noFontScale(),
                                color = if(ticketData.status==TicketStatus.InProgress()|| ticketData.status == TicketStatus.Closed()) Color.Black else Color.LightGray,
                                fontWeight = FontWeight(400)
                            )
                            StatusColorComponent(
                                withCircle = true,
                                status = "In Progress",
                                color =  if(ticketData.status==TicketStatus.InProgress()|| ticketData.status==TicketStatus.Closed())TicketStatus.InProgress().inProgressColor else Color.LightGray,
                                textColor = if(ticketData.status==TicketStatus.InProgress()|| ticketData.status==TicketStatus.Closed())TicketStatus.InProgress().inProgressTextColor else Color.LightGray,
                            )
                        }
                        Column {
                            CustomText(
                                text = "Your complaint has been resolved!",
                                fontSize = 14.sp.noFontScale(),
                                color = if(ticketData.status==TicketStatus.Closed()) Color.Black else Color.LightGray,
                                fontWeight = FontWeight(400)
                            )
                            StatusColorComponent(
                                withCircle = true,
                                status = "Closed",
                                color = if(ticketData.status==TicketStatus.Closed())TicketStatus.Closed().closedColor else Color.LightGray,
                                textColor =  if(ticketData.status==TicketStatus.Closed())TicketStatus.Closed().closedTextColor else Color.LightGray,
                            )
                        }
                    }
                }
            }

            CardDetailComponent(
                s = stringResource(R.string.priority),
                valueComposable = {
                    FilterSelection(
                        ticketData.priority,
                        color = ticketData.priority.color,
                        onSelect = {})
                },
                s1 = "",
                statusComponent = {
                    CardDetailTitleComponent(
                        s = "Raised Date",
                        s1 = ZonedDateFormatter.format(ticketData.date, pattern = "dd MMM, yyyy")
                            ?: ticketData.date
                    )
                })
            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(end = 20.dp), verticalArrangement = Arrangement.spacedBy(5.dp)){
                CustomText(
                    text = stringResource(R.string.description_detail),
                    color = ticketTextLightColor,
                    fontSize = 14.sp.noFontScale()
                )
                CustomText(
                    text = ticketData.description,
                    textAlign = TextAlign.Justify,
                    fontSize = 14.sp.noFontScale(),
                    fontWeight = FontWeight(500)
                )
            }


        }
    }
}
