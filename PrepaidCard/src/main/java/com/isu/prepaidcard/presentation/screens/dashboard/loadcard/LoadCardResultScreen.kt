package com.isu.prepaidcard.presentation.screens.dashboard.loadcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.events.CommonScreenEvents
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.ui.theme.appMainColor
import com.isu.common.utils.ZonedDateFormatter
import kotlinx.coroutines.launch


@Composable
fun LoadCardResultScreen(
    modifier: Modifier = Modifier,
    state: LoadCardState,
    onEvent: (CommonScreenEvents) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val upiResult = state.loadCardResult
    val clipboardManager = LocalClipboardManager.current
    val config = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        focusManager.clearFocus()
    }
    Scaffold(topBar = {
        CustomProfileTopBar(text = "")
    }, containerColor = Color.White) {
        KeyBoardAwareScreen(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),

                start = 22.dp,
                end = 22.dp
            ), shouldScroll = false, backHandler = {
                NavigationEvent.helper.navigateTo(
                    ProfileScreen.CardManagementScreen,
                    ProfileScreen.DashBoardScreen
                )
            }
        ) {
            Column(
                modifier = Modifier.heightIn(config.screenHeightDp.dp - (it.calculateTopPadding() + 90.dp)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(
                            upiResult?.status?.image ?: com.isu.common.R.drawable.load_card_failure
                        ), ""
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomText(
                        text = "${upiResult?.status?.uiText}",
                        color = upiResult?.status?.color ?: Color.Black,
                        fontWeight = FontWeight(600)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomText(text = "${upiResult?.ammount ?: "0"}", fontWeight = FontWeight(650))
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        Modifier.border(
                            border = BorderStroke(1.dp, Color.LightGray),
                            shape = RoundedCornerShape(5.dp)
                        ).fillMaxWidth().padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(end = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CustomText(text = "TxnId: ")
                                CustomText(
                                    text = "${upiResult?.txnId}",
                                    fontWeight = FontWeight(500)
                                )

                            }
                            Row(
                                modifier = Modifier.padding(horizontal = 0.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(onClick = {
                                    clipboardManager.setText(buildAnnotatedString {
                                        append(upiResult?.txnId)
                                    })
                                }, modifier = Modifier.size(25.dp)) {
                                    Icon(
                                        painter = painterResource(com.isu.common.R.drawable.copy),
                                        ""
                                    )
                                }
                            }

                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CustomText(text = "Mode: ")
                            CustomText(text = "UPI", fontWeight = FontWeight(500))
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CustomText(
                                text = "${
                                    ZonedDateFormatter.formatMillisecondsToDate(
                                        System.currentTimeMillis(),
                                        "dd MMM yyyy, hh:mm a"
                                    )
                                } ", fontWeight = FontWeight(500)
                            )

                        }
                    }

                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    CustomButton(text = "Go To Home", onClick = {
                        scope.launch {
                            NavigationEvent.helper.navigateTo(
                                ProfileScreen.CardManagementScreen,
                                ProfileScreen.HomeScreen
                            )
                        }
                    }, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(10.dp))
                    if (upiResult?.status == UpiPaymentStatus.Success()) {
                        CustomButton(
                            modifier = Modifier.weight(1f)
                                .border(
                                    BorderStroke(1.dp, Color.LightGray.copy(0.6f)),
                                    shape = RoundedCornerShape(5.dp)
                                ),
                            innerComponent = {
                                CustomText(
                                    text = "Add Again",
                                    color = appMainColor
                                )
                            },
                            color = Color.White,
                            onClick = {
                                scope.launch {
                                    NavigationEvent.helper.navigateBack()
                                }

                            }
                        )
                    } else {
                        CustomButton(
                            modifier = Modifier.weight(1f)
                                .border(BorderStroke(1.dp, Color.LightGray.copy(0.6f))),
                            innerComponent = {
                                CustomText(
                                    text = "Try Again",
                                    color = appMainColor
                                )
                            },
                            color = Color.White,
                            onClick = {
                                scope.launch {
                                    NavigationEvent.helper.navigateBack()
                                }

                            }
                        )
                    }

                }
            }
        }
    }
}