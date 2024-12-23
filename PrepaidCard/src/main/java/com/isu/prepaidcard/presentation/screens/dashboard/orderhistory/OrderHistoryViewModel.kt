package com.isu.prepaidcard.presentation.screens.dashboard.orderhistory

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.customcomposables.ShippingAddressItem
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.utils.ZonedDateFormatter
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.data.request.FetchOrderCardHistoryRequest
import com.isu.prepaidcard.domain.usecase.FetchOrderHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val fetchOrderHistoryUseCase: FetchOrderHistoryUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val listOfOrderDetails = mutableListOf<OrderDetails>()
    var selectedOrderDetails = mutableStateOf<OrderDetails?>(null)
    fun fetchOrderHistory(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val request = FetchOrderCardHistoryRequest(
                deviceId = deviceId,
                latLong = latLong,

                )
            handleFlow(
                apiCall = {
                    fetchOrderHistoryUseCase.invoke(request)
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = {
                    listOfOrderDetails.clear()
                    listOfOrderDetails.addAll(it?.data?.map { details ->
                        OrderDetails(
                            cardReferenceNumber = details?.cardReferenceNumber ?: "",
                            orderID = details?.orderId ?: "",
                            userName = details?.productName ?: "",
                            cardType = details?.cardType ?: "",
                            orderAmmount = details?.amount ?: "",
                            orderDate = ZonedDateFormatter.format(
                                details?.orderDate ?: "",
                                pattern = "dd/MM/yyyy"
                            ) ?: "",
                            deliveryDate = ZonedDateFormatter.format(
                                details?.expectedDeliveryDate ?: "", pattern = "dd/MM/yyyy"
                            ) ?: "",
                            orderStatus = when (details?.orderStatus) {
                                "CONFIRMED" -> OrderStatus.CONFIRMED
                                "IN_TRANSIT" -> OrderStatus.IN_TRANSIT
                                "DELIVERED" -> OrderStatus.DELIVERED
                                else -> OrderStatus.CONFIRMED
                            },
                            orderAddress = ShippingAddressItem(
                                country = details?.shippingAddress?.country,
                                city = details?.shippingAddress?.city,
                                address2 = details?.shippingAddress?.address2,
                                address1 = details?.shippingAddress?.address1,
                                pinCode = details?.shippingAddress?.pincode.toString(),
                                state = details?.shippingAddress?.state
                            )
                        )
                    } ?: emptyList())
                    onSuccess()
                }
            )
        }
    }
}