package com.isu.profile.presentation.screens.basicInfo

/**
 * @author-karthik
 * Represents the state of the Basic Info screen.
 *
 * @property id The unique identifier for the profile.
 * @property name The name of the user.
 * @property email The email address of the user.
 * @property gstin The GSTIN of the user.
 * @property invoicingAddress The invoicing address of the user.
 * @property city The city associated with the invoicing address.
 * @property state The state associated with the invoicing address.
 * @property country The country associated with the invoicing address.
 * @property pincode The postal code associated with the invoicing address.
 * @property kycStatus The current KYC status of the user.
 * @property mobileNumber The mobile number of the user.
 */
data class BasicInfoState(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val gstin: String = "",
    val invoicingAddress: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "India",
    val pincode: String = "",
    val kycStatus: String = "",
    val mobileNumber: String = "",
)
