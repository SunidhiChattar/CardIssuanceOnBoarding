package com.isu.authentication.domain.usecases

import com.google.gson.Gson
import com.isu.authentication.data.remote.dto.request.BulkCustomerGenerateOTP
import com.isu.authentication.data.remote.dto.request.BulkVerifyOTPRequest
import com.isu.authentication.data.remote.dto.request.SignInRequest
import com.isu.authentication.data.remote.dto.response.BulkCustomerGenerateOTPResponse
import com.isu.authentication.data.remote.dto.response.BulkVerifyOTPResponse
import com.isu.authentication.data.remote.dto.response.SignInResponse
import com.isu.authentication.domain.repo.AuthRepository
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.common.utils.encryptdecrypt.EncryptedData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(val repository: AuthRepository) :
    ApiUseCase<SignInRequest, SignInResponse> {
    override fun invoke(request: SignInRequest): Flow<NetworkResource<SignInResponse>> {
        val encryptedRequest =
            EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
        return repository.authSignInForNewUser(encryptedRequest ?: EncryptedData())
    }
}

class BulkOtpGenerateUseCase @Inject constructor(val repository: AuthRepository) :
    ApiUseCase<BulkCustomerGenerateOTP, BulkCustomerGenerateOTPResponse> {
    override fun invoke(request: BulkCustomerGenerateOTP): Flow<NetworkResource<BulkCustomerGenerateOTPResponse>> {

        return repository.bulkGenerateOTP(request)
    }
}

class BulkVerifyOTPUseCase @Inject constructor(val repository: AuthRepository) :
    ApiUseCase<BulkVerifyOTPRequest, BulkVerifyOTPResponse> {
    override fun invoke(request: BulkVerifyOTPRequest): Flow<NetworkResource<BulkVerifyOTPResponse>> {
        val encryptedRequest =
            EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
        return repository.bulkVerifyOTP(request)
    }
}