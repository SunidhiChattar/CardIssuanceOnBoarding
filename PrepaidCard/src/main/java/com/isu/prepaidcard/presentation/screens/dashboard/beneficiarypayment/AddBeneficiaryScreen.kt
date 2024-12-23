package com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileDropDown
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.ui.theme.appMainColor
import kotlinx.coroutines.launch

@Composable
fun AddBeneficiaryScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val name = remember {
        mutableStateOf("")
    }
    val accountNumber = remember {
        mutableStateOf("")
    }
    val IFSCCode = remember {
        mutableStateOf("")
    }
    val beneBank = remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            CustomProfileTopBar(text = "Add Beneficiary")
        },
        containerColor = White,

        ) {
        val config = LocalConfiguration.current
        KeyBoardAwareScreen(
            modifier = Modifier.heightIn(config.screenHeightDp.dp).fillMaxWidth()
                .padding(top = it.calculateTopPadding(), start = 22.dp, end = 22.dp)
        ) {
            Column(
                modifier = Modifier.heightIn(config.screenHeightDp.dp - 170.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                CustomInputField(labelComponent = {
                    val label = buildAnnotatedString {
                        append("Beneficiary Name")
                        withStyle(style = SpanStyle(color = Red)) {
                            append("*")
                        }
                    }
                    CustomText(
                        annotatedString = label,
                        lineHeight = 30.sp.noFontScale(),
                        color = Black
                    )
                }, placeholder = "Enter Beneficiary Name", state = name.value) {

                }
                CustomInputField(labelComponent = {
                    val label = buildAnnotatedString {
                        append("Beneficiary Account Number")
                        withStyle(style = SpanStyle(color = Red)) {
                            append("*")
                        }
                    }
                    CustomText(
                        annotatedString = label,
                        lineHeight = 30.sp.noFontScale(),
                        color = Black
                    )
                }, placeholder = "Enter Beneficiary Account Number", state = accountNumber.value) {

                }
                CustomInputField(labelComponent = {
                    val label = buildAnnotatedString {
                        append("IFSC Code")
                        withStyle(style = SpanStyle(color = Red)) {
                            append("*")
                        }
                    }
                    CustomText(
                        annotatedString = label,
                        lineHeight = 30.sp.noFontScale(),
                        color = Black
                    )
                }, placeholder = "Enter IFSC Code", label = IFSCCode.value) {

                }

                CustomProfileDropDown(labelComponent = {
                    val label = buildAnnotatedString {
                        append("Beneficiary Bank")
                        withStyle(style = SpanStyle(color = Red)) {
                            append("*")
                        }
                    }
                    CustomText(
                        annotatedString = label,
                        lineHeight = 30.sp.noFontScale(),
                        color = Black
                    )
                }, placeholder = "Select the Bank", state = beneBank.value) {

                }


            }
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomButton(text = "Add", onClick = {
                            scope.launch {
                                NavigationEvent.helper.navigateTo(
                                    CardManagement.BeneSelectionScreen,
                                    ProfileScreen.DashBoardScreen
                                )
                            }
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

        }
    }
}