package com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomOTPInputFieldWithBox
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.OrderCard
import com.isu.common.customcomposables.noFontScale
import com.isu.common.ui.theme.appMainColor
import com.isu.common.utils.BeneDetails
import kotlinx.coroutines.delay

@Composable
fun BeneAmountScreen(modifier: Modifier = Modifier) {
    val showPayDialog = remember {
        mutableStateOf(false)
    }
    val otp = remember {
        mutableStateOf("")
    }
    val otpErr = remember {
        mutableStateOf(false)
    }
    val otpErrMsg = remember {
        mutableStateOf("")
    }
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val latLong = remember {
        mutableStateOf("")
    }


    // Timer state
    val timer = remember { mutableIntStateOf(0) }
    val startTimer = remember {
        mutableStateOf(false)
    }


    // Clear fields on launch
    LaunchedEffect(Unit) {
        /*   otp.value = ""
           otpErr.value = false
           otpErrMsg.value = ""*/


    }
    val ammount = remember {
        mutableStateOf("")
    }
    val remarks = remember {
        mutableStateOf("")
    }

    // Timer logic
    LaunchedEffect(key1 = startTimer) {
        timer.intValue = 90
        while (timer.intValue > 0) {
            timer.intValue--
            delay(1000)
        }
    }
    if (showPayDialog.value) {
        Dialog(onDismissRequest = {

        }) {
            val showOTP = remember {
                mutableStateOf(false)
            }
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(300.dp)
                    .background(White, RoundedCornerShape(15.dp)).padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!showOTP.value) {
                    AnimatedVisibility(!showOTP.value) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .background(White, RoundedCornerShape(15.dp)).padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {


                                Row(
                                    modifier = Modifier.weight(1f).padding(5.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        "",
                                        modifier = Modifier.clickable {
                                            showPayDialog.value = false
                                        })

                                }


                            }
                            Column(
                                Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {


                                    CustomText(
                                        text = "Confirm Transaction",
                                        fontSize = 16.sp.noFontScale(),
                                        fontWeight = FontWeight.Bold,
                                        color = Black
                                    )


                                }

                                CustomText(
                                    text = "Are You Interested in Renewing Your Card for Continued Benefits?",
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp.noFontScale(),
                                    lineHeight = 20.sp.noFontScale()
                                )
                            }

                            Spacer(Modifier.height(20.dp))
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {


                                Row(
                                    modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(
                                            text = "Pay By :",
                                            fontWeight = FontWeight(400),
                                            fontSize = 14.sp.noFontScale()
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(
                                            text = "Krishna Das",
                                            color = Black,
                                            fontWeight = FontWeight(500)
                                        )
                                    }
                                }
                                Row(
                                    modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(
                                            text = "Account Number :",
                                            fontWeight = FontWeight(400),
                                            fontSize = 14.sp.noFontScale()
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(text = "2364839956", color = Black)
                                    }
                                }
                                Row(
                                    modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(
                                            text = "Amount :",
                                            fontWeight = FontWeight(400),
                                            fontSize = 14.sp.noFontScale()
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(text = "₹ ${ammount.value}", color = Black)
                                    }
                                }
                                Row(
                                    modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(
                                            text = "From :",
                                            fontWeight = FontWeight(400),
                                            fontSize = 14.sp.noFontScale()
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(text = "ART5489439", color = Black)
                                    }
                                }
                                Row(
                                    modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(
                                            text = "For : :",
                                            fontWeight = FontWeight(400),
                                            fontSize = 14.sp.noFontScale()
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(text = "NA", color = Black)
                                    }
                                }
                                Row(
                                    modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start,
                                    ) {
                                        CustomText(
                                            text = "Transaction On :",
                                            fontWeight = FontWeight(400),
                                            fontSize = 14.sp.noFontScale()
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(text = "12/03/2024, 11:24:12", color = Black)
                                    }
                                }
                                Row(
                                    modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(
                                            text = "Payment Type :",
                                            fontWeight = FontWeight(400),
                                            fontSize = 14.sp.noFontScale()
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        CustomText(text = "One Type", color = Black)
                                    }
                                }
                            }
                            Spacer(Modifier.height(20.dp))
                            CustomButton(
                                text = "Continue",
                                modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
                                onClick = {
                                    showOTP.value = true
                                })
                            Spacer(Modifier.height(10.dp))

                        }
                    }
                } else {
                    AnimatedVisibility(showOTP.value) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(5.dp)
                                .background(White, RoundedCornerShape(15.dp)).padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Close,
                                    "",
                                    modifier = Modifier.clickable {
                                        showPayDialog.value = false
                                    })
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {


                                Image(
                                    painter = painterResource(com.isu.common.R.drawable.mail),
                                    "",
                                    modifier = Modifier.size(50.dp)
                                )


                            }
                            CustomText(
                                text = "Phone Verification",
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp.noFontScale(),
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(10.dp))
                            CustomText(
                                text = "To ensure your account security, please verify your phone number by entering the OTP (One-Time Password) sent to your mobile device.",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(10.dp)
                            )
                            CustomOTPInputFieldWithBox(
                                boxSize = 40.dp,
                                requireTimer = true,
                                label = "",
                                onValueChange = {},
                                otpLength = 6,
                                textStyle = TextStyle(fontSize = 14.sp.noFontScale())
                            )
                            Row(modifier = Modifier.fillMaxWidth()) {
                                CustomButton(text = "Verify", onClick = {

                                }, modifier = Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(10.dp))

                                CustomButton(modifier = Modifier.weight(1f).border(
                                    BorderStroke(1.dp, appMainColor),
                                    shape = RoundedCornerShape(5.dp)
                                ), innerComponent = {
                                    CustomText(
                                        text = "Cancel", color = appMainColor
                                    )
                                }, color = White, onClick = {


                                })


                            }
                        }
                    }
                }


            }


        }
    }


    Scaffold(modifier = modifier, containerColor = White, topBar = {
        CustomProfileTopBar(text = "Pay")
    }) { paddingValues ->
        KeyBoardAwareScreen(modifier = Modifier.padding(
            22.dp, top = paddingValues.calculateTopPadding(), 22.dp
        ), Alignment.CenterHorizontally, content = {
            Column(
                modifier = Modifier.heightIn(config.screenHeightDp.dp - 170.dp)
                    .padding(top = 10.dp), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {


                    OrderCard(headingText = "Transfer to", onCLick = {

                    }, beneDetails = BeneDetails())

                    CustomInputField(modifier = Modifier.height(100.dp), textStyle = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = appMainColor
                    ), placeholderComposable = {
                        CustomText(
                            text = "00.00",
                            fontSize = 22.sp.noFontScale(),
                            color = appMainColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }, labelComponent = {
                        val label = buildAnnotatedString {
                            append("Amount")
                            withStyle(style = SpanStyle(color = Red)) {
                                append("*")
                            }
                        }
                        CustomText(
                            annotatedString = label,
                            lineHeight = 30.sp.noFontScale(),
                            color = Black
                        )
                    }, keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                        state = ammount.value, onValueChange = {
                            if (it.isDigitsOnly()) {
                                ammount.value = it
                            }
                        })
                    CustomInputField(labelComponent = {
                        CustomText(
                            text = "Remark", lineHeight = 30.sp.noFontScale(), color = Black
                        )
                    }, placeholder = "Enter remarks", onValueChange = {
                        remarks.value = it
                    })


                }
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomButton(text = "Pay", onClick = {
                            showPayDialog.value = true
                        }, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(10.dp))

                        CustomButton(modifier = Modifier.weight(1f).border(
                            BorderStroke(1.dp, Color.LightGray.copy(0.6f)),
                            shape = RoundedCornerShape(5.dp)
                        ), innerComponent = {
                            CustomText(
                                text = "Cancel", color = appMainColor
                            )
                        }, color = White, onClick = {


                        })


                    }
                }
            }

        })
    }
}