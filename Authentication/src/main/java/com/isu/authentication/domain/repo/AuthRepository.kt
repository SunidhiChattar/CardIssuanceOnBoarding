package  com.isu.authentication.domain.repo

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
import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.common.utils.encryptdecrypt.EncryptedResponse
import data.remote.dto.response.LoginResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author-karthik
 * Interface representing the repository for authentication-related operations.
 */
interface AuthRepository {


    /**

     * Authenticates a new user.
     *
     * @param request the login request model containing username and password
     * @return a [Flow] emitting [NetworkResource] containing the [LoginResponse]
     */
    fun authSignInForNewUser(request: EncryptedData): Flow<NetworkResource<SignInResponse>>





    fun generateDeviceBindingOTP(request: EncryptedData): Flow<NetworkResource<EncryptedResponse>>
    fun verifyDeviceBindingOTP(request: EncryptedData): Flow<NetworkResource<EncryptedResponse>>
    fun minKycUpdate(
        request: EncryptedData,
        authToken: String,
    ): Flow<NetworkResource<MinKycUpdateResponse>>

    fun otpVerificationForMinKycUpdate(
        request: EncryptedData,
        authToken: String,
    ): Flow<NetworkResource<EncryptedResponse>>


    fun fetchPinCodeData(
        request: FetchPinCodeDataRequest,
    ): Flow<NetworkResource<FetchPinCodeDataResponse>>

    fun verifySelfSignUp(
        request: VerifySignInRequest,

        ): Flow<NetworkResource<VerifySelfSignInResponse>>
    fun resendSelfSignUpOtp(
        request: ResendSignUpOtp,

        ): Flow<NetworkResource<ResenSignUpOtpResponse>>

    fun statusCheck(request: StatusCheckRequestModel): Flow<NetworkResource<StatusCheckResponse>>
    fun generateTwoFaOtp(request: TwoFARequestModel): Flow<NetworkResource<TwoFAOtpResponse>>
    fun verifyTwoFaOtp(request: VerifyTwoFAOTPRequest): Flow<NetworkResource<VerifyTwoFAOTPResponse>>
    fun authToken(
        request: AuthRequest,
    ): Flow<NetworkResource<AuthResponse>>


    fun fetchDeviceChangeOTP(
        request: DeviceChangeOTPRequest,
    ): Flow<NetworkResource<DeviceChangeOTPResponse>>


    fun verifyDeviceChangeOTP(
        request: VerifyDeviceChangeOTPRequest,
    ): Flow<NetworkResource<VerifyDeviceChangeOTPResponse>>

    fun addOnCardForChild(
        request: AddonCardForChildRequest,
    ): Flow<NetworkResource<AddOnCardForChildResponse>>

    fun bulkGenerateOTP(
        request: BulkCustomerGenerateOTP,
    ): Flow<NetworkResource<BulkCustomerGenerateOTPResponse>>

    fun bulkVerifyOTP(
        request: BulkVerifyOTPRequest,
    ): Flow<NetworkResource<BulkVerifyOTPResponse>>

    fun customerInitiateAPI(
        request: CustomerInitiateRequest,
        clientId:String, clientSecret:String
    ): Flow<NetworkResource<CustomerInitiateResponse>>

    fun profileDetailsFetchForBulk(
        request: ProfileDetailsFetchForBulkRequest
    ):Flow<NetworkResource<ProfileDetailsFetchForBulkResponse>>
}
