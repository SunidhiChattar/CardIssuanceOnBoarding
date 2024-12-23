package com.isu.cardissuanceonboarding.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.cardissuanceonboarding.R
import com.isu.cardissuanceonboarding.presentation.ui.theme.IsuGradOne
import com.isu.cardissuanceonboarding.presentation.ui.theme.IsuGradTwo
import com.isu.cardissuanceonboarding.presentation.ui.theme.TextColorDark
import com.isu.cardissuanceonboarding.presentation.ui.theme.TextColorLight
import com.isu.common.customcomposables.noFontScale

/**
 * @author Sunidhi
 */
@Composable
fun CustomText(
    modifier: Modifier,
    text: String,
               color: Color,
               size: TextUnit,
               textAlign: TextAlign,
               fontWeight: FontWeight,
               lineHeight: TextUnit){
    Text(modifier = modifier,
        lineHeight = lineHeight,
        text = text,
        color = color,
        fontSize = size,
        fontFamily = FontFamily(Font(R.font.lato_regural)),
        fontWeight = fontWeight,
        textAlign = textAlign
        )
}

/**
 * @Sunidhi
 */
@Composable
fun CustomButton(onClickAction: ()-> Unit){

        val verticalGradient = Brush.verticalGradient(
            colors = listOf(
           IsuGradOne, IsuGradTwo
        )
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 11.dp, 10.dp, 11.dp)
            .background(
                brush = verticalGradient,
                shape = RoundedCornerShape(10.dp)
            )
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        interactionSource = remember {
            MutableInteractionSource()
        },
        onClick = {onClickAction.invoke()}) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomText(
                modifier = Modifier,
                text = stringResource(R.string.get_started),
                color = White,
                size = 18.sp.noFontScale(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.W400,
                lineHeight = 28.sp.noFontScale()
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "",
                tint = White
            )
        }
    }
}

data class SplashContent(
    val title: String = "",
    val subTitle: String = "",
    val image: Int
)
/**
 * @author Sunidhi
 */
@Composable
fun CustomSplash(splashContent: SplashContent) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            modifier = Modifier.size(330.dp),
            painter = painterResource(id = splashContent.image),
            contentDescription = "splash"
        )
        com.isu.common.customcomposables.CustomText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            text = splashContent.title,
            color = TextColorDark,
            fontSize = 34.sp.noFontScale(),
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.Center,
            lineHeight = 33.sp.noFontScale()
        )
        com.isu.common.customcomposables.CustomText(
            modifier = Modifier.fillMaxWidth(),
            text = splashContent.subTitle,
            color = TextColorLight,
            fontSize = 18.sp.noFontScale(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W400,
            lineHeight = 20.sp.noFontScale()
        )
        /*      CustomText(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 16.dp),
                  text = splashContent.title,
                  color = TextColorDark,
                  size = 34.sp.noFontScale(),
                  fontWeight = FontWeight.W600,
                  textAlign = TextAlign.Center,
                  lineHeight = 43.sp.noFontScale()
              )*/

    }
}

/**
 * @author Sunidhi
 */
@Composable
fun GradientHorizontalPagerIndicator(
    pagerState: PagerState,
    indicatorWidth: Dp,
    indicatorHeight: Dp,
    activeColor: Brush,
    inactiveColor:Brush
) {
    val pageCount = pagerState.pageCount
    val currentPage = pagerState.currentPage

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        repeat(pageCount) { page ->
            Box(
                modifier = Modifier
                    .size(width = indicatorWidth, height = indicatorHeight)
                    .clip(CircleShape)
                    .background(
                        brush = if (page == currentPage) activeColor else inactiveColor
                    )
            )
        }
    }
}