package  data.remote.networkservice

import com.isu.authentication.data.remote.dto.request.AuthRequest
import com.isu.authentication.data.remote.dto.request.DeviceChangeOTPRequest
import com.isu.authentication.data.remote.dto.request.FetchPinCodeDataRequest
import com.isu.authentication.data.remote.dto.request.VerifyDeviceChangeOTPRequest
import com.isu.authentication.data.remote.dto.response.AddOnCardForChildResponse
import com.isu.authentication.data.remote.dto.response.AuthResponse
import com.isu.authentication.data.remote.dto.response.BulkCustomerGenerateOTPResponse
import com.isu.authentication.data.remote.dto.response.BulkVerifyOTPResponse
import com.isu.authentication.data.remote.dto.response.DeviceChangeOTPResponse
import com.isu.authentication.data.remote.dto.response.FetchPinCodeDataResponse
import com.isu.authentication.data.remote.dto.response.TwoFAOtpResponse
import com.isu.authentication.data.remote.dto.response.VerifyDeviceChangeOTPResponse
import com.isu.authentication.data.remote.dto.response.VerifyTwoFAOTPResponse
import com.isu.common.BuildConfig
import com.isu.common.models.EncryptedRequest
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.common.utils.encryptdecrypt.EncryptedResponse
import com.isu.common.utils.encryptdecrypt.EncryptedResponseCBC
import data.remote.dto.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * @author karthik
 * Interface defining the authentication API services.
 */
interface AuthApiService {


    /**
     * Authenticates a new user.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @param grant the grant type, default is "password"
     * @param token the authorization token, default is a predefined token
     * @return a [Response] containing the [LoginResponse]
     */

    @POST("https://prepaidcard-services.iserveu.online/user/self_signup")
    suspend fun signIn(
        @Body request: EncryptedData,
    ): Response<EncryptedResponse>

    @POST("https://ppcard-staging.txninfra.com/api-encr/ppcard/mw/user/self_signup")
    suspend fun customerInitiateAPI(
        @Body request: EncryptedRequest,
    /*    @Header("header_secrets") headerSecret: String,
        @Header("client_id") clientID: String="2OeAVRMa79SuVz68gfHhZxKiGmSY2fqCNIGxuf3PuQmxyn8C",*/
    ): Response<com.isu.common.models.EncryptedResponse>

    @POST("https://ppcard-staging.txninfra.com/api-encr/ppcard/mw/user/profile_details_fetch")
    suspend fun profileDetailsFetchForBulkOnBoardingCustomer(
        @Body request: EncryptedRequest,
    /*    @Header("header_secrets") headerSecret: String,
        @Header("client_id") clientID: String="2OeAVRMa79SuVz68gfHhZxKiGmSY2fqCNIGxuf3PuQmxyn8C",*/
    ): Response<com.isu.common.models.EncryptedResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/user/generate_otp_for_customer")
    suspend fun bulkSignInGenerateOTP(
        @Body request: EncryptedData,
    ): Response<BulkCustomerGenerateOTPResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/user/otp_verification")
    suspend fun bulkSignInVerifyOTP(
        @Body request: EncryptedData,
    ): Response<BulkVerifyOTPResponse>

    @POST("https://prepaidcard-services.iserveu.online/card/generate_otp")
    suspend fun generateDeviceBindingOTP(
        @Body request: EncryptedData,
    ): Response<EncryptedResponse>

    @POST("https://prepaidcard-services.iserveu.online/card/verify_otp")
    suspend fun verifyBindingOTP(
        @Body request: EncryptedData,
    ): Response<EncryptedResponse>


    @POST("https://prepaidcard-gateway.iserveu.online/user/generate_otp_for_customer")
    suspend fun minKycUpdate(
        @Body request: EncryptedData,
        @Header("Authorization") auth: String,
    ): Response<EncryptedResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/user/otp_verification")
    suspend fun otpVerificationForMinKycUpdate(
        @Body request: EncryptedData,
        @Header("Authorization") authToken: String,
    ): Response<EncryptedResponse>

    @POST("https://ppcard-staging.txninfra.com/api-encr/ppcard/mw/user/user_status_check")
    suspend fun statusCheck(
        @Body request: EncryptedRequest,
//        @Header("header_secrets") headerSecret: String,
//        @Header("client_id") clientID: String="2OeAVRMa79SuVz68gfHhZxKiGmSY2fqCNIGxuf3PuQmxyn8C",

        ): Response<EncryptedResponseCBC>

    @POST(BuildConfig.FETCH_PIN_CODE)
    suspend fun fetchPinCodeData(
        @Body request: FetchPinCodeDataRequest,
    ): Response<FetchPinCodeDataResponse>

    @POST("https://ppcard-staging.txninfra.com/api-encr/ppcard/mw/user/verify_self_signup")
    suspend fun verifySelfSignUp(
        @Body request: EncryptedRequest,
    ): Response<EncryptedResponseCBC>

    @POST("https://ppcard-staging.txninfra.com/api-encr/ppcard/mw/user/resend_otp_generation")
    suspend fun resendSelfSignUpOtp(
        @Body request: EncryptedRequest,
    ): Response<EncryptedResponseCBC>


    @POST("https://prepaidcard-services.iserveu.online/card/generate_otp")
    suspend fun twoFAOtp(
        @Body request: EncryptedData,
    ): Response<TwoFAOtpResponse>

    @POST("https://prepaidcard-services.iserveu.online/card/verify_otp")
    suspend fun verifyTwoFAOtp(
        @Body request: EncryptedData,
    ): Response<VerifyTwoFAOTPResponse>

    @POST("https://services.iserveu.online/prepaidcard/user-registration/user/login")
    suspend fun authToken(
        @Body request: AuthRequest,
        @Header("Authorization") auth: String = "Basic aXN1LWZpbm8tY2xpZW50OmlzdS1maW5vLXBhc3N3b3Jk",
    ): Response<AuthResponse>

    @POST("https://services.iserveu.online/prepaidcard/user-registration/utility/send-device-change-otp")
    suspend fun fetchDeviceChangeOTP(
        @Body request: DeviceChangeOTPRequest,
    ): Response<DeviceChangeOTPResponse>

    @POST("https://services.iserveu.online/prepaidcard/user-registration/utility/verify-otp-change-device-id")
    suspend fun verifyDeviceChangeOTP(
        @Body request: VerifyDeviceChangeOTPRequest,
    ): Response<VerifyDeviceChangeOTPResponse>

    @POST("https://prepaidcard-gateway.iserveu.online/card/add_on_card_for_child")
    suspend fun addOnCardForChild(
        @Body request: EncryptedData,
    ): Response<AddOnCardForChildResponse>


}
