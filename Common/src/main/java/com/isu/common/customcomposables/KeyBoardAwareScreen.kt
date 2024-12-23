package com.isu.common.customcomposables

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.isu.common.navigation.NavigationEvent
import com.isu.common.utils.rememberImeState
import kotlinx.coroutines.launch

/**
 * Key board aware screen
 *
 * @param modifier
 * @param horizontalAlignment
 * @param shouldScroll
 * @param keyBoardAware
 * @param screenScrollState
 * @param content
 * @receiver
 * A scrollable screen which get notified when a keyboard is open or closed
 */
@Composable
fun KeyBoardAwareScreen(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    shouldScroll: Boolean = true,
    keyBoardAware: Boolean = true,
    screenScrollState: ScrollState = rememberScrollState(),
    backHandler: suspend () -> Unit = { NavigationEvent.helper.navigateBack() },
    scrollValue:((Int)->Int)?=null,
    content: @Composable () -> Unit,
) {
    val config = LocalConfiguration.current
    val isKeyBoardOpen = rememberImeState()
    val screenHeight = config.screenHeightDp
    val coroutineScope = rememberCoroutineScope()
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    BackHandler {
        coroutineScope.launch {
            backHandler()
        }

    }
    LaunchedEffect(key1 = isKeyBoardOpen.value) {

        coroutineScope.launch {
            if (shouldScroll) {

                screenScrollState.animateScrollTo(scrollValue?.invoke(screenScrollState.maxValue)?:screenScrollState.maxValue)
            }
        }
    }

    val startAnimation=remember{
        mutableStateOf(false)
    }
    if(offsetY>screenHeight/3){
        startAnimation.value=true
    }
    val animOffset=animateIntOffsetAsState(if(startAnimation.value) IntOffset(0,200) else IntOffset(0,0), animationSpec = spring(0.5f,10f,))
        Column(
            modifier = modifier
                .heightIn(screenHeight.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume() // Consume the gesture event to avoid further processing

                        // Update the offset values based on the drag amount

                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
                .fillMaxWidth()
                .verticalScroll(screenScrollState),
            horizontalAlignment = horizontalAlignment
        ) {
            content()
            if (keyBoardAware && isKeyBoardOpen.value) {
                Column(modifier = Modifier.height(200.dp)) { }
            }
        }


}