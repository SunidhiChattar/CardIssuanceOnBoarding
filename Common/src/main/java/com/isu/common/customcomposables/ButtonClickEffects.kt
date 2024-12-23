package com.isu.common.customcomposables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Enum representing the state of the button - whether it's pressed or idle.
 */
enum class ButtonState { Pressed, Idle }

/**
 * Composable modifier for creating a bounce effect on button clicks.
 *
 * @param onClickAction The action to be executed when the button is clicked.
 */
fun Modifier.bounceClick(onClickAction:()->Unit) = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }

    // Animate the scale of the button on click
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.90f else 1f,
        label = ""
    )
    // Apply the graphics layer transformation and handle click events
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { NoRippleInteractionSource() },
            indication = null,
            onClick = {
                onClickAction.invoke()
            }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

/**
 * Custom InteractionSource that disables ripple effects.
 */
class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {
        // Not Required
    }

    override fun tryEmit(interaction: Interaction) = true
}