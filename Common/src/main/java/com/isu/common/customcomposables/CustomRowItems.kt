package com.isu.common.customcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.ui.theme.appMainColor

/**
 * Creates a composable row with an image, header text, sub header text, and a test tag for UI testing.
 *
 * @param image: Resource ID for the image to be displayed.
 * @param headerText: Text to be displayed as the header.
 * @param subHeader: Text to be displayed as the sub header.
 * @param testTag: A unique identifier for UI testing purposes.
 */
@Composable
fun CustomRowItems(image: Int, headerText: String, subHeader: String, testTag: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Icon(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .testTag(testTag),
            tint = appMainColor
        )
        Column {
            CustomText(
                text = headerText,
                fontWeight = FontWeight.Bold,
                fontSize = 18.20.sp.noFontScale(),
                modifier = Modifier.padding(start = 20.80.dp),
                color = Color.Black
            )
            CustomText(
                text = subHeader,
                fontSize = 15.60.sp.noFontScale(),
                modifier = Modifier.padding(start = 20.80.dp),
                color = Color.Gray
            )

        }
    }
    Spacer(modifier = Modifier.height(20.80.dp))
}