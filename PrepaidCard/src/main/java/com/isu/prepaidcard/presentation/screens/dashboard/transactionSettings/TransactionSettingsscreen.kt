package com.isu.prepaidcard.presentation.screens.dashboard.transactionSettings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.SetLimitComponent
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.appMainColor
import kotlinx.coroutines.launch

@Composable
fun TransactionSettingsScreen(
    modifier: Modifier = Modifier,
    transactionViewModel: TransactionViewModel,
) {
    val config = LocalConfiguration.current
    val atmEnable = transactionViewModel.atmEnable
    val atmLimit = transactionViewModel.atmLimit
    val ecomEnable = transactionViewModel.ecomEnable
    val ecomLimit = transactionViewModel.ecomLimit
    val contactLess = transactionViewModel.contactLess
    val contactLessLimit = transactionViewModel.contactLessLimit
    val posEnable = transactionViewModel.posEnable
    val posenableLimit = transactionViewModel.posLimit
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        transactionViewModel.getCardNumberByRefid()
    }
    Scaffold(topBar = {
        CustomProfileTopBar(text = "Transaction Settings")
    }, containerColor = Color.White) {
        KeyBoardAwareScreen(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = 22.dp,
                end = 22.dp,
                bottom = 0.dp,
            ),
            shouldScroll = false
        ) {
            Column(
                modifier = Modifier.heightIn(config.screenHeightDp.dp - (it.calculateTopPadding() + 90.dp))
                    .fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (transactionViewModel.atmEnableResp.value) {
                        SetLimitComponent(
                            label = "ATM Withdrawal",
                            onlineEnabled = atmEnable,
                            limit = atmLimit,
                            maxLimit = transactionViewModel.atmMaxLimit
                        )
                    }
                    if (transactionViewModel.posEnableResp.value) {
                        SetLimitComponent(
                            label = "POS",
                            onlineEnabled = posEnable,
                            limit = posenableLimit,
                            maxLimit = transactionViewModel.posMaxLimit
                        )
                    }
                    if (transactionViewModel.ecomEnableResp.value) {
                        SetLimitComponent(
                            label = "Ecommerce",
                            onlineEnabled = ecomEnable,
                            limit = ecomLimit,
                            maxLimit = transactionViewModel.ecomMaxLimit
                        )
                    }
                    if (transactionViewModel.contactLessResp.value) {
                        SetLimitComponent(
                            label = "Contactless",
                            onlineEnabled = contactLess,
                            limit = contactLessLimit,
                            maxLimit = transactionViewModel.contactLessMaxLimit
                        )
                    }


                }
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomButton(text = "Submit", onClick = {
                            transactionViewModel.setTransactionLimit()
                        }, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(10.dp))
                        CustomButton(
                            onClick = {
                                scope.launch {
                                    NavigationEvent.helper.navigateBack()
                                }
                            },
                            modifier = Modifier.weight(1f)
                                .border(
                                    BorderStroke(1.dp, Color.LightGray.copy(0.6f)),
                                    shape = RoundedCornerShape(5.dp)
                                ),
                            innerComponent = {
                                CustomText(
                                    text = "Cancel",
                                    color = appMainColor
                                )
                            },
                            color = Color.White
                        )


                    }
                }
            }

        }
    }
}

