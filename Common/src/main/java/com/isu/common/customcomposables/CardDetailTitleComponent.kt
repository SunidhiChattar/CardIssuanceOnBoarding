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
fun CardDetailTitleComponent(s: String, s1: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f).padding(end = 20.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                CustomText(
                    text = s,
                    color = ticketTextLightColor,
                    fontSize = 13.sp.noFontScale(),
                    fontWeight = FontWeight(500)
                )
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
    }
}