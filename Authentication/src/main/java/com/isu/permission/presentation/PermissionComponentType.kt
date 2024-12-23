package com.isu.permission.presentation

import com.isu.common.events.Clickables


/**
 * @author karthik
 * Component types for screen
 *
 * @constructor Create empty Text fields type
 */


//Button types for login screen
sealed interface PermissionButtonType : Clickables {
    data object PermissionGranted : PermissionButtonType

}


