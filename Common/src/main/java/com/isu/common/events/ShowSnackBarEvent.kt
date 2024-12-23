package com.isu.common.events

import com.isu.common.utils.UiText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

sealed interface SnackBarType{
    data object SuccessSnackBar: SnackBarType
    data object ErrorSnackBar: SnackBarType
}
sealed interface ShowSnackBarEvent {
    object helper{
        private val _showSnackBarEvent = MutableSharedFlow<ShowSnackBarEvent>()
        val showSnackBarEvent: SharedFlow<ShowSnackBarEvent> = _showSnackBarEvent
        suspend fun emit(event:ShowSnackBarEvent) {
            _showSnackBarEvent.emit(event)
        }
    }
    data class show(val type: SnackBarType, val msg: UiText) : ShowSnackBarEvent
    
}