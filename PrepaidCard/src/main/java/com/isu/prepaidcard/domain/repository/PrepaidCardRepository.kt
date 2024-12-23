package com.isu.prepaidcard.domain.repository

import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.prepaidcard.data.request.AddNewAddressRequest
import com.isu.prepaidcard.data.request.AddOnRequest
import com.isu.prepaidcard.data.request.ChangeCardStatusRequest
import com.isu.prepaidcard.data.request.ChangeMccRequestBody
import com.isu.prepaidcard.data.request.DeleteAddressRequest
import com.isu.prepaidcard.data.request.DetailedStatementRequest
import com.isu.prepaidcard.data.request.EditAddressRequest
import com.isu.prepaidcard.data.request.FetchAddressRequest
import com.isu.prepaidcard.data.request.FetchOrderCardHistoryRequest
import com.isu.prepaidcard.data.request.FetchPinCodeDataRequest
import com.isu.prepaidcard.data.request.KitToKitRequest
import com.isu.prepaidcard.data.request.LinkCarrdRequest
import com.isu.prepaidcard.data.request.LoadCardRequest
import com.isu.prepaidcard.data.request.MiniStatementRequest
import com.isu.prepaidcard.data.request.ModifyPinRequestBody
import com.isu.prepaidcard.data.request.OrderPhysicalCardRequest
import com.isu.prepaidcard.data.request.SetPrimaryRequest
import com.isu.prepaidcard.data.request.TransactionSettingsRequest
import com.isu.prepaidcard.data.request.TwoFARequestModel
import com.isu.prepaidcard.data.request.UpiIntentRequest
import com.isu.prepaidcard.data.request.ViewCardCvvRequest
import com.isu.prepaidcard.data.request.ViewCardDataByCardRefNumberRequest
import com.isu.prepaidcard.data.request.ViewCardDataByMobileNumber
import com.isu.prepaidcard.data.response.AddAddressResponse
import com.isu.prepaidcard.data.response.AddOnCardResponse
import com.isu.prepaidcard.data.response.ChangeCardStatusResponse
import com.isu.prepaidcard.data.response.ChangeMccResponseBody
import com.isu.prepaidcard.data.response.DeleteAddressResponse
import com.isu.prepaidcard.data.response.DetailedStatementResponse
import com.isu.prepaidcard.data.response.EditAddressResponse
import com.isu.prepaidcard.data.response.FetchAddressResponse
import com.isu.prepaidcard.data.response.FetchOrderCardHistoryResponse
import com.isu.prepaidcard.data.response.FetchPinCodeDataResponse
import com.isu.prepaidcard.data.response.KitToKitResponse
import com.isu.prepaidcard.data.response.LinkCardResponse
import com.isu.prepaidcard.data.response.LoadCardResponse
import com.isu.prepaidcard.data.response.MiniStatementResponse
import com.isu.prepaidcard.data.response.ModifyPinResponse
import com.isu.prepaidcard.data.response.OrderPhysicalCardResponse
import com.isu.prepaidcard.data.response.SetPrimaryResponse
import com.isu.prepaidcard.data.response.TwoFAOtpResponse
import com.isu.prepaidcard.data.response.UpiIntentResponse
import com.isu.prepaidcard.data.response.ViewCardBalanceResponse
import com.isu.prepaidcard.data.response.ViewCardCvvResponse
import com.isu.prepaidcard.data.response.ViewCardDataByMobileNumberResponse
import com.isu.prepaidcard.data.response.ViewCardDataByRefIdResponse
import kotlinx.coroutines.flow.Flow

/**
 * This interface defines methods for interacting with the prepaid card data.
 */
interface PrepaidCardRepository {
    /**
     * Fetches card data by card reference number.
     *
     * @return A Flow representing the network operation result, containing either a success response, an error, or a loading state.
     */
    fun getCardDataByCardRefNo(viewCardDataByCardRefNumberRequest: ViewCardDataByCardRefNumberRequest, token: String, tokenProperties: String): Flow<NetworkResource<ViewCardDataByRefIdResponse>>

