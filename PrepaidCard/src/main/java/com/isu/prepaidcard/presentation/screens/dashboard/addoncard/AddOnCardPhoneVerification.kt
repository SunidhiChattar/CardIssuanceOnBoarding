package com.isu.prepaidcard.presentation.screens.dashboard.addoncard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.R
import com.isu.common.customcomposables.CustomAuthTopSection
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomOTPInputField
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.AuthenticationScreens
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.ui.theme.appMainColor
import com.isu.common.utils.UiText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddOnCardPhoneVerification(
    modifier: Modifier = Modifier,
    addOnCardViewModel: AddOnCardViewModel,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val otp = addOnCardViewModel.otp
    val otpError = addOnCardViewModel.otpError
    val otpErrorMessage = addOnCardViewModel.otpErrorMessage
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val timer = remember { mutableIntStateOf(0) }
    val startTimer = remember {
        mutableStateOf(false)
    }
    val androidId = android.provider.Settings.Secure.getString(
        context.contentResolver,
        android.provider.Settings.Secure.ANDROID_ID
    )


    // Clear fields on launch
    LaunchedEffect(Unit) {
        otp.value = ""
        otpError.value = false
        otpErrorMessage.value = ""


    }

    // Timer logic
    LaunchedEffect(key1 = startTimer) {
        timer.intValue = 90
        while (timer.intValue > 0) {
            timer.intValue--
            delay(1000)
        }
    }
    Scaffold(modifier = modifier, containerColor = Color.White) { paddingValues ->
        KeyBoardAwareScreen(
            modifier = Modifier
                .padding(22.dp, paddingValues.calculateTopPadding(), 22.dp)
                .heightIn(screenHeight.dp),
            Alignment.CenterHorizontally,
            content = {
                Column(modifier = Modifier.height(screenHeight.dp).padding(top = 10.dp)) {
                    CustomAuthTopSection(
                        headingText = "Phone Verification",
                        infoText = stringResource(R.string.phn_veriy_body),
                        imageVector = R.drawable.password_bg
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Spacer(modifier = Modifier.heightIn(0.dp))
                        // OTP Input Field
                        CustomOTPInputField(
                            otpLength = 4,
                            timer = timer.intValue,
                            state = otp.value,
                            isError = otpError.value,
                            errorMessage = otpErrorMessage.value,
                            onValueChange = {
                                otp.value = it
                            },
                        )
                        Spacer(Modifier.height(20.dp))
                        // Verify Button
                        CustomButton(
                            onClick = {

                                focusManager.clearFocus()
                                addOnCardViewModel.modifyPin {
                                    scope.launch {
                                        NavigationEvent.helper.navigateTo(ProfileScreen.CardManagementScreen)
//                                    viewModel.verifyEmail(androidId)

                                    }
                                }


                            },
                            text = "Verify and Set Pin"
                        )
                    }
                    Column(
                        modifier = Modifier.heightIn(200.dp, 350.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Resend OTP Text
                        CustomText(
                            text = stringResource(R.string.resend_otp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = timer.intValue == 0) {

                                    addOnCardViewModel.generateOTP {
                                        scope.launch {
                                            ShowSnackBarEvent.helper.emit(
                                                ShowSnackBarEvent.show(
                                                    SnackBarType.SuccessSnackBar,
                                                    UiText.DynamicString("Otp sent success fully")
                                                )
                                            )
                                        }
                                    }
                                },
                            textAlign = TextAlign.Center,
                            color = if (timer.intValue == 0) appMainColor else Color.LightGray
                        )
                        // Additional Information Text
                        CustomText(
                            text = stringResource(R.string.phn_verify_bottom),
                            fontSize = 14.sp.noFontScale(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },

            )
    }
}