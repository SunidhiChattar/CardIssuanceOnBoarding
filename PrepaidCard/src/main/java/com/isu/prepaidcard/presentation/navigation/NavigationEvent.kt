package com.isu.prepaidcard.presentation.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

sealed interface NavigationEvent {
    data class NavigateToNextScreen(val text:String) : NavigationEvent
}