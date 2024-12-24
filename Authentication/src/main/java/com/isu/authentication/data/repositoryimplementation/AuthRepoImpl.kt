package  com.isu.authentication.data.repositoryimplementation

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.isu.authentication.data.remote.dto.request.AddonCardForChildRequest
import com.isu.authentication.data.remote.dto.request.AuthRequest
import com.isu.authentication.data.remote.dto.request.BulkCustomerGenerateOTP
import com.isu.authentication.data.remote.dto.request.BulkVerifyOTPRequest
import com.isu.authentication.data.remote.dto.request.CustomerInitiateRequest
import com.isu.authentication.data.remote.dto.request.DeviceChangeOTPRequest
import com.isu.authentication.data.remote.dto.request.FetchPinCodeDataRequest
import com.isu.authentication.data.remote.dto.request.ProfileDetailsFetchForBulkRequest
import com.isu.authentication.data.remote.dto.request.ResendSignUpOtp
import com.isu.authentication.data.remote.dto.request.StatusCheckRequestModel
import com.isu.authentication.data.remote.dto.request.TwoFARequestModel
import com.isu.authentication.data.remote.dto.request.VerifyDeviceChangeOTPRequest
import com.isu.authentication.data.remote.dto.request.VerifySignInRequest
import com.isu.authentication.data.remote.dto.request.VerifyTwoFAOTPRequest
import com.isu.authentication.data.remote.dto.response.AddOnCardForChildResponse
import com.isu.authentication.data.remote.dto.response.AuthResponse
import com.isu.authentication.data.remote.dto.response.BulkCustomerGenerateOTPResponse
import com.isu.authentication.data.remote.dto.response.BulkVerifyOTPResponse
import com.isu.authentication.data.remote.dto.response.CustomerInitiateResponse
import com.isu.authentication.data.remote.dto.response.DeviceChangeOTPResponse
import com.isu.authentication.data.remote.dto.response.FetchPinCodeDataResponse
import com.isu.authentication.data.remote.dto.response.MinKycUpdateResponse
import com.isu.authentication.data.remote.dto.response.ProfileDetailsFetchForBulkResponse
import com.isu.authentication.data.remote.dto.response.ResenSignUpOtpResponse
import com.isu.authentication.data.remote.dto.response.SignInResponse
import com.isu.authentication.data.remote.dto.response.StatusCheckResponse
import com.isu.authentication.data.remote.dto.response.TwoFAOtpResponse
import com.isu.authentication.data.remote.dto.response.VerifyDeviceChangeOTPResponse
import com.isu.authentication.data.remote.dto.response.VerifySelfSignInResponse
import com.isu.authentication.data.remote.dto.response.VerifyTwoFAOTPResponse
import com.isu.authentication.domain.repo.AuthRepository
import com.isu.common.models.EncryptedRequest
import com.isu.common.models.HeaderSecretGenerationModel
import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.common.utils.encryptdecrypt.EncryptDecryptCBC
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.common.utils.encryptdecrypt.EncryptedResponse
import com.isu.common.utils.handleFlowResponse
import com.isu.common.utils.mapFun
import data.remote.networkservice.AuthApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

/**
 * @author-karthik
 * Implementation of the [AuthRepository] interface.
 *
 * @param apiService the [AuthApiService] to make network requests
 */
