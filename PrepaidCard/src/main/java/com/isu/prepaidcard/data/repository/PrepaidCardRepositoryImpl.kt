package com.isu.prepaidcard.data.repository


import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.common.utils.handleFlowResponse
import com.isu.prepaidcard.data.mappers.demo_response_mapper.DemoResponseMapper
import com.isu.prepaidcard.data.remote.PrepaidCardApiService
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
import com.isu.prepaidcard.data.response.CardDataItem
import com.isu.prepaidcard.data.response.ChangeCardStatusResponse
import com.isu.prepaidcard.data.response.ChangeMccResponseBody
import com.isu.prepaidcard.data.response.Data
import com.isu.prepaidcard.data.response.DeleteAddressResponse
import com.isu.prepaidcard.data.response.DetailedStatementResponse
import com.isu.prepaidcard.data.response.EditAddressResponse
import com.isu.prepaidcard.data.response.FetchAddressResponse
import com.isu.prepaidcard.data.response.FetchOrderCardHistoryResponse
import com.isu.prepaidcard.data.response.FetchPinCodeDataResponse
import com.isu.prepaidcard.data.response.KitToKitResponse
import com.isu.prepaidcard.data.response.LinkCardResponse
import com.isu.prepaidcard.data.response.LoadCardData
import com.isu.prepaidcard.data.response.LoadCardResponse
import com.isu.prepaidcard.data.response.MiniStatementResponse
import com.isu.prepaidcard.data.response.ModifyPinResponse
import com.isu.prepaidcard.data.response.OrderPhysicalCardResponse
import com.isu.prepaidcard.data.response.SetPrimaryResponse
import com.isu.prepaidcard.data.response.StatementsItem
import com.isu.prepaidcard.data.response.TwoFAOtpResponse
import com.isu.prepaidcard.data.response.UpiIntentResponse
import com.isu.prepaidcard.data.response.ViewCardBalanceData
import com.isu.prepaidcard.data.response.ViewCardBalanceResponse
import com.isu.prepaidcard.data.response.ViewCardCvvResponse
import com.isu.prepaidcard.data.response.ViewCardDataByMobileNumberResponse
import com.isu.prepaidcard.data.response.ViewCardDataByRefIdResponse
import com.isu.prepaidcard.data.response.ViewCardDataItem
import com.isu.prepaidcard.data.response.getRandomColor
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type
import javax.inject.Inject

// Custom serializer for Long values
class LongSerializer : JsonSerializer<Long> {
    override fun serialize(src: Long, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        // Use JsonPrimitive to create a number element
        return JsonPrimitive(src)
    }
}
/**
 * This class implements the `PrepaidCardRepository` interface, providing the actual implementation for interacting with prepaid card data.
 *
 * @Inject indicates that this class is a Dagger dependency injection class.
 */
