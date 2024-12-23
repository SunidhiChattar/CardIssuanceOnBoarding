package com.isu.common.customcomposables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.isu.common.R
import com.isu.common.ui.theme.appMainColor

/**
 * Custom loader
 * Loader dialog for loading scenario
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
 fun CustomLoader() {
    Dialog(onDismissRequest ={} ){

        Row(
            modifier = Modifier.background(Color.White,
                RoundedCornerShape(12.dp)
            )
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CircularProgressIndicator(color = appMainColor)
            CustomText(text = "Please wait")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CustomLoaderFootball() {
    val startAnimation = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        startAnimation.value = true
    }
    val rotate = animateFloatAsState(
        if (startAnimation.value) 360f else 0f, animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing)
        )
    )
    val jump = animateFloatAsState(
        if (startAnimation.value) -100f else 0f, animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Dialog(onDismissRequest = {}) {


        Image(
            painter = painterResource(R.drawable.football_loader),
            contentDescription = "",
            modifier = Modifier.size(100.dp).rotate(rotate.value)
        )

    }
}