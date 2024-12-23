package com.isu.prepaidcard.presentation.screens.dashboard.reserpin

import com.isu.common.events.Clickables
import com.isu.common.events.CommonTextField

sealed class ModifyPinButton : Clickables {

    data object Submit : ModifyPinButton()

    data object Cancel : ModifyPinButton()

}

sealed class ModifyPinTextField : CommonTextField {

    data object Pin : ModifyPinTextField()

    data object ConfirmPin : ModifyPinTextField()

}