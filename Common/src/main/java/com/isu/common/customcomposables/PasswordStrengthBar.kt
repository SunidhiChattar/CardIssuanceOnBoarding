package com.isu.common.customcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.isu.common.ui.theme.appMainColor

/**
 * Password strength bar show strength of password
 *
 * @param barCount
 * @param strength
 */
@Composable
fun PasswordStrengthBar(barCount: Int, strength: Float) {
    Row(modifier = Modifier.fillMaxWidth().height(7.dp)) {
        for (i in 1..barCount) {
            val toFill = if (strength > i) 1f else 1f - (i - strength)
            Row(
                Modifier.weight(1f).fillMaxHeight().background(
                    shape = RoundedCornerShape(50.dp),
                    color = Color.LightGray.copy(0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(toFill)
                        .background(shape = RoundedCornerShape(50.dp), color = appMainColor)
                        .fillMaxHeight()
                ) {
                    // Empty row for spacing
                }
            }
        }
    }
}