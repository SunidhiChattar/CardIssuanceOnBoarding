package com.isu.profile.presentation.screens.customersupport.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.ui.theme.errorColor
import com.isu.common.ui.theme.plaeholderColor

@Composable
fun CustomerSupportNewScreen() {
    Scaffold(topBar = {
        CustomProfileTopBar(text = "Help and Support") { }
    }, containerColor = White) {
        KeyBoardAwareScreen(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = 22.dp,
                end = 22.dp,
                bottom = 22.dp
            )
        ) {
            CustomInputField(
                enabled = false,
                labelRequired = false,
                leadingIcon = {
                    Icon(
                        painter = painterResource(com.isu.common.R.drawable.complaint),
                        contentDescription = "",
                        modifier = Modifier.size(18.dp),
                        tint = DarkGray
                    )
                },
                placeholder = "Raise a complaint",
                shape = RoundedCornerShape(8.dp),
                color = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = LightGray,
                    focusedIndicatorColor = DarkGray,
                    disabledIndicatorColor = LightGray.copy(0.5f),
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    disabledContainerColor = White,
                    disabledTextColor = plaeholderColor,
                    errorIndicatorColor = errorColor,
                    errorContainerColor = White,
                )
            )
            CustomInputField(
                enabled = false,
                labelRequired = false,
                leadingIcon = {
                    Icon(
                        painter = painterResource(com.isu.common.R.drawable.complaint),
                        contentDescription = "",
                        modifier = Modifier.size(18.dp),
                        tint = DarkGray
                    )
                },
                shape = RoundedCornerShape(8.dp),
                placeholder = "Raised issues",
                color = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = LightGray,
                    focusedIndicatorColor = DarkGray,
                    disabledIndicatorColor = LightGray.copy(0.5f),
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    disabledContainerColor = White,
                    disabledTextColor = plaeholderColor,
                    errorIndicatorColor = errorColor,
                    errorContainerColor = White,
                )
            )
        }
    }
}