package com.isu.prepaidcard.presentation.screens.dashboard.transactionSettings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.data.request.AuthTokenData
import com.isu.prepaidcard.data.request.TransactionSettingsRequest
import com.isu.prepaidcard.data.request.ViewCardDataByCardRefNumberRequest
import com.isu.prepaidcard.domain.usecase.SetTransactionLimitApiUseCase
import com.isu.prepaidcard.domain.usecase.carddata_api_usecase.ViewCardDataByRefIdUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val setTransactionLimitApiUseCase: SetTransactionLimitApiUseCase,
    private val viewCardDataByRefIdUseCase: ViewCardDataByRefIdUsecase,
) : ViewModel() {
    val atmEnable =
        mutableStateOf(false)
    val atmEnableResp =
        mutableStateOf(false)

    val atmLimit =
        mutableStateOf(0f)
    val atmMaxLimit =
        mutableStateOf(0f)

    val ecomEnable =
        mutableStateOf(false)
    val ecomEnableResp =
        mutableStateOf(false)

    val ecomLimit =
        mutableStateOf(0f)
    val ecomMaxLimit =
        mutableStateOf(0f)

    val contactLess =
        mutableStateOf(false)
    val contactLessResp =
        mutableStateOf(false)

    val contactLessLimit =
        mutableStateOf(0f)
    val contactLessMaxLimit =
        mutableStateOf(0f)

    val posEnable =
        mutableStateOf(false)
    val posEnableResp =
        mutableStateOf(false)

    val posLimit =
        mutableStateOf(0f)
    val posMaxLimit =
        mutableStateOf(0f)

    fun setTransactionLimit() {

        viewModelScope.launch {
            val cardReferenceNumber = try {
                dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)?.toLong()
            } catch (e: Exception) {
                0L
            }
            val latLong = LatLongFlowProvider.latLongFlow.first()

            val username =
                "CUST${dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)}"
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val request = TransactionSettingsRequest(
                atmLimit = atmLimit.value.toDouble(),
                cardRefNo = cardReferenceNumber?.toLong(),

                contactlessLimit = contactLessLimit.value.toDouble(),
                deviceId = deviceId,
                ecomLimit = ecomLimit.value.toDouble(),
                isAtmAllowed = atmEnable.value,
                isContactlessAllowed = contactLess.value,
                isEcomAllowed = ecomEnable.value,
                isPosAllowed = posEnable.value,
                latLong = latLong,
                posLimit = posLimit.value.toDouble(),

            )
            handleFlow(
                apiCall = {
                    setTransactionLimitApiUseCase.invoke(request)
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    if (it?.statusCode == 0) {
                        viewModelScope.launch {

                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.DynamicString(
                                        it.statusDesc ?: "Set Transaction Limit Successfully"
                                    )
                                )
                            )
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

    fun getCardNumberByRefid() {
        viewModelScope.launch {
            val cardReferenceNumber =
                dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val androidId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val request = ViewCardDataByCardRefNumberRequest(
                cardRefNo = cardReferenceNumber.toString(), deviceId = androidId, latLong = latLong,

                )
            handleFlow(
                apiCall = {
                    viewCardDataByRefIdUseCase.invoke(AuthTokenData("", "", request = request))
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    if (it?.statusCode == 0) {
                        viewModelScope.launch {
                            atmMaxLimit.value = it.data?.atm_max_limit?.toFloat() ?: 0f
                            posMaxLimit.value = it.data?.pos_max_limit?.toFloat() ?: 0f
                            ecomMaxLimit.value = it.data?.ecom_max_limit?.toFloat() ?: 0f
                            contactLessMaxLimit.value =
                                it.data?.contactless_max_limit?.toFloat() ?: 0f
                            atmLimit.value = it.data?.atm_limit?.toFloat() ?: 0f
                            posLimit.value = it.data?.pos_limit?.toFloat() ?: 0f
                            ecomLimit.value = it.data?.ecom_limit?.toFloat() ?: 0f
                            contactLessLimit.value = it.data?.contactless_limit?.toFloat() ?: 0f
                            ecomEnable.value = it.data?.isEcomAllowed ?: false
                            ecomEnableResp.value = it.data?.isEcomAllowed ?: false
                            atmEnable.value = it.data?.isAtmAllowed ?: false
                            atmEnableResp.value = it.data?.isAtmAllowed ?: false
                            posEnable.value = it.data?.isPosAllowed ?: false
                            posEnableResp.value = it.data?.isPosAllowed ?: false
                            contactLess.value = it.data?.isContactlessAllowed ?: false
                            contactLessResp.value = it.data?.isContactlessAllowed ?: false

                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.DynamicString(
                                        it.statusDesc ?: "Set Transaction Limit Successfully"
                                    )
                                )
                            )
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
}