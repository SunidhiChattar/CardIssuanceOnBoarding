package com.isu.prepaidcard.presentation.screens.dashboard.reserpin

import com.isu.common.utils.UiText

data class ModifyPinState(
    val cardNumber: String = "",
    val cardHolderName: String = "",
    val pin: String = "",
    val pinError: Boolean = false,
    val pinErrorMessage: UiText = UiText.DynamicString(""),
    val confirmPin: String = "",
    val confirmPinError: Boolean = false,
    val confirmPinErrorMessage: UiText = UiText.DynamicString(""),

    )
