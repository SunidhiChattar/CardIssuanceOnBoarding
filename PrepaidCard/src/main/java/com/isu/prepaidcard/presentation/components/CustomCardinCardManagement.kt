package com.isu.prepaidcard.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.isu.common.R
import com.isu.common.customcomposables.CardCustomText
import com.isu.common.ui.theme.appMainColor
import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.prepaidcard.data.response.CardDataItem

/**
 * This composable function displays a customizable card for card management.
 *
 * @param modifier An optional modifier to be applied to the card element.
 * @param cardDetails A required object of type `CardFeature` that contains information about the card, such as color and potentially expiry date/encrypted card number (assuming getters exist).
 * @param leadingIconOrText An optional lambda expression that defines the content to be displayed on the left side of the card.
 * @param trailingIconOrText An optional lambda expression that defines the content to be displayed on the right side of the card.
 * @param onViewBalanceClick An optional callback function to be invoked when the "View Balance" button is clicked.
 *
 * @Composable This annotation indicates that the function is composable and can be used within a Jetpack Compose composable hierarchy.
 */
@Composable
fun CustomCardinCardManagement(
    modifier: Modifier = Modifier,
    color: Color = appMainColor,
//    cardDetails: CardFeature,
    cardDetails: CardDataItem? = CardDataItem(),
    leadingIconOrText: (@Composable () -> Unit)? = null,
    trailingIconOrText: (@Composable () -> Unit)? = null,
    onCvvClick: (onSuccess: (String) -> Unit) -> Unit = {},
    onViewBalanceClick: (onSuccess: (String) -> Unit) -> Unit = {},
) {

    val startAnimation = remember {
        mutableStateOf(false)
    }
//    val lockState= remember {
//        mutableStateOf(false)
//    }

    LaunchedEffect(key1 = true, block = {
        startAnimation.value = !startAnimation.value
    })
    val animateOffset = animateOffsetAsState(
        targetValue = if (startAnimation.value) Offset(100f, 100f) else Offset(-100f, -110f),
        animationSpec = tween(2000)
    )
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val showCvv = remember {
        mutableStateOf(false)
    }

    val showBalance = remember {
        mutableStateOf(false)
    }
    val balance = remember {
        mutableStateOf("")
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val cardPressed = interactionSource.collectIsPressedAsState()
    val animateFloat = animateFloatAsState(targetValue = if (cardPressed.value) 1.05f else 1f)

    @Composable
    fun calculateScreenHeight(): Int {
        var cardHeight =
            if (screenHeight < 680) (screenHeight * 1 / 3).toInt() else (screenHeight * 1 / 3.5).toInt()

        return cardHeight
    }

    Card(
        modifier = Modifier.fillMaxWidth()
        ,
        colors = CardDefaults.cardColors(cardDetails?.cardColor!!)
    ) {
        Box(
            modifier = Modifier
                .width(340.dp)
                .height(210.dp)
                .background(
                    color = Color.Transparent, shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.pattern), contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIconOrText != null) {
                        leadingIconOrText()
                    }
                    if (trailingIconOrText != null) {
                        trailingIconOrText()
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier
                        .border(
                            BorderStroke(1.dp, Color.White),
                            RoundedCornerShape(50.dp)
                        ), horizontalArrangement = Arrangement.Start) {
                        CardCustomText(
                            modifier = Modifier.clickable(indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                onCvvClick.invoke {
                                    showCvv.value = true
                                }
                            }
                                .padding(10.dp).width(60.dp),
                            text = if (showCvv.value) {
                                val dec = try {
                                    EncryptDecrypt.aesGcmDecryptFromBase64(
                                        encrypted = EncryptedData(
                                            encryptedMessage = cardDetails.decrypted
                                        )
                                    )
                                } catch (e: Exception) {
                                    "123"
                                }
                                dec.toString()
                            } else "CVV",
                            fontSize = if (LocalConfiguration.current.screenWidthDp < 400) {
                                2.5.em
                            } else {
                                2.em
                            },
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                    }
                    Row(modifier = Modifier.border(
                        BorderStroke(1.dp, Color.White),
                        RoundedCornerShape(50.dp)
                    ), horizontalArrangement = Arrangement.Start) {
                        CardCustomText(
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                onViewBalanceClick.invoke {
                                    showBalance.value = true
                                    balance.value = it
                                }
                            }.padding(10.dp).width(110.dp),
                            text = if (showBalance.value) balance.value else "View Balance",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = if (LocalConfiguration.current.screenWidthDp < 400) {
                                3.em
                            } else {
                                2.em
                            }
                        )
                    }


                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CardCustomText(
                        modifier = Modifier.width(120.dp),
//                        text = "EXP XX/XX",
                        text = "EXP ${cardDetails.expiryDate}",
                        color = Color.White,
                        fontSize = if (LocalConfiguration.current.screenWidthDp < 400) {
                            2.5.em
                        } else {
                            2.em
                        }
                    )
                    CardCustomText(
                        modifier = Modifier,
//                        text = "xxxx xxxx xxxx 7690",
                        text = cardDetails.decrypted.let {
                            if (it != null && it.length % 4 == 0) {
                            it.chunked(4).joinToString(" ")
                        }else{
                            ""
                        }
                        },
                        color = Color.White,
                        fontSize = if (LocalConfiguration.current.screenWidthDp < 400) {
                            2.5.em
                        } else {
                            2.em
                        }
                    )
                }
            }
//            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
//                horizontalArrangement = Arrangement.spacedBy(20.dp),
//                verticalAlignment = Alignment.CenterVertically) {
//                Column(modifier = Modifier.fillMaxHeight(),
//                    verticalArrangement = Arrangement.SpaceEvenly,
//                    ) {
//                }
//                Column(modifier = Modifier.fillMaxHeight(),
//                    verticalArrangement =
//                        Arrangement.SpaceEvenly
//                ) {
//                }
//            }
        }
    }

}