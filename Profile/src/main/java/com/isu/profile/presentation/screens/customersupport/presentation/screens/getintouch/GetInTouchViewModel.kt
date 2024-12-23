package com.isu.profile.presentation.screens.customersupport.presentation.screens.getintouch

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.CommonTextField
import com.isu.common.utils.UiText
import com.isu.common.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * @author-karthik
 * Get in touch view model
 * handles with the business logic of getInTouch ViewMoel
 * @constructor Create empty Get in touch view model
 */
@HiltViewModel
class GetInTouchViewModel @Inject constructor():ViewModel() {
    private val _screenUiState= MutableStateFlow(GetInTouchUiState())
    val screenUiState: StateFlow<GetInTouchUiState> =_screenUiState

    fun onEvent(event: CommonScreenEvents){
        when(event){
            is CommonScreenEvents.CallApi<*> -> TODO()
            CommonScreenEvents.ClearFields -> TODO()
            CommonScreenEvents.ClearStack -> TODO()
            is CommonScreenEvents.NavigateTo -> TODO()
            is CommonScreenEvents.OnCheckChanged -> TODO()
            is CommonScreenEvents.OnClick<*> -> TODO()
            is CommonScreenEvents.OnTextChanged -> handleInputChange(event.type,event.text)
            else -> {

            }
        }
    }

    private fun handleInputChange(type: CommonTextField, text: String) {
        when(type){
            GetInTouchInput.CompanyName -> {
                _screenUiState.update {
                    it.copy(companyName = text)
                }
            }
            GetInTouchInput.CompanySize -> {
                _screenUiState.update {
                    it.copy(companySize = text)
                }
            }
            GetInTouchInput.Country -> {
                _screenUiState.update {
                    it.copy(country = text)
                }
            }
            GetInTouchInput.FirstName -> {
                _screenUiState.update {
                    it.copy(firstName = text)
                }
            }
            GetInTouchInput.LastName ->{
                _screenUiState.update {
                    it.copy(lastName = text)
                }
            }
            GetInTouchInput.Phone -> {
                if(text.isDigitsOnly()){
                    Validation.isValidPhoneNumber(text, onValid = {
                        _screenUiState.update {
                            it.copy(
                                phoneNumber = text,
                                phoneNumberError = false,
                                phoneNumberErrorMessage = UiText.StringResource(
                                    com.isu.common.R.string.empty
                                )
                            )
                        }
                    }, onError = { msg ->
                        _screenUiState.update {
                            it.copy(
                                phoneNumber = text,
                                phoneNumberError = true,
                                phoneNumberErrorMessage = msg
                            )
                        }
                    })
                    _screenUiState.update {
                        it.copy(phoneNumber = text)
                    }
                }
            }

            GetInTouchInput.PointOfDiscussion -> {
                _screenUiState.update {
                    it.copy(pointOfDiscussion = text)
                }

            }
        }
    }


}