package com.isu.common.customcomposables

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.FontProvider.LATO_FONT
import com.isu.common.utils.FontProvider.QUICK_SAND
import com.isu.common.utils.FontProvider.getFont

/**
 * Custom text
 *
 * @param modifier
 * @param text
 * @param fontSize
 * @param fontFamily
 * @param textAlign
 * @param color
 * @param lineHeight
 * @param fontWeight
 * text that stays consistent whit varied font configuration in a device
 */
@Preview
@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    annotatedString: AnnotatedString? = null,
    text: String = "",
    fontSize: TextUnit = 15.sp.noFontScale(),
    fontFamily: String = QUICK_SAND,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = authTextColor,
    textDecoration: androidx.compose.ui.text.style.TextDecoration = androidx.compose.ui.text.style.TextDecoration.None,
    lineHeight: TextUnit = fontSize,
    fontWeight: FontWeight = FontWeight.Medium,
    maxLines: Int = Int.MAX_VALUE,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    shimmer: Boolean = false,
) {
    if (annotatedString != null) {
        Text(
            text = annotatedString,
            modifier = modifier,
            color = color,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontFamily = getFont(fontFamily, fontWeight),
            fontSize = fontSize,
            fontWeight = fontWeight,
            maxLines = maxLines,
            softWrap = softWrap,
            overflow = overflow,
            textDecoration = textDecoration
        )
    } else {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        lineHeight = lineHeight,
        fontFamily = getFont(fontFamily,fontWeight),
        fontSize = fontSize,
        fontWeight = fontWeight,
        maxLines = maxLines,
        softWrap = softWrap,
        overflow = overflow,
        textDecoration = textDecoration
    )
}

}

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    fontSize: TextUnit = 16.sp.noFontScale(),
    fontFamily: String = LATO_FONT,
    textAlign: TextAlign = TextAlign.Start,
    textDecoration: androidx.compose.ui.text.style.TextDecoration = androidx.compose.ui.text.style.TextDecoration.None,
    color: Color = authTextColor,
    fontWeight: FontWeight = FontWeight.Medium,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        lineHeight = 11.sp.noFontScale(),
        fontFamily = getFont(fontFamily,fontWeight),
        fontSize = fontSize,
        fontWeight = fontWeight,
        onTextLayout = onTextLayout,
        textDecoration = textDecoration
    )
}

@Composable
fun AnnotatedClickableText(
    text: String,
    linkText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontFamily: String = LATO_FONT,
    color: Color = authTextColor,
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {
    // Build the AnnotatedString
    val annotatedText = buildAnnotatedString {
        append(text)
        val start = text.indexOf(linkText)
        if (start >= 0) {
            addStyle(
                SpanStyle(color = appMainColor),
                start,
                start + linkText.length
            )
            addStringAnnotation(
                tag = "URL",
                annotation = "link",
                start = start,
                end = start + linkText.length
            )
        }
    }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    // Handle the click
    val pressIndicator = Modifier.pointerInput(Unit) {
        detectTapGestures { pos ->
            val layoutResult = textLayoutResult
            layoutResult?.let {
                val offset = it.getOffsetForPosition(pos)
                annotatedText.getStringAnnotations(offset, offset).firstOrNull()
                    ?.let { annotation ->
                        if (annotation.tag == "URL") {
                            onClick()
                        }
                    }
            }
        }
    }

    CustomText(
        text = annotatedText,
        modifier = modifier.then(pressIndicator),
        onTextLayout = {
            textLayoutResult = it
            onTextLayout(it)
        },
        color = color,
        fontFamily = fontFamily
    )
}

@Composable
fun TextUnit.noFontScale():TextUnit{
    return this.div(LocalDensity.current.fontScale)
}
@Composable
fun Dp.scaledDp( baseWidth: Float = 360f): Dp {
    val configuration = LocalConfiguration.current
    val smallestWidthDp = configuration.smallestScreenWidthDp.toFloat()



    // Calculate scaling factor
    val scaleFactor = smallestWidthDp / baseWidth

    // Return scaled dp value
    return (this.value/(scaleFactor-8)).dp
}
