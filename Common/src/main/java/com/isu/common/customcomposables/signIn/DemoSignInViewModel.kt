package com.isu.common.customcomposables.signIn

import androidx.lifecycle.ViewModel
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.CommonTextField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class CardInfoSCreenState(
    val cardType: String = "",
    val cardNumber: String = "",
)

data class PersonalInfoScreenState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
)

data class AddressScreenState(
    val addressLine1: String = "",
    val addressLine2: String = "",
    val city: String = "",
    val state: String = "",
    val pinCode: String = "",
    val country: String = "",
)

@HiltViewModel
class DemoSignInViewModel @Inject constructor() : ViewModel() {
    private val _cardInfoState = MutableStateFlow(CardInfoSCreenState())
    val cardInfoState = _cardInfoState.asStateFlow()
    private val _personalInfoState = MutableStateFlow(PersonalInfoScreenState())
    val personalInfoState = _personalInfoState.asStateFlow()
    private val _addressState = MutableStateFlow(AddressScreenState())
    val addressState = _addressState.asStateFlow()
    fun onEvent(event: CommonScreenEvents) {
        when (event) {
            is CommonScreenEvents.CallApi<*> -> {}
            CommonScreenEvents.ClearFields -> {}
            CommonScreenEvents.ClearStack -> {}
            is CommonScreenEvents.NavigateTo -> {}
            is CommonScreenEvents.OnCheckChanged -> {}
            is CommonScreenEvents.OnClick<*> -> {}
            is CommonScreenEvents.OnTextChanged -> {
                handleTextChange(event.type, event.text)
            }

            is CommonScreenEvents.SaveToDataStore<*> -> {}
            is CommonScreenEvents.GetDataStoreData<*> -> {}
            else -> {

            }
        }

    }

    private fun handleTextChange(type: CommonTextField, text: String) {
        when (type) {
            PersonalInfoTextInput.FirstName -> {
                _personalInfoState.value = personalInfoState.value.copy(firstName = text)
            }

            PersonalInfoTextInput.LastName -> {
                _personalInfoState.value = personalInfoState.value.copy(lastName = text)

            }

            PersonalInfoTextInput.DateOfBirth -> {
                _personalInfoState.value = personalInfoState.value.copy(dateOfBirth = text)
            }

            PersonalInfoTextInput.Email -> {
                _personalInfoState.value = personalInfoState.value.copy(email = text)
            }

            PersonalInfoTextInput.Gender -> {
                _personalInfoState.value = personalInfoState.value.copy(gender = text)
            }

            PersonalInfoTextInput.PhoneNumber -> {
                _personalInfoState.value = personalInfoState.value.copy(phoneNumber = text)

            }

            CardInfoTextInput.CardNumber -> {
                _cardInfoState.value = cardInfoState.value.copy(cardNumber = text)
            }

            CardInfoTextInput.CardType -> {
                _cardInfoState.value = cardInfoState.value.copy(cardType = text)
            }

        }
    }

}