package com.isu.prepaidcard.presentation.screens.dashboard.linkcard

import com.isu.common.events.Clickables
import com.isu.common.events.CommonTextField

sealed class LinkCardTextFieldType : CommonTextField {
    data object OtpField : LinkCardTextFieldType()
    data object CardRefferenceNumberField : LinkCardTextFieldType()

}

sealed class LinkCardButtonType : Clickables {
    data object OtpVerificationButton : LinkCardButtonType()
    data object LinkCardButton : LinkCardButtonType()


}