package com.isu.prepaidcard.presentation.screens.dashboard.reserpin

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.events.CommonScreenEvents
import com.isu.common.ui.theme.appMainColor
import com.isu.common.utils.datastore.PreferencesKeys

@Composable
fun ModifyPin(
    modifier: Modifier = Modifier,
    state: ModifyPinState,
    onEvent: (CommonScreenEvents) -> Unit,
) {
    val config = LocalConfiguration.current
    LaunchedEffect(Unit) {
        onEvent(CommonScreenEvents.GetDataStoreData(preferenceData = PreferencesKeys.CARD_REF_ID))
        onEvent(CommonScreenEvents.ClearFields)
    }
    Scaffold(topBar = {
        CustomProfileTopBar(text = "Modify Pin")
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
                        label = "Card Number",
                        state = "XXXX XXXX XXXXX ${state.cardNumber.takeLast(4)}"
                    ) {

                    }


                    CustomInputField(
                        label = "Enter PIN",
                        placeholder = "PIN",
                        state = state.pin,
                        isError = state.pinError,
                        errorMessage = state.pinErrorMessage.asString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    ) {
                        onEvent(
                            CommonScreenEvents.OnTextChanged(
                                type = ModifyPinTextField.Pin,
                                text = it
                            )
                        )
                    }
                    CustomInputField(
                        label = "Confirm PIN",
                        placeholder = "Confirm PIN",
                        state = state.confirmPin,
                        isError = state.confirmPinError,
                        errorMessage = state.confirmPinErrorMessage.asString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                    ) {
                        onEvent(
                            CommonScreenEvents.OnTextChanged(
                                type = ModifyPinTextField.ConfirmPin,
                                text = it
                            )
                        )
                    }
                }
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomButton(text = "Submit", onClick = {
                            onEvent(CommonScreenEvents.OnClick<Any>(type = ModifyPinButton.Submit))
                        }, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(10.dp))
                        CustomButton(
                            onClick = {
                                onEvent(CommonScreenEvents.OnClick<Any>(type = ModifyPinButton.Cancel))
                            },
                            modifier = Modifier.weight(1f)
                                .border(
                                    BorderStroke(
                                        1.dp, Color.LightGray.copy(0.6f)
                                    ), shape = RoundedCornerShape(5.dp)
                                ),
                            innerComponent = {
                                CustomText(
                                    text = "Cancel",
                                    color = appMainColor
                                )
                            },
                            color = Color.White
                        )


                    }
                }
            }

        }
    }
}