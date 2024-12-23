package com.isu.authentication


import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationState
import com.isu.common.customcomposables.signIn.AddressScreenState
import com.isu.common.customcomposables.signIn.CardInfoSCreenState
import com.isu.common.customcomposables.signIn.PersonalInfoScreenState
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.StateEvent


data class AuthModuleStateEvent(

    val permission: StateEvent<Any, CommonScreenEvents>,
    val cardInfo: StateEvent<CardInfoSCreenState, CommonScreenEvents>,
    val personalInfo: StateEvent<PersonalInfoScreenState, CommonScreenEvents>,
    val addressInfo: StateEvent<AddressScreenState, CommonScreenEvents>,
    val registrationState: StateEvent<RegistrationState, CommonScreenEvents>,
)