    /**
     * Fetches card data by mobile number.
     *
     * @return A Flow representing the network operation result, containing either a success response, an error, or a loading state.
     */
    fun getCardDataByMobileNumber(request: ViewCardDataByMobileNumber): Flow<NetworkResource<ViewCardDataByMobileNumberResponse>>

    /**
     * Fetches card balance.
     *
     * @param viewCardBalanceRequest The card balance request object.
     * @return A Flow representing the network operation result, containing either a success response, an error, or a loading state.
     */
    fun getCardBalance(viewCardBalanceRequest: ViewCardCvvRequest): Flow<NetworkResource<ViewCardBalanceResponse>>


    /**
     * Loads a card based on the provided request.

     * @param loadCardRequest The request data containing card information.
     * @param token The authentication token for the request.
     * @param tokenProperty The token property for the request.
     * @return A Flow of NetworkResource representing the asynchronous network operation.
     *         - Success: NetworkResource holding the LoadCardResponse data.
     *         - Failure: NetworkResource with an appropriate error message.
     */
    fun loadCardApi(loadCardRequest: LoadCardRequest): Flow<NetworkResource<LoadCardResponse>>



    fun changeCardStatus(changeCardStatusRequest: ChangeCardStatusRequest, token: String, tokenProperties: String): Flow<NetworkResource<ChangeCardStatusResponse>>

    fun viewCardCvv(viewCardCvvRequest: ViewCardCvvRequest): Flow<NetworkResource<ViewCardCvvResponse>>
    fun linkCard(linkCardRequest: LinkCarrdRequest): Flow<NetworkResource<LinkCardResponse>>
    fun getUpiIntentData(
        upiIntentRequest: UpiIntentRequest,
        clientId: String = "ca2e01b799ad65414814d0277c2b3e328c162efe68a32319a07e928b73e3eba70006aab72cd4193da4321985d530cc1a",
        clientSecret: String = "d764082b6626cbd3a71a536205d3bb021c0622169aa9eff480abeb7eb932598bc2f1107c72640749f37fe50d5dcad68d9f0ad4fd6e976b7711306e98214f593b",
    ): Flow<NetworkResource<UpiIntentResponse>>

    fun generateTwoFaOtp(request: TwoFARequestModel): Flow<NetworkResource<TwoFAOtpResponse>>

    fun fetchAddress(request: FetchAddressRequest): Flow<NetworkResource<FetchAddressResponse>>
    fun addAddress(request: AddNewAddressRequest): Flow<NetworkResource<AddAddressResponse>>
    fun editAddress(request: EditAddressRequest): Flow<NetworkResource<EditAddressResponse>>
    fun deleteAddress(request: DeleteAddressRequest): Flow<NetworkResource<DeleteAddressResponse>>
    fun setPrimary(request: SetPrimaryRequest): Flow<NetworkResource<SetPrimaryResponse>>
    fun kitToKitTransfer(request: KitToKitRequest): Flow<NetworkResource<KitToKitResponse>>
    fun orderPhysicalCard(request: OrderPhysicalCardRequest): Flow<NetworkResource<OrderPhysicalCardResponse>>

    fun fetchOrderPhysicalCardHistory(request: FetchOrderCardHistoryRequest): Flow<NetworkResource<FetchOrderCardHistoryResponse>>
    fun setTransactionLimit(request: TransactionSettingsRequest): Flow<NetworkResource<OrderPhysicalCardResponse>>
    fun mccStatus(request: ChangeMccRequestBody): Flow<NetworkResource<ChangeMccResponseBody>>
    fun modifyPin(request: ModifyPinRequestBody): Flow<NetworkResource<ModifyPinResponse>>
    fun addOnCard(request: AddOnRequest): Flow<NetworkResource<AddOnCardResponse>>
    fun miniStatement(request:MiniStatementRequest): Flow<NetworkResource<MiniStatementResponse>>
    fun detailedStatement(request:DetailedStatementRequest): Flow<NetworkResource<DetailedStatementResponse>>
    fun fetchPinCodeData(
        request: FetchPinCodeDataRequest,
    ): Flow<NetworkResource<FetchPinCodeDataResponse>>
}