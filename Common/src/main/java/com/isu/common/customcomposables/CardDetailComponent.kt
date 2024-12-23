package com.isu.common.customcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.ui.theme.ticketTextDarkColor
import com.isu.common.ui.theme.ticketTextLightColor

@Composable
fun CardDetailComponent(
    s: String,
    valueComposable: (@Composable () -> Unit)? = null,
    s1: String,
    statusComponent: (@Composable () -> Unit)? = null,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(0.5f).padding(end = 20.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                CustomText(text = s, color = ticketTextLightColor, fontSize = 14.sp.noFontScale())
                if (valueComposable != null) {
                    valueComposable()
                } else {
                    CustomText(
                        text = s1,
                        fontSize = 13.5.sp.noFontScale(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight(500),
                        color = ticketTextDarkColor
                    )
                }

            }
            Column(
                modifier = Modifier.weight(0.5f),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                if (statusComponent != null) {
                    statusComponent()
                }
            }
        }

    }
}
