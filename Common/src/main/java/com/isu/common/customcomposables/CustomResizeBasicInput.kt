package com.isu.common.customcomposables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.ui.theme.ticketTextDarkColor
import com.isu.common.utils.FontProvider

@Composable
fun CustomResizeBasicInput(
    value: String, onValueChange: (String) -> Unit,
    icon: @Composable () -> Unit = {
        Icon(
            Icons.Default.Search,
            "",
            tint = ticketTextDarkColor,
            modifier = Modifier.width(40.dp)
        )
    },
    placeHolder: String = "",
) {

    Row(modifier = Modifier.fillMaxWidth()) {

        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                fontFamily = FontProvider.getFont("Inter", weight = FontWeight(400)),
                fontSize = 14.sp.noFontScale()
            ),
            singleLine = true
        ) {
            Box(
                Modifier.height(40.dp)
                    .border(1.dp, Color.LightGray, shape = RoundedCornerShape(2.dp))
                    .fillMaxWidth(0.7f).padding(10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            if (value.isEmpty()) {
                                CustomText(
                                    text = placeHolder,
                                    fontSize = 13.sp.noFontScale(),
                                    modifier = Modifier
                                )
                            }
                            it.invoke()

                        }

                    }

                    icon()
                }

            }
        }
    }
}