package com.isu.prepaidcard.presentation.screens.dashboard.orderphysicalcard

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.customcomposables.ShippingAddressItem
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.data.request.FetchAddressRequest
import com.isu.prepaidcard.data.request.OrderPhysicalCardRequest
import com.isu.prepaidcard.domain.usecase.FetchAdrressUseCase
import com.isu.prepaidcard.domain.usecase.FetchOrderHistoryUseCase
import com.isu.prepaidcard.domain.usecase.orderphysical_api_usecase.OrderPhysicalApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderPhysicalCardViewModel @Inject constructor(
    private val orderPhysicalCardUseCase: OrderPhysicalApiUseCase,
    private val fetchAddressUseCase: FetchAdrressUseCase,
    private val orderHistoryUseCase: FetchOrderHistoryUseCase,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    val userName: MutableState<String> =
        mutableStateOf("")
    val cardNumber: MutableState<String> =
        mutableStateOf("")

    val availanleAddress: SnapshotStateList<ShippingAddressItem?> = mutableStateListOf()
    val selectedAddressForShipping: MutableState<ShippingAddressItem?> = mutableStateOf(null)
    val nameOnCard: MutableState<String> = mutableStateOf("")
    val nameOnCardError: MutableState<Boolean> = mutableStateOf(false)
    val nameOnCardErrorMessage: MutableState<String> = mutableStateOf("")
    fun clearFields() {
        nameOnCard.value = ""
        nameOnCardError.value = false
        nameOnCardErrorMessage.value = ""
    }

    fun orderPhysicalCard(onSuccess: () -> Unit) {
        val selectedAddress = selectedAddressForShipping.value
        viewModelScope.launch {
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val cardRefId = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val userName =
                "CUST${dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)}"
            val request = OrderPhysicalCardRequest(
                addresType = selectedAddress?.type,
                address = selectedAddress?.address2,
                address1 = selectedAddress?.address1,
                cardRefId = cardRefId.toString(),
                city = selectedAddress?.city,
                country = selectedAddress?.country,
                deviceId = deviceId,
                latLong = latLong,
                nameOnCard = nameOnCard.value,
                personalizationMethod = "INDENT",
                pinType = "GREENPIN",
                pincode = selectedAddress?.pinCode,
                state = selectedAddress?.state

            )
            handleFlow(
                apiCall = {
                    orderPhysicalCardUseCase.invoke(request)
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    if (it?.statusCode == 0) {
                        viewModelScope.launch {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.DynamicString(it.statusDesc ?: "Success")
                                )
                            )



                            viewModelScope.launch {
                                    NavigationEvent.helper.navigateTo(ProfileScreen.CardManagementScreen)
                                }


                        }
                    } else {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    error = UiText.DynamicString(
                                        it?.statusDesc ?: ""
                                    )
                                )
                            )
                        }
                    }
                }

            )
        }


    }



    fun fetchAddress() {
        viewModelScope.launch {
            val userName =
                "CUST${dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)}"
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val deviceID = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val request = FetchAddressRequest(
                deviceId = deviceID,
                latLong = latLong
            )
            handleFlow(
                apiCall = {
                    fetchAddressUseCase.invoke(request)
                },
                onSuccesss = {
                    if (it?.statusCode == 0) {
                        availanleAddress.clear()
                        availanleAddress.addAll(it.data?.shippingAddress ?: emptyList())
                    } else {
                        viewModelScope.launch {

                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    error = UiText.DynamicString(
                                        it?.statusDesc ?: ""
                                    )
                                )
                            )
                        }
                    }

                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,

                )
        }

    }

    fun getSavedData() {
        viewModelScope.launch {
            userName.value =
                dataStoreManager.getPreferenceValue(PreferencesKeys.USERNAME).toString()
            nameOnCard.value = userName.value
            cardNumber.value =
                dataStoreManager.getPreferenceValue(PreferencesKeys.CARDNUMBER).toString()
        }
    }

}