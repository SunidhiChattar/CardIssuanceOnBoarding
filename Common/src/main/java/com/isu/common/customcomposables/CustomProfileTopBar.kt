package com.isu.common.customcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.appMainColor
import kotlinx.coroutines.launch

@Composable
fun CustomProfileTopBar(
    text: String = "Basic Info", trailinIcon: @Composable () -> Unit = {},
    onBackClick: suspend () -> Unit = {
        NavigationEvent.helper.emit(NavigationEvent.NavigateBack)
    },
)
{


    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val recompose = remember {
        mutableStateOf(false)
    }
    val iconRecomose = remember(recompose.value) {
        mutableStateOf(Icons.AutoMirrored.Filled.KeyboardArrowLeft)

    }
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {

                    iconRecomose.value = Icons.AutoMirrored.Filled.KeyboardArrowLeft
                }

                Lifecycle.Event.ON_PAUSE -> {

                    iconRecomose.value = Icons.AutoMirrored.Outlined.KeyboardArrowLeft
                }

                else -> {}
            }
        }
        lifeCycleOwner.lifecycle.addObserver(
            observer
        )
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(recompose.value) {

    }

    val scope= rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    )
    {
        Row(modifier = Modifier.weight(1f).padding(start = 20.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            IconButton(
                onClick = {scope.launch {
                    onBackClick()
                }},
                modifier = Modifier.background(appMainColor, shape = CircleShape)
                    .border(1.dp, Color.White, CircleShape).size(23.dp)
            ) {
                Icon(imageVector = iconRecomose.value, "", tint = Color.White)

            }
        }
        Row(
            modifier = Modifier.wrapContentHeight().fillMaxHeight().widthIn(100.dp, 350.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ProfileText(
                text = text,
                color = Color.Black,
                fontSize = 18.sp.noFontScale(),
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier.weight(1f).fillMaxHeight().padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            trailinIcon()
        }
    }
}