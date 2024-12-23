package com.isu.common.utils

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.isu.common.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first

/**
 * nav controller utility to ensure safe navigation and prevent crashes
 */
val NavHostController.canProceed: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

fun NavHostController.navigateBack(id: String? = null, inclusive: Boolean? = null) {
    if (canProceed) {
        if (id != null) {
            popBackStack(id, inclusive = inclusive == true)
        } else {
            popBackStack()
        }

    }
}

fun NavHostController.navigateTo(
    destination: Screen,
    popDestination: Screen? = null,
    inclusive: Boolean? = null,
) {
    if (canProceed) {
        Log.d("kdes", "navigateTo:${destination}${this.currentBackStackEntry?.destination?.navigatorName}${destination==this.currentBackStackEntry?.destination} ")
        if(this.previousBackStackEntry?.destination!=destination){

            navigate(destination) {
                Log.d("popk", "navigateTo:${popDestination} ")
                if (popDestination != null) {
                    Log.d("popk", "navigateTo:${popDestination} ")
                    popUpTo(popDestination) {
                        inclusive == true
                    }
                }

            }
        }
    }
}

