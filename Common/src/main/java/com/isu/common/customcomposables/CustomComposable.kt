package com.isu.common.customcomposables

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

inline fun <reified T:Any> NavGraphBuilder.CustomComposable(noinline screen:@Composable (NavBackStackEntry)->Unit){
    composable<T>(enterTransition = { slideInHorizontally(tween(100)) { it } }, popEnterTransition = { slideInHorizontally(
        tween(100)
    ) { -it } }, exitTransition = { slideOutHorizontally{-it} }, popExitTransition = { slideOutHorizontally{ it } }){
        val lifecyclerOwner = LocalLifecycleOwner.current

        screen(it)
    }
}