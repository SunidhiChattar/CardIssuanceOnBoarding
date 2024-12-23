package com.isu.common.customcomposables.signIn

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomText
import com.isu.common.ui.theme.appMainColor

@Composable
fun CardSelectBox(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    text: String = "GPR",
    onSelect: () -> Unit,
) {

    Row(
        modifier.then(
            if (selected) {
                Modifier.border(1.dp, color = appMainColor, RoundedCornerShape(4.dp))
            } else {
                Modifier.border(1.dp, color = Color.Gray, RoundedCornerShape(4.dp))
            }


        )
            .padding(horizontal = 0.dp)
            .wrapContentHeight().wrapContentWidth().clickable {
                onSelect()
            }, verticalAlignment = Alignment.CenterVertically
    ) {


            RadioButton(
                selected = selected,
                onClick = {
                    onSelect()
                },
                colors = RadioButtonColors(
                    selectedColor = appMainColor,
                    unselectedColor = Color.LightGray,
                    disabledSelectedColor = Color.Gray,
                    disabledUnselectedColor = Color.Gray
                )
            )

        CustomText(text = text, color = if (selected) appMainColor else Color.Gray)
        Spacer(modifier = Modifier.weight(1f))


    }
}