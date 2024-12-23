package com.isu.common.navigation

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Singleton

@Stable
@Singleton
sealed interface NavigationEvent {
    object helper{

        private val _navigationEvent: MutableSharedFlow<NavigationEvent> = MutableSharedFlow<com.isu.common.navigation.NavigationEvent>()
        val navigationEvent: SharedFlow<com.isu.common.navigation.NavigationEvent> = _navigationEvent
        suspend fun emit(event: NavigationEvent) {
            _navigationEvent.emit(event)
        }
        suspend fun navigateTo(screen: Screen,popUpTo: Screen?=null,inclusive:Boolean?=null){
            _navigationEvent.emit(NavigateToNextScreen(screen,popUpTo, inclusive))
        }
        suspend fun navigateBack(screen: Screen?=null){
            _navigationEvent.emit(NavigateBack)
        }
        suspend fun clearNavStack(){
            _navigationEvent.emit(ClearStack)
        }
    }

    data class NavigateToNextScreen(val screen: Screen,val popUpTo:Screen?=null,val inclusive:Boolean?=null) : NavigationEvent
    data object NavigateBack:NavigationEvent
    data object ClearStack : NavigationEvent
}


