package com.isu.common.customcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SliderWithMovingText() {
    var sliderValue by remember { mutableStateOf(0f) }
    var sliderWidth by remember { mutableStateOf(0) }
    var thumbOffsetX by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Box to measure slider width
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    sliderWidth = coordinates.size.width
                }
        ) {
            constraints.maxWidth

            // Slider component
            Slider(
                value = sliderValue,
                onValueChange = {
                    sliderValue = it
                    // Calculate thumb X offset based on slider width and current value
                    thumbOffsetX = sliderValue * sliderWidth
                },
                modifier = Modifier.fillMaxWidth()

            )

            // Text that moves in sync with the slider thumb
            Text(
                text = sliderValue.toString(),
                modifier = Modifier
                    .offset {
                        IntOffset(thumbOffsetX.roundToInt() - 30, -30) // Position above thumb
                    }
                    .background(Color.White)
            )
        }
    }
}
