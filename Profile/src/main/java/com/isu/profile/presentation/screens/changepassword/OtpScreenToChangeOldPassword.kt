package com.isu.profile.presentation.screens.changepassword

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.isu.common.R
import com.isu.common.customcomposables.CustomAuthTopSection
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomOTPInputField
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.ui.theme.appMainColor
import kotlinx.coroutines.delay

@Composable
fun OtpScreenTOChangeOldPassword(
    modifier: Modifier = Modifier,
    screenState: ChangePasswordUiState,
    onEvent: (CommonScreenEvents) -> Unit,
) {
    val otp = screenState.otp
    val otpErr = screenState.otpError
    val otpErrMsg = screenState.otpErrorMessage.asString()
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val focusManager = LocalFocusManager.current

    // Timer state
    val timer = remember { mutableIntStateOf(0) }
    val startTimer = remember { mutableStateOf(false) }
    val apiSuccess = remember {
        mutableStateOf(false)
    }

    // Clear fields on launch
    LaunchedEffect(Unit) {

        onEvent(CommonScreenEvents.CallApi<Any>(apiType = ChangePasswordApiType.OtpToChangePassword) {
            apiSuccess.value = true
        })
    }

    // Timer logic
    LaunchedEffect(key1 = startTimer.value) {

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
            shouldScroll = false,
            content = {
                Column(modifier = Modifier.height(config.screenHeightDp.dp).padding(top = 10.dp)) {
                    CustomAuthTopSection(
                        headingText = stringResource(R.string.phone_verification_hd),
                        infoText = stringResource(
                            R.string.phn_veriy_body
                        ),
                        imageVector = R.drawable.password_bg
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Spacer(modifier = Modifier.heightIn(0.dp))
                        // OTP Input Field
                        CustomOTPInputField(
                            otpLength = 6,
                            timer = timer.intValue,
                            state = otp,
                            isError = otpErr,
                            errorMessage = otpErrMsg,
                            onValueChange = {
                                if (it.isDigitsOnly()) {
                                    onEvent(
                                        CommonScreenEvents.OnTextChanged(
                                            it,
                                            ChangePasswordTextField.Otp
                                        )
                                    )
                                }
                            },
                        )
                        Spacer(Modifier.height(20.dp))
                        // Verify Button
                        CustomButton(
                            onClick = {
                                onEvent(CommonScreenEvents.OnClick<Any>(ChangePasswordClickables.VeifyOTP))
                                focusManager.clearFocus()
                            },
                            text = stringResource(R.string.verify_btn)
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
                                    apiSuccess.value = false
                                    onEvent(CommonScreenEvents.OnClick<Any>(ChangePasswordClickables.ResendOTP) {
                                        apiSuccess.value = true
                                    })
                                    startTimer.value = !startTimer.value

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