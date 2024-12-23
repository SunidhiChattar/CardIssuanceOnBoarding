package com.isu.cardissuanceonboarding.inapp.inapp.common

import android.content.Context
import android.widget.Toast

/**
 * Extension function for creating a custom composable within a navigation graph.
 *
 * This function defines a custom composable within a navigation graph, allowing
 * for custom animations and transitions when navigating to this destination.
 *
 * @param route The route associated with this composable.
 * @param content The content of the composable to be displayed when navigating to the specified route.
 */
/*fun NavGraphBuilder.customNavGraphComposable(route: String, content: @Composable () -> Unit) {
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
}*/


/**
 * Displays a short-duration toast message.
 * @param message The message to be shown in the toast.
 */
fun Context.showToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}