package com.isu.prepaidcard.presentation.screens.dashboard.tokens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CardCustomText
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.ui.theme.authTextColor

/**
 * This composable function represents the tokens screen of the application.
 *
 * @Composable This annotation indicates that the function is a composable and can be used within a Jetpack Compose composable hierarchy.
 */
@Composable
fun Tokens(){

    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Tokens"
            )
        },
        containerColor = Color.White
    ) {

        Column(
            modifier = Modifier.background(Color.White).padding(top = it.calculateTopPadding())
        ) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment
                    .CenterHorizontally) {
                Image(
                    painter = painterResource(com.isu.common.R.drawable.token),
                    "",
                    modifier = Modifier
                )
            }
        }
    }
}