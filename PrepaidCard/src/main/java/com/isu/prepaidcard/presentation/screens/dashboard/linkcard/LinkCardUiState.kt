package com.isu.prepaidcard.presentation.screens.dashboard.linkcard

import com.isu.common.utils.UiText

data class LinkCardUiState(
    val otp: String = "",
    val otpError: Boolean = false,
    val otpErrorMessage: UiText = UiText.StringResource(com.isu.common.R.string.empty),

    val cardRefferenceNumber: String = "",
    val cardRefferenceNumberError: Boolean = false,
    val cardRefferenceNumberErrorMessage: UiText = UiText.StringResource(com.isu.common.R.string.empty),

    )