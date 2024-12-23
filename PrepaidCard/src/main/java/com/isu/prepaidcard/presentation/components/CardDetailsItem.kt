package com.isu.prepaidcard.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.R
import com.isu.common.customcomposables.ProfileText
import com.isu.common.customcomposables.StatusColorComponent
import com.isu.common.customcomposables.noFontScale
import com.isu.common.ui.theme.ActiveTextBackground
import com.isu.common.ui.theme.TextGreen
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.errorColor
import com.isu.common.utils.CARD_STATUS
import com.isu.common.utils.ManageFeatures
import com.isu.prepaidcard.data.response.CardDataItem
import com.isu.prepaidcard.data.response.ViewCardDataByRefIdResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CardDetailsItem(
    modifier: Modifier = Modifier,
    status: String = CARD_STATUS.isActive,
    cardData: ViewCardDataByRefIdResponse.Data? = null,
    cardDatByMobileNumber: CardDataItem? = null,
    balance: String,
    cvv: String = "123",
    showBalance: MutableState<Boolean>,
    showDetails: MutableState<Boolean>,
    cvvToggleState: MutableState<Boolean>,
    onCvvToggle: (onSuccess: () -> Unit) -> Unit = { it.invoke() },
    onShowDetails: (onSuccess: () -> Unit) -> Unit = { it.invoke() },
    onCopyCardNumberr: () -> Unit = {},
    setPrimary: () -> Unit = {},
) {
    val startRotating = remember {
        mutableStateOf(false)
    }
    val rotationDegrees =
        animateFloatAsState(if (startRotating.value) 360f else 0f, animationSpec = tween(2000))
    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .widthIn(max = 500.dp)
            .background(White)
            .padding(start = 20.dp, end = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CVVSwitch(cvv = cvv, cvvToggleState, onCvvToggle = {
                onCvvToggle {
                    it.invoke()
                }
            })
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                startRotating.value = true
                onShowDetails {
                    showBalance.value = !showBalance.value
                    scope.launch {
                        delay(2000)
                        startRotating.value = false
                    }

                }
            }) {


                ProfileText(
                    text = if (showBalance.value) "₹$balance" else "₹",
                    color = Black,
                    fontWeight = FontWeight(400)
                )
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "",
                    tint = Black,
                    modifier = Modifier.size(20.dp).then(
                        if (startRotating.value) {

                            Modifier.rotate(rotationDegrees.value)
                        } else {
                            Modifier
                        }
                    )
                )
            }
            Row(
                Modifier
                    .border(
                        BorderStroke(1.dp, LightGray.copy(0.5f)),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(
                        White,
                        RoundedCornerShape(5.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 10.dp).clickable {


                            showDetails.value = !showDetails.value

                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                ProfileText(
                    text = if (showDetails.value) "Hide Details" else "Show Details",
                    fontSize = 13.sp.noFontScale(),
                    color = Black,
                    fontWeight = FontWeight(400)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (cardData?.isLost == true) {
                    StatusColorComponent(
                        status = ManageFeatures.LOST,
                        color = errorColor.copy(0.2f),
                        textColor = errorColor,
                        withCircle = false
                    )
                } else if (cardData?.isDamage == true) {
                    StatusColorComponent(
                        status = ManageFeatures.DAMAGE,
                        color = errorColor.copy(0.2f),
                        textColor = errorColor,
                        withCircle = false
                    )
                } else if (cardData?.isStolen == true) {
                    StatusColorComponent(
                        status = "Stolen",
                        color = errorColor.copy(0.2f),
                        textColor = errorColor,
                        withCircle = false
                    )
                } else if (cardData?.isActive == true) {
                    StatusColorComponent(
                        status = "Active",
                        color = ActiveTextBackground,
                        textColor = TextGreen,
                        withCircle = false
                    )
                } else if (cardData?.isHotlist == true) {
                    StatusColorComponent(
                        status = ManageFeatures.DE_ACTIVATE,
                        color = errorColor.copy(0.2f),
                        textColor = errorColor,
                        withCircle = false
                    )
                } else if (cardData?.isBlock == true) {
                    StatusColorComponent(
                        status = "Temp-Blocked",
                        color = Color(0xffFEF8EA),
                        textColor = Color(0xffF6BC2F),
                        withCircle = false
                    )
                } else {
                    StatusColorComponent(
                        status = ManageFeatures.INACTIVE,
                        color = errorColor.copy(0.2f),
                        textColor = errorColor,
                        withCircle = false
                    )
                }

                if (cardDatByMobileNumber?.isPrimary == true) {
                    StatusColorComponent(
                        status = "Primary",
                        color = Color(0XFFCFAC46),
                        textColor = White,
                        withCircle = true, circleSize = 3
                    )
                } else if (cardDatByMobileNumber?.isActive == true) {
                    StatusColorComponent(
                        modifier = Modifier.border(
                            1.dp,
                            Color(0XFFCFAC46),
                            RoundedCornerShape(50.dp)
                        ).clickable {
                            setPrimary()
                        },
                        status = "Set Primary",
                        color = White,
                        textColor = Color(0XFFCFAC46),
                        withCircle = true, circleSize = 3

                    )
                }
            }

            Row(
                Modifier

                    .border(
                        BorderStroke(1.dp, LightGray.copy(0.5f)),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(
                        White,
                        RoundedCornerShape(5.dp)
                    )
                    .padding(horizontal = 8.dp).clickable {
                        onCopyCardNumberr()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {


                ProfileText(
                    text = "Copy Card Number",
                    color = Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400)
                )
                IconButton(onClick = { onCopyCardNumberr() }, modifier = Modifier.width(25.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.copy),
                        contentDescription = "",
                        tint = Black
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CVVSwitch(
    cvv: String = "123",
    cvvToggleState: MutableState<Boolean>,
    onCvvToggle: (onSuccess: () -> Unit) -> Unit = { it.invoke() },
) {
    val showCvv = cvvToggleState
    Row(
        Modifier

            .border(BorderStroke(1.dp, LightGray.copy(0.5f)), shape = RoundedCornerShape(5.dp))
            .background(
                White,
                RoundedCornerShape(5.dp)
            )
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        ProfileText(
            text = "CVV",
            color = Black,
            fontSize = 12.sp.noFontScale(),
            fontWeight = FontWeight(400)
        )
        ProfileText(
            text = if (showCvv.value) {
                cvv
        }else{
            "***"
        }, color = Black)
        Switch(checked = showCvv.value, onCheckedChange = {
            onCvvToggle {
                showCvv.value = !showCvv.value
            }
        }, modifier = Modifier.scale(0.6f).width(30.dp), thumbContent = {
            Box(
                Modifier
                    .fillMaxHeight()
                    .background(shape = CircleShape, color = White)
            )
        }, colors = SwitchDefaults.colors(
            checkedThumbColor = White,
            checkedTrackColor = appMainColor.copy(0.5f),
            checkedBorderColor = Transparent,
            uncheckedThumbColor = White,
            uncheckedTrackColor = LightGray,
            uncheckedBorderColor = Transparent,

            )
        )
    }
}