class AuthRepoImpl(
    private val apiService: AuthApiService
) : AuthRepository {


    /**
     * Decrypts the encrypted response and maps it to the specified type [T].
     *
     * @param encryptedResponseModel The encrypted response model.
     * @return A flow emitting the decrypted response model of type [T].
     */
    inline fun <reified T> mapFunCBC(
        encryptedResponseModel: String,
    ): Flow<T?> = flow {
        val decryptResponseString =
            EncryptDecryptCBC.decryptResponse(encryptedData = encryptedResponseModel)?.decodeToString()
        val type = object : TypeToken<T>() {}.type
        val decryptedResponseModel =
            try {
                Gson()
                    .fromJson<T>(decryptResponseString, type)
            } catch (e: Exception) {

                e.printStackTrace()
                null
            }
        Log.d("DECR", "mapFun:${decryptedResponseModel} ")
        emit(decryptedResponseModel)
    }


    override fun authSignInForNewUser(request: EncryptedData): Flow<NetworkResource<SignInResponse>> {
        return handleFlowResponse(call = {
            apiService.signIn(request)
        }, mapFun = {
            var decrytedData: SignInResponse.Data? = mapFun<SignInResponse.Data>(it.data).first()
            SignInResponse(
                data = decrytedData,
                status = it.status,
                statusDesc = it.statusDesc,
                statusCode = it.statusCode
            )


        })
    }


    override fun generateDeviceBindingOTP(request: EncryptedData): Flow<NetworkResource<EncryptedResponse>> {
        return handleFlowResponse(
            call = { apiService.generateDeviceBindingOTP(request) },
            mapFun = { it }
        )
    }

    override fun verifyDeviceBindingOTP(request: EncryptedData): Flow<NetworkResource<EncryptedResponse>> {
        return handleFlowResponse(
            call = { apiService.verifyBindingOTP(request) },
            mapFun = { it }
        )
    }


    override fun minKycUpdate(
        request: EncryptedData,
        authToken: String,
    ): Flow<NetworkResource<MinKycUpdateResponse>> {
        return handleFlowResponse(
            call = { apiService.minKycUpdate(request, authToken) },
            mapFun = {
                var decrytedData: MinKycUpdateResponse.Data? = null
                mapFun<MinKycUpdateResponse.Data>(it.data).collectLatest {
                    decrytedData = it
                }

                MinKycUpdateResponse(
                    status = it.status,
                    statusCode = it.statusCode,
                    statusDesc = it.statusDesc,
                    data = decrytedData
                )

            }
        )
    }

    override fun otpVerificationForMinKycUpdate(
        request: EncryptedData,
        authToken: String,
    ): Flow<NetworkResource<EncryptedResponse>> {
        return handleFlowResponse(
            call = { apiService.otpVerificationForMinKycUpdate(request, authToken) },
            mapFun = { it }
        )
    }

    override fun fetchPinCodeData(request: FetchPinCodeDataRequest): Flow<NetworkResource<FetchPinCodeDataResponse>> {
        return handleFlowResponse(
            call = {
                apiService.fetchPinCodeData(request)
            }, mapFun = { it })
    }

    override fun verifySelfSignUp(request: VerifySignInRequest): Flow<NetworkResource<VerifySelfSignInResponse>> {

        return handleFlowResponse(
            call = {
                val encryptedData =
                    EncryptDecryptCBC.encryptRequest( Gson().toJson(request).toByteArray())
                val encryptedRequest=EncryptedRequest(
                    encryptedData
                )

                    apiService.verifySelfSignUp(encryptedRequest)



            },
            mapFun = {
                    resp->
                val responseJson=Gson().toJson(resp)
                val responseObject= JSONObject(responseJson)
                val encrypted=responseObject.getString("ResponseData")
                Log.d("RESP", "statusCheck:${responseObject}${encrypted} ")
                var respToReturn: VerifySelfSignInResponse?=null
                mapFunCBC<VerifySelfSignInResponse>(encrypted).collectLatest {
                    respToReturn=it
                    Log.d("RESP", "statusCheck:${respToReturn} ")
                }
                Log.d("RESP", "statusCheck:${respToReturn} ")
                respToReturn
            })
    }

    override fun resendSelfSignUpOtp(request: ResendSignUpOtp): Flow<NetworkResource<ResenSignUpOtpResponse>> {
        return handleFlowResponse(
            call = {
                val encryptedData =
                    EncryptDecryptCBC.encryptRequest( Gson().toJson(request).toByteArray())
                val encryptedRequest=EncryptedRequest(
                    encryptedData
                )

                apiService.resendSelfSignUpOtp(encryptedRequest)



            },
            mapFun = {resp->
                val responseJson=Gson().toJson(resp)
                val responseObject= JSONObject(responseJson)
                val encrypted=responseObject.getString("ResponseData")
                Log.d("RESP", "statusCheck:${responseObject}${encrypted} ")
                var respToReturn: ResenSignUpOtpResponse?=null
                mapFunCBC<ResenSignUpOtpResponse>(encrypted).collectLatest {
                    respToReturn=it
                    Log.d("RESP", "statusCheck:${respToReturn} ")
                }
                Log.d("RESP", "statusCheck:${respToReturn} ")
                respToReturn
            }
        )
    }

    override fun statusCheck(
        request: StatusCheckRequestModel,

    ): Flow<NetworkResource<StatusCheckResponse>> {
        val requestJson=Gson().toJson(request)
        val encryptedData=EncryptDecryptCBC.encryptRequest(requestJson.toByteArray())
        val encryptedRequest=EncryptedRequest(encryptedData)
        val headerRequest=HeaderSecretGenerationModel(epoch = System.currentTimeMillis().toString())
        val headerRequestJson=Gson().toJson(headerRequest)
        val headerSecret=EncryptDecryptCBC.encryptRequest(headerRequestJson.toByteArray(), key = "W30gz/ZGS/BbKI5YLqSbk/SqiDhOenlOb3cpk1gRy2E=")
        return handleFlowResponse(
            call = {

                apiService.statusCheck(encryptedRequest)
            }, mapFun = {resp->
                val responseJson=Gson().toJson(resp)
                val responseObject= JSONObject(responseJson)
                val encrypted=responseObject.getString("ResponseData")
                Log.d("RESP", "statusCheck:${responseObject}${encrypted} ")
                var respToReturn: StatusCheckResponse?=null
                mapFunCBC<StatusCheckResponse>(encrypted).collectLatest {
                    respToReturn=it
                    Log.d("RESP", "statusCheck:${respToReturn} ")
                }
                Log.d("RESP", "statusCheck:${respToReturn} ")
                respToReturn
            }

        )
    }

    override fun generateTwoFaOtp(request: TwoFARequestModel): Flow<NetworkResource<TwoFAOtpResponse>> {
        return handleFlowResponse(call = {
            val encrRequest =
                EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
            apiService.twoFAOtp(encrRequest)
        }, mapFun = {
            it
        })
    }

    override fun verifyTwoFaOtp(request: VerifyTwoFAOTPRequest): Flow<NetworkResource<VerifyTwoFAOTPResponse>> {
        return handleFlowResponse(
            call = {
                val encrRequest =
                    EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
                apiService.verifyTwoFAOtp(encrRequest)
            }, mapFun = {
                it
            }

        )
    }

    override fun authToken(request: AuthRequest): Flow<NetworkResource<AuthResponse>> {
        return handleFlowResponse(
            call = {
                apiService.authToken(request = request)
            }, mapFun = {
                it
            }

        )
    }

    override fun fetchDeviceChangeOTP(request: DeviceChangeOTPRequest): Flow<NetworkResource<DeviceChangeOTPResponse>> {
        return handleFlowResponse(
            call = {
                apiService.fetchDeviceChangeOTP(request)
            }, mapFun = {
                it
            }

        )
    }

    override fun verifyDeviceChangeOTP(request: VerifyDeviceChangeOTPRequest): Flow<NetworkResource<VerifyDeviceChangeOTPResponse>> {
        return handleFlowResponse(
            call = {
                apiService.verifyDeviceChangeOTP(request)
            }, mapFun = {
                it
            }

        )
    }

    override fun addOnCardForChild(request: AddonCardForChildRequest): Flow<NetworkResource<AddOnCardForChildResponse>> {
        return handleFlowResponse(
            call = {
                val encrRequest =
                    EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
                apiService.addOnCardForChild(encrRequest)
            }, mapFun = {
                it
            }

        )
    }

    override fun bulkGenerateOTP(request: BulkCustomerGenerateOTP): Flow<NetworkResource<BulkCustomerGenerateOTPResponse>> {
        return handleFlowResponse(
            call = {
                val encrRequest =
                    EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
                apiService.bulkSignInGenerateOTP(encrRequest)
            }, mapFun = {
                it
            }

        )
    }

    override fun bulkVerifyOTP(request: BulkVerifyOTPRequest): Flow<NetworkResource<BulkVerifyOTPResponse>> {
        return handleFlowResponse(
            call = {
                val encrRequest =
                    EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
                apiService.bulkSignInVerifyOTP(encrRequest)
            }, mapFun = {
                it
            }

        )
    }

    override fun customerInitiateAPI(request: CustomerInitiateRequest, clientId:String, clientSecret:String): Flow<NetworkResource<CustomerInitiateResponse>> {
        val headerRequest=
            HeaderSecretGenerationModel(epoch = System.currentTimeMillis().toString(), clientSecret = clientSecret)
        val headerRequestJson=Gson().toJson(headerRequest)
        Log.d("HEADER", "customerInitiateAPI:${headerRequestJson} ")
        val headerSecret=EncryptDecryptCBC.encryptRequest(headerRequestJson.toByteArray(), key = "W30gz/ZGS/BbKI5YLqSbk/SqiDhOenlOb3cpk1gRy2E=")
        Log.d("HEADER", "customerInitiateAPI:${headerSecret} ")

        return handleFlowResponse(
            call = {
                val encryptedData =
                    EncryptDecryptCBC.encryptRequest( Gson().toJson(request).toByteArray())
                val encryptedRequest=EncryptedRequest(
                    encryptedData
                )
                if(headerSecret!=null){
                    apiService.customerInitiateAPI(encryptedRequest)
                }else{
                    apiService.customerInitiateAPI(encryptedRequest)
                }


            }, mapFun = {resp->
                val responseJson=Gson().toJson(resp)
                val responseObject= JSONObject(responseJson)
                val encrypted=responseObject.getString("ResponseData")
                Log.d("RESP", "statusCheck:${responseObject}${encrypted} ")
                var respToReturn: CustomerInitiateResponse?=null
                mapFunCBC<CustomerInitiateResponse>(encrypted).collectLatest {
                    respToReturn=it
                    Log.d("RESP", "statusCheck:${respToReturn} ")
                }
                Log.d("RESP", "statusCheck:${respToReturn} ")
                respToReturn
            }

        )
    }

    override fun profileDetailsFetchForBulk(request: ProfileDetailsFetchForBulkRequest): Flow<NetworkResource<ProfileDetailsFetchForBulkResponse>> {
        return handleFlowResponse(
            call = {
                val encryptedData =
                    EncryptDecryptCBC.encryptRequest( Gson().toJson(request).toByteArray())
                val encryptedRequest=EncryptedRequest(
                    encryptedData
                )

                    apiService.profileDetailsFetchForBulkOnBoardingCustomer(encryptedRequest)




            }, mapFun = {
                    resp->
                val responseJson=Gson().toJson(resp)
                val responseObject= JSONObject(responseJson)
                val encrypted=responseObject.getString("ResponseData")
                Log.d("RESP", "statusCheck:${responseObject}${encrypted} ")
                var respToReturn: ProfileDetailsFetchForBulkResponse?=null
                mapFunCBC<ProfileDetailsFetchForBulkResponse>(encrypted).collectLatest {
                    respToReturn=it
                    Log.d("RESP", "statusCheck:${respToReturn} ")
                }
                Log.d("RESP", "statusCheck:${respToReturn} ")
                respToReturn
            }

        )
    }

}
