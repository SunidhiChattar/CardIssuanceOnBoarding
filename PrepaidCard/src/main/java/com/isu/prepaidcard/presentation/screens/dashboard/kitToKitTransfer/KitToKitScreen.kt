package com.isu.prepaidcard.presentation.screens.dashboard.kitToKitTransfer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.appMainColor
import kotlinx.coroutines.launch

@Composable
fun KitToKitScreen(modifier: Modifier = Modifier, kitToKitViewModel: KitToKitViewModel) {
    val config = LocalConfiguration.current
    val toCard = kitToKitViewModel.toCard
    val toCardError = kitToKitViewModel.toCardError
    val toCardErrorMessage = kitToKitViewModel.toCardErrorMessage
    val cardHolderName = kitToKitViewModel.cardHolderName
    val cardNumber = kitToKitViewModel.fromCard
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        kitToKitViewModel.clearFields()
        kitToKitViewModel.getDataStoreData()
    }
    Scaffold(topBar = {
        CustomProfileTopBar(text = "Kit to Kit Transfer")
    }, containerColor = Color.White) {
        KeyBoardAwareScreen(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = 22.dp,
                end = 22.dp,
                bottom = 0.dp,
            ),
            shouldScroll = false
        ) {
            Column(
                modifier = Modifier.heightIn(config.screenHeightDp.dp - (it.calculateTopPadding() + 90.dp))
                    .fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {

                    CustomInputField(
                        enabled = false,
                        label = "Card Holder Name",
                        state = "${cardHolderName.value}"
                    ) {

                    }
                    CustomInputField(
                        enabled = false,
                        label = "From Card",
                        state = "XXXX XXXX XXXX ${
                            cardNumber.value.let {
                                if (it.length > 4) {
                                    it.takeLast(4)
                                } else {
                                    it
                                }

                            }
                        }"
                    ) {

                    }


                    CustomInputField(
                        label = "To Card",
                        placeholder = "Enter card number",
                        state = toCard.value,
                        isError = toCardError.value,
                        errorMessage = toCardErrorMessage.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    ) {
                        if (it.isDigitsOnly()) {
                            if (it.length <= 16) {
                                if (it.length == 16) {
                                    toCard.value = it
                                    toCardError.value = false
                                    toCardErrorMessage.value = ""
                                } else {
                                    toCard.value = it
                                    toCardError.value = true
                                    toCardErrorMessage.value = "Invalid Card"
                                }


                            }
                        }
                    }
                }
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomButton(text = "Submit", onClick = {
                            if (toCard.value.isEmpty()) {
                                toCardError.value = true
                                toCardErrorMessage.value = "Field cannot be empty"
                            } else if (!toCardError.value) {
                                kitToKitViewModel.kitToKitTransferBalance()
                            }

                        }, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(10.dp))
                        CustomButton(
                            modifier = Modifier.weight(1f)
                                .border(
                                    BorderStroke(1.dp, Color.LightGray.copy(0.6f)),
                                    shape = RoundedCornerShape(5.dp)
                                ),
                            innerComponent = {
                                CustomText(
                                    text = "Cancel",
                                    color = appMainColor
                                )
                            },
                            onClick = {
                                scope.launch {
                                    NavigationEvent.helper.navigateBack()
                                }
                            },
                            color = Color.White
                        )


                    }
                }
            }

        }
    }
}