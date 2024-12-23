package com.isu.common.customcomposables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.isu.common.ui.theme.ReferButtonColor

@Composable
fun ReferAndEarnButton(
    modifier: Modifier,
    text:String,
    onClick: ()->Unit
){
val config = LocalConfiguration.current
    Button(
        onClick = { onClick() },
        modifier = modifier
            .wrapContentWidth().padding(2.dp)
            .height( if (config.screenWidthDp.dp < 400.dp) 38.dp else 44.dp),
        shape = RoundedCornerShape(10.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            ReferButtonColor
        ),
    ) {
        CardCustomText(text = text,
            fontSize = if (config.screenWidthDp.dp<400.dp)3.em else 2.em,
            fontFamily = "Inter",
            modifier = Modifier, color = Color.White)
    }
}
