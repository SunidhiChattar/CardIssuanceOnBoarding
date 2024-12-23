package com.isu.prepaidcard.presentation.screens.dashboard.linkcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.appMainColor
import com.isu.common.utils.UiText
import kotlinx.coroutines.launch

@Preview
@Composable
fun LinkCard(modifier: Modifier = Modifier, linkCardViewModel: LinkCardViewModel) {
    val cardReference = linkCardViewModel.cardRefNumber
    val cardReferenceError = remember {
        mutableStateOf(false)
    }
    val cardreferenceErrorMessage = remember {
        mutableStateOf("")
    }
    val mobileNumber = linkCardViewModel.mobileNumber
    val mobileNumberError = remember {
        mutableStateOf(false)
    }
    val mobileNumberErrorMessage = remember {
        mutableStateOf("")
    }
    val upiMode = remember {
        mutableStateOf(false)
    }
    val vpaMode = remember {
        mutableStateOf(false)
    }
    val qrMode = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        linkCardViewModel.getDataStoreData()
        cardReference.value = ""

    }
    val context = LocalContext.current

    val config = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Link Card"
            )
        },
        containerColor = Color.White
    ) {
        it
        val screenHeight = LocalConfiguration.current.screenHeightDp
        KeyBoardAwareScreen(
            shouldScroll = false,
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = 22.dp,
                bottom = 0.dp,
                end = 22.dp
            )
        ) {
            Column(
                modifier = Modifier.heightIn(config.screenHeightDp.dp - (it.calculateTopPadding() + 90.dp)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    CustomInputField(
                        enabled = false,
                        state = "GPR",
                        label = "Selected Card Type"
                    )
                    CustomInputField(
                        enabled = true,
                        state = cardReference.value,
                        onValueChange = {
                            if (it.isDigitsOnly()) {
                                if (it.length <= 12) {
                                    if (it.length < 10) {
                                        cardReferenceError.value = true
                                        cardreferenceErrorMessage.value =
                                            "Card Reference must 10 to 12 digits long"
                                    } else {
                                        cardReferenceError.value = false
                                        cardreferenceErrorMessage.value = ""
                                    }

                                    cardReference.value = it
                                }

                            }
                        },
                        isError = cardReferenceError.value,
                        errorMessage = cardreferenceErrorMessage.value,
                        placeholder = "Enter Card Reference Number",
                        label = "Card Reference Number",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    CustomText(text = "")

                }
                Column(modifier = Modifier.heightIn(20.dp)) {}
                Column {

                    CustomButton(text = "Continue", color = appMainColor, onClick = {
                        if (mobileNumber.value.isEmpty()) {
                            mobileNumberError.value = true
                            mobileNumberErrorMessage.value = "Field cannot be empty"
                        } else if (cardReference.value.isEmpty()) {
                            cardReferenceError.value = true
                            cardreferenceErrorMessage.value = "Field cannot be empty"
                        } else if (!mobileNumberError.value && !cardReferenceError.value) {
                            linkCardViewModel.generateLinkCardOtp(onSucces = {

                                scope.launch {
                                    NavigationEvent.helper.navigateTo(CardManagement.LinkCardOtpScreen)
                                }
                            })
                        }


                    })
                }
            }


        }
    }


}