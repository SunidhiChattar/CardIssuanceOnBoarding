package com.isu.prepaidcard.presentation.screens.dashboard.reserpin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.isu.common.events.ShowBottomBarEvent
import com.isu.common.ui.theme.appMainColor
import com.isu.prepaidcard.presentation.screens.dashboard.linkcard.LinkCardButtonType
import com.isu.prepaidcard.presentation.screens.dashboard.linkcard.LinkCardTextFieldType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ModifyPinOtpScreen(modifier: Modifier = Modifier, modifyPinViewModel: ModifyPinViewModel) {
    val otp = modifyPinViewModel.otp
    val otpErr = modifyPinViewModel.otpError
    val otpErrMsg = modifyPinViewModel.otpErrorMessage
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val latLong = remember {
        mutableStateOf("")
    }


    // Timer state
    val timer = remember { mutableIntStateOf(0) }
    val startTimer = remember { mutableStateOf(false) }
    val androidId = android.provider.Settings.Secure.getString(
        context.contentResolver,
        android.provider.Settings.Secure.ANDROID_ID
    )


    // Clear fields on launch
    LaunchedEffect(Unit) {
        /*   otp.value = ""
           otpErr.value = false
           otpErrMsg.value = ""*/
        ShowBottomBarEvent.hide()
        otp.value = ""
        otpErr.value = false
        otpErrMsg.value = ""


    }
    LaunchedEffect(Unit) {
        otp.value = ""

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
            content = {
                Column(modifier = Modifier.height(config.screenHeightDp.dp).padding(top = 10.dp)) {
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
                            isError = otpErr.value,
                            errorMessage = otpErrMsg.value,
                            onValueChange = {
                                otp.value = it
                            },
                        )
                        Spacer(Modifier.height(20.dp))
                        // Verify Button
                        CustomButton(
                            onClick = {

                                focusManager.clearFocus()
                                scope.launch {
                                    modifyPinViewModel.modifyPin()
//


                                }

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

//                                viewModel.generateEmailVerificationOTP {
//                                        startTimer.value = !startTimer.value
//                                    }
                                    modifyPinViewModel.generateOtp {

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