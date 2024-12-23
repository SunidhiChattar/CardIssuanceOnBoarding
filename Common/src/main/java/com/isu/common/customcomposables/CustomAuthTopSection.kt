package com.isu.common.customcomposables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.isu.common.R
import com.isu.common.events.ShowBottomBarEvent
import com.isu.common.utils.FontProvider.LATO_FONT
import com.isu.common.utils.FontProvider.getFont
import androidx.compose.foundation.Image as Image1

@Composable
fun CustomAuthTopSection(headingText: String, infoText: String, @DrawableRes imageVector: Int) {
    val recompose = remember {
        mutableStateOf(true)
    }
    val focusManager = LocalFocusManager.current
    val logoImage = remember {
        mutableStateOf(R.drawable.isu_logo)
    }
    val imageVector = remember {
        mutableStateOf(imageVector)
    }
    LaunchedEffect(Unit) {
        ShowBottomBarEvent.hide()
        focusManager.clearFocus()
    }
    val lifeCycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {

                    recompose.value = true
                }

                Lifecycle.Event.ON_PAUSE -> {

                    recompose.value = false
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

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (recompose.value) {
                Text("")
            }

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                // Logo Image
                Image1(
                    painterResource(R.drawable.odishafclogo),
                    "",
                    modifier = Modifier.heightIn(14.dp),
                )

                // Background Image
                Image1(
                    modifier = Modifier.size(220.dp),
                    painter = painterResource(imageVector.value),
                    contentDescription = stringResource(R.string.empty)
                )
            }

            // Middle section with header and instruction text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(Modifier.height(2.dp))
                CustomText(
                    text = headingText,
                    fontSize = 24.sp.noFontScale(),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(0.8f),
                    lineHeight = 20.sp.noFontScale(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(2.dp))
                if (infoText.isNotEmpty()) {

                    Spacer(modifier = Modifier.height(10.dp))
                    CustomText(
                        text = infoText,
                        fontSize = 14.sp.noFontScale(),
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp.noFontScale(),
                        fontWeight = FontWeight(500)
                    )
                }
            }
        }


}

@Composable
fun CustomAuthTopSectionForSuccess(
    headingText: String,
    infoText: String,
    @DrawableRes imageVector: Int
) {
    val recompose = remember {
        mutableStateOf(true)
    }
    val focusManager = LocalFocusManager.current
    val logoImage = remember {
        mutableStateOf(R.drawable.isu_logo)
    }
    val imageVector = remember {
        mutableStateOf(imageVector)
    }
    LaunchedEffect(Unit) {
        ShowBottomBarEvent.hide()
        focusManager.clearFocus()
    }
    val lifeCycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {

                    recompose.value = true
                }

                Lifecycle.Event.ON_PAUSE -> {

                    recompose.value = false
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

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (recompose.value) {
            Text("")
        }

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            // Logo Image
            Image1(
                modifier = Modifier.heightIn(17.dp),
                imageVector = ImageVector.vectorResource(R.drawable.isu_logo),
                contentDescription = stringResource(R.string.empty),
            )
            // Background Image
            Image1(
                modifier = Modifier.size(220.dp),
                imageVector = ImageVector.vectorResource(imageVector.value),
                contentDescription = stringResource(R.string.empty)
            )
        }
        Spacer(Modifier.height(10.dp))
        // Middle section with header and instruction text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = headingText,
                fontSize = 25.sp.noFontScale(),
                fontWeight = FontWeight.Bold,
                fontFamily = getFont(LATO_FONT),
                lineHeight = 1.sp.noFontScale(),
                textAlign = TextAlign.Center
            )
            if (infoText.isNotEmpty()) {

                Spacer(modifier = Modifier.height(0.dp))
                CustomText(
                    text = infoText,
                    fontSize = 14.sp.noFontScale(),
                    fontFamily = LATO_FONT,
                    textAlign = TextAlign.Center,
                        lineHeight = 15.sp.noFontScale(),
                        fontWeight = FontWeight(500)
                    )
                }
            }
        }


}


