package com.isu.authentication.presentation.screens.newflow.newRegistration
//package com.isu.authentication.presentation.screens.newRegistration

import com.isu.authentication.presentation.screens.newflow.kyc.OVDTypes
import com.isu.common.utils.UiText

data class RegistrationState(
    val name: String = "",
    val isNameError: Boolean = false,
    val nameErrorMessage: UiText = UiText.DynamicString(""),

    val lastName: String = "",
    val isLastNameError: Boolean = false,
    val lastNameErrorMessage: UiText = UiText.DynamicString(""),

    val dateOfBirth: String = "",
    val isDateOfBirthError: Boolean = false,
    val dateOfBirthErrorMessage: UiText = UiText.DynamicString(""),

    val email: String = "",
    val isEmailError: Boolean = false,
    val emailErrorMessage: UiText = UiText.DynamicString(""),

    val gender: String = "",
    val isGenderError: Boolean = false,
    val genderErrorMessage: UiText = UiText.DynamicString(""),

    val pinCode: String = "",
    val isPinCodeError: Boolean = false,
    val pinCodeErrorMessage: UiText = UiText.DynamicString(""),

    val referralCode: String = "",
    val isReferralCodeError: Boolean = false,
    val referralCodeErrorMessage: UiText = UiText.DynamicString(""),

    val phoneNumber: String = "",
    val isPhoneNumberError: Boolean = false,
    val phoneNumberErrorMessage: UiText = UiText.DynamicString(""),

    val ovdType: OVDTypes = OVDTypes.NONE(),
    val ovdTypeError: Boolean = false,
    val ovdTypeErrorMessage: UiText = UiText.DynamicString(""),

    val ovdNumber: String = "",
    val isOVDNumberError: Boolean = false,
    val ovdNumberErrorMessage: UiText = UiText.DynamicString(""),

    val onBoardingOTP: String = "",
    val onBoardingOTPError: Boolean = false,
    val onBoardingOTPErrorMessage: UiText = UiText.DynamicString(""),

    val twoFAOTP: String = "",
    val twoFAOTPError: Boolean = false,
    val twoFAOTPErrorMessage: UiText = UiText.DynamicString(""),

    val pincodeData: String = "",
    val pinCodeDataLoading: Boolean = false,
    val pincodeDataError: Boolean = false,

    val startTimer: Boolean = false,
    val deviceChangeOTP: String = "",
    val deviceChangeOTPError: Boolean = false,
    val deviceChangeOTPErrorMessage: UiText = UiText.DynamicString(""),

    )
