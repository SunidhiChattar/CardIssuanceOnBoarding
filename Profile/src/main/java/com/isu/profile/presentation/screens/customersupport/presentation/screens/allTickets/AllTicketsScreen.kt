package com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.isu.common.customcomposables.CloseTicketDialog
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomResizeBasicInput
import com.isu.common.customcomposables.CustomResizeButton
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.OpenTicketDialog
import com.isu.common.customcomposables.ProfileText
import com.isu.common.customcomposables.StatusColorComponent
import com.isu.common.customcomposables.TicketCard
import com.isu.common.customcomposables.TicketData
import com.isu.common.customcomposables.TicketPriority
import com.isu.common.customcomposables.TicketStatus
import com.isu.common.customcomposables.bounceClick
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.navigation.CustomerSupportScreen
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.ticketTextDarkColor
import com.isu.common.utils.LocalNavController
import com.isu.common.utils.navigateTo
import com.isu.profile.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Composable function to display the Customer Support screen.
 *
 * @param onEvent A lambda function to handle screen events.
 * @param state The current state of the customer support screen.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllTicketsScreen(onEvent: (CommonScreenEvents) -> Unit, state: CustomerSupportData) {
    val navController = LocalNavController.current

    // State variables
    val wantToCreateATicket = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }
    val showClosedDialog = remember { mutableStateOf(false) }
    val showOpenDialog = remember { mutableStateOf(false) }
    val ticketToChangeStatus: MutableState<TicketData?> = remember { mutableStateOf(null) }
    val listState = rememberLazyListState()

    // Handle side effects
    LaunchedEffect(Unit) {
        onEvent(CommonScreenEvents.ClearFields)
        if (state.requireReloading) {
            onEvent(CommonScreenEvents.CallApi<Any>(CustomerSupportApiType.FetchTickets) {})
        }
    }

    // Show dialogs based on state
    if (showClosedDialog.value && ticketToChangeStatus.value != null) {
        CloseTicketDialog(
            ticketToChangeStatus.value!!,
            dismiss = { showClosedDialog.value = false },
            proceed = {
                onEvent(
                    CommonScreenEvents.OnClick(
                        CustomerSupportClickables.TicketStatus,
                        ticketToChangeStatus.value
                    ) { showClosedDialog.value = false }
                )
            }
        )
    }

    if (showOpenDialog.value && ticketToChangeStatus.value != null) {
        OpenTicketDialog(
            ticketData = ticketToChangeStatus.value!!,
            dismiss = { showOpenDialog.value = false }
        ) {
            onEvent(
                CommonScreenEvents.OnClick(
                    CustomerSupportClickables.TicketStatus,
                    ticketToChangeStatus.value
                ) { showOpenDialog.value = false }
            )
        }
    }

    Scaffold(
        topBar = { CustomProfileTopBar(text = stringResource(R.string.customer_support_hd)) },
        containerColor = Color.White,
        floatingActionButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomResizeButton(
                    onClick = {

                        onEvent(CommonScreenEvents.CallApi<Any>(CustomerSupportApiType.FetchTickets) {})
                    },
                    color = Color.White,
                    modifier = Modifier
                        .height(35.dp)
                        .fillMaxWidth(0.88f)
                        .border(0.5.dp, authTextColor, shape = RoundedCornerShape(5.dp)),
                    innerComponent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "",
                                tint = authTextColor
                            )
                            CustomText(
                                text = "Refresh Screen",
                                color = authTextColor
                            )
                        }
                    }
                )
            }
            /*if (!wantToCreateATicket.value) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomResizeButton(
                        onClick = {
                            navController.navigateTo(CustomerSupportScreen.RaiseTicketScreen)
                        },
                        modifier = Modifier
                            .height(35.dp)
                            .fillMaxWidth(0.88f),
                        innerComponent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    painter = painterResource(com.isu.common.R.drawable.ticket_flag),
                                    contentDescription = "",
                                    tint = Color.White
                                )
                                CustomText(
                                    text = stringResource(R.string.raise_a_ticket_hd),
                                    color = Color.White
                                )
                            }
                        }
                    )
                }

            }
            }*/
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    start = 22.dp,
                    top = innerPadding.calculateTopPadding(),
                    end = 22.dp,
                    bottom = 22.dp
                )
        ) {
            val scope = rememberCoroutineScope()
            var isRefreshing by remember { mutableStateOf(false) }
            var swipeOffset by remember { mutableStateOf(0f) }
            // State variables for filters and list
            val list: SnapshotStateList<TicketData> = remember { mutableStateListOf() }
            val showFilterDialog = remember { mutableStateOf(false) }
            val listOfStatusFilters: SnapshotStateList<String> = remember { mutableStateListOf() }
            val listOfPriorityFilters: SnapshotStateList<String> = remember { mutableStateListOf() }

            // Filter lists
            val statusFilterList = listOf(
                TicketStatus.InProgress(),
                TicketStatus.Open(),
                TicketStatus.Escalated(),
                TicketStatus.Closed()
            )
            val priorityFilterList = listOf(
                TicketPriority.Low(),
                TicketPriority.Medium(),
                TicketPriority.High()
            )

            val reloadList = remember { mutableStateOf(false) }

            val showReloadButton = remember { mutableStateOf(false) }
            val context = LocalContext.current

            // Apply filters to the list
            LaunchedEffect(searchText.value, state, reloadList.value) {
                list.clear()
                scope.launch(Dispatchers.IO) {
                    delay(200)
                    list.addAll(state.listOfTicketData.filter {
                        satisfiesFilterCondition(
                            searchText.value,
                            it,
                            listOfStatusFilters,
                            listOfPriorityFilters
                        )
                    })
                }
            }

            // Search and filter UI
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(Modifier.weight(1f)) {
                    CustomResizeBasicInput(
                        value = state.search,
                        onValueChange = {
                            onEvent(
                                CommonScreenEvents.OnTextChanged(
                                    text = it,
                                    CustomerSupportTextField.Search
                                )
                            )
                            searchText.value = it
                        },
                        placeHolder = stringResource(R.string.search_placeholder)
                    )
                }

                Row(
                    modifier = Modifier
                        .width(45.dp)
                        .height(40.dp)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(3.dp),
                            color = Color.LightGray
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        IconButton(onClick = { showFilterDialog.value = !showFilterDialog.value }) {
                            Icon(
                                painter = painterResource(com.isu.common.R.drawable.black_filter),
                                contentDescription = "",
                                tint = ticketTextDarkColor
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            DropdownMenu(
                                expanded = showFilterDialog.value,
                                onDismissRequest = { showFilterDialog.value = false },
                                properties = PopupProperties(
                                    focusable = false,
                                    dismissOnBackPress = true,
                                    dismissOnClickOutside = true,
                                    clippingEnabled = false
                                ),
                                modifier = Modifier
                                    .background(Color.White, RoundedCornerShape(10.dp))
                                    .padding(vertical = 10.dp, horizontal = 10.dp)
                                    .fillMaxWidth(0.9f)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    ProfileText(text = stringResource(R.string.filters))
                                    ProfileText(
                                        text = stringResource(R.string.clear_all),
                                        modifier = Modifier.clickable {
                                            listOfStatusFilters.clear()
                                            listOfPriorityFilters.clear()
                                            reloadList.value = !reloadList.value
                                        }
                                    )
                                }
                                Spacer(Modifier.height(20.dp))
                                Column {
                                    ProfileText(
                                        text = stringResource(R.string.select_status),
                                        color = Color.Gray
                                    )
                                    Spacer(Modifier.height(5.dp))
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        statusFilterList.forEach {
                                            val selected = remember {
                                                mutableStateOf(
                                                    listOfStatusFilters.contains(it.status)
                                                )
                                            }
                                            FilterSelection(
                                                it,
                                                selected,
                                                color = if (listOfStatusFilters.contains(it.status)) it.color else Color.LightGray
                                            ) { selected ->
                                                if (selected) {
                                                    if (!listOfStatusFilters.contains(it.status)) {
                                                        listOfStatusFilters.add(it.status)
                                                    }
                                                } else {
                                                    listOfStatusFilters.remove(it.status)
                                                }
                                                reloadList.value = !reloadList.value
                                            }
                                        }
                                    }
                                }
                                /*Spacer(Modifier.height(20.dp))
                                Column {
                                    ProfileText(
                                        text = stringResource(R.string.select_priority),
                                        color = Color.Gray
                                    )
                                    Spacer(Modifier.height(5.dp))
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        priorityFilterList.forEach {
                                            val selected = remember {
                                                mutableStateOf(
                                                    listOfPriorityFilters.contains(it.status)
                                                )
                                            }
                                            FilterSelection(
                                                it,
                                                selected,
                                                color = if (listOfPriorityFilters.contains(it.status)) it.color else Color.LightGray
                                            ) { selected ->
                                                if (selected) {
                                                    if (!listOfPriorityFilters.contains(it.status)) {
                                                        listOfPriorityFilters.add(it.status)
                                                    }
                                                } else {
                                                    listOfPriorityFilters.remove(it.status)
                                                }
                                                reloadList.value = !reloadList.value
                                            }
                                        }
                                    }
                                }*/
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource,
                    ): Offset {
                        // Detect upward swipe and reset swipe offset
                        if (available.y > 0) {
                            swipeOffset = 0f
                        }
                        return Offset.Zero
                    }
                }
            }
            // Display list of tickets
            androidx.compose.animation.AnimatedVisibility(
                visible = !wantToCreateATicket.value,
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it },
                modifier = Modifier.nestedScroll(nestedScrollConnection).pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        Toast.makeText(context, "called", Toast.LENGTH_LONG).show()
                        swipeOffset += dragAmount
                        if (swipeOffset > 300 && !isRefreshing) { // Threshold for triggering refresh
                            isRefreshing = true
                            swipeOffset = 0f
                        }
                    }
                }

            ) {
                AnimatedVisibility(
                    visible = isRefreshing,
                    enter = slideInVertically(animationSpec = spring(10f, 0.5f))
                ) {
                    Icon(painter = painterResource(com.isu.common.R.drawable.replacement), "")
                }

                LazyColumn(
                    state = listState,
                    verticalArrangement = if (list.isEmpty()) Arrangement.Center else Arrangement.spacedBy(
                        15.dp
                    ),
                    modifier = Modifier
                        .height(590.dp)
                        .fillMaxWidth().pointerInput(null) {
                            detectDragGestures { change, dragAmount ->
                                if (dragAmount.y > 0) {
                                    Toast.makeText(context, "called", Toast.LENGTH_LONG).show()
                                    scope.launch {
                                        showReloadButton.value = true
                                        delay(2000)
                                        showReloadButton.value = false
                                    }

                                }
                                change.consume()
                            }
                        }
                ) {

                    if (list.isEmpty()) {
                        item {
                            Image(
                                painter = painterResource(com.isu.common.R.drawable.no_data),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer { translationY = -220f }
                            )
                        }
                    } else {
                        items(list) {
                            TicketCard(
                                it,
                                onClick = {
                                    onEvent(
                                        CommonScreenEvents.OnClick(
                                            CustomerSupportClickables.TicketCard,
                                            additionData = it
                                        )
                                    )
                                },
                                onDropDownClick = { status ->
                                    ticketToChangeStatus.value = it
                                    if (status == "Close") {
                                        showClosedDialog.value = true


                                    } else {
                                        showOpenDialog.value = true
                                    }
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Checks if the ticket data satisfies the filter conditions.
 *
 * @param value The search query.
 * @param state The ticket data.
 * @param listOfStatusFilters The list of selected status filters.
 * @param listOfPriorityFilters The list of selected priority filters.
 * @return True if the ticket data satisfies the filter conditions, false otherwise.
 */
fun satisfiesFilterCondition(
    value: String,
    state: TicketData,
    listOfStatusFilters: SnapshotStateList<String>,
    listOfPriorityFilters: SnapshotStateList<String>,
): Boolean {

    return try {
        (state.ticketId.contains(value.toRegex()) || state.requestTitle.contains(value.toRegex()) || state.description.contains(
            value.toRegex()
        )) &&
                (if (listOfStatusFilters.isEmpty()) true else listOfStatusFilters.contains(state.status.status)) &&
                (if (listOfPriorityFilters.isEmpty()) true else listOfPriorityFilters.contains(state.priority.status))
    } catch (e: Exception) {

        false
    }
}

/**
 * Composable function to display a filter selection option for TicketStatus.
 *
 * @param stat The ticket status to display.
 * @param selected The current selection state.
 * @param color The color to use for the filter selection.
 * @param onSelect Callback to handle the selection change.
 */
@Composable
fun FilterSelection(
    stat: TicketStatus,
    selected: MutableState<Boolean>,
    color: Color,
    onSelect: (Boolean) -> Unit,
) {
    Box(Modifier.bounceClick {
        selected.value = !selected.value
        onSelect(selected.value)
    }) {
        StatusColorComponent(status = stat.status, color = color, textColor = stat.textColor)
    }
}

/**
 * Composable function to display a filter selection option for TicketPriority.
 *
 * @param stat The ticket priority to display.
 * @param selected The current selection state.
 * @param color The color to use for the filter selection.
 * @param onSelect Callback to handle the selection change.
 */
@Composable
fun FilterSelection(
    stat: TicketPriority,
    selected: MutableState<Boolean> = mutableStateOf(false),
    color: Color,
    onSelect: (Boolean) -> Unit,
) {
    Box(Modifier.bounceClick {
        selected.value = !selected.value
        onSelect(selected.value)
    }) {
        StatusColorComponent(
            status = stat.status,
            color = color,
            withCircle = false,
            textColor = stat.textColor
        )
    }
}

/**
 * Data class representing the details of an uploaded document.
 *
 * @param name The name of the document.
 * @param time The time when the document was uploaded.
 * @param size The size of the document.
 */
data class DocumentDetails(
    @DrawableRes val icon: Int = com.isu.common.R.drawable.pdf,
    val name: String = "user-journey-01.pdf",
    val time: String = "2m ago",
    val size: String = "604KB",
    val url: String = "",
)

/**
 * Composable function to display details of an uploaded document.
 *
 * @param details The details of the uploaded document.
 */
@Composable
fun UploadedDocumentDetail(details: DocumentDetails, removeDocumentAction: () -> Unit) {
    val removeDocument = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .heightIn(40.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(5.dp, end = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(details.icon),
                contentDescription = "",
                tint = authTextColor,
                modifier = Modifier.size(30.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            CustomText(
                text = details.name,
                fontSize = 13.sp.noFontScale(),
                fontWeight = FontWeight(500)
            )
            CustomText(
                text = details.time,
                fontSize = 12.sp.noFontScale()
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Default.Refresh, "", tint = appMainColor, modifier = Modifier.size(20.dp))
            Row(
                modifier = Modifier
                    .height(20.dp)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .height(20.dp)
                        .wrapContentWidth()
                        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(3.dp))
                        .padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = details.size + stringResource(R.string.mb),
                        fontSize = 12.sp.noFontScale()
                    )
                }
            }
            Box {
                Icon(Icons.Default.MoreVert, "", modifier = Modifier.size(20.dp).clickable {
                    removeDocument.value = !removeDocument.value
                })
                DropdownMenu(
                    expanded = removeDocument.value,
                    onDismissRequest = { removeDocument.value = false },
                    Modifier.background(
                        Color.White
                    )
                ) {
                    DropdownMenuItem(text = {
                        CustomText(
                            text = stringResource(R.string.remove),
                            color = Color.Black
                        )
                    }, modifier = Modifier.height(30.dp).width(150.dp).background(
                        Color.White
                    ), onClick = {
                        removeDocumentAction()
                        removeDocument.value = false
                    })
                }
            }

        }
    }
}
