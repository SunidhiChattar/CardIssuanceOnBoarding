package com.isu.profile.presentation.screens.changepassword

import com.isu.common.utils.UiText

/**
 * Data class representing the UI state for the Change Password screen.
 *
 * @property oldPassword The current password entered by the user.
 * @property newPassword The new password entered by the user.
 * @property confirmPassword The confirmation of the new password entered by the user.
 * @property isPasswordVisible Indicates whether the password should be visible or masked.
 * @property isConfirmPasswordVisible Indicates whether the confirm password should be visible or masked.
 * @property isOldPasswordVisible Indicates whether the old password should be visible or masked.
 * @property oldPasswordError Indicates if there is an error in the old password input.
 * @property newPasswordError Indicates if there is an error in the new password input.
 * @property confirmPasswordError Indicates if there is an error in the confirm password input.
 * @property oldPasswordErrorMessage Error message for the old password input.
 * @property newPasswordErrorMessage Error message for the new password input.
 * @property confirmPasswordErrorMessage Error message for the confirm password input.
 * @property oldPasswordHideState Controls the visibility of the old password (hidden or visible).
 * @property newPasswordHideState Controls the visibility of the new password (hidden or visible).
 * @property confirmPasswordHideState Controls the visibility of the confirm password (hidden or visible).
 */
data class ChangePasswordUiState(
    val otp: String = "",  // The OTP entered by the user.
    val otpError: Boolean = false,  // Indicates if there is an error in the OTP input.
    val otpErrorMessage: UiText = UiText.DynamicString(""),  // Error message for the OTP input.
    val oldPassword: String = "",  // The current password entered by the user.
    val newPassword: String = "",  // The new password entered by the user.
    val confirmPassword: String = "",  // The confirmation of the new password entered by the user.
    val isPasswordVisible: Boolean = false,  // Whether the password should be visible or masked.
    val isConfirmPasswordVisible: Boolean = false,  // Whether the confirm password should be visible or masked.
    val isOldPasswordVisible: Boolean = false,  // Whether the old password should be visible or masked.
    val oldPasswordError: Boolean = false,  // Indicates if there is an error in the old password input.
    val newPasswordError: Boolean = false,  // Indicates if there is an error in the new password input.
    val confirmPasswordError: Boolean = false,  // Indicates if there is an error in the confirm password input.
    val oldPasswordErrorMessage: UiText = UiText.DynamicString(""),  // Error message for the old password input.
    val newPasswordErrorMessage: UiText = UiText.DynamicString(""),  // Error message for the new password input.
    val confirmPasswordErrorMessage: UiText = UiText.DynamicString(""),  // Error message for the confirm password input.
    val oldPasswordHideState: Boolean = false,  // Controls the visibility of the old password (hidden or visible).
    val newPasswordHideState: Boolean = false,  // Controls the visibility of the new password (hidden or visible).
    val confirmPasswordHideState: Boolean = false,  // Controls the visibility of the confirm password (hidden or visible).
)
