package com.isu.prepaidcard.presentation.screens.dashboard.linkcard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.CommonTextField
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.utils.ResponseBody
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.R
import com.isu.prepaidcard.data.request.LinkCarrdRequest
import com.isu.prepaidcard.data.request.TwoFARequestModel
import com.isu.prepaidcard.domain.usecase.LinkCardUseCase
import com.isu.prepaidcard.domain.usecase.TwoFAUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LinkCardViewModel @Inject constructor(
    private val linkCardUseCase: LinkCardUseCase,
    private val generateOtpUseCase: TwoFAUseCase,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    val mobileNumber = mutableStateOf("")
    val cardRefNumber = mutableStateOf("")
    val cardRefNumberErrorMessage = mutableStateOf("")
    val cardRefNumberError = mutableStateOf(false)
    private val _linkCardUiState = MutableStateFlow(LinkCardUiState())
    val linkCardUiState = _linkCardUiState.asStateFlow()
    val otp = mutableStateOf("")
    val otpError = mutableStateOf(false)
    val otpErrorMessage = mutableStateOf("")

    val linkparams = mutableStateOf("")

    fun onEvent(event: CommonScreenEvents) {
        when (event) {
            is CommonScreenEvents.CallApi<*> -> {}
            CommonScreenEvents.ClearFields -> {}
            CommonScreenEvents.ClearStack -> {}
            is CommonScreenEvents.NavigateTo -> {}
            is CommonScreenEvents.OnCheckChanged -> {}
            is CommonScreenEvents.OnClick<*> -> {}
            is CommonScreenEvents.OnTextChanged -> {
                handleLinkCardTextChange(type = event.type, value = event.text)
            }

            is CommonScreenEvents.SaveToDataStore<*> -> {}
            is CommonScreenEvents.GetDataStoreData<*> -> {
                viewModelScope.launch {
                    cardRefNumber.value =
                        dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID).toString()
                    mobileNumber.value =
                        dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
                            .toString()
                }

            }
            else -> {

            }
        }
    }
    fun generateLinkCardOtp(onSucces: () -> Unit) {
        if (validCardReferenceNumber()) {
            viewModelScope.launch {
                val cardRefId = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
                val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
                val mobileNUmber =
                    dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
                val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
                val latLong = LatLongFlowProvider.latLongFlow.first()
                val params = "LINK${Random.nextInt()}"

                val request = TwoFARequestModel(
                    deviceId = deviceId,
                    expiryTime = "5",
                    latLong = latLong,
                    mobileNumber = mobileNUmber,
                    paramA = "",
                    paramB = "",
                    paramC = "",
                    params = params,
                    purpose = "LINKCARD",
                    purposeInfo = cardRefNumber.value


                )
                handleFlow(
                    apiCall = {
                        generateOtpUseCase.invoke(request)
                    },
                    scope = viewModelScope,
                    dispatcher = Dispatchers.IO,
                    onSuccesss = {
                        viewModelScope.launch {
                            if (it?.statusCode == 0) {
                                onSucces()
                                linkparams.value = params
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.SuccessSnackBar,
                                        UiText.DynamicString(it.statusDesc ?: "Success")
                                    )
                                )
                            } else {
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

    }

    private fun validCardReferenceNumber(): Boolean {
        return true
    }

    fun linkCard() {
        viewModelScope.launch {
            val cardRefId = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val params = "LINK${Random.nextInt()}"

            val request = LinkCarrdRequest(
                cardRefNo = cardRefNumber.value,
                deviceId = deviceId,
                latLong = latLong,
                otp = otp.value,
                params = linkparams.value,

                )
            handleFlow(
                apiCall = {
                    linkCardUseCase.invoke(request)
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    viewModelScope.launch {
                        if (it?.statusCode == 0) {
                            NavigationEvent.helper.emit(
                                NavigationEvent.NavigateToNextScreen(
                                    ProfileScreen.CardManagementScreen
                                )
                            )

                        } else {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        it?.statusDesc ?: ""
                                    )
                                )
                            )
                        }

                    }
                },
                onFailure = { err, message: String, errBody: ResponseBody? ->
                    var errMessage = message + errBody
                    try {
                        val obj = JSONObject(errBody)
                        if (obj.has("statusDesc")) {
                            errMessage = obj.getString("statusDesc")

                        }
                    } catch (e: Exception) {

                    }
                    viewModelScope.launch {
                        LoadingErrorEvent.helper.emit(
                            LoadingErrorEvent.errorEncountered(
                                UiText.DynamicString(
                                    errMessage
                                )
                            )
                        )
                    }
                }


            )
        }

    }

    private fun handleLinkCardTextChange(type: CommonTextField, value: String) {
        when (type) {
            LinkCardTextFieldType.CardRefferenceNumberField -> {
                if (value.length <= 16) {
                    if (value.length == 16) {
                        _linkCardUiState.update {
                            it.copy(
                                cardRefferenceNumber = value,
                                cardRefferenceNumberError = false,
                                cardRefferenceNumberErrorMessage = UiText.StringResource(com.isu.common.R.string.empty)
                            )
                        }
                    } else {
                        _linkCardUiState.update {
                            it.copy(
                                cardRefferenceNumber = value,
                                cardRefferenceNumberError = true,
                                cardRefferenceNumberErrorMessage = UiText.StringResource(

                                    R.string.card_reference_number_must_be_12_digits_long
                                )
                            )
                        }

                    }
                }
            }

            LinkCardTextFieldType.OtpField -> {
                if (value.length <= 6) {
                    if (value.length == 6) {
                        _linkCardUiState.update {
                            it.copy(
                                otp = value,
                                otpError = false,
                                otpErrorMessage = UiText.StringResource(com.isu.common.R.string.empty)
                            )
                        }
                    } else {
                        _linkCardUiState.update {
                            it.copy(
                                otp = value,
                                otpError = true,
                                otpErrorMessage = UiText.StringResource(com.isu.common.R.string.otp_must_be_6_digits)
                            )
                        }
                    }

                }
            }
        }


    }

    fun getDataStoreData() {
        viewModelScope.launch {

        }
    }
}