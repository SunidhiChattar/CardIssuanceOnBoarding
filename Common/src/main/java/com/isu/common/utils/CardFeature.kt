package com.isu.common.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.isu.common.R
import com.isu.common.customcomposables.CardCustomText
import com.isu.common.ui.theme.CardBlue
import com.isu.common.ui.theme.CardGreen
import com.isu.common.ui.theme.CardLightGreen
import com.isu.common.ui.theme.CardRed
import com.isu.common.ui.theme.CreditCardGreen

data class CardFeature(
    val cardColor: Color,
    val leadingIconOrText: (@Composable () -> Unit),
    val trailingIconOrText: (@Composable () -> Unit)
)

data class CircularBoxLayout(
    val icon: Int,
    val iconSize: Dp,
    val text: String
)

val quickAccess = mutableStateListOf(
    CircularBoxLayout(
        icon = R.drawable.kyc_icon, iconSize = 20.dp,
        text = "Complete your KYC"
    ),
    CircularBoxLayout(
        icon = R.drawable.bxs_offer,
        iconSize = 20.dp,
        text = "Cashback"
    ),
    CircularBoxLayout(
        icon = R.drawable.ph_link, iconSize = 20.dp,
        text = "Link\nCard"
    ),

)
val payment = mutableStateListOf(
    CircularBoxLayout(
        icon = R.drawable.send_fill,
        iconSize = 20.dp,
        text = "Pay"
    ),
    CircularBoxLayout(
        icon = R.drawable.tabler_recharging, iconSize = 20.dp,
        text = "Recharge"
    ),
    CircularBoxLayout(
        icon = R.drawable.solar_tv,
        iconSize = 20.dp,
        text = "DTH"
    )
)
val allCards = mutableStateListOf(
    CardFeature(
        cardColor = CardBlue,
        leadingIconOrText = {
            CardCustomText(
                modifier = Modifier.width(150.dp),
                text = "GPR Card",
                color = Color.White,
                fontSize = if (LocalConfiguration.current.screenWidthDp < 400) 3.9.em
                else 2.8.em,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = if (LocalConfiguration.current.screenWidthDp < 400) 25.sp else 18.sp
            )
        },
        trailingIconOrText = {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.card_logo),
                contentDescription = "",
                modifier = Modifier
                    .width(120.dp)
                    .height(30.dp)
            )
        }
    ),
    CardFeature(
        cardColor = CardLightGreen,
        leadingIconOrText = {
            Image(
                painter = painterResource(id = R.drawable.visa_logo),
                contentDescription = "",
                modifier = Modifier
                    .width(120.dp)
                    .height(
                        60.dp
                    )
            )
        },
        trailingIconOrText = {
            CardCustomText(
                modifier = Modifier.width(150.dp),
                text = "GPR Card",
                color = Color.White,
                fontSize = if (LocalConfiguration.current.screenWidthDp < 400) 3.9.em
                else 2.8.em,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = if (LocalConfiguration.current.screenWidthDp < 400) 25.sp else 18.sp
            )
        }
    ),
    CardFeature(
        cardColor = CardGreen,
        leadingIconOrText = {
            CardCustomText(
                modifier = Modifier.width(150.dp),
                text = "GIFT Card",
                color = Color.White,
                fontSize = if (LocalConfiguration.current.screenWidthDp < 400) 3.9.em
                else 2.8.em,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = if (LocalConfiguration.current.screenWidthDp < 400) 25.sp else 18.sp
            )
        },
        trailingIconOrText = {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.card_logo),
                contentDescription = "",
                modifier = Modifier
                    .width(120.dp)
                    .height(30.dp)
            )
        }
    ),
    CardFeature(
        cardColor = CardRed,
        leadingIconOrText = {
            Image(
                painter = painterResource(id = R.drawable.visa_logo),
                contentDescription = "",
                modifier = Modifier
                    .width(120.dp)
                    .height(
                        60.dp
                    )
            )
        },
        trailingIconOrText = {
            CardCustomText(
                modifier = Modifier.width(150.dp),
                text = "Gift Card",
                color = Color.White,
                fontSize = if (LocalConfiguration.current.screenWidthDp < 400) 3.9.em
                else 2.8.em,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = if (LocalConfiguration.current.screenWidthDp < 400) 25.sp else 18.sp
            )
        }
    ),
    CardFeature(
        cardColor = Color.Black,
        leadingIconOrText = {
            CardCustomText(
                modifier = Modifier.width(150.dp),
                text = "Super Saver\nCredit Card",
                color = Color.White,
                fontSize = if (LocalConfiguration.current.screenWidthDp < 400) 3.9.em
                else 2.5.em,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = if (LocalConfiguration.current.screenWidthDp < 400) 25.sp else 18.sp
            )
        },
        trailingIconOrText = {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.card_logo),
                contentDescription = "",
                modifier = Modifier
                    .width(120.dp)
                    .height(30.dp)
            )
        }
    ),
    CardFeature(
        cardColor = CreditCardGreen,
        leadingIconOrText = {
            Image(
                painter = painterResource(id = R.drawable.visa_logo),
                contentDescription = "",
                modifier = Modifier
                    .width(120.dp)
                    .height(
                        60.dp
                    )
            )
        },
        trailingIconOrText = {
            CardCustomText(
                modifier = Modifier.width(150.dp),
                text = "Super Saver\nCredit Card",
                color = Color.White,
                fontSize = if (LocalConfiguration.current.screenWidthDp < 400) 3.9.em
                else 2.5.em,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = if (LocalConfiguration.current.screenWidthDp < 400) 25.sp else 18.sp
            )
        }
    )
)