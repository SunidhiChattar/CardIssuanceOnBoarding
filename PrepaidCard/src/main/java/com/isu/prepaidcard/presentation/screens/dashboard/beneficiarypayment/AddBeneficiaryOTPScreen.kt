package com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment

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
import androidx.compose.ui.graphics.Color.Companion.White
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
import com.isu.common.navigation.AuthenticationScreens
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.appMainColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddBeneficiaryOTPScreen(modifier: Modifier) {
    val otp = remember {
        mutableStateOf("")
    }
    val otpErr = remember {
        mutableStateOf(false)
    }
    val otpErrMsg = remember {
        mutableStateOf("")
    }
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
    val startTimer = remember {
        mutableStateOf(false)
    }


    // Clear fields on launch
    LaunchedEffect(Unit) {
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

    Scaffold(modifier = modifier, containerColor = White) { paddingValues ->
        KeyBoardAwareScreen(modifier = Modifier.padding(
            22.dp,
            paddingValues.calculateTopPadding(),
            22.dp
        ).heightIn(screenHeight.dp), Alignment.CenterHorizontally, content = {
            Column(
                modifier = Modifier.heightIn(config.screenHeightDp.dp).padding(top = 10.dp)
            ) {
                CustomAuthTopSection(
                    headingText = "Phone Verification",
                    infoText = stringResource(R.string.phn_veriy_body),
                    imageVector = R.drawable.password_bg
                )
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {


                    CustomOTPInputField(
                        otpLength = 4,
                        timer = timer.intValue,
                        state = otp.value,
                        isError = otpErr.value,
                        errorMessage = otpErrMsg.value,
                        onValueChange = {

                        },
                    )
                    Spacer(Modifier.height(20.dp))
                    // Verify Button
                    CustomButton(
                        onClick = {

                            focusManager.clearFocus()
                            scope.launch {

//                                    viewModel.verifyEmail(androidId)

                            }

                        }, text = stringResource(R.string.verify_btn)
                    )
                }
                Spacer(Modifier.height(40.dp))
                Column(
                    modifier = Modifier, verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {

                    // Resend OTP Text
                    CustomText(
                        text = stringResource(R.string.resend_otp),
                        modifier = Modifier.fillMaxWidth()
                            .clickable(enabled = timer.intValue == 0) {


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
        }, backHandler = {
            NavigationEvent.helper.emit(
                NavigationEvent.NavigateToNextScreen(
                    AuthenticationScreens.EnterMobileNumberDeviceBindingScreen
                )
            )
        })
    }
}