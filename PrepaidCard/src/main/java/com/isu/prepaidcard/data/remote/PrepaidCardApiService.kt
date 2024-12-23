package com.isu.prepaidcard.data.remote

import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.common.utils.encryptdecrypt.EncryptedResponse
import com.isu.prepaidcard.BuildConfig
import com.isu.prepaidcard.data.request.AddNewAddressRequest
import com.isu.prepaidcard.data.request.DeleteAddressRequest
import com.isu.prepaidcard.data.request.DetailedStatementRequest
import com.isu.prepaidcard.data.request.EditAddressRequest
import com.isu.prepaidcard.data.request.FetchAddressRequest
import com.isu.prepaidcard.data.request.FetchPinCodeDataRequest
import com.isu.prepaidcard.data.request.TokenProperty
import com.isu.prepaidcard.data.request.UpiIntentRequest
import com.isu.prepaidcard.data.response.AddAddressResponse
import com.isu.prepaidcard.data.response.AddOnCardResponse
import com.isu.prepaidcard.data.response.ChangeMccResponseBody
import com.isu.prepaidcard.data.response.DeleteAddressResponse
import com.isu.prepaidcard.data.response.DetailedStatementResponse
import com.isu.prepaidcard.data.response.EditAddressResponse
import com.isu.prepaidcard.data.response.FetchAddressResponse
import com.isu.prepaidcard.data.response.FetchPinCodeDataResponse
import com.isu.prepaidcard.data.response.KitToKitResponse
import com.isu.prepaidcard.data.response.LinkCardResponse
import com.isu.prepaidcard.data.response.MiniStatementResponse
import com.isu.prepaidcard.data.response.ModifyPinResponse
import com.isu.prepaidcard.data.response.OrderPhysicalCardResponse
import com.isu.prepaidcard.data.response.SetPrimaryResponse
import com.isu.prepaidcard.data.response.TwoFAOtpResponse
import com.isu.prepaidcard.data.response.UpiIntentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/*
 * This interface defines methods for making network calls related to prepaid card data.
 *
 * @see PrepaidCardRepository
 */
interface PrepaidCardApiService {

    @POST("https://prepaidcard-gateway.iserveu.online/card/view_card_data_by_cardrefnumber")
    suspend fun getCardDataByCardRefNo(
        @Body viewCardDataByRefid: EncryptedData,
    ): Response<EncryptedResponse>

    @POST(com.isu.common.BuildConfig.FETCH_PIN_CODE)
    suspend fun fetchPinCodeData(
        @Body request: FetchPinCodeDataRequest,
    ): Response<FetchPinCodeDataResponse>

    /**
     * Fetches card data by mobile number.
     *
     * @param token The authentication token.
     * @param tokenProperties The token properties.
     * @return A response containing the encrypted card data.
     */
    @POST("https://prepaidcard-gateway.iserveu.online/card/view_card_data_by_mobileno")
    suspend fun getCardDataByMobileNumber(
        @Body request: EncryptedData,
    ): Response<EncryptedResponse>

    /**
     * Fetches card balance.
     *
     * @param viewCardBalanceRequest The card balance request object.
     * @param token The authentication token.
     * @param tokenProperties The token properties.
     * @return A response containing the encrypted card balance.
     */
    @POST("https://prepaidcard-gateway.iserveu.online/card/view_card_balance")
    suspend fun getViewCardBalance(
        @Body viewCardBalanceRequest: EncryptedData,

    ): Response<EncryptedResponse>

    /**
     * Loads a card based on the provided request.
     *
     * @param loadCardRequest The encrypted request data containing card information.
     * @param token The authentication token for the request.
     * @param tokenProperties The properties associated with the token.
     * @return A response containing the encrypted response data.
     *
     * @throws IOException If there's an error communicating with the server.
     * @throws HttpException If the server returns an error response.
     */
    @POST(BuildConfig.LOAD_CARD)
    suspend fun loadCardApi(
        @Body loadCardRequest: EncryptedData,

    ): Response<EncryptedResponse>

