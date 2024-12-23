package com.isu.common.events

import com.isu.common.utils.UiText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

sealed interface LoadingErrorEvent{
    object helper{
        private val _loadingErrorEvent = MutableSharedFlow<LoadingErrorEvent>()
        val loadingErrorEvent: SharedFlow<LoadingErrorEvent> = _loadingErrorEvent
        suspend fun emit(event: LoadingErrorEvent){
           _loadingErrorEvent.emit(event)
        }
        
    }
    data class isLoading(val isLoading:Boolean): LoadingErrorEvent
    data class errorEncountered(val error:UiText): LoadingErrorEvent
}

object LatLongFlowProvider {
    val latLongFlow = MutableStateFlow<String>("")
}

