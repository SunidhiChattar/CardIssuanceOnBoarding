package com.isu.common.customcomposables




import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.FontProvider.INTER
import androidx.compose.ui.graphics.Color


@Composable
fun ProfileText(
    modifier: Modifier = Modifier,
    text: String = "",
    fontSize: TextUnit = 14.sp.noFontScale(),
    fontFamily: String = INTER,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = authTextColor,
    lineHeight: TextUnit = 1.0.em,
    fontWeight: FontWeight = FontWeight.Medium,
) {
    CustomText(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        fontFamily = fontFamily,
        textAlign = textAlign,
        color = color,
        lineHeight = lineHeight,
        fontWeight = fontWeight
    )
}