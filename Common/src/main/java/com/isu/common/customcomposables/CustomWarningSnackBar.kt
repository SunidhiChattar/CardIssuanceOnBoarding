package com.isu.common.customcomposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.R
import com.isu.common.ui.theme.errorColor
import kotlinx.coroutines.delay

@Composable
fun CustomWarningSnackBar(
    modifier: Modifier = Modifier,
    warning: MutableState<Boolean>? = null,
    warningMessage: String,
) {

    val warningMsg = remember(warning) {
        mutableStateOf(warningMessage)
    }
    LaunchedEffect(Unit) {
        delay(2000)
        warning?.value = false

    }
    AnimatedVisibility(modifier = modifier, visible = warning?.value ?: true, exit = fadeOut()) {
        Snackbar(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .height(55.dp),
            dismissAction = {
                if (warning != null) {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            warning.value = false
                            warningMsg.value = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                }


            },
            containerColor = errorColor

        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        painterResource(id = R.drawable.ic_circ_error),
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 10.dp)
                    )

                }

                CustomText(
                    text=warningMessage,
                    fontSize = 14.sp.noFontScale(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )


            }


        }
    }


}

@Composable
fun CustomSuccessSnackBar(
    modifier: Modifier = Modifier,
    success: MutableState<Boolean>? = null,
    successMsg: String,
) {
    val warningMsg = remember(success) {
        mutableStateOf(successMsg)
    }
    LaunchedEffect(Unit){
        delay(1000)
        success?.value=false
    }
    AnimatedVisibility(modifier = modifier, visible = success?.value ?: true, exit = fadeOut()) {
        Snackbar(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .height(45.dp),
            dismissAction = {
                if (success != null) {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            success.value = false
                            warningMsg.value = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                }

            },
            containerColor = Color(0xff0E8345)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .size(25.dp)
                            .padding(start = 3.dp)
                    )

                }

                CustomText(
                    text=successMsg, fontSize = 13.sp.noFontScale(), fontWeight = FontWeight.Bold, color = Color.White
                )


            }


        }
    }


}