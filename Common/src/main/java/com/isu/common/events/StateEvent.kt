package com.isu.common.events

data class StateEvent<T,O>(
    val state: T,
    val event:(O)->Unit
)