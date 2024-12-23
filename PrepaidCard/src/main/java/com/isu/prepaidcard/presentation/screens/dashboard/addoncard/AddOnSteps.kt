package com.isu.prepaidcard.presentation.screens.dashboard.addoncard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomCancelButton
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.ui.theme.CardBlue
import com.isu.common.ui.theme.authTextColor

@Composable
fun AddOnSteps() {
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Add Card"
            )
        },
        bottomBar = {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)){
                CustomButton(
                    color = CardBlue,
                    text = "Resend Invite",
                    modifier = Modifier.weight(1f)
                )
                CustomCancelButton(text = "Show all Cards",
                    modifier = Modifier.weight(1f))
            }
        },
        containerColor = Color.White
    ) {
        KeyBoardAwareScreen(shouldScroll = false) {
            Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {

                Column(
                    modifier = Modifier.padding(
                        vertical = 10.dp,
                        horizontal = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomText(text = "Voila! New Card was just Created",
                        fontWeight = FontWeight.Bold)

                    Box(Modifier.widthIn(400.dp, 450.dp).height(230.dp)) {
                        Image(
                            painterResource(com.isu.common.R.drawable.card_bg),
                            modifier = Modifier.widthIn(400.dp, 450.dp).height(250.dp)
                                .aspectRatio(1f),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null
                        )
                        Column(
                            Modifier.fillMaxSize().padding(22.dp),
                            verticalArrangement = Arrangement.Center
                        ) {

                            Column {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = com.isu.common.R.drawable.chip),
                                        "",
                                        modifier = Modifier.size(50.dp)
                                    )
                                    Image(
                                        painter = painterResource(id = com.isu.common.R.drawable.network),
                                        "",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                CustomText(
                                    text = "XXXX XXXX XXXX XXXX",
                                    fontSize = 20.sp.noFontScale(),
                                    color = Color.White
                                )
                            }
                        }
                    }


                    CustomText(text = "The invitation for the Card has been sent to the phone number.",
                        fontWeight = FontWeight(550)
                    )
                    Column(modifier = Modifier.fillMaxWidth()
                        .border(BorderStroke(1.dp,
                        color = authTextColor.copy(0.5f)),
                            shape = RoundedCornerShape(10.dp)
                        ).padding(start = 5.dp, bottom = 5.dp)
                    ) {
                        CustomText(
                            modifier = Modifier.padding(10.dp),
                            text = "Steps for activation",
                            color = Color.Black,
                            fontSize = 16.sp.noFontScale(),
                            fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            CustomText(text = "1.")
                            CustomText(text = "Download the app using the link shared.")
                        }
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            CustomText(text = "2.")
                            CustomText(text = "Invitee signs up using their phone number.")
                        }
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            CustomText(text = "3.")
                            CustomText(text = "Create their own card!")
                        }
                    }
                }
            }
        }
    }
}