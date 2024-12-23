package com.isu.prepaidcard.presentation.screens.dashboard.kitToKitTransfer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.NavigationEvent
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.encryptdecrypt.AesGcmEncryption
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.data.request.KitToKitRequest
import com.isu.prepaidcard.domain.usecase.KitToKitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KitToKitViewModel @Inject constructor(
    private val kitToKitUseCase: KitToKitUseCase,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    val fromCard = mutableStateOf("")
    val toCard = mutableStateOf("")
    val toCardError = mutableStateOf(false)
    val toCardErrorMessage = mutableStateOf("")
    val cardHolderName = mutableStateOf("")

    fun clearFields() {
        toCard.value = ""
    }

    fun getDataStoreData() {
        viewModelScope.launch {
            fromCard.value = "XXXX XXXX XXXXX ${
                dataStoreManager.getPreferenceValue(PreferencesKeys.CARDNUMBER)?.takeLast(4)
            }"
            cardHolderName.value =
                dataStoreManager.getPreferenceValue(PreferencesKeys.USERNAME).toString()
        }
    }

    fun kitToKitTransferBalance() {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val cardRefId = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val username =
                "CUST${dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)}"
            val encryptedCard = AesGcmEncryption.encryptWithGivenIvAndAuth(data = toCard.value)
            val request = KitToKitRequest(
                deviceId = deviceId,
                latLong = latLong,
                parentCardRefNumber = cardRefId.toString(),
                reissuedEncryptedCard = encryptedCard,


                )
            handleFlow(apiCall = { kitToKitUseCase.invoke(request = request) },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    if (it?.statusCode == 0) {
                        viewModelScope.launch {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    type = SnackBarType.SuccessSnackBar,
                                    UiText.DynamicString(
                                        it.statusDesc ?: "Kit to Kit Transfer Successfull"
                                    )
                                )
                            )
                            NavigationEvent.helper.navigateBack()
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
                })
        }
    }
}