package com.isu.prepaidcard.presentation.screens.dashboard.addoncard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomCancelButton
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomSetPin
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.ui.theme.CardBlue
import com.isu.common.utils.UiText
import com.isu.prepaidcard.presentation.screens.dashboard.CardManagement
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddOnSetPin(viewMdel: AddOnCardViewModel) {
    val pin = viewMdel.pin
    val confirmPin = viewMdel.confirmPin
    val isError = viewMdel.isError
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Set PIN"
            )
        },
        bottomBar = {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)){
                CustomButton(
                    color = CardBlue,
                    text = "Set PIN",
                    modifier = Modifier.weight(1f),
                    onClick = {

                        viewMdel.generateOTP(onSuccess = {
                            scope.launch {
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.SuccessSnackBar,
                                        UiText.DynamicString("OTP sent successfully")
                                    )
                                )
                                delay(500)
                                NavigationEvent.helper.navigateTo(CardManagement.AddOnCardPhoneVerificationSCreen)
                            }
                        })


                    }
                )
                CustomCancelButton(text = "Cancel",
                    modifier = Modifier.weight(1f), onClick = {
                        scope.launch {
                            NavigationEvent.helper.navigateBack()
                        }
                    })
            }
        },
        containerColor = Color.White
    ) {
        KeyBoardAwareScreen(shouldScroll = false) {
            Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
                CustomText(text = "Enter PIN", modifier = Modifier.padding(horizontal = 20.dp),
                    fontWeight = FontWeight.Bold)
                CustomSetPin(
                    pinLength = 4,
                    state = pin.value,
                    onValueChange = {
                      pin.value = it
                    }
                )
                CustomText(text = "Confirm PIN",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                CustomSetPin(
                    isError = isError.value,
                    errorMessage = "Pin doesn't match",
                    pinLength = 4,
                    state = confirmPin.value,
                    onValueChange = {
                      confirmPin.value = it
                        if (confirmPin.value!=pin.value){
                            isError.value = true
                        }else{
                            isError.value = false
                        }
                    }
                )
            }
        }
            }
}