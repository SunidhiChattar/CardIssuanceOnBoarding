package com.isu.common.customcomposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.isu.common.R
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.errorColo2
import com.isu.common.ui.theme.errorColor

/**
 * This composable function displays a confirmation dialog for canceling a mandate.
 *
 * @param onDismiss A callback function to be invoked when the dialog is dismissed.
 * @param onConfirm A callback function to be invoked when the user confirms cancellation.
 *
 * @Composable This annotation indicates that the function is composable and can be used within a Jetpack Compose composable hierarchy.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelMandateDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    val config = LocalConfiguration.current
    val fontSize = if (config.screenWidthDp.dp < 400.dp) 3.em else 2.em

    val gradientRed =  Brush.verticalGradient(
        listOf(errorColor, errorColo2)
    )
    BasicAlertDialog(onDismissRequest = { onDismiss.invoke() }
    ) {
        Card(
            colors = CardDefaults.cardColors(Color.White),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                        .align(alignment = Alignment.End).clip(CircleShape)
                        .clickable { onDismiss.invoke() },
                    tint = Color.Black
                )
                Image(
                    painter = painterResource(R.drawable.mandatedialogimage),
                    contentDescription = ""
                )
                Column(modifier = Modifier.fillMaxWidth().padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    CardCustomText(
                        modifier = Modifier,
                        text = stringResource(R.string.cancel_mandate),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = fontSize
                    )
                    CardCustomText(
                        modifier = Modifier,
                        text = stringResource(R.string.worried_about_unauthorized_charges_do_you_want_to_block_your_card_now_for_enhanced_security_and_financial_protection),
                        color = authTextColor,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center, fontSize = fontSize
                    )
                    TextButton(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                            .background(brush = gradientRed, shape = RoundedCornerShape(10.dp))
                    ) {
                        CardCustomText(
                            modifier = Modifier,
                            text = stringResource(R.string.cancel),
                            color = Color.White, fontSize = fontSize
                        )
                    }
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, authTextColor.copy(0.5f)),
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                        CardCustomText(
                            modifier = Modifier,
                            text = stringResource(R.string.go_back),
                            color = Color.Black, fontSize = fontSize
                        )
                    }
                }

            }
        }
    }
}