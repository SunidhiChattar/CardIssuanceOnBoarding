package com.isu.prepaidcard.presentation.screens.dashboard.loadcard

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.events.Clickables
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.CommonTextField
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.Screen
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.data.request.UpiIntentRequest
import com.isu.prepaidcard.data.response.UpiIntentResponse
import com.isu.prepaidcard.domain.usecase.GetUpiIntentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LoadCardViewModel @Inject constructor(
    val getUpiIntentUseCase: GetUpiIntentUseCase,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    val intentUri = mutableStateOf<String?>(null)
    private val _loadCardState = MutableStateFlow(LoadCardState())
    val loadCardState = _loadCardState.asStateFlow()


    fun onEvent(event: CommonScreenEvents) {
        when (event) {
            is CommonScreenEvents.CallApi<*> -> {}
            CommonScreenEvents.ClearFields -> {}
            CommonScreenEvents.ClearStack -> {}
            is CommonScreenEvents.NavigateTo -> {
                handleLoadCardNavigation(event.screen)
            }

            is CommonScreenEvents.OnCheckChanged -> {}
            is CommonScreenEvents.OnClick<*> -> {
                handleLoadCardButtonClicks(
                    type = event.type,
                    onComplete = { event.onComplete() }
                )
            }

            is CommonScreenEvents.OnTextChanged -> {
                handleTextChange(type = event.type, text = event.text)
            }

            is CommonScreenEvents.SaveToDataStore<*> -> {}
            is CommonScreenEvents.GetDataStoreData<*> -> {
                viewModelScope.launch {
                    _loadCardState.update {
                        it.copy(
                            cardRefId = dataStoreManager.getPreferenceValue(PreferencesKeys.CARDNUMBER)
                                .toString()
                        )
                    }
                }

            }
        }
    }

    private fun handleLoadCardNavigation(event: Screen) {
        viewModelScope.launch {
            NavigationEvent.helper.navigateTo(event)
        }
    }

    private fun handleTextChange(type: CommonTextField, text: String) {
        when (type) {
            LoadCardTextFields.LoadCardAmount -> {
                _loadCardState.update {
                    it.copy(
                        amountToLoad = text,
                        amountToLoadError = false,
                        amountToLoadErrorMessage = UiText.DynamicString("")
                    )
                }
            }
        }
    }

    private fun handleLoadCardButtonClicks(type: Clickables, onComplete: () -> Unit) {
        when (type) {
            LoadCardButtons.LoadCard -> {
                if (amountValid()) {
                    viewModelScope.launch {


                        callGetIntentApi(onSuccess = { resp ->


                            viewModelScope.launch {
                                _loadCardState.update {
                                    it.copy(

                                        payIntentUri = resp.intentData,

                                        loadCardResult = UpiPaymentInfo(
                                            ammount = loadCardState.value.amountToLoad,
                                            txnId = resp.txnId,
                                            txnRef = resp.clientRefId,
                                            status = UpiPaymentStatus.Success(),
                                            responseCode = resp.statusCode,
                                            approvalRefNo = null

                                        )


                                    )

                                }
                                LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(true))
                                delay(1000)
                                LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(false))
                                onComplete()
                            }


                        })
                    }
                }
                Log.d("INTENT", "LoadCard:not vm ")

            }

            LoadCardButtons.PayApp -> {

                onComplete()

            }
        }
    }

    fun generateRandom19DigitNumber(): String {
        val firstPart = Random.nextLong(1_000_000_000L, 10_000_000_000L) // Generates 10 digits
        val secondPart = Random.nextLong(100_000_000L, 1_000_000_000L) // Generates 9 digits

        return "$firstPart$secondPart" // Concatenates 10 digits + 9 digits to form 19-digit number
    }

    private fun callGetIntentApi(onSuccess: (UpiIntentResponse) -> Unit) {
        var random = generateRandom19DigitNumber()
        viewModelScope.launch {
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val cardRefNumber = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)

        handleFlow(
            apiCall = {
                val request = UpiIntentRequest(
                    amount = loadCardState.value.amountToLoad,
                    clientRefId = "ISU${random.takeLast(16)}",
                    isWalletTopUp = false,
                    merchantType = "AGGREGATE",
                    paramA = cardRefNumber.toString(),
                    paramB = "",
                    paramC = "",
                    paymentMode = "INTENT",
                    remarks = "SchedulerTest",
                    requestingUserName = "CUST${userName}",
                    virtualAddress = "shalinipasumarthy@ybl"

                )
                getUpiIntentUseCase.invoke(request)
            },
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            onSuccesss = {
                if (it != null) {
                    if (it.statusCode == "0") {
                        Log.d("upiRESPONSE", "callGetIntentApi:$ ")
                        _loadCardState.update { res ->
                            res.copy(payIntentUri = it.intentData)

                        }
                        intentUri.value = it.intentData
                        onSuccess(it)
                    } else {
                        viewModelScope.launch {

                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        it.statusDesc ?: ""
                                    )
                                )
                            )
                        }
                    }

                }
            }

        )
        }
    }

    private fun amountValid(): Boolean {
        if (loadCardState.value.amountToLoad.isEmpty()) {
            _loadCardState.update {
                it.copy(
                    amountToLoadError = true,
                    amountToLoadErrorMessage = UiText.DynamicString("Please enter the amount")

                )
            }
        }
        return !loadCardState.value.amountToLoadError && loadCardState.value.amountToLoad.isNotEmpty()
    }

    fun updateFailurePaymenData() {
        val paymentInfo = loadCardState.value.loadCardResult
        _loadCardState.update {
            it.copy(
                loadCardResult = UpiPaymentInfo(
                    ammount = paymentInfo?.ammount,
                    txnId = paymentInfo?.txnId,
                    txnRef = paymentInfo?.txnRef,
                    status = UpiPaymentStatus.Failure(),
                    responseCode = paymentInfo?.responseCode,
                    approvalRefNo = paymentInfo?.approvalRefNo
                )
            )

        }
    }

}