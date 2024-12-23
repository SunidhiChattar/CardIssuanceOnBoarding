package com.isu.profile.presentation.screens.customersupport.presentation.screens.getintouch

import com.isu.common.utils.UiText


/**
 *  @author-karthik
 *  Data class representing the UI state for the Get In Touch screen.
 * Get in touch ui state
 *
 * @property firstName The first name of the user.
 * @property lastName The last name of the user.
 * @property companyName The name of the company.
 * @property phoneNumber The phone number of the user.
 * @property companySize The size of the company.
 * @property country The country where the user or company is located.
 * @property pointOfDiscussion The main point of discussion or inquiry.
 * @constructor Create empty Get in touch ui state
 */
data class GetInTouchUiState(
    val firstName: String = "", // The first name of the user.
    val firstNameError: Boolean = false, // Error state for the first name field.
    val firstNameErrorMessage: String = "", // Error message for the first name field.

    val lastName: String = "", // The last name of the user.
    val lastNameError: Boolean = false, // Error state for the last name field.
    val lastNameErrorMessage: String = "", // Error message for the last name field.

    val companyName: String = "", // The name of the company.
    val companyNameError: Boolean = false, // Error state for the company name field.
    val companyNameErrorMessage: String = "", // Error message for the company name field.

    val phoneNumber: String = "", // The phone number of the user.
    val phoneNumberError: Boolean = false, // Error state for the phone number field.
    val phoneNumberErrorMessage: UiText = UiText.StringResource(com.isu.common.R.string.empty), // Error message for the phone number field.

    val companySize: String = "", // The size of the company.
    val companySizeError: Boolean = false, // Error state for the company size field.
    val companySizeErrorMessage: String = "", // Error message for the company size field.

    val country: String = "India", // The country where the user or company is located.
    val countryError: Boolean = false, // Error state for the country field.
    val countryErrorMessage: String = "", // Error message for the country field.

    val pointOfDiscussion: String = "", // The main point of discussion or inquiry.
    val pointOfDiscussionError: Boolean = false, // Error state for the point of discussion field.
    val pointOfDiscussionErrorMessage: String = "", // Error message for the point of discussion field.
)
