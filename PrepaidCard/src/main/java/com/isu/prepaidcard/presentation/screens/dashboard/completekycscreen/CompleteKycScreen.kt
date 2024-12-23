package com.isu.prepaidcard.presentation.screens.dashboard.completekycscreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CompleteKycPaymentOfferCard
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen

@Composable
fun CompleteKycScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Complete KYC"
            )
        },
        containerColor = Color.White
    ) {
        it
        val screenHeight = LocalConfiguration.current.screenHeightDp
        KeyBoardAwareScreen(
            shouldScroll = false,
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = 22.dp,
                bottom = 22.dp,
                end = 22.dp
            )
        ) {
            CustomText(text = "Please choose a package to complete your KYC")
            Spacer(Modifier.height(10.dp))
            CompleteKycPaymentOfferCard()


        }
    }
}