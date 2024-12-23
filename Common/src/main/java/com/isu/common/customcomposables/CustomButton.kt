package com.isu.common.customcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.ui.theme.buttonGradientBottom
import com.isu.common.ui.theme.buttonGradientTop


/**
 * Custom button
 *
 * @param enabled
 * @param onClick
 * @param text
 * @param modifier
 * @receiver
 */

@Composable
fun CustomResizeButton(
    brush:Brush = Brush.linearGradient(
        listOf(buttonGradientTop, buttonGradientBottom.copy(0.8f)),
        start = Offset(0f, 0f),
        end = Offset(0f, 100f)
    ),color: Color?=null,
                         onClick: () -> Unit = {},
                         text: String = "karthik",
                         innerComponent: @Composable () -> Unit = {CustomText(text = text, fontSize = 16.sp.noFontScale(), fontFamily = "Inter", color = Color.White)},
                         modifier: Modifier = Modifier.fillMaxWidth()
                             .height(44.dp),
                         shape: RoundedCornerShape= RoundedCornerShape(5.dp),){
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .bounceClick { onClick() }
            .then(
                if (color == null) {
                    Modifier.background(brush, shape)
                } else {
                    Modifier.background(color = color, shape)
                }
            )

    ) {
        innerComponent()
    }

}
@Preview
@Composable
fun CustomButton(
    color: Color?=null,
    brush: Brush= Brush.linearGradient(
        listOf(buttonGradientTop, buttonGradientBottom.copy(0.8f)),
        start = Offset(0f, 0f),
        end = Offset(0f, 100f)
    ),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    text: String = "karthik",
    innerComponent: @Composable () -> Unit = {
        CustomText(
            text = text,
            fontSize = 14.sp.noFontScale(),
            fontFamily = "Inter",
            color = Color.White
        )
    },
    modifier: Modifier = Modifier.fillMaxWidth()
        .height(44.dp),
    shape: RoundedCornerShape= RoundedCornerShape(5.dp),
) {
    Button(
        onClick = { onClick() },
        enabled = enabled,
        modifier = modifier
            .then(
                if(color==null){
                    Modifier.background(brush, shape)
                }else{
                    Modifier.background(color = color, shape)
                }
            ),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            Color.Transparent
        ),
    ) {
        innerComponent()
    }
}


@Composable
fun CustomCancelButton(
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    text: String = "karthik",
    innerComponent: @Composable () -> Unit = {
        CustomText(
            text = text,
            fontSize = 14.sp.noFontScale(),
            fontFamily = "Inter",
            color = Color.Black
        )
    },
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape= RoundedCornerShape(5.dp),
) {
    OutlinedButton(
        onClick = { onClick() },
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp),
        shape = shape,

        colors = ButtonDefaults.buttonColors(
            Color.White
        ),
    ) {
        innerComponent()
    }

}