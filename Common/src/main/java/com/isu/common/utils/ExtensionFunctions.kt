package com.isu.common.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Extension function to convert a JSON string to a Map<String, Any?>.
 *
 * This function parses a JSON string into a Map<String, Any?> using Gson library.
 * It handles generic types and allows flexibility in the structure of the JSON string.
 *
 * @return A Map<String, Any?> representing the parsed JSON string.
 * @author Anandeswar Sahu
 */
fun String.toMap(): Map<String, Any?> {
    // Create a Gson instance
    val gson = Gson()

    // Use TypeToken to specify the type of the map
    val mapType = object : TypeToken<Map<String, Any?>>() {}.type

    // Parse the JSON string into a Map<String, String?>
    return gson.fromJson(this, mapType)
}

/**
 * Extension function for creating a custom composable within a navigation graph.
 *
 * This function defines a custom composable within a navigation graph, allowing
 * for custom animations and transitions when navigating to this destination.
 *
 * @param route The route associated with this composable.
 * @param content The content of the composable to be displayed when navigating to the specified route.
 */
fun NavGraphBuilder.customNavGraphComposable(route: String, content: @Composable () -> Unit) {
    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        }) {
        content.invoke()
    }
}


/**
 * Displays a short-duration toast message.
 * @param message The message to be shown in the toast.
 */
fun Context.showToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavHostController not provided")
}

fun TextUnit.toEm(): TextUnit {
    return (this * 0.0624)
}