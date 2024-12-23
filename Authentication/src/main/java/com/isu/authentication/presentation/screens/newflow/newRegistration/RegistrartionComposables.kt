package com.isu.authentication.presentation.screens.newflow.newRegistration

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import com.isu.common.events.Clickables
import com.isu.common.events.CommonTextField

sealed interface RegistrationInputField : CommonTextField {
    data object Mobile : RegistrationInputField
    data object Name : RegistrationInputField
    data object LastName : RegistrationInputField
    data object DateOfBirth : RegistrationInputField
    data object Email : RegistrationInputField
    data object PinCode : RegistrationInputField
    data object ReferalCode : RegistrationInputField
    data object Gender : RegistrationInputField
    data object OnBoardingOtpField : RegistrationInputField
    data object TwoFAOtpField : RegistrationInputField
    data object OVDType : RegistrationInputField
    data object OVDNumber : RegistrationInputField
    data object DeviceChangeOTP : RegistrationInputField

}

sealed interface RegistrationButtonType : Clickables {
    data object RegistrationButton : RegistrationButtonType
    data object ChangeDeviceBinding : RegistrationButtonType
    data object ResendOTP : RegistrationButtonType
    data object ResendTwoFAOTP : RegistrationButtonType
    data object EnterPhoneNumberSubmitButton : RegistrationButtonType
    data object OtpVerificationButton : RegistrationButtonType
    data object TwoFAOtpVerificationButton : RegistrationButtonType
    data class KycOtpGenerationButton(val context : Context,val launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) : RegistrationButtonType
    data object ResendChangeDeviceBindingOTp : RegistrationButtonType
}