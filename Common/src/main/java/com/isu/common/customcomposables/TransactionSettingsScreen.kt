package com.isu.common.customcomposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.ui.theme.appMainColor
import kotlin.math.roundToInt



@Composable
fun SetLimitComponent(
    onlineEnabled: MutableState<Boolean>,
    limit: MutableState<Float>,
    maxLimit: MutableState<Float>,
    label: String,
) {

    Column(
        modifier = Modifier.border(
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(10.dp)
        ).padding(top = 0.dp, start = 16.dp, end = 16.dp)
    ) {
        TransactionToggleOption(
            label = label,
            checked = onlineEnabled.value,
            onCheckedChange = { onlineEnabled.value = it }
        )

        if (onlineEnabled.value) {
            SliderWithLabel(
                maxLimit = maxLimit.value,
                value = limit.value,
                onValueChange = { limit.value = ((it.roundToInt() / 100) * 100).toFloat() }
            )
        }
    }
}

@Composable
fun TransactionToggleOption(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = label, modifier = Modifier.weight(1f), fontSize = 16.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            Modifier.scale(0.7f),
            colors = SwitchDefaults.colors(
                checkedTrackColor = appMainColor,
                uncheckedBorderColor = Color.Transparent,
                uncheckedThumbColor = Color.White
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderWithLabel(value: Float, onValueChange: (Float) -> Unit, maxLimit: Float) {
    val x = remember {
        mutableStateOf(0f)
    }
    var sliderValue = value
    var sliderWidth by remember { mutableIntStateOf(0) }
    var thumbOffsetX = remember { mutableFloatStateOf(value / maxLimit * sliderWidth) }



    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                sliderWidth = coordinates.size.width
            }
    ) {
        LaunchedEffect(Unit) {
            thumbOffsetX.value = value / maxLimit * sliderWidth
        }
        constraints.maxWidth

        // Slider component
        Slider(
            value = sliderValue,
            onValueChange = {

                sliderValue = (it.roundToInt() / 100f) * 100f
                // Calculate thumb X offset based on slider width and current value
                thumbOffsetX.value = it / maxLimit * sliderWidth
                onValueChange(sliderValue)
            },
            valueRange = 0f..maxLimit,
            track = {
                Row(
                    Modifier.fillMaxWidth().height(8.dp).background(
                        Color.LightGray,
                        RoundedCornerShape(50.dp)
                    )
                ) {
                    Row(
                        Modifier.fillMaxWidth(it.value / it.valueRange.endInclusive).height(8.dp)
                            .background(
                                appMainColor, RoundedCornerShape(50.dp)
                            )
                    ) {

                    }
                }
            },
            thumb = {

            Row(
                    modifier = Modifier.size(25.dp).background(appMainColor, CircleShape),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.size(17.dp).background(Color.White, CircleShape)) { }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = appMainColor,
                activeTrackColor = appMainColor
            )

        )

        // Text that moves in sync with the slider thumb
        Text(
            text = "Rs." + sliderValue.toInt().toString(),
            modifier = Modifier
                .widthIn(60.dp)
                .offset {
                    IntOffset(
                        if (!thumbOffsetX.value.isNaN()) {
                            thumbOffsetX.value.roundToInt() + if (sliderValue < 0.7 * maxLimit) -70 else -140

                        } else {
                            0
                        }, -60

                    ) // Position above thumb
                }
                .background(Color.White).border(
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }

}

