package com.isu.common.customcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.isu.common.R
import com.isu.common.ui.theme.errorColor

/**
 * Custom error dialog
 * error dialog for error scenario
 * @param title
 * @param content
 * @param dismissAction
 * @receiver
 */
@Preview
@Composable
fun CustomErrorDialog(
    title: String = "Error",
    content: String = "kdkkdsjkjdsljkdskdjslk",
    dismissAction: () -> Unit = {},
) {
    Dialog(onDismissRequest = {}) {
        Card(
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier.wrapContentHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(50.dp).background(errorColor)
                    .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_circ_error),
                    tint = Color.White,
                    contentDescription = ""
                )
                CustomText(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp.noFontScale()
                )
            }
            Row(Modifier.wrapContentHeight().padding(16.dp)) {
                CustomText(text = content)
            }
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = dismissAction, colors = ButtonDefaults.buttonColors(errorColor)) {
                    CustomText(text = "OK", color = Color.White)
                }
            }
        }
    }
}