package com.isu.authentication.presentation.screens.newflow.newRegistration

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.iserveu.permission.multiplepermission.MyActivityResultCallback
import com.isu.common.R
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomDateField
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.getLatLong
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.APIType
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.models.DataForCardSDK
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.errorColor
import com.isu.common.utils.datastore.PreferenceData
import com.isu.common.utils.datastore.PreferencesKeys

sealed class RegistrationAPIType{
    object INITIATE_API:APIType
    object STATUS_CHECK:APIType
}
@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    registrationState: RegistrationState,
    onEvent: (CommonScreenEvents) -> Unit,
) {
    val context= LocalContext.current
    val scope= rememberCoroutineScope()
    val firstName = registrationState.name
    val firstNameError = registrationState.isNameError
    val firstNameErrorMessage = registrationState.nameErrorMessage

    val lastName = registrationState.lastName
    val lastNameError = registrationState.isLastNameError
    val lastNameErrorMessage = registrationState.lastNameErrorMessage

    val dateOfBirth = registrationState.dateOfBirth
    val dateOfBirthError = registrationState.isDateOfBirthError
    val dateOfBirthErrorMessage = registrationState.dateOfBirthErrorMessage

    val email = registrationState.email
    val emailError = registrationState.isEmailError
    val emailErrorMessage = registrationState.emailErrorMessage

    val gender = registrationState.gender
    val genderError = registrationState.isGenderError
    val genderErrorMessage = registrationState.genderErrorMessage

    val pinCode = registrationState.pinCode
    val pinCodeError = registrationState.isPinCodeError
    val pinCodeErrorMessage = registrationState.pinCodeErrorMessage

    val referralCode = registrationState.referralCode
    val referralCodeError = registrationState.isReferralCodeError
    val referralCodeErrorMessage = registrationState.referralCodeErrorMessage
    val androidId = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
    BackHandler {
        (context as Activity).finish()
    }
    val multiplePermissioin =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            MyActivityResultCallback.onActivityResult(permissions)
        }
    val intentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            MyActivityResultCallback.onActivityResult(result)
        }
    LaunchedEffect(Unit){
        getLatLong(context=context, multiplePermissioin = multiplePermissioin, intentLauncher = intentLauncher,mlatLong = LatLongFlowProvider.latLongFlow, scope = scope)

        onEvent(CommonScreenEvents.SaveToDataStore<String>(
            preferenceData = PreferenceData(PreferencesKeys.ANDROID_ID,androidId)
        ))
    }
    LaunchedEffect(Unit){
        Log.d("API_CALL", "RegistrationScreen: ")
        onEvent(CommonScreenEvents.CallApi<Any>(apiType = RegistrationAPIType.STATUS_CHECK, onSuccess = {}, additionalInfo = androidId))
    }

    Scaffold(containerColor = Color.White) {
        it
        KeyBoardAwareScreen(Modifier.padding(22.dp), shouldScroll = false, backHandler = {
            (context as Activity).finish()
        }) {
            Image(
                modifier = Modifier.heightIn(15.dp),
                painter = painterResource(R.drawable.odishafclogo),
                contentDescription = stringResource(R.string.empty),
            )
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 22.dp)) {
                CustomText(text = "Please enter personla details to register")
            }

            CustomInputField(
                annotatedLabel = buildAnnotatedString {
                    append("FirstName")
                    withStyle(
                        style = SpanStyle(
                            color = errorColor
                        )
                    ) {
                        append("*")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                state = firstName,
                isError = firstNameError,
                errorMessage = firstNameErrorMessage.asString(),
                placeholder = "Enter First Name"
            ) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        type = RegistrationInputField.Name,
                        text = it
                    )
                )
            }
            CustomInputField(
                annotatedLabel = buildAnnotatedString {
                    append("LastName")
                    withStyle(
                        style = SpanStyle(
                            color = errorColor
                        )
                    ) {
                        append("*")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                state = lastName,
                isError = lastNameError,
                errorMessage = lastNameErrorMessage.asString(),
                placeholder = "Enter Last Name"
            ) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        type = RegistrationInputField.LastName,
                        text = it
                    )
                )
            }



            Column {
                CustomText(text = "Gender")
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(1) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = gender == "Male", onClick = {
                                onEvent(
                                    CommonScreenEvents.OnTextChanged(
                                        type = RegistrationInputField.Gender,
                                        text = "Male"
                                    )
                                )
                            }, colors = RadioButtonDefaults.colors(
                                selectedColor = if (genderError) errorColor else appMainColor,
                                unselectedColor = if (genderError) errorColor else appMainColor
                            )
                            )
                            CustomText(text = "Male")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = gender == "Female",
                                onClick = {
                                    onEvent(
                                        CommonScreenEvents.OnTextChanged(
                                            type = RegistrationInputField.Gender,
                                            text = "Female"
                                        )
                                    )
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = if (genderError) errorColor else appMainColor,
                                    unselectedColor = if (genderError) errorColor else appMainColor
                                )
                            )
                            CustomText(text = "Female")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = gender == "Others", onClick = {
                                onEvent(
                                    CommonScreenEvents.OnTextChanged(
                                        type = RegistrationInputField.Gender,
                                        text = "Others"
                                    )
                                )
                            }, colors = RadioButtonDefaults.colors(
                                selectedColor = if (genderError) errorColor else appMainColor,
                                unselectedColor = if (genderError) errorColor else appMainColor
                            )
                            )
                            CustomText(text = "Others")
                        }
                    }
                }
                if (genderError) {
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.ic_circ_error),
                            "",
                            tint = errorColor
                        )
                        CustomText(text = genderErrorMessage.asString(), color = errorColor)
                    }
                }
            }
            CustomDateField(
                annotatedLabel = buildAnnotatedString {
                    append("DOB")

                    withStyle(style = SpanStyle(color = errorColor)) {
                        append("*")
                    }
                },
                pattern = "yyyy-MM-dd",
                placeholder = "Select Date of Birth",
                state = dateOfBirth,
                errorMessage = dateOfBirthErrorMessage.asString(),
                isError = dateOfBirthError
            ) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        type = RegistrationInputField.DateOfBirth,
                        text = it
                    )
                )
            }

            CustomInputField(
                annotatedLabel = buildAnnotatedString {
                    append("Email")

                    withStyle(style = SpanStyle(color = errorColor)) {
                        append("*")
                    }
                },
                placeholder = "Enter Email",
                state = email,
                isError = emailError,
                errorMessage = emailErrorMessage.asString()
            ) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        type = RegistrationInputField.Email,
                        text = it
                    )
                )
            }
            CustomInputField(
                annotatedLabel = buildAnnotatedString {
                    append("PIN Code")

                    withStyle(style = SpanStyle(color = errorColor)) {
                        append("*")
                    }
                },
                supportingText = {
                    if (registrationState.pinCodeDataLoading == true) {
                        CircularProgressIndicator(
                            color = appMainColor,
                            modifier = Modifier.size(15.dp),
                            strokeWidth = 1.5.dp
                        )
                    } else if (registrationState.pincodeData.isNotEmpty()) {
                        CustomText(
                            text = registrationState.pincodeData,
                            fontSize = 12.sp.noFontScale(),
                            color = appMainColor,
                            modifier = Modifier.graphicsLayer { translationX = -30f })
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                placeholder = "Enter PIN Code",
                state = pinCode,
                isError = pinCodeError,
                errorMessage = pinCodeErrorMessage.asString()
            ) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        type = RegistrationInputField.PinCode,
                        text = it
                    )
                )
            }
            if (registrationState.pincodeData.isNotEmpty()) {
                Spacer(Modifier.heightIn(10.dp))
            }
            CustomInputField(
                annotatedLabel = buildAnnotatedString {
                    append("Referral Code")
                },
                placeholder = "Enter Referral Code",
                state = referralCode,
                isError = referralCodeError,
                errorMessage = referralCodeErrorMessage.asString()
            ) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        type = RegistrationInputField.ReferalCode,
                        text = it
                    )
                )
            }
            CustomButton(
                enabled = !registrationState.pincodeDataError && !registrationState.pinCodeDataLoading,
                onClick = {
                onEvent(CommonScreenEvents.OnClick<Any>(RegistrationButtonType.RegistrationButton))
            }, text = "Continue")
            Spacer(Modifier.height(30.dp))
        }
    }
}
fun sendToCardSDK(context: Context, launcherForCardSDK: ManagedActivityResultLauncher<Intent, ActivityResult>, data: DataForCardSDK) {
    val packageManager: PackageManager = context.packageManager
    val jsonStringData= Gson().toJson(data)
    Log.d("DATA_TO_ONBOARD", "sendToOnBoardingSDK: ${jsonStringData}")

    val intent = packageManager.getLaunchIntentForPackage("com.isu.cardissuance")?.apply {
        // Add custom data to the intent
        putExtra("data", jsonStringData)
    }
    intent?.addCategory(Intent.CATEGORY_LAUNCHER)
    if(intent!=null){
        launcherForCardSDK.launch(intent)
    }else{
        Toast.makeText(context, "Intent was null", Toast.LENGTH_SHORT).show()
    }
}