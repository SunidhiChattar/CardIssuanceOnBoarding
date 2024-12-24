package com.isu.authentication.presentation.screens.newflow

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationButtonType
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationInputField
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationState
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationViewModel
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
import kotlinx.coroutines.launch


@Composable
fun PhoneVerificationScreen(
    state: RegistrationState,
    onEvent: (CommonScreenEvents) -> Unit,
    viewModel: RegistrationViewModel,
    modifier: Modifier = Modifier,
) {
    val otp = state.onBoardingOTP
    val otpErr = state.onBoardingOTPError
    val otpErrMsg = state.onBoardingOTPErrorMessage
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
    val startTimer = state.startTimer
    val androidId = android.provider.Settings.Secure.getString(
        context.contentResolver,
        android.provider.Settings.Secure.ANDROID_ID
    )
    val launcherForCardSDK = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result

    }


    // Clear fields on launch
    LaunchedEffect(Unit) {
        viewModel.kycClearField()
        onEvent(CommonScreenEvents.ClearFields)

        /*   otp.value = ""
           otpErr.value = false
           otpErrMsg.value = ""*/


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
                Column(modifier = Modifier.height(config.screenHeightDp.dp).padding(top = 10.dp)) {
                    CustomAuthTopSection(
                        headingText = "KYC Verification",
                        infoText = stringResource(R.string.phn_veriy_body),
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
                            errorMessage = otpErrMsg.asString(),
                            onValueChange = {
                                onEvent(
                                    CommonScreenEvents.OnTextChanged(
                                        type = RegistrationInputField.OnBoardingOtpField,
                                        text = it
                                    )
                                )
                            },
                        )
                        Spacer(Modifier.height(20.dp))
                        // Verify Button
                        CustomButton(
                            onClick = {

                                focusManager.clearFocus()
                                scope.launch {
                                    onEvent(CommonScreenEvents.OnClick<Any>(type = RegistrationButtonType.OtpVerificationButton(context,launcherForCardSDK)))
//                                    viewModel.verifyEmail(androidId)

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
                                    onEvent(CommonScreenEvents.OnClick<Any>(type = RegistrationButtonType.ResendOTP))


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
            backHandler = {
                (context as Activity).finish()
            }
        )
    }
}

@Composable
fun ChangeDeviceBindingScreen(
    state: RegistrationState,
    onEvent: (CommonScreenEvents) -> Unit,
    modifier: Modifier = Modifier,
) {
    val otp = state.deviceChangeOTP
    val otpErr = state.deviceChangeOTPError
    val otpErrMsg = state.deviceChangeOTPErrorMessage
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
    val startTimer = state.startTimer
    val androidId = android.provider.Settings.Secure.getString(
        context.contentResolver,
        android.provider.Settings.Secure.ANDROID_ID
    )


    // Clear fields on launch
    LaunchedEffect(Unit) {
        onEvent(CommonScreenEvents.ClearFields)
        /*   otp.value = ""
           otpErr.value = false
           otpErrMsg.value = ""*/


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
                Column(modifier = Modifier.height(config.screenHeightDp.dp).padding(top = 10.dp)) {
                    CustomAuthTopSection(
                        headingText = "Device Binding Verification",
                        infoText = "To securely link this device to your account, an OTP (One-Time Password) has been sent to your registered mobile number (+XX XXXXXXXX). Please enter the OTP below to complete the binding process.",
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
                            errorMessage = otpErrMsg.asString(),
                            onValueChange = {
                                onEvent(
                                    CommonScreenEvents.OnTextChanged(
                                        type = RegistrationInputField.DeviceChangeOTP,
                                        text = it
                                    )
                                )
                            },
                        )
                        Spacer(Modifier.height(20.dp))
                        // Verify Button
                        CustomButton(
                            onClick = {

                                focusManager.clearFocus()
                                scope.launch {
                                    onEvent(CommonScreenEvents.OnClick<Any>(type = RegistrationButtonType.ChangeDeviceBinding))
//                                    viewModel.verifyEmail(androidId)

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
                                    onEvent(CommonScreenEvents.OnClick<Any>(type = RegistrationButtonType.ResendChangeDeviceBindingOTp))


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
