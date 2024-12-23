package com.isu.cardissuanceonboarding.inapp.inapp.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


/**
 * Composable function for displaying an alert dialog with dynamic content.
 *
 * @param errorMessage The error message to be displayed in the dialog.
 */
@Composable
fun AlertDialogComponent(
    title: String = "Alert",
    errorMessage: String,
    confirmButtonText: String = "Ok",
    testTag: String = "unknown_resource",
    tesTagButton: String = "",
    onDismissClick: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .testTag(testTag)
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primary
            ),
        onDismissRequest = {
//            onDismissClick()
        },
        title = {
            Text(
                text = title, color = Color.Black, fontWeight = FontWeight.Bold,

                )
        },
        text = {
            if (errorMessage.isNotEmpty()) {
                Text(
                    errorMessage, color = Color.Black,

                    )
            } else {
                Text(
                    "Something went wrong", color = Color.Black,

                    )
            }
        },
        confirmButton = {
            TextButton(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .testTag(tesTagButton),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary

                ),
                shape = RoundedCornerShape(5.dp),
                elevation = ButtonDefaults.buttonElevation(10.dp),
                onClick = {
                    onDismissClick()
                }
            ) {
                Text(
                    confirmButtonText,
                    color = Color.White,

                    )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        textContentColor = Color.White
    )
}
