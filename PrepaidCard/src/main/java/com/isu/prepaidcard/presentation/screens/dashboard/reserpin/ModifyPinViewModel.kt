package com.isu.prepaidcard.presentation.screens.dashboard.reserpin

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.events.Clickables
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.CommonTextField
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.encryptdecrypt.AesGcmEncryption
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.R
import com.isu.prepaidcard.data.request.ModifyPinRequestBody
import com.isu.prepaidcard.data.request.TwoFARequestModel
import com.isu.prepaidcard.domain.usecase.TwoFAUseCase
import com.isu.prepaidcard.domain.usecase.modifypin_usecase.ModifyPinApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ModifyPinViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val generateOtpUseCase: TwoFAUseCase,
    private val modifyPinUseCase: ModifyPinApiUseCase,
) :
    ViewModel() {
    val otp = mutableStateOf("")
    val otpError = mutableStateOf(false)
    val otpErrorMessage = mutableStateOf("")
    private val _modifyPinState = MutableStateFlow<ModifyPinState>(ModifyPinState())
    val modifyPinState = _modifyPinState.asStateFlow()

    val otpParams = mutableStateOf("")

    fun onEvent(event: CommonScreenEvents) {
        when (event) {
            is CommonScreenEvents.CallApi<*> -> {}
            CommonScreenEvents.ClearFields -> {
                otp.value = ""
                otpErrorMessage.value = ""
                otpError.value = false
                _modifyPinState.update {
                    it.copy(
                        pin = "",
                        confirmPin = "",
                        pinError = false,
                        pinErrorMessage = UiText.DynamicString(""),
                        confirmPinError = false,
                        confirmPinErrorMessage = UiText.DynamicString("")
                    )
                }
            }
            CommonScreenEvents.ClearStack -> {}
            is CommonScreenEvents.GetDataStoreData<*> -> {
                viewModelScope.launch {
                    val cardNumber = dataStoreManager.getPreferenceValue(PreferencesKeys.CARDNUMBER)
                    val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USERNAME)
                    _modifyPinState.update {
                        it.copy(cardNumber = cardNumber ?: "", cardHolderName = userName ?: "")
                    }
                }
            }

            is CommonScreenEvents.NavigateTo -> {}
            is CommonScreenEvents.OnCheckChanged -> {}
            is CommonScreenEvents.OnClick<*> -> {
                handleModifyButtonClick(type = event.type)
            }

            is CommonScreenEvents.OnTextChanged -> {
                handleModifyPinTextChange(type = event.type, value = event.text)
            }

            is CommonScreenEvents.SaveToDataStore<*> -> {}
        }
    }

    suspend fun modifyPin(
        cardReferenceId: Int? = null
    ){
        val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
        val latLong = LatLongFlowProvider.latLongFlow.first()
        val cardRefId = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)

        val request = ModifyPinRequestBody(
            cardRefId = cardRefId,
            encryptPin = AesGcmEncryption.encryptWithGivenIvAndAuth(data = modifyPinState.value.pin),
            latLong = latLong,
            channel = "ANDROID",
            otp = otp.value,
            params = otpParams.value,
            deviceId = deviceId
        )
        handleFlow(
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            apiCall = {
                modifyPinUseCase.invoke(
                    request = request
                )
            }
        ) {
            onLoading {
                viewModelScope.launch {
                    LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                }
            }

            onSuccess { resp ->
                viewModelScope.launch {
                    if (resp?.statusCode == 0) {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.SuccessSnackBar,
                                UiText.DynamicString("Your PIN has been successfully reset.")
                            )
                        )

                        NavigationEvent.helper.navigateTo(
                            ProfileScreen.CardManagementScreen,
                            ProfileScreen.DashBoardScreen
                        )
                    } else {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.ErrorSnackBar,
                                UiText.DynamicString(resp?.statusDesc.toString())
                            )
                        )
                    }
                }
            }
        }
    }
    private fun handleModifyButtonClick(type: Clickables) {
        when (type) {
            ModifyPinButton.Cancel -> {
                viewModelScope.launch {
                    NavigationEvent.helper.emit(NavigationEvent.NavigateToNextScreen(ProfileScreen.CardManagementScreen))
                }
            }

            ModifyPinButton.Submit -> {
                //appicall
                if (fieldsValid()) {
                    viewModelScope.launch {
                        val cardRef =
                            dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
                        generateOtp {
                            viewModelScope.launch {
                                NavigationEvent.helper.navigateTo(CardManagement.ModifyPinOTPScreen)
                            }

                        }

                    }
                }


            }
        }
    }

    private fun fieldsValid(): Boolean {
        val isPinValid = isPinValid()
        val isConfirmPinValis = isConfirmPinValid()
        return isPinValid && isConfirmPinValis
    }

    private fun isConfirmPinValid(): Boolean {
        if (modifyPinState.value.pin.isEmpty()) {
            _modifyPinState.update {
                it.copy(
                    confirmPinError = true,
                    confirmPinErrorMessage = UiText.StringResource(com.isu.common.R.string.empty_field)
                )
            }
        }
        return modifyPinState.value.confirmPin.isNotEmpty() && !modifyPinState.value.confirmPinError
    }

    private fun isPinValid(): Boolean {
        if (modifyPinState.value.pin.isEmpty()) {
            _modifyPinState.update {
                it.copy(
                    pinError = true,
                    pinErrorMessage = UiText.StringResource(com.isu.common.R.string.empty_field)
                )
            }
        }
        return modifyPinState.value.pin.isNotEmpty() && !modifyPinState.value.pinError
    }

    private fun handleModifyPinTextChange(type: CommonTextField, value: String) {
        when (type) {
            ModifyPinTextField.ConfirmPin -> {

                if (value.length <= 4) {
                    if (value.length == 4) {

                        if (modifyPinState.value.pin.matches(value.toRegex())) {
                            _modifyPinState.update {

                                it.copy(
                                    confirmPin = value,
                                    confirmPinError = false,
                                    confirmPinErrorMessage = UiText.StringResource(
                                        com.isu.common.R.string.empty
                                    )
                                )
                            }
                        } else {
                            _modifyPinState.update {

                                it.copy(
                                    confirmPin = value,
                                    confirmPinError = true,
                                    confirmPinErrorMessage = UiText.StringResource(
                                        R.string.pin_mismatch
                                    )
                                )
                            }
                        }
                    } else {
                        _modifyPinState.update {
                            it.copy(
                                confirmPin = value,
                                confirmPinError = true,
                                confirmPinErrorMessage = UiText.StringResource(
                                    R.string.pinlength
                                )
                            )
                        }
                    }
                }


            }

            ModifyPinTextField.Pin -> {
                if (value.length <= 4) {

                    if (modifyPinState.value.confirmPin.isEmpty() || value.matches(modifyPinState.value.confirmPin.toRegex())) {
                        _modifyPinState.update {
                            it.copy(
                                pin = value,
                                pinError = false,
                                pinErrorMessage = UiText.StringResource(
                                    com.isu.common.R.string.empty
                                )
                            )
                        }
                    } else {
                        _modifyPinState.update {
                            it.copy(
                                pin = value,
                                pinError = true,
                                pinErrorMessage = UiText.StringResource(
                                    R.string.pin_mismatch
                                )
                            )
                        }
                    }

                }

            }
        }
    }

    fun generateOtp(onSucess: () -> Unit) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val mobileNumber =
                dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val cardReferenceId = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val params = "RESETPIN${Random.nextInt()}"

            val request = TwoFARequestModel(

                deviceId = deviceId,
                expiryTime = "5",
                latLong = latLong,
                mobileNumber = mobileNumber,
                paramA = "",
                paramB = "",
                paramC = "",
                params = params,
                purpose = "RESET_PIN",
                purposeInfo = cardReferenceId.toString()

            )
            handleFlow(apiCall = {
                generateOtpUseCase.invoke(request = request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {

                viewModelScope.launch {
                    ShowSnackBarEvent.helper.emit(
                        ShowSnackBarEvent.show(
                            SnackBarType.SuccessSnackBar,
                            UiText.DynamicString(it?.statusDesc ?: "")
                        )
                    )
                    NavigationEvent.helper.navigateTo(CardManagement.ModifyPinOTPScreen)
                }
                otpParams.value = params

            }

            )
        }
    }
}