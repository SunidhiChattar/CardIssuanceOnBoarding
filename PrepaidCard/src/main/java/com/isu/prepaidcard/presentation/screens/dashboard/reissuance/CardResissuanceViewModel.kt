package com.isu.prepaidcard.presentation.screens.dashboard.reissuance

import android.util.Log
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
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.utils.CardStatus
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.data.request.AddNewAddressRequest
import com.isu.prepaidcard.data.request.ChangeCardStatusRequest
import com.isu.prepaidcard.data.request.DeleteAddressRequest
import com.isu.prepaidcard.data.request.EditAddressRequest
import com.isu.prepaidcard.data.request.FetchAddressRequest
import com.isu.prepaidcard.data.request.FetchPinCodeDataRequest
import com.isu.prepaidcard.data.response.FetchPinCodeDataResponse
import com.isu.prepaidcard.domain.usecase.AddAddressUseCase
import com.isu.prepaidcard.domain.usecase.DeleteAddressUseCase
import com.isu.prepaidcard.domain.usecase.EditAddressUseCase
import com.isu.prepaidcard.domain.usecase.FetchAdrressUseCase
import com.isu.prepaidcard.domain.usecase.FetchPinCodeUseCase
import com.isu.prepaidcard.domain.usecase.changecardstatus_api_usecase.ChangeCardStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardReIssuanceViewModel @Inject constructor(
    private val addAddressUseCase: AddAddressUseCase,
    private val fetchAddressUseCase: FetchAdrressUseCase,
    private val changeStatusUseCase: ChangeCardStatusUseCase,
    private val editAddressUseCase: EditAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val dataStoreManager: DataStoreManager,
    private val fetchPinCodeData: FetchPinCodeUseCase
) : ViewModel(

) {


    val cardHolderName = mutableStateOf("")
    val cardNumber = mutableStateOf("")
    val homeSelected = mutableStateOf(false)
    val workSelected = mutableStateOf(false)
    val othersSelected = mutableStateOf(false)
    val edithomeSelected = mutableStateOf(false)
    val editworkSelected = mutableStateOf(false)
    val editothersSelected = mutableStateOf(false)

    val addressLine1 = mutableStateOf("")
    val addressLine2 = mutableStateOf("")
    val pinCode = mutableStateOf("")
    val state = mutableStateOf("")
    val city = mutableStateOf("")
    val addressLine1Error = mutableStateOf(false)
    val addressLine2Error = mutableStateOf(false)
    val pinCodeError = mutableStateOf(false)
    val stateError = mutableStateOf(false)
    val cityError = mutableStateOf(false)
    val addressLine1ErrorMessage = mutableStateOf("")
    val addressLine2ErrorMessage = mutableStateOf("")
    val pinCodeErrorMessage = mutableStateOf("")
    val stateErrorMessage = mutableStateOf("")
    val cityErrorMessage = mutableStateOf("")
    val editAddressID = mutableStateOf("")
    val editaddressLine1 = mutableStateOf("")
    val editaddressLine2 = mutableStateOf("")
    val editpinCode = mutableStateOf("")
    val editstate = mutableStateOf("")
    val editcity = mutableStateOf("")
    val editaddressLine1Error = mutableStateOf(false)
    val editaddressLine2Error = mutableStateOf(false)
    val editpinCodeError = mutableStateOf(false)
    val editstateError = mutableStateOf(false)
    val editcityError = mutableStateOf(false)
    val editaddressLine1ErrorMessage = mutableStateOf("")
    val editaddressLine2ErrorMessage = mutableStateOf("")
    val editpinCodeErrorMessage = mutableStateOf("")
    val editstateErrorMessage = mutableStateOf("")
    val editcityErrorMessage = mutableStateOf("")
    val addressList: SnapshotStateList<ShippingAddressItem?> = mutableStateListOf()

    fun addAddress(onSuccess: () -> Unit) {
        if (allValid()) {
            viewModelScope.launch {
                val latLong = LatLongFlowProvider.latLongFlow.first()
                val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
                val shippingAddress = AddNewAddressRequest.ShippingAddress(
                    address1 = addressLine1.value.trim(),
                    address2 = addressLine2.value.trim(),
                    city = city.value.trim(),
                    country = "India",
                    pinCode = pinCode.value.trim(),
                    state = state.value.trim(),
                    addressType = if (homeSelected.value) {
                        "Home"
                    } else if (workSelected.value) {
                        "Work"
                    } else {
                        "Others"
                    }

                )
                val request = AddNewAddressRequest(
                    shippingAddress = shippingAddress,
                    latLong = latLong,
                    deviceId = deviceId

                )
                handleFlow(
                    apiCall = {
                        addAddressUseCase.invoke(request)
                    },
                    scope = viewModelScope,
                    dispatcher = Dispatchers.IO,
                    onSuccesss = {
                        viewModelScope.launch {

                            if (it?.statusCode == 0) {
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.SuccessSnackBar,
                                        msg = UiText.DynamicString(it.statusDesc ?: "")
                                    )
                                )
                                onSuccess()
                            } else {
                                LoadingErrorEvent.helper.emit(
                                    LoadingErrorEvent.errorEncountered(
                                        UiText.DynamicString(it?.statusDesc ?: "")
                                    )
                                )
                            }
                        }
                    }

                )
            }
        }

    }

    private fun allValid(): Boolean {
        val addressLine1Valid: Boolean = isAddressLine1Valid()
        val addressLine2Valid: Boolean = isAddressLine2Valid()
        val pinCodeValid: Boolean = isPinCodeValid()
        val cityValid: Boolean = isCityValid()
        val stateValid: Boolean = isStateValid()
        return addressLine1Valid && addressLine2Valid && pinCodeValid && cityValid && stateValid
    }


    private fun isStateValid(): Boolean {
        if (state.value.isEmpty()) {
            stateError.value = true
            stateErrorMessage.value = "Field cannot be empty"
        }
        return state.value.isNotEmpty()
    }

    private fun isPinCodeValid(): Boolean {
        if (pinCode.value.isEmpty()) {
            pinCodeError.value = true
            pinCodeErrorMessage.value = "Field cannot be empty"
        }
        return pinCode.value.isNotEmpty() && !pinCodeError.value
    }

    private fun isCityValid(): Boolean {
        if (city.value.isEmpty()) {
            cityError.value = true
            cityErrorMessage.value = "Field cannot be empty"
        }
        return city.value.isNotEmpty()
    }

    private fun isAddressLine2Valid(): Boolean {
        if (state.value.isEmpty()) {
            addressLine2Error.value = true
            addressLine2ErrorMessage.value = "Field cannot be empty"
        }
        return addressLine2.value.isNotEmpty()
    }

    private fun isAddressLine1Valid(): Boolean {
        if (addressLine1.value.isEmpty()) {
            addressLine1Error.value = true
            addressLine1ErrorMessage.value = "Field cannot be empty"
        }
        return addressLine1.value.isNotEmpty()
    }

    fun fetchAddress() {
        viewModelScope.launch {
            val deviceID =
                dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()

            val request = FetchAddressRequest(
                deviceId = deviceID,
                latLong = latLong,
            )
            handleFlow(
                apiCall = {
                    fetchAddressUseCase.invoke(request)
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    viewModelScope.launch {

                        if (it?.statusCode == 0) {
                            addressList.clear()
                            addressList.addAll(it.data?.shippingAddress ?: emptyList())
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    msg = UiText.DynamicString(it.statusDesc ?: "")
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
                onFailure = { message, errorMessage, errorObjectJsonString ->
                    viewModelScope.launch {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.ErrorSnackBar,
                                msg = UiText.DynamicString(errorMessage)
                            )
                        )
                    }


                }

            )
        }
    }

    fun getSavedData() {
        viewModelScope.launch {
            cardNumber.value = dataStoreManager.getPreferenceValue(PreferencesKeys.CARDNUMBER) ?: ""
            cardHolderName.value =
                dataStoreManager.getPreferenceValue(PreferencesKeys.USERNAME) ?: ""
        }
    }

    fun reIssueCard(onSuccess: () -> Unit) {
        if (slectaddressLine1.value.isNotEmpty()) {
            viewModelScope.launch {
                val cardREdfNumber =
                    dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
                val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
                val latLong = LatLongFlowProvider.latLongFlow.first()
                val request = ChangeCardStatusRequest(
                    cardRefNumber = cardREdfNumber.toString(),
                    deviceId = deviceId,
                    latLong = latLong,
                    requestedStatus = CardStatus.REISSUANCE.value,
                    shippingAddress1 = slectaddressLine1.value,
                    shippingAddress2 = slectaddressLine2.value,
                    shippingCity = slectcity.value,
                    shippingCountry = "India",
                    shippingPinCode = slectpinCode.value,
                    shippingState = slectstate.value
                )
                handleFlow(
                    apiCall = {
                        changeStatusUseCase.invoke(request)
                    }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {
                        viewModelScope.launch {
                            if (it?.statusCode == 0) {

                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.SuccessSnackBar,
                                        UiText.DynamicString(it.statusDesc ?: "Success")
                                    )
                                )

                                NavigationEvent.helper.emit(
                                    NavigationEvent.NavigateToNextScreen(
                                        ProfileScreen.CardManagementScreen
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
        } else {
            viewModelScope.launch {
                ShowSnackBarEvent.helper.emit(
                    ShowSnackBarEvent.show(
                        SnackBarType.ErrorSnackBar,
                        UiText.DynamicString("Please select address")
                    )
                )
            }
        }


    }

    val slectaddressLine1 = mutableStateOf("")
    val slectaddressLine2 = mutableStateOf("")
    val slectpinCode = mutableStateOf("")
    val slectstate = mutableStateOf("")
    val slectcity = mutableStateOf("")

    fun selectedAddress(it: ShippingAddressItem) {
        slectaddressLine1.value = it.address1 ?: ""
        slectaddressLine2.value = it.address2 ?: ""
        slectpinCode.value = it.pinCode ?: ""
        slectstate.value = it.state ?: ""
        slectcity.value = it.city ?: ""
    }


    fun deleteAddress(it: ShippingAddressItem, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()

            val request = DeleteAddressRequest(
                deviceId = deviceId,
                id = it.id.toString(),
                latLong = latLong
            )
            handleFlow(
                apiCall = {
                    deleteAddressUseCase.invoke(request)
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    if (it?.statusCode == 0) {
                        viewModelScope.launch {
                            fetchAddress()
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.DynamicString(it.statusDesc.toString())
                                )
                            )
                            onSuccess()
                        }
                    } else {
                        viewModelScope.launch {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.ErrorSnackBar,
                                    UiText.DynamicString(it?.statusDesc.toString())
                                )
                            )
                        }
                    }
                }
            )
        }
    }

    fun moveToEditAddress(it: ShippingAddressItem) {
        Log.d("type", "moveToEditAddress:${it.type} ")

        viewModelScope.launch {

            editcity.value = it.city.toString()
            editstate.value = it.state.toString()
            editaddressLine1.value = it.address1.toString()
            editaddressLine2.value = it.address2.toString()
            editpinCode.value = it.pinCode.toString()
            editAddressID.value = it.id.toString()
            edithomeSelected.value = it.type == "Home"
            editworkSelected.value = it.type == "Work"
            editothersSelected.value = it.type == "Others"
            NavigationEvent.helper.navigateTo(CardManagement.EditAddressScreen)
        }
    }

    fun editAddress(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val request = EditAddressRequest(
                deviceId = deviceId,
                id = editAddressID.value,
                latLong = latLong,
                shippingAddress = EditAddressRequest.ShippingAddress(
                    address1 = editaddressLine1.value.toString(),
                    address2 = editaddressLine2.value.toString(),
                    addressType = if (edithomeSelected.value) "Home" else if (editworkSelected.value) "Work" else "Others",
                    city = editcity.value,
                    country = "INDIA",
                    pinCode = editpinCode.value,
                    state = editstate.value
                )
            )
            handleFlow(
                apiCall = {
                    editAddressUseCase.invoke(request)

                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    onSuccess()
                    editAddressID.value = ""
                }
            )
        }
    }

    fun fetchPinCodeData(pin: String, onSuccess: (FetchPinCodeDataResponse?) -> Unit) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val request = FetchPinCodeDataRequest(

                pin = pin.toInt()
            )
            handleFlow(
                apiCall = {
                    fetchPinCodeData.invoke(request)

                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    onSuccess(it)

                }
            )
        }
    }
}