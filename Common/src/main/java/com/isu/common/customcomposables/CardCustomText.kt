package com.isu.common.customcomposables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.isu.common.utils.FontProvider.INTER
import com.isu.common.utils.FontProvider.getFont

/**
 * This composable function displays a custom text within a card.
 *
 * @param text The text to be displayed.
 * @param modifier The modifier applied to the text element.
 * @param color The color of the text.
 * @param textAlign The text alignment within the card.
 * @param fontFamily The font family used for the text.
 * @param fontSize The font size of the text.
 * @param lineHeight The line height of the text.
 * @param fontWeight The font weight of the text.
 * @param onTextLayout A callback function that is invoked when the text layout is available.
 *
 * @Composable This annotation indicates that the function is composable and can be used within a Jetpack Compose composable hierarchy.
 */
@Composable
fun CardCustomText(
    text: String,
    modifier: Modifier,
    color: Color = Color.White,
    textAlign: TextAlign = TextAlign.Start,
    fontFamily: String = INTER,
    fontSize: TextUnit = 2.em,
    lineHeight: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.W700,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        lineHeight = lineHeight,
        fontFamily = getFont(fontFamily,fontWeight),
        fontSize = fontSize,
        fontWeight = fontWeight,
        onTextLayout = onTextLayout,
        softWrap = true
    )
}
