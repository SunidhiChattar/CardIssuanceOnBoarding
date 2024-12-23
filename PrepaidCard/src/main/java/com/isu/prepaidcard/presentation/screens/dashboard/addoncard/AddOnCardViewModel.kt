package com.isu.prepaidcard.presentation.screens.dashboard.addoncard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.events.FetchAllCards
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.encryptdecrypt.AesGcmEncryption
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.data.request.AddOnRequest
import com.isu.prepaidcard.data.request.ModifyPinRequestBody
import com.isu.prepaidcard.data.request.TwoFARequestModel
import com.isu.prepaidcard.data.response.AddOnCardResponse
import com.isu.prepaidcard.domain.usecase.TwoFAUseCase
import com.isu.prepaidcard.domain.usecase.addoncard_usecase.AddOnCardApiUseCase
import com.isu.prepaidcard.domain.usecase.modifypin_usecase.ModifyPinApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class AddOnCardViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val addOnCardApiUseCase: AddOnCardApiUseCase,
    private val modifyPinUseCase: ModifyPinApiUseCase,
    private val generateOTP: TwoFAUseCase,
): ViewModel() {
    val otp =
        mutableStateOf("")

    val otpError =
        mutableStateOf(false)

    val otpErrorMessage =
        mutableStateOf("")

    val pin = mutableStateOf("")
    val confirmPin = mutableStateOf("")
    val isError = mutableStateOf(false)
    val otpParams = mutableStateOf("")

    val beneNumber = mutableStateOf("")
    val beneNumberError = mutableStateOf(false)
    val beneNumberErrorMessage = mutableStateOf("")

    fun modifyPin(onSuccess: () -> Unit) {

        viewModelScope.launch {
            val userName =
                "CUST${dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)}"
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val cardRef = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val request = ModifyPinRequestBody(
                encryptPin = AesGcmEncryption.encryptWithGivenIvAndAuth(data = confirmPin.value),
                latLong = latLong,
                cardRefId = cardRef,
                channel = "ANDROID",
                otp = otp.value,
                params = otpParams.value,
                deviceId = deviceId

            )
            handleFlow(apiCall = {
                modifyPinUseCase.invoke(request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {
                if (it?.statusCode == 0) {
                    viewModelScope.launch {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.SuccessSnackBar,
                                UiText.DynamicString("Pin modified successfully.")
                            )
                        )
                        onSuccess()
                    }
                } else {
                    viewModelScope.launch {
                        LoadingErrorEvent.helper.emit(
                            LoadingErrorEvent.errorEncountered(
                                UiText.DynamicString(
                                    it?.statusDesc ?: "Something went wrong"
                                )
                            )
                        )
                    }
                }
            }

            )
        }
    }


    fun generateOTP(onSuccess: () -> Unit) {

        viewModelScope.launch {
            val mobileNumber =
                dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val cardRef = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val otpParam = "RESET${Random.nextInt()}"
            val request = TwoFARequestModel(
                deviceId = deviceId,
                expiryTime = "5",
                latLong = latLong,
                mobileNumber = mobileNumber,
                paramA = null,
                paramB = null,
                paramC = null,
                params = otpParam,
                purpose = "RESET_PIN",
                purposeInfo = cardRef.toString()

            )
            handleFlow(apiCall = {
                generateOTP.invoke(request)
            }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {
                if (it?.statusCode == 0) {
                    viewModelScope.launch {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.SuccessSnackBar,
                                UiText.DynamicString("Pin modified successfully.")
                            )
                        )
                        otpParams.value = otpParam
                        onSuccess()
                    }
                }
            }

            )
        }
    }


    fun addOnCard(
        cardReferenceId: Int? = null,
        nameOnCard: String,
        addCardForSelf: Boolean,
        onSuccess: (AddOnCardResponse) -> Unit,
    ){
        viewModelScope.launch {


        val userName =
            "CUST${dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)}"
        val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
        val cardRef = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
        val latLong = LatLongFlowProvider.latLongFlow.first()
        val request = AddOnRequest(
            parentCardRefId = cardRef.toString(),
            latLong = latLong,
            channel = "ANDROID",
            deviceId = deviceId,
            nameonCard = nameOnCard,
            addoncardforself = addCardForSelf,
            childMobileNumber = if (beneNumber.value.isEmpty()) null else beneNumber.value

        )
        handleFlow(
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            apiCall = {
                addOnCardApiUseCase.invoke(
                    request = request
                )
            }
        ) {
            onLoading {
                viewModelScope.launch {
                    LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                }
            }
            onFailure { msg, _, _, resp ->
                viewModelScope.launch {
                    LoadingErrorEvent.helper.emit(
                        LoadingErrorEvent.errorEncountered(
                            UiText.DynamicString(
                                msg
                            )
                        )
                    )
                }
            }
            onSuccess { resp ->
                viewModelScope.launch {
                    if (resp?.statusCode == 0) {
                        onSuccess.invoke(resp)
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.SuccessSnackBar,
                                UiText.DynamicString("Add on card implemented successfully.")
                            )
                        )
                        FetchAllCards.fetch()
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
    }
}