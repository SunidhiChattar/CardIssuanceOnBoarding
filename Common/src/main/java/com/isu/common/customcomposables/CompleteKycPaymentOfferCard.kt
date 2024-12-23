package com.isu.common.customcomposables

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.R
import kotlinx.coroutines.delay

@Composable
fun CompleteKycPaymentOfferCard() {

    val startAnim = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(true) {
        delay(100)
        startAnim.value = true
    }
    val startOffset = animateOffsetAsState(
        targetValue = if (!startAnim.value) Offset(-700f, -700f) else Offset(-50f, 0f),
        label = "", animationSpec = spring(0.3f, 10f)
    )

    val goldBrush =
        Brush.linearGradient(
            listOf(Color(0xffCEAA44), Color(0XFFF8EF8A), Color(0XFFB28A28)),
            start = startOffset.value
        )
    val silverBrush =
        Brush.linearGradient(
            listOf(Color(0XFF6C6D71), Color(0Xff6F7074).copy(0.3f), Color(0Xff6F7074)),
            start = startOffset.value
        )
    Column(
        modifier = Modifier

            .background(brush = silverBrush, shape = RoundedCornerShape(20f))
            .height(200.dp)
            .widthIn(350.dp, 400.dp)
            .padding(vertical = 22.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                CustomText(
                    text = "SILVER",
                    fontSize = 20.sp.noFontScale(),
                    fontWeight = FontWeight(750),
                    color = White
                )
                CustomText(text = "1 Year", color = White, fontWeight = FontWeight(550))
            }
            CustomText(
                text = "Rs. 150",
                fontSize = 20.sp.noFontScale(),
                fontWeight = FontWeight(750),
                color = White
            )
        }
        Row(
            modifier = Modifier
                .height(1.5.dp)
                .fillMaxWidth()
                .background(White)
        ) {

        }
        Row(verticalAlignment = Alignment.Bottom) {

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f).fillMaxSize()
            ) {
                CustomText(
                    text = "KYC and Wallet Balance up to Rs. 2L",
                    fontSize = 15.sp.noFontScale(),
                    fontWeight = FontWeight(550),
                    color = White
                )

                CustomText(
                    text = "Create your UPI ID",
                    fontSize = 15.sp.noFontScale(),
                    fontWeight = FontWeight(550),
                    color = White
                )

                CustomText(
                    text = "UPI/QR Payment & Money transfer",
                    fontSize = 15.sp.noFontScale(),
                    fontWeight = FontWeight(550),
                    color = White
                )

                CustomText(
                    text = "Wallet Balance up to Rs. 2L",
                    fontSize = 15.sp.noFontScale(),
                    fontWeight = FontWeight(550),
                    color = White
                )

            }
            Icon(
                painter = painterResource(R.drawable.arrow_up),
                modifier = Modifier.rotate(90f),
                tint = White,
                contentDescription = ""
            )
        }

    }
    Spacer(modifier = Modifier.height(10.dp))
    Column(
        modifier = Modifier
            .background(brush = goldBrush, shape = RoundedCornerShape(20f))
            .widthIn(350.dp, 400.dp)
            .height(200.dp)
            .fillMaxWidth()
            .padding(vertical = 22.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                CustomText(
                    text = "GOLD",
                    fontSize = 20.sp.noFontScale(),
                    fontWeight = FontWeight(750),
                    color = White
                )
                CustomText(text = "1 Year", color = White, fontWeight = FontWeight(550))
            }
            CustomText(
                text = "Rs. 569",
                fontSize = 20.sp.noFontScale(),
                fontWeight = FontWeight(750),
                color = White
            )
        }
        Row(
            modifier = Modifier
                .height(1.5.dp)
                .fillMaxWidth()
                .background(White)
        ) {

        }
        Row(verticalAlignment = Alignment.Bottom) {

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f).fillMaxSize()
            ) {
                CustomText(
                    text = "KYC and Wallet Balance up to Rs. 2L",
                    fontSize = 15.sp.noFontScale(),
                    fontWeight = FontWeight(550),
                    color = White
                )

                CustomText(
                    text = "Create your UPI ID",
                    fontSize = 15.sp.noFontScale(),
                    fontWeight = FontWeight(550),
                    color = White
                )

                CustomText(
                    text = "UPI/QR Payment & Money transfer",
                    fontSize = 15.sp.noFontScale(),
                    fontWeight = FontWeight(550),
                    color = White
                )

                CustomText(
                    text = "Wallet Balance up to Rs. 2L",
                    fontSize = 15.sp.noFontScale(),
                    fontWeight = FontWeight(550),
                    color = White
                )

            }
            Icon(
                painter = painterResource(R.drawable.arrow_up),
                modifier = Modifier.rotate(90f),
                tint = White,
                contentDescription = ""
            )
        }

    }
}
