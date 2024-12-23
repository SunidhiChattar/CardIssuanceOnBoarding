package com.isu.profile.presentation.screens.customersupport.presentation.screens.customesupportscreen

import com.isu.common.events.Clickables

sealed interface CustomerSupportComponentTypes : Clickables {

    data object ToRaiseScreen : CustomerSupportComponentTypes

    data object ToAllTicketScreen : CustomerSupportComponentTypes
}