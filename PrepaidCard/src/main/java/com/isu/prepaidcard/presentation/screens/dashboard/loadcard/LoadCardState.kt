package com.isu.prepaidcard.presentation.screens.dashboard.loadcard

import androidx.compose.ui.graphics.Color
import com.isu.common.utils.UiText

data class UpiPaymentInfo(
    val ammount: String? = null,
    val txnId: String? = null,
    val txnRef: String? = null,
    val status: UpiPaymentStatus? = null,
    val responseCode: String? = null,
    val approvalRefNo: String? = null,
)

sealed class UpiPaymentStatus(
    val image: Int,
    val uiText: String? = null,
    val color: Color? = null,
) {
    data class Success(
        val successImage: Int = com.isu.common.R.drawable.load_card_success,
        val successUiText: String = "Funds added",
        val successColor: Color = Color(0xff0D763E),
    ) : UpiPaymentStatus(successImage, successUiText, successColor)

    data class Failure(
        val failureImage: Int = com.isu.common.R.drawable.load_card_failure,
        val failureUiText: String = "Funds not Added",
        val failureColor: Color = Color(0xffDE1135),
    ) : UpiPaymentStatus(failureImage, failureUiText, failureColor)

    data class Pending(val pendingImage: Int = com.isu.common.R.drawable.load_card_failure) :
        UpiPaymentStatus(pendingImage)

    data class Cancelled(val cancelledImage: Int = com.isu.common.R.drawable.load_card_failure) :
        UpiPaymentStatus(cancelledImage)
}

data class LoadCardState(
    val cardRefId: String = "",
    val selectedCard: String = "",
    val payIntentUri: String? = null,
    val amountToLoad: String = "",
    val amountToLoadError: Boolean = false,
    val amountToLoadErrorMessage: UiText = UiText.StringResource(com.isu.common.R.string.empty),
    val loadCardResult: UpiPaymentInfo? = null,


    )
