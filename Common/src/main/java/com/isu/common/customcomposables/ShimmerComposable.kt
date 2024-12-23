package com.isu.common.customcomposables

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ShimmerComposable(component:@Composable (shimmerBrush:Brush)->Unit){
    val startAnime= remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        startAnime.value=true
    }
    val animateOffset= animateOffsetAsState(if(startAnime.value) Offset(x=100f,y=100f) else Offset.Zero, infiniteRepeatable(tween(1000), repeatMode = RepeatMode.Reverse))
    val brush= Brush.linearGradient(
        colors = listOf(Color.Gray.copy(0.4f), Color.LightGray.copy(0.4f), Color.Gray.copy(0.4f)),
        start = animateOffset.value)
    component(brush)
}