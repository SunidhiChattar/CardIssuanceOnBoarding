package com.isu.profile.presentation.screens.basicInfo

import com.isu.common.utils.UiText

/**
 * @author-karthik
 * Data class representing the state for the Basic Info Edit screen.
 *
 * @property id The ID of the user.
 * @property idError Indicates if there is an error with the ID field.
 * @property idErrorMessage The error message for the ID field.
 * @property name The name of the user.
 * @property nameError Indicates if there is an error with the name field.
 * @property nameErrorMessage The error message for the name field.
 * @property email The email address of the user.
 * @property emailError Indicates if there is an error with the email field.
 * @property emailErrorMessage The error message for the email field.
 * @property gstin The GSTIN of the user.
 * @property gstinError Indicates if there is an error with the GSTIN field.
 * @property gstinErrorMessage The error message for the GSTIN field.
 * @property invoicingAddress The invoicing address of the user.
 * @property invoicingAddressError Indicates if there is an error with the invoicing address field.
 * @property invoicingAddressErrorMessage The error message for the invoicing address field.
 * @property city The city of the user.
 * @property cityError Indicates if there is an error with the city field.
 * @property cityErrorMessage The error message for the city field.
 * @property state The state of the user.
 * @property stateError Indicates if there is an error with the state field.
 * @property stateErrorMessage The error message for the state field.
 * @property country The country of the user.
 * @property countryError Indicates if there is an error with the country field.
 * @property countryErrorMessage The error message for the country field.
 * @property pincode The pincode of the user.
 * @property pincodeError Indicates if there is an error with the pincode field.
 * @property pincodeErrorMessage The error message for the pincode field.
 * @property kycStatus The KYC status of the user.
 * @property kycStatusError Indicates if there is an error with the KYC status field.
 * @property kycStatusErrorMessage The error message for the KYC status field.
 * @property mobileNumber The mobile number of the user.
 * @property mobileNumberError Indicates if there is an error with the mobile number field.
 * @property mobileNumberErrorMessage The error message for the mobile number field.
 */
data class BasicInfoEditState(
    val id: String = "",
    val idError: Boolean = false,
    val idErrorMessage: UiText = UiText.DynamicString(""),

    val name: String = "",
    val nameError: Boolean = false,
    val nameErrorMessage: UiText = UiText.DynamicString(""),

    val email: String = "",
    val emailError: Boolean = false,
    val emailErrorMessage: UiText = UiText.DynamicString(""),

    val gstin: String = "",
    val gstinError: Boolean = false,
    val gstinErrorMessage: UiText = UiText.DynamicString(""),

    val invoicingAddress: String = "",
    val invoicingAddressError: Boolean = false,
    val invoicingAddressErrorMessage: UiText = UiText.DynamicString(""),

    val city: String = "",
    val cityError: Boolean = false,
    val cityErrorMessage: UiText = UiText.DynamicString(""),

    val state: String = "",
    val stateError: Boolean = false,
    val stateErrorMessage: UiText = UiText.DynamicString(""),

    val country: String = "India",
    val countryError: Boolean = false,
    val countryErrorMessage: UiText = UiText.DynamicString(""),

    val pincode: String = "",
    val pincodeError: Boolean = false,
    val pincodeErrorMessage: UiText = UiText.DynamicString(""),

    val kycStatus: String = "",
    val kycStatusError: Boolean = false,
    val kycStatusErrorMessage: UiText = UiText.DynamicString(""),

    val mobileNumber: String = "",
    val mobileNumberError: Boolean = false,
    val mobileNumberErrorMessage: UiText = UiText.DynamicString(""),
)