class PrepaidCardRepositoryImpl @Inject constructor(
    private val encryptDecrypt: EncryptDecrypt,
    private val prepaidCardApiService: PrepaidCardApiService,
    private val demoResponseMapper: DemoResponseMapper
) : PrepaidCardRepository {

    /**
     * Internal helper function to map a Flow of EncryptedData to a Flow of the desired response type.
     *
     * @param encryptedResponseModel The response containing encrypted data.
     * @param T The expected response type.
     * @return A Flow of the desired response type after decryption and deserialization.
     */
    private inline fun <reified T> mapFun(
        encryptedResponseModel: EncryptedData
    ): Flow<T?> = flow {

        val decryptResponseString =
            encryptDecrypt.aesGcmDecryptFromBase64(encrypted = encryptedResponseModel)
        val type = object : TypeToken<T>() {}.type
        val decryptedResponseModel =
            try{
                Gson()
                    .fromJson<T>(decryptResponseString, type)
            }catch (e:Exception){

                e.printStackTrace()
                Log.d("DECR", "mapFun:${e.message} ")
                null
            }
        Log.d("DECR", "mapFun:${decryptedResponseModel} ")
        emit(decryptedResponseModel)

    }

    /**
     * Fetches card data by card reference number.
     *
     * @return A Flow representing the network operation result, containing either a success response, an error, or a loading state.
     * The success response contains a `DemoResponseDomain` object.
     */
    override fun getCardDataByCardRefNo(
        viewCardDataByCardRefNumberRequest: ViewCardDataByCardRefNumberRequest,
        token: String,
        tokenProperties: String
    ): Flow<NetworkResource<ViewCardDataByRefIdResponse>> {
        return handleFlowResponse(
            call = {
                val encryptedRequest =
                    encryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = viewCardDataByCardRefNumberRequest.let {
                        val gson = GsonBuilder().disableHtmlEscaping().create()
                        gson.toJson(it)
                    })
                prepaidCardApiService.getCardDataByCardRefNo(
                    viewCardDataByRefid = encryptedRequest,

                )
            },
            mapFun = {viewCardData->
                var resp: ViewCardDataByRefIdResponse? = null
                if (viewCardData.statusCode == 0){
                    mapFun<ViewCardDataByRefIdResponse.Data>(viewCardData.data).collectLatest {

                        val data = it?.copy(
                            encryptedCard = decryptExtractedData(it.encryptedCard.toString()).replace(
                                "\"",
                                ""
                            ),
                            expiryDate = try {
                                decryptExtractedData(encrypt = it.expiryDate.toString()).replace(
                                    "\"",
                                    ""
                                )
                                    .slice(0..1) + "/" + decryptExtractedData(encrypt = it.expiryDate.toString()).replace(
                                    "\"",
                                    ""
                                ).slice(2..5)

                            } catch (e: Exception) {
                                decryptExtractedData(encrypt = it.expiryDate.toString()).replace(
                                    "\"",
                                    ""
                                )
                            },
                            nameOnCard = decryptExtractedData(it.nameOnCard.toString()).replace(
                                "\"",
                                ""
                            ),
                            cardColor = getRandomColor()

                        )

                        resp = ViewCardDataByRefIdResponse(
                            data = data,
                            status = viewCardData.status,
                            statusCode = viewCardData.statusCode,
                            statusDesc = viewCardData.statusDesc
                        )
                    }
                }else{
                    resp = ViewCardDataByRefIdResponse(
                        data = null,
                        status =  viewCardData.status,
                        statusDesc = viewCardData.statusDesc,
                        statusCode =  viewCardData.statusCode
                    )
                }
                resp
            }
        )
    }

    override fun fetchPinCodeData(request: FetchPinCodeDataRequest): Flow<NetworkResource<FetchPinCodeDataResponse>> {
        return handleFlowResponse(
            call = {
                prepaidCardApiService.fetchPinCodeData(request)
            }, mapFun = { it })
    }

    /**
     * Fetches card data by mobile number.
     *
     * @param token The authentication token.
     * @param tokenProperties The token properties.
     * @return A Flow representing the network operation result, containing either a success response, an error, or a loading state.
     * The success response contains a `ViewCardDataByMobileNumberResponse` object.
     */
    override fun getCardDataByMobileNumber(request: ViewCardDataByMobileNumber):
            Flow<NetworkResource<ViewCardDataByMobileNumberResponse>> {
        return handleFlowResponse(
            call = {
                val encrRequest =
                    EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
                prepaidCardApiService.getCardDataByMobileNumber(
                    encrRequest

                )
            },
            mapFun = { encryptedResp ->
                var res: ViewCardDataByMobileNumberResponse? = null
                if (encryptedResp.statusCode == 0) {
                    mapFun<ViewCardDataItem>(encryptedResp.data).collectLatest {
                        Log.d("MERA_RESPONSE", "getCardDataByMobileNumber:${it} ")
                        val cardData = it?.data?.map {

                                CardDataItem(
                                    cardRefId = it.cardRefId,
                                    productCategory = it.productCategory ?: "Not Data Found",
                                    encryptedCard = it.decrypted ?: "",
                                    decrypted = decryptExtractedData(it.decrypted.toString()).replace(
                                        "\"",
                                        ""
                                    ),
                                    expiryDate = try {
                                        decryptExtractedData(encrypt = it.expiryDate.toString()).replace(
                                            "\"",
                                            ""
                                        )
                                            .slice(0..1) + "/" + decryptExtractedData(encrypt = it.expiryDate.toString()).replace(
                                            "\"",
                                            ""
                                        ).slice(2..5)

                                    } catch (e: Exception) {
                                        decryptExtractedData(encrypt = it.expiryDate.toString()).replace(
                                            "\"",
                                            ""
                                        )
                                    },
                                    nameOnCard = decryptExtractedData(it.nameOnCard.toString()).replace(
                                        "\"",
                                        ""
                                    ),
                                    isActive = it.isActive ?: false,
                                    addOnStatus = it.addOnStatus,
                                    childCard = it.childCard ?: false,
                                    isPrimary = it.isPrimary,
                                    isReissued = it.isReissued,
                                    cardColor = getRandomColor()

                                )
                            }


                        res = ViewCardDataByMobileNumberResponse(
                            data = ViewCardDataItem(
                                status = it?.status,
                                statusDesc = it?.statusDesc,
                                data = cardData,
                                kycStatus = it?.kycStatus
                            ),
                            status = encryptedResp.status!!,
                            statusDesc = encryptedResp.statusDesc!!,
                            statusCode = encryptedResp.statusCode
                        )
                    }

                } else {
                    res = ViewCardDataByMobileNumberResponse(
                        data = null,
                        status = encryptedResp.status!!,
                        statusDesc = encryptedResp.statusDesc!!,
                        statusCode = encryptedResp.statusCode!!
                    )
                }
                res
            }
        )
    }


    /**
     * Fetches card balance.
     *
     * @param viewCardBalanceRequest The card balance request object.
     * @param token The authentication token.
     * @param tokenProperties The token properties.
     * @return A Flow representing the network operation result, containing either a success response, an error, or a loading state.
     * The success response contains a `ViewCardBalanceResponse` object.
     */
    override fun getCardBalance(
        viewCardBalanceRequest: ViewCardCvvRequest,

    ): Flow<NetworkResource<ViewCardBalanceResponse>> {
        val encryptDecry =
            encryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = viewCardBalanceRequest.let {
                val gson = GsonBuilder().disableHtmlEscaping().create()
                gson.toJson(it)
            })
        return handleFlowResponse(
            call = {
                prepaidCardApiService.getViewCardBalance(
                    viewCardBalanceRequest = encryptDecry,

                )
            },
            mapFun = {viewCardBalance ->
                var resp: ViewCardBalanceResponse? = null
                if (viewCardBalance.statusCode == 0) {
                    mapFun<ViewCardBalanceData>(viewCardBalance.data).collectLatest {
                        val viewCardBalanceData = (ViewCardBalanceData(
                            balance = it?.balance,
                            status = it?.status,
                            statusDesc = it?.statusDesc
                        ))
                        resp = ViewCardBalanceResponse(
                            data = viewCardBalanceData,
                            status = viewCardBalance.status!!,
                            statusDesc = viewCardBalance.statusDesc!!,
                            statusCode = viewCardBalance.statusCode
                        )
                    }
                } else {
                    resp = ViewCardBalanceResponse(
                        data = null,
                        status = viewCardBalance.status!!,
                        statusDesc = viewCardBalance.statusDesc!!,
                        statusCode = viewCardBalance.statusCode!!
                    )
                }
                resp
            }
        )
    }

    /**
     * Load card from the server using Retrofit.
     *
     * @param loadCardRequest The unencrypted request data containing card information.
     * @param token The authentication token for the request.
     * @param tokenProperty The token property as a string (consider using a dedicated class).
     * @return A Flow of NetworkResource representing the asynchronous network operation.
     *         - Success: NetworkResource holding the decrypted LoadCardResponse data.
     *         - Failure: NetworkResource with an appropriate error message.
     *
     * @throws Exception Any unexpected exception that might occur during the operation.
     */
    override fun loadCardApi(
        loadCardRequest: LoadCardRequest,

    ): Flow<NetworkResource<LoadCardResponse>> {

        val encryptDecryptRequest =
            encryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = loadCardRequest.let {
                val gson = GsonBuilder().disableHtmlEscaping().create()
                gson.toJson(it)
            })

        return handleFlowResponse(
            call = {
                prepaidCardApiService.loadCardApi(
                    loadCardRequest = encryptDecryptRequest,

                )
            },
            mapFun = {loadCardResp->
                var resp: LoadCardResponse? = null
                if (loadCardResp.statusCode == 0) {
                    mapFun<LoadCardData>(loadCardResp.data).collectLatest {
                        val loadCardData = LoadCardData(
                            status = it?.status,
                            statusDesc = it?.statusDesc
                        )
                        resp = LoadCardResponse(
                            statusDesc = loadCardResp.statusDesc,
                            status = loadCardResp.status,
                            statusCode = loadCardResp.statusCode,
                            loadCardData = loadCardData
                        )
                    }
                } else {
                    resp = LoadCardResponse(
                        statusDesc = loadCardResp.statusDesc,
                        status = loadCardResp.status,
                        statusCode = loadCardResp.statusCode,
                        loadCardData = null
                    )
                }
                resp
            })
    }



    override fun changeCardStatus(
        changeCardStatusRequest: ChangeCardStatusRequest,
        token: String,
        tokenProperties: String
    ): Flow<NetworkResource<ChangeCardStatusResponse>> {
        return handleFlowResponse(
            call = {
                val encryptRequest =
                    encryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = changeCardStatusRequest.let {
                        val gson = GsonBuilder().disableHtmlEscaping().create()
                        gson.toJson(it)

                    })
                prepaidCardApiService.changeCardStatus(
                    changeCardStatus = encryptRequest,

                    )
            },
            mapFun = {
                changeCardStatus->
                var resp = ChangeCardStatusResponse(
                        data = null,
                        status = changeCardStatus.status,
                        statusCode = changeCardStatus.statusCode,
                        statusDesc = changeCardStatus.statusDesc
                    )

                resp
            }
        )
    }

    override fun viewCardCvv(
        viewCardCvvRequest: ViewCardCvvRequest,

        ): Flow<NetworkResource<ViewCardCvvResponse>> {
        return handleFlowResponse(
            call = {
                val encryptRequest =
                    encryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = viewCardCvvRequest.let {
                        val gson = GsonBuilder().disableHtmlEscaping().create()
                        gson.toJson(it)
                    })

                prepaidCardApiService.viewCvv(
                    viewCardCvvRequest = encryptRequest,

                    )
            },
            mapFun = {
                    viewCvv ->
                var resp: ViewCardCvvResponse? = null
                if (viewCvv.statusCode == 0) {

                    mapFun<ViewCardCvvResponse.Data>(viewCvv.data).collectLatest {
                        val decryptedCvv = decryptExtractedData(it?.cvv.toString())
                        Log.d("ENCDECCVV", "viewCardCvv:$decryptedCvv ")

                        val decrypt = ViewCardCvvResponse.Data(decryptedCvv.replace("\"", ""))

                        resp = ViewCardCvvResponse(
                            data = decrypt,
                            status = viewCvv.status!!,
                            statusDesc = viewCvv.statusDesc!!,
                            statusCode = viewCvv.statusCode
                        )
                    }
                } else {
                    resp = ViewCardCvvResponse(
                        data = null,
                        status = viewCvv.status!!,
                        statusDesc = viewCvv.statusDesc!!,
                        statusCode = viewCvv.statusCode!!
                    )
                }
                resp
            }
        )
    }

    override fun linkCard(linkCardRequest: LinkCarrdRequest): Flow<NetworkResource<LinkCardResponse>> {
        return handleFlowResponse(
            call = {
                val request = EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(
                    data = Gson().toJson(linkCardRequest)
                )
                prepaidCardApiService.linkCard(request = request)
            }, mapFun = {
                it
            }

        )
    }

    override fun getUpiIntentData(
        upiIntentRequest: UpiIntentRequest,
        clientId: String,
        clientSecret: String,
    ): Flow<NetworkResource<UpiIntentResponse>> {
        return handleFlowResponse(
            call = {
                prepaidCardApiService.getUpiIntentData(upiIntentRequest)
            },
            mapFun = {
                it
            }
        )
    }
    override fun generateTwoFaOtp(request: TwoFARequestModel): Flow<NetworkResource<TwoFAOtpResponse>> {
        return handleFlowResponse(call = {
            val encrRequest =
                EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
            prepaidCardApiService.twoFAOtp(encrRequest)
        }, mapFun = {
            it
        })
    }

    override fun fetchAddress(request: FetchAddressRequest): Flow<NetworkResource<FetchAddressResponse>> {
        return handleFlowResponse(call = {
            prepaidCardApiService.fetchAddress(request)
        }, mapFun = {
            it
        })
    }

    override fun addAddress(request: AddNewAddressRequest): Flow<NetworkResource<AddAddressResponse>> {
        return handleFlowResponse(call = {
            prepaidCardApiService.addAddress(request)
        }, mapFun = {
            it
        })
    }

    override fun editAddress(request: EditAddressRequest): Flow<NetworkResource<EditAddressResponse>> {
        return handleFlowResponse(call = {
            prepaidCardApiService.editAddress(request)
        }, mapFun = {
            it
        })
    }

    override fun deleteAddress(request: DeleteAddressRequest): Flow<NetworkResource<DeleteAddressResponse>> {
        return handleFlowResponse(call = {
            prepaidCardApiService.deleteAddress(request)
        }, mapFun = {
            it
        })
    }

    override fun setPrimary(request: SetPrimaryRequest): Flow<NetworkResource<SetPrimaryResponse>> {
        return handleFlowResponse(call = {
            val requestJson = Gson().toJson(request)
            val request = EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = requestJson)
            prepaidCardApiService.setPrimary(request)
        }, mapFun = {
            it
        })
    }

    override fun kitToKitTransfer(request: KitToKitRequest): Flow<NetworkResource<KitToKitResponse>> {
        return handleFlowResponse(call = {
            val requestJson = Gson().toJson(request)
            val request = EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = requestJson)
            prepaidCardApiService.kitToKitTransfer(request)
        }, mapFun = {
            it
        })
    }

    override fun orderPhysicalCard(request: OrderPhysicalCardRequest): Flow<NetworkResource<OrderPhysicalCardResponse>> {
        return handleFlowResponse(call = {
            val requestJson = Gson().toJson(request)
            val request = EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = requestJson)
            prepaidCardApiService.orderPhysicalCard(request)
        }, mapFun = {
            it
        })
    }

    override fun fetchOrderPhysicalCardHistory(request: FetchOrderCardHistoryRequest): Flow<NetworkResource<FetchOrderCardHistoryResponse>> {
        return handleFlowResponse(call = {
            val requestJson = Gson().toJson(request)
            val request = EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = requestJson)
            prepaidCardApiService.fetchOrderPhysicalCardHistory(request)
        }, mapFun = { orderHistory ->
            var resp: FetchOrderCardHistoryResponse? = null
            if (orderHistory.statusCode == 0) {
                mapFun<List<FetchOrderCardHistoryResponse.Data>>(orderHistory.data).collectLatest {

                    resp = FetchOrderCardHistoryResponse(
                        data = it,
                        status = orderHistory.status!!,
                        statusDesc = orderHistory.statusDesc!!,
                        statusCode = orderHistory.statusCode
                    )
                }
            } else {
                resp = FetchOrderCardHistoryResponse(
                    data = null,
                    status = orderHistory.status!!,
                    statusDesc = orderHistory.statusDesc!!,
                    statusCode = orderHistory.statusCode!!
                )
            }
            resp
        })
    }


    override fun setTransactionLimit(request: TransactionSettingsRequest): Flow<NetworkResource<OrderPhysicalCardResponse>> {
        return handleFlowResponse(call = {
            val requestJson = Gson().toJson(request)
            val request = EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = requestJson)
            prepaidCardApiService.setTransactionLimit(request)
        }, mapFun = {
            it
        })
    }

    override fun mccStatus(request: ChangeMccRequestBody): Flow<NetworkResource<ChangeMccResponseBody>> {
        return handleFlowResponse(call = {
            val requestJson = Gson().toJson(request)
            val request = EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = requestJson)
            prepaidCardApiService.changeMccStatus(changeMccRequestBody = request)
        }, mapFun = {
            it
        })
    }

    override fun modifyPin(request: ModifyPinRequestBody): Flow<NetworkResource<ModifyPinResponse>> {
        return handleFlowResponse(call = {
            val requestJson = Gson().toJson(request)
            val requestBody = EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = requestJson)
            prepaidCardApiService.modifyPin(modifyPinRequestBody = requestBody)
        },
            mapFun = {
                it
            })
    }

    override fun addOnCard(request: AddOnRequest): Flow<NetworkResource<AddOnCardResponse>> {
        return handleFlowResponse(call = {
            val requestJson = Gson().toJson(request)
            val requestBody = EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = requestJson)
            prepaidCardApiService.addOnCard(addOnCardRequest = requestBody)
        },
            mapFun = {it}
        )
    }

    override fun miniStatement(request: MiniStatementRequest): Flow<NetworkResource<MiniStatementResponse>> {
        return handleFlowResponse(call = {
            val encrRequest =
                EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
            prepaidCardApiService.miniStatement(
                encrRequest
            )
        },
            mapFun = { miniStatement->
                var resp: MiniStatementResponse?= null
                if(miniStatement.statusCode == 0){
                    mapFun<Data>(miniStatement.data).collectLatest { resp = MiniStatementResponse(
                        data = it,
                        status = miniStatement.status,
                        statusDesc = miniStatement.statusDesc,
                        statusCode = miniStatement.statusCode
                    )
                    }
                }else{
                    resp = MiniStatementResponse(
                        data = null,
                        statusDesc = miniStatement.statusDesc,
                        statusCode = miniStatement.statusCode,
                        status = miniStatement.status
                    )
                }
                resp
            })
    }

    override fun detailedStatement(request: DetailedStatementRequest): Flow<NetworkResource<DetailedStatementResponse>> {
        return handleFlowResponse(call = {
            prepaidCardApiService.detailedStatement(detailedStatementRequest = request)
        },
            mapFun = {it})
    }

    /**
     * Internal helper function to decrypt data extracted from the encrypted response.
     *
     * @param encrypt The encrypted data string potentially containing separators.
     * @return The decrypted data as a String.
     */
    private fun decryptExtractedData(encrypt: String): String {
        val sepparatorList = encrypt.split("|")
        return try {
            return if (sepparatorList.size == 3) {
                val iv = sepparatorList[0]

                val message = sepparatorList[1]
                val auth = sepparatorList[2]
                Log.d("ENCDECEXCEPTION", "decryptExtractedData:${iv}\n${auth}\n${message} ")
                EncryptDecrypt.aesGcmDecryptFromBase64(
                    encrypted = EncryptedData(
                        encryptedMessage = message,
                        iv = iv,
                        authTag = auth
                    )
                ) ?: ""
            } else {
                ""
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("ENCDECEXCEPTION", "decryptExtractedData:${e.message} ")
            ""
        }


    }


}