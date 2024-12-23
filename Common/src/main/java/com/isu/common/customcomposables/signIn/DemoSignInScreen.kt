package com.isu.common.customcomposables.signIn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomText
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.CommonTextField

@Composable
fun DemoSignInScreen(number: Int = 0, onClick: () -> Unit) {

    Column(modifier = Modifier.padding(22.dp).wrapContentSize()) {
        CustomText(text = "Screen ${number}")
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(6) {
                CustomInputField { }
            }
        }
        CustomButton(text = "Next", onClick = { onClick() })


    }
}

sealed interface PersonalInfoTextInput : CommonTextField {
    data object FirstName : PersonalInfoTextInput
    data object LastName : PersonalInfoTextInput
    data object Email : PersonalInfoTextInput
    data object PhoneNumber : PersonalInfoTextInput
    data object DateOfBirth : PersonalInfoTextInput
    data object Gender : PersonalInfoTextInput
}

sealed interface CardInfoTextInput : CommonTextField {
    data object CardType : CardInfoTextInput
    data object CardNumber : CardInfoTextInput
}

@Composable
fun CardInfoScreen(
    cardInfoSCreenState: CardInfoSCreenState,
    number: Int = 0,
    onClick: () -> Unit,
    onEvent: (CommonScreenEvents) -> Unit,
) {


    Column(modifier = Modifier.padding(22.dp).wrapContentSize()) {
        CustomText(text = "Screen ${number}")
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            CustomInputField(state = cardInfoSCreenState.cardType) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        text = it,
                        type = CardInfoTextInput.CardType
                    )
                )
            }
            CustomInputField(state = cardInfoSCreenState.cardNumber) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        text = it,
                        type = CardInfoTextInput.CardNumber
                    )
                )
            }

            CustomButton(text = "Next", onClick = { onClick() })


        }
    }
}

@Composable
fun PersonalInfoScreen(
    personalInfoScreenState: PersonalInfoScreenState,
    number: Int = 0,
    onEvent: (CommonScreenEvents) -> Unit,
    onClick: () -> Unit,

    ) {


    Column(modifier = Modifier.padding(22.dp).wrapContentSize()) {
        CustomText(text = "Screen ${number}")
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CustomInputField(state = personalInfoScreenState.firstName) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        text = it,
                        type = PersonalInfoTextInput.FirstName
                    )
                )
            }
            CustomInputField(state = personalInfoScreenState.lastName) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        text = it,
                        type = PersonalInfoTextInput.LastName
                    )
                )
            }
            CustomInputField(state = personalInfoScreenState.gender) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        text = it,
                        type = PersonalInfoTextInput.Gender
                    )
                )
            }
            CustomInputField(state = personalInfoScreenState.email) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        text = it,
                        type = PersonalInfoTextInput.Email
                    )
                )
            }
            CustomInputField(state = personalInfoScreenState.phoneNumber) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        text = it,
                        type = PersonalInfoTextInput.PhoneNumber
                    )
                )
            }
            CustomInputField(state = personalInfoScreenState.dateOfBirth) {
                onEvent(
                    CommonScreenEvents.OnTextChanged(
                        text = it,
                        type = PersonalInfoTextInput.DateOfBirth
                    )
                )
            }
        }

        CustomButton(text = "Next", onClick = { onClick() })


    }
}