package com.isu.authentication.presentation.screens.newflow.kyc

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationButtonType
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationInputField
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationState
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomClosingTopBar
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomLoader
import com.isu.common.customcomposables.CustomProfileDropDown
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.ui.theme.appMainColor

sealed class OVDTypes(val type: String) {
    data class Aadhaar(val aadharType: String = "AADHAR") : OVDTypes(aadharType)
    data class PAN(val panType: String = "PAN") : OVDTypes(panType)
    data class Passport(val passportType: String = "PAASPORT") : OVDTypes(passportType)
    data class DrivingLicense(val drivingLicenseType: String = "DL") :
        OVDTypes(drivingLicenseType)
    data class VoterID(val voterIDType: String = "VOTERID") : OVDTypes(voterIDType)
    data class NONE(val noneType: String = "") : OVDTypes(noneType)
}

/**
 * <item>Aadhhar</item>
 *         <item>Paasport</item>
 *         <item>Driving License</item>
 *         <item>Voter ID</item>
 */
/**
 * Kyc info screen
 *
 * @param modifier
 * @param viewModel
 */
@Composable
fun KycInfoScreen(
    modifier: Modifier = Modifier,
    state: RegistrationState,
    onEvent: (CommonScreenEvents) -> Unit,
) {
    val simCount = remember { mutableStateOf(1) }
    val selectedSim = remember {
        mutableStateOf(0)
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val mobileNumber = state.phoneNumber
    val mobileNumberError = state.isPhoneNumberError
    val mobileNumberErrorMessage = state.phoneNumberErrorMessage

    val showLoader = remember {
        mutableStateOf(false)
    }
    val ovdType: OVDTypes = state.ovdType
    val ovdTypeError: Boolean = state.ovdTypeError
    val ovdTypeErrorMessage: String = state.ovdTypeErrorMessage.asString()
    val ovdNumber: String = state.ovdNumber
    val ovdNumberError = state.isOVDNumberError
    val ovdNumberErrorMessage = state.ovdNumberErrorMessage
    val listOfOVD = listOf(
        OVDTypes.PAN(),
        OVDTypes.Aadhaar(),
        OVDTypes.VoterID(),
        OVDTypes.Passport(),
        OVDTypes.DrivingLicense()
    )
    val launcherForCardSDK = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result

    }

    /*
        val phoneNumberHintIntentResultLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { res ->
                try {
                    mobileNumber =
                        Identity.getSignInClient(context).getPhoneNumberFromIntent(res.data)
                            .takeLast(10)
                } catch (e: Exception) {
                    Log.e("KAPI", "Phone Number Hint failed")
                }
            }
        val request: GetPhoneNumberHintIntentRequest = GetPhoneNumberHintIntentRequest.builder().build()
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    */

    Scaffold(topBar = {
        CustomClosingTopBar { }
    }, containerColor = Color.White) {
        it
        if (showLoader.value) {
            CustomLoader()
        }

        KeyBoardAwareScreen(modifier = Modifier.padding(it)) {
            Column(
                Modifier.heightIn(screenHeight.dp - 100.dp).fillMaxWidth()
                    .padding(horizontal = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    CustomText(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp.noFontScale(),
                        text = "KYC Info"
                    )

                    CustomInputField(
                        annotatedLabel = buildAnnotatedString {
                            append("KYC-TYPE")
                            withStyle(
                                SpanStyle(color = Color.Red)
                            ) {
                                append("*")
                            }
                        },
                        placeholder = "Enter Your Mobile Number",
                        state = "Min-KYC",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        enabled = false,
                        onValueChange = {

                        },
                        errorMessage = mobileNumberErrorMessage.asString(),
                        isError = mobileNumberError
                    )
                    CustomProfileDropDown(
                        state = ovdType.type,
                        items = listOfOVD.map { it.type }.toTypedArray(),
                        placeholder = "Select the OVD Type",
                        isError = ovdTypeError,
                        errorMessage = ovdTypeErrorMessage,
                        label = buildAnnotatedString {
                            append("OVD Type")
                            withStyle(
                                SpanStyle(color = Color.Red)
                            ) {
                                append("*")
                            }
                        },


                        onSelected = { typ ->
                            onEvent(
                            CommonScreenEvents.OnTextChanged(
                                text = typ,
                                type = RegistrationInputField.OVDType
                            )
                            )
                        })
                    CustomInputField(
                        annotatedLabel = buildAnnotatedString {
                            append("OVD Number")
                            withStyle(
                                SpanStyle(color = Color.Red)
                            ) {
                                append("*")
                            }
                        },
                        placeholder = "Enter Your OVD Number",
                        state = ovdNumber,
                        isError = ovdNumberError,
                        errorMessage = ovdNumberErrorMessage.asString(),
                        onValueChange = {
                            onEvent(
                                CommonScreenEvents.OnTextChanged(
                                    text = it,
                                    type = RegistrationInputField.OVDNumber
                                )
                            )

                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = if (ovdType.type == OVDTypes.Passport().type) {
                                KeyboardCapitalization.Characters
                            } else
                                KeyboardCapitalization.Unspecified
                        )

                        )


                }
                CustomButton(appMainColor, text = "Continue", onClick = {
                    onEvent(CommonScreenEvents.OnClick<Any>(RegistrationButtonType.KycOtpGenerationButton(context,launcherForCardSDK)))
//                    scope.launch {
//                        NavigationEvent.helper.navigateTo(AuthenticationScreens.PhoneVerificationScreen)
//                    }

                })
            }
        }
    }
}