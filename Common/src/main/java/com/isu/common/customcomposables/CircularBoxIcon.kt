package com.isu.common.customcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.R
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.ui.theme.FrameGrayBackground
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.UiText
import kotlinx.coroutines.launch

/**
 * This composable function displays a circular icon with text below it.
 *
 * @param icon The resource ID of the icon image to be displayed.
 * @param iconSize The size of the icon in Dp units.
 * @param text The text to be displayed below the icon.
 * @param onClick An optional callback function that is invoked when the circular box is clicked.
 *
 * @Composable This annotation indicates that the function is composable and can be used within a Jetpack Compose composable hierarchy.
 */
@Composable
fun CircularBoxIcon(
    isNotActive: Boolean = false,
    modifier: Modifier=Modifier,
    icon: Int,
    iconSize: Dp,
    text: String,
    onClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val config = LocalConfiguration.current
    Box(contentAlignment = Alignment.TopCenter) {


        Column(
            modifier = modifier.width(85.dp), verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(60.dp)

                    .background(
                        if (isNotActive) FrameGrayBackground.copy(0.6f) else FrameGrayBackground,
                        shape = CircleShape
                    )
                    .clip(CircleShape)

                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    ) {
                        if (isNotActive) {
                            coroutineScope.launch {
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.ErrorSnackBar,
                                        UiText.DynamicString("Card is not active")
                                    )
                                )
                            }
                        } else {
                            onClick.invoke()
                        }

                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(id = icon),
                    modifier = Modifier.size(iconSize).alpha(if (isNotActive) 0.3f else 1f),
                    contentDescription = ""
                )
            }
            CardCustomText(
                modifier = Modifier,
                text = text,
                textAlign = TextAlign.Center,
                color = authTextColor,
                fontWeight = FontWeight(500),
                fontSize = 10.sp.noFontScale()
            )
        }
        if (isNotActive) {
            Icon(
                painter = painterResource(R.drawable.baseline_block_24),
                modifier = Modifier.padding(top = 0.dp, end = 15.dp).size(15.dp).align(
                    Alignment.TopEnd
                ),
                contentDescription = "",
                tint = Color.Red.copy(0.1f)
            )
        }

    }

}