package com.isu.cardissuanceonboarding.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.isu.cardissuanceonboarding.R
import com.isu.cardissuanceonboarding.common.CustomButton
import com.isu.cardissuanceonboarding.presentation.ui.theme.TextColorDark
import com.isu.common.customcomposables.noFontScale
import com.isu.common.navigation.SplashScreens

@Composable
fun SplashDashboard(navHostController: NavHostController) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Scaffold(
        topBar = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                Image(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth(),
                    painter = painterResource(id = com.isu.common.R.drawable.odishafclogo),
                    contentDescription = stringResource(R.string.iserveu)
                )
            }
        }
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        Column (modifier = Modifier
            .padding(top = it.calculateTopPadding())
            .fillMaxWidth()
            .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally){
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .height((0.4 * screenHeight).dp)
                            .wrapContentWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.FillHeight,
                        painter = painterResource(id = R.drawable.all_cards),
                        contentDescription = stringResource(R.string.splash)
                    )
                    com.isu.common.customcomposables.CustomText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        text = stringResource(R.string.unlock_the_world_of_possibilities),
                        color = TextColorDark,
                        fontSize = 34.sp.noFontScale(),
                        fontWeight = FontWeight(550),
                        textAlign = TextAlign.Center,
                        lineHeight = 43.sp.noFontScale()
                    )
                    com.isu.common.customcomposables.CustomText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        text = stringResource(R.string.purchase_add_modify_or_share_your_cards_seamlessly_with_the_card_issuance_app),
                        color = TextColorDark,
                        fontSize = 18.sp.noFontScale(),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp.noFontScale()
                    )

                }
                CustomButton {
                    navHostController.navigate(SplashScreens.SplashScreen)
                }
        }
    }
}