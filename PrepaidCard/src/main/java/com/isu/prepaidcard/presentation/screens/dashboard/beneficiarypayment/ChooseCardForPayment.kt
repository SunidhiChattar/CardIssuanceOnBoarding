package com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomProfileDropDown
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.KeyBoardAwareScreen

@Composable
fun ChooseCardForPayment(modifier: Modifier) {
    Scaffold(topBar = {
        CustomProfileTopBar(text = "Pay")
    }, containerColor = White, bottomBar = {
        Row(Modifier.padding(22.dp)) {
            CustomButton(text = "Proceed to Pay")
        }

    }) {
        KeyBoardAwareScreen(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(), start = 22.dp, end = 22.dp
            )
        ) {
            CustomProfileDropDown(placeholder = "Select card", label = buildAnnotatedString {
                append("From Card")
                withStyle(
                    style = SpanStyle(color = Red)
                ) {
                    append("*")
                }
            }) {

            }
        }
    }
}