package com.isu.prepaidcard.presentation.screens.dashboard.mandate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.isu.common.R
import com.isu.common.customcomposables.CancelMandateDialog
import com.isu.common.customcomposables.CardCustomText
import com.isu.common.customcomposables.CustomTopBar
import com.isu.common.customcomposables.MerchantStatusCard
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.MerchantData
import com.isu.common.utils.UiText
import com.isu.prepaidcard.presentation.viewmodels.MandateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This composable function represents the mandates screen of the application.
 *
 * @param mandateViewModel ViewModel containing the mandate related data and logic.
 *
 * @Composable This annotation indicates that the function is composable and can be used within a Jetpack Compose composable hierarchy.
 */
@Composable
fun Mandates(mandateViewModel: MandateViewModel){
    val scope= rememberCoroutineScope()
    val config = LocalConfiguration.current
    val history = remember { mutableStateOf(false) }
    val list = mandateViewModel.list
    val canceledList = mandateViewModel.canceledList
    val cancelDialog = remember { mutableStateOf(false) }
    val clickedMerchantData :MutableState<MerchantData?> = remember { mutableStateOf(null) }

    Scaffold(
        topBar = {
            CustomTopBar(
                leadingIcon = {
                IconButton(
                    onClick = {scope.launch {
                        NavigationEvent.helper.emit(NavigationEvent.NavigateBack)
                    }},
                    modifier = Modifier.border(1.dp, Color.LightGray, CircleShape).size(22.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, "")

                }
            },
                text = "Mandates",
                trailingIcon = {
                        Image(painter = painterResource(com.isu.common.R.drawable.menu),
                            contentDescription = "")
                },
                onTrailingIconClick = {
                    history.value = true
                }
            )

            Column(modifier = Modifier.padding(top = 10.dp, end = 10.dp).fillMaxWidth(), horizontalAlignment = Alignment.End) {
                AnimatedVisibility(visible = history.value){
                    Card(modifier = Modifier
                        .clickable(indication = null, interactionSource = remember { MutableInteractionSource()}){
                            scope.launch {
                                NavigationEvent.helper.navigateTo(CardManagement.MandateHistory)
                            }
                        },
                        colors = CardDefaults.cardColors(Color.White),
                        border = BorderStroke(1.dp, authTextColor)) {
                        Row (modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)){
                            CardCustomText(text = "History",
                                modifier = Modifier,
                                color = authTextColor,
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em)
                            Icon(painter = painterResource(com.isu.common.R.drawable.ic_history), contentDescription = "",
                                tint = authTextColor,
                                modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier.background(Color.White).padding(top = it.calculateTopPadding())
        ) {

            LazyColumn(verticalArrangement =
            if (list.isEmpty())
                Arrangement.Center
            else
                Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxWidth()
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        history.value = false
                    }.padding(20.dp)
            ) {
                items(list) {
                    MerchantStatusCard(it) {
                        clickedMerchantData.value=it
                        cancelDialog.value = true
                    }
                    if (cancelDialog.value){
                        CancelMandateDialog(onDismiss = {
                            cancelDialog.value = false},
                            onConfirm = {
                                scope.launch (Dispatchers.IO){
                                    canceledList.addAll(list.filter { it==clickedMerchantData.value}.map {it.copy(status = "Cancelled")})
                                    list.removeAll(list.filter { it==clickedMerchantData.value })
                                    scope.launch {
                                        cancelDialog.value = false
                                        ShowSnackBarEvent.helper.emit(
                                            ShowSnackBarEvent.show(
                                                SnackBarType.SuccessSnackBar,
                                                UiText.StringResource(
                                                        com.isu.prepaidcard.R.string.mandate_for_has_been_cancelled,
                                                        it.merchant
                                                    )
                                            )
                                        )
                                    }
                                }
                            })
                    }
                }
                if (list.isEmpty()) {
                    item {
                        Column(modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment
                                .CenterHorizontally) {
                            Image(
                                painter = painterResource(R.drawable.no_data),
                                "",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

            }
        }
    }
    }