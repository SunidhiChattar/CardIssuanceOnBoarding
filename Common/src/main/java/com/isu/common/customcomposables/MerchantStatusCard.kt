package com.isu.common.customcomposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.isu.common.R
import com.isu.common.ui.theme.ActiveTextBackground
import com.isu.common.ui.theme.CancelTextBackground
import com.isu.common.ui.theme.TextGreen
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.errorColor
import com.isu.common.ui.theme.ticketTextLightColor
import com.isu.common.utils.MerchantData

@Composable
fun MerchantStatusCard(
    merchantData:MerchantData,
    onCancelClick: () -> Unit = {}
){
    val config = LocalConfiguration.current
    val showDialog = remember { mutableStateOf(false) }
    val showTransaction = remember { mutableStateOf(false) }
    Card (
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier.wrapContentHeight()
            .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(5.dp))
            .padding(start = 14.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
        shape = RoundedCornerShape(5.dp)
    ){
        Column(  modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Image(
                        painter = painterResource(merchantData.merchantProfile),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                    CardCustomText(
                        text = merchantData.merchant, modifier = Modifier,
                        color = authTextColor,
                        fontSize = if (config.screenWidthDp.dp < 400.dp) 3.5.em else 2.5.em
                    )
                }
                if (merchantData.status == "Active") {
                    Box(contentAlignment = Alignment.CenterStart) {
                        IconButton(onClick = {
                            showDialog.value = !showDialog.value
                        }, modifier = Modifier.size(if(config.screenWidthDp.dp<400.dp)20.dp else 30.dp)) {
                            Image(
                                painter = painterResource(R.drawable.menu), contentDescription = ""
                            )
                        }
                        DropdownMenu(
                            expanded = showDialog.value,
                            onDismissRequest = { showDialog.value = false },
                            Modifier.background(
                                Color.White
                            )
                        ) {
                            DropdownMenuItem(
                                text = {
                                    CardCustomText(
                                        text = "Cancel Mandate",
                                        modifier = Modifier,
                                        color = errorColor,
                                        fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em
                                    )
                                },
                                onClick = {
                                    showDialog.value = false
                                    onCancelClick.invoke()
                                },
                                modifier = Modifier.height(40.dp).fillMaxWidth()
                                    .background(color = Color.White)
                                    .clip(RoundedCornerShape(30.dp)),
                                colors = MenuDefaults.itemColors(Color.White),
                                trailingIcon = {
                                    Image(
                                        painter = painterResource(R.drawable.round_cancel),
                                        contentDescription = "",
                                        modifier = Modifier.size( if (config.screenWidthDp.dp < 400.dp) 20.dp else 30.dp)
                                    )
                                }
                            )
                        }
                    }
                } else {
                    Icon(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            showTransaction.value = !showTransaction.value
                        },
                        imageVector = if (showTransaction.value) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            }
            if (merchantData.status == "Active") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f).padding(end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            CardCustomText(
                                modifier = Modifier, text = "Merchant",
                                color = ticketTextLightColor,
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em
                            )
                            CardCustomText(
                                modifier = Modifier, text = merchantData.merchant,
                                color = authTextColor,
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            CardCustomText(
                                modifier = Modifier, text = "Description",
                                fontSize =if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = ticketTextLightColor
                            )
                            CardCustomText(
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                modifier = Modifier, text = "Monthly Subs",
                                color = authTextColor
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f).padding(end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            CardCustomText(
                                modifier = Modifier, text = "Start Date",
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = ticketTextLightColor
                            )
                            CardCustomText(
                                modifier = Modifier, text = merchantData.startDate,
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = authTextColor
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            CardCustomText(
                                modifier = Modifier, text = "End Date",
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = ticketTextLightColor
                            )
                            CardCustomText(
                                modifier = Modifier, text = merchantData.endDate,
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = authTextColor
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f).padding(end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            CardCustomText(
                                modifier = Modifier, text = "Max Amount",
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = ticketTextLightColor
                            )
                            CardCustomText(
                                modifier = Modifier, text = merchantData.maxAmount,
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = authTextColor
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            CardCustomText(
                                modifier = Modifier, text = "Amount Type",
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = ticketTextLightColor
                            )
                            CardCustomText(
                                modifier = Modifier, text = merchantData.amountType,
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = authTextColor
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f).padding(end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            CardCustomText(
                                modifier = Modifier, text = "Frequency",
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = ticketTextLightColor
                            )
                            CardCustomText(
                                modifier = Modifier, text = merchantData.frequency,
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = authTextColor
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            CardCustomText(
                                modifier = Modifier, text = "SiHub ID",
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = ticketTextLightColor
                            )
                            CardCustomText(
                                modifier = Modifier, text = merchantData.siHubId,
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                color = authTextColor
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            CardCustomText(
                                text = "Status",
                                color = ticketTextLightColor,
                                fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                modifier = Modifier
                            )
                            Row(
                                modifier = Modifier.height(30.dp).wrapContentHeight()
                                    .background(
                                        color = ActiveTextBackground ,
                                        shape = RoundedCornerShape(100.dp)
                                    )
                                    .padding(vertical = 4.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(10.dp).background(
                                        shape = CircleShape,
                                        color =TextGreen
                                    )
                                )
                                CardCustomText(
                                    modifier = Modifier,
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    text = merchantData.status,
                                    color =TextGreen,
                                )
                            }
                        }
                    }
                }
            }
            else{
                AnimatedVisibility(visible = showTransaction.value, modifier = Modifier.wrapContentSize()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f).padding(end = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                CardCustomText(
                                    modifier = Modifier, text = "Merchant",
                                    color = ticketTextLightColor,
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em
                                )
                                CardCustomText(
                                    modifier = Modifier, text = merchantData.merchant,
                                    color = authTextColor,
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f).padding(end = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                CardCustomText(
                                    modifier = Modifier, text = "Description",
                                    fontSize =if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = ticketTextLightColor
                                )
                                CardCustomText(
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    modifier = Modifier, text = "Monthly Subs",
                                    color = authTextColor
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f).padding(end = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                CardCustomText(
                                    modifier = Modifier, text = "Start Date",
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = ticketTextLightColor
                                )
                                CardCustomText(
                                    modifier = Modifier, text = merchantData.startDate,
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = authTextColor
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f).padding(end = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                CardCustomText(
                                    modifier = Modifier, text = "End Date",
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = ticketTextLightColor
                                )
                                CardCustomText(
                                    modifier = Modifier, text = merchantData.endDate,
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = authTextColor
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f).padding(end = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                CardCustomText(
                                    modifier = Modifier, text = "Max Amount",
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = ticketTextLightColor
                                )
                                CardCustomText(
                                    modifier = Modifier, text = merchantData.maxAmount,
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = authTextColor
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f).padding(end = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                CardCustomText(
                                    modifier = Modifier, text = "Amount Type",
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = ticketTextLightColor
                                )
                                CardCustomText(
                                    modifier = Modifier, text = merchantData.amountType,
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = authTextColor
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f).padding(end = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                CardCustomText(
                                    modifier = Modifier, text = "Frequency",
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = ticketTextLightColor
                                )
                                CardCustomText(
                                    modifier = Modifier, text = merchantData.frequency,
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = authTextColor
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f).padding(end = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                CardCustomText(
                                    modifier = Modifier, text = "SiHub ID",
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = ticketTextLightColor
                                )
                                CardCustomText(
                                    modifier = Modifier, text = merchantData.siHubId,
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    color = authTextColor
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                CardCustomText(
                                    text = "Status",
                                    color = ticketTextLightColor,
                                    fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                    modifier = Modifier
                                )
                                Row(
                                    modifier = Modifier.height(30.dp).wrapContentHeight()
                                        .background(
                                            color = CancelTextBackground,
                                            shape = RoundedCornerShape(100.dp)
                                        )
                                        .padding(vertical = 4.dp, horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(10.dp).background(
                                            shape = CircleShape,
                                            color = errorColor
                                        )
                                    )
                                    CardCustomText(
                                        modifier = Modifier,
                                        fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em,
                                        text = merchantData.status,
                                        color = errorColor,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}