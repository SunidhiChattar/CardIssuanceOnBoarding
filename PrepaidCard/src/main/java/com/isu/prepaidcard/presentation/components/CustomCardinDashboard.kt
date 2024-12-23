package com.isu.prepaidcard.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.R
import com.isu.common.customcomposables.CardCustomText
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.noFontScale
import com.isu.common.ui.theme.appMainColor
import com.isu.prepaidcard.data.response.ViewCardDataByRefIdResponse

/**
 * This composable function displays a customizable card within a dashboard.
 *
 * @param modifier An optional modifier to be applied to the card element.
 * @param leadingIconOrText An optional lambda expression that defines the content to be displayed on the left side of the card.
 * @param trailingIconOrText An optional lambda expression that defines the content to be displayed on the right side of the card.
 * @param cardDetails A required object of type `CardFeature` that contains information about the card, such as color and potentially expiry date/encrypted card number (assuming getters exist).
 * @param onCardClick An optional callback function to be invoked when the card is clicked.
 *
 * @Composable This annotation indicates that the function is composable and can be used within a Jetpack Compose composable hierarchy.
 */
@Composable
fun CustomCardinDashboard(
    newCard: String = "",
    color: Color = appMainColor,
    isPrimary: Boolean = true,
    modifier: Modifier = Modifier,
    leadingIconOrText: @Composable() (() -> Unit)? = null,
    trailingIconOrText: @Composable() (() -> Unit)? = null,
    showDetails: MutableState<Boolean>,
    cardDataByRefId: ViewCardDataByRefIdResponse.Data? = null,

    onCardClick: (String) -> Unit = {},

) {

    val startAnimation = remember {
        mutableStateOf(false)
    }

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
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val cardPressed = interactionSource.collectIsPressedAsState()
    val animateFloat = animateFloatAsState(targetValue = if (cardPressed.value) 1.05f else 1f)

    Box {

        Card(
            modifier = Modifier.wrapContentSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {

                }.alpha(if (cardDataByRefId?.isHotlist == true) 0.5f else 1f)
                .scale(animateFloat.value),
            colors = CardDefaults.cardColors(color)
//        colors = CardDefaults.cardColors(appMainClor)
        ) {

            Box(
                modifier = Modifier
                    .widthIn(400.dp, 450.dp)
                    .height(195.dp)
                    .background(
                        color = Color.Transparent, shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                ) {
                    /*AsyncImage(model=cardDataByRefId?.templateURL.toString(), contentDescription = "", modifier =   Modifier.fillMaxSize().aspectRatio(1.5f), contentScale =ContentScale.FillHeight)*/
                    Image(
                        painter = painterResource(R.drawable.pattern),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize().aspectRatio(1.5f),
                        contentScale = ContentScale.FillHeight
                    )

                }


                Column(
                    modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.weight(1f).padding(20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            CardCustomText(
                                modifier = Modifier,
                                lineHeight = 18.sp.noFontScale(),
//                        text = "xxxx xxxx xxxx 7690",

                                text = cardDataByRefId?.encryptedCard.let {
                                    if (showDetails.value) {

                                        if (!it.isNullOrEmpty()) {
                                            if (it.length >= 4) {
                                                it.chunked(4).joinToString(" ")
                                            } else {
                                                it
                                            }

                                        } else {
                                            "XXXX XXXX XXXX ${it?.takeLast(4)}"
                                        }


                                    } else {
                                        "XXXX XXXX XXXX ${it?.takeLast(4)}"
                                    }
                                },
                                fontSize = 14.sp.noFontScale(),
                                color = White,
                                textAlign = TextAlign.End,
                                fontWeight = FontWeight(550),
                            )
                            CardCustomText(
                                modifier = Modifier,
                                lineHeight = 18.sp.noFontScale(),
                                text = cardDataByRefId?.nameOnCard ?: "",
                                fontSize = 14.sp.noFontScale(),
                                color = White,
                                textAlign = TextAlign.End,
                                fontWeight = FontWeight(550),
                            )
                        }
                        if (trailingIconOrText != null) {
                            trailingIconOrText()
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CardCustomText(
                            modifier = Modifier.width(120.dp),
//                        text = "EXP XX/XX",
                            text = " ${if (showDetails.value) cardDataByRefId?.expiryDate else "XX/XX"}",
                            color = White,
                            fontWeight = FontWeight(550),
                            fontSize = 14.sp.noFontScale(),
                            textAlign = TextAlign.Start
                        )
                        /*if (!isPrimary) {

                            CustomText(
                                text = "Set As Primary",
                                color = White,
                                modifier = Modifier.border(
                                    BorderStroke(width = 1.dp, color = White),
                                    shape = RoundedCornerShape(15.dp)
                                ).padding(5.dp).clickable {

                                    onCardClick(cardDataByRefId?.cardRefId.toString())
                                },
                                fontSize = 13.sp.noFontScale()
                            )
                        }*/

                    }

                }
//            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 5.dp),
//                horizontalArrangement = Arrangement.spacedBy(20.dp),
//                verticalAlignment = Alignment.CenterVertically) {
////                Column(modifier = Modifier.fillMaxHeight(),
////                    verticalArrangement = Arrangement.SpaceEvenly,
////                ) {
////                }
//                Column(modifier = Modifier.fillMaxHeight(),
//                    verticalArrangement =
//                        Arrangement.SpaceEvenly
//                    ,
//                ) {
//
//                }
//            }
                if (cardDataByRefId?.isReissued == true) {
                    Box(
                        modifier = Modifier.align(Alignment.TopEnd)
                            .padding(top = 10.dp, start = 80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.goldtag),
                            "",
                            modifier = Modifier.height(30.dp).width(100.dp).aspectRatio(3f),
                            contentScale = ContentScale.FillBounds,
                        )
                        CustomText(
                            text = "Re-issued",
                            color = White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 5.dp),
                            fontSize = 13.sp.noFontScale()
                        )
//                StatusColorComponent(
//                    status = "Re-issued",
//                    color = Color(0XFFCFAC46),
//                    textColor = White,
//                    withCircle = true, circleSize = 3,
//                    modifier = Modifier.align(Alignment.TopEnd)
//                )
                    }
                }
                if (cardDataByRefId?.cardRefId.toString().matches(newCard.toRegex())) {
                    Box(
                        modifier = Modifier.align(Alignment.TopEnd)
                            .padding(top = 10.dp, start = 30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.redtag),
                            "",
                            modifier = Modifier.height(30.dp).width(100.dp).aspectRatio(3f),
                            contentScale = ContentScale.FillBounds,
                        )
                        CustomText(
                            text = "New",
                            color = White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 5.dp),
                            fontSize = 14.sp.noFontScale()
                        )
//                StatusColorComponent(
//                    status = "Re-issued",
//                    color = Color(0XFFCFAC46),
//                    textColor = White,
//                    withCircle = true, circleSize = 3,
//                    modifier = Modifier.align(Alignment.TopEnd)
//                )
                    }

                }
            }
        }
        if (cardDataByRefId?.isHotlist == true) {
            Box(
                Modifier.widthIn(400.dp, 450.dp)
                    .height(195.dp)
                    .background(Color.Black.copy(0.5f), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_block_24),
                    "",
                    tint = White,
                    modifier = Modifier.padding(10.dp)
                )
            }

        }
    }
}
