package com.isu.common.utils

/**
 * Connection state
 * data class representing connection states
 * @constructor Create empty Connection state
 */
sealed class ConnectionState{
    data object Available : ConnectionState()
    data object Unavailable : ConnectionState()
}