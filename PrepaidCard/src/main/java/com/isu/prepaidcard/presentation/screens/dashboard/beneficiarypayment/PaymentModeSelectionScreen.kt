package com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import kotlinx.coroutines.launch

@Composable
fun PaymentModeSelectionScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    Scaffold(topBar = {
        CustomProfileTopBar(text = "Pay")
    }, containerColor = White, bottomBar = {
        Row(Modifier.padding(22.dp)) {
            CustomButton(text = "Proceed to Pay", onClick = {
                scope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.BeneSelectionScreen)
                }
            })
        }

    }) {
        KeyBoardAwareScreen(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(), start = 22.dp, end = 22.dp
            )
        ) {
            CustomInputField(
                trailingIcon = {
                    RadioButton(selected = true, onClick = {})
                },
                placeholder = "To bank Account",
                label = "Select a mode to Pay",
                enabled = false,
                color = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.LightGray, disabledContainerColor = White
                )
            ) {

            }
        }
    }
}