    @POST(BuildConfig.ORDER_PHYSICAL_CARD)
    suspend fun orderPhysicalCard(
        @Body orderPhysicalRequest: EncryptedData,
        @Header("Authorization") token: String = "",
        @Header("token_properties") tokenProperties: TokenProperty
    ): Response<EncryptedResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/card/change_card_status")
    suspend fun changeCardStatus(
        @Body changeCardStatus: EncryptedData,

    ): Response<EncryptedResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/cms/view_cvv_by_card_refid")
    suspend fun viewCvv(
        @Body viewCardCvvRequest: EncryptedData,
    ): Response<EncryptedResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/card/link_card")
    suspend fun linkCard(
        @Body request: EncryptedData,
    ): Response<LinkCardResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/card/generate_otp")
    suspend fun twoFAOtp(
        @Body request: EncryptedData
    ): Response<TwoFAOtpResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/user/shipping_address_fetch")
    suspend fun fetchAddress(@Body request: FetchAddressRequest): Response<FetchAddressResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/user/shipping_address_update")
    suspend fun addAddress(
        @Body request: AddNewAddressRequest,
    ): Response<AddAddressResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/user/shipping_address_update")
    suspend fun editAddress(
        @Body request: EditAddressRequest,
    ): Response<EditAddressResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/user/shipping_address_update")
    suspend fun deleteAddress(
        @Body request: DeleteAddressRequest,
    ): Response<DeleteAddressResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/cms/updateprimary_card")
    suspend fun setPrimary(
        @Body request: EncryptedData,
    ): Response<SetPrimaryResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/card/kit_to_kit_balance_transfer")
    suspend fun kitToKitTransfer(
        @Body request: EncryptedData,
    ): Response<KitToKitResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/card/order_physical_card")
    suspend fun orderPhysicalCard(
        @Body request: EncryptedData,
    ): Response<OrderPhysicalCardResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/card/fetch_card_order_history")
    suspend fun fetchOrderPhysicalCardHistory(
        @Body request: EncryptedData,
    ): Response<EncryptedResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/cms/transaction_setting")
    suspend fun setTransactionLimit(
        @Body request: EncryptedData,
    ): Response<OrderPhysicalCardResponse>

    @POST("https://apidev.iserveu.online/dev/isu/prepaid/upi/initiate-dynamic-transaction")
    suspend fun getUpiIntentData(
        @Body viewCardCvvRequest: UpiIntentRequest,
        @Header("client_id") clientID: String = "ca2e01b799ad65414814d0277c2b3e328c162efe68a32319a07e928b73e3eba70006aab72cd4193da4321985d530cc1a",
        @Header("client_secret") clientSecret: String = "d764082b6626cbd3a71a536205d3bb021c0622169aa9eff480abeb7eb932598bc2f1107c72640749f37fe50d5dcad68d9f0ad4fd6e976b7711306e98214f593b",
    ): Response<UpiIntentResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/cms/change_mcc_status")
    suspend fun changeMccStatus(
        @Body changeMccRequestBody: EncryptedData
    ): Response<ChangeMccResponseBody>

    @POST("https://prepaidcard-gateway.iserveu.online/cms/reset_pin")
    suspend fun modifyPin(
        @Body modifyPinRequestBody: EncryptedData
    ): Response<ModifyPinResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/card/add_on_card")
    suspend fun addOnCard(
        @Body addOnCardRequest: EncryptedData
    ): Response<AddOnCardResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/cms/mini_statement")
    suspend fun miniStatement(
        @Body miniStatementRequest: EncryptedData
    ): Response<EncryptedResponse>

    @POST(BuildConfig.DETAILED_STATEMENT)
    suspend fun detailedStatement(
        @Body detailedStatementRequest: DetailedStatementRequest
    ): Response<DetailedStatementResponse>
}