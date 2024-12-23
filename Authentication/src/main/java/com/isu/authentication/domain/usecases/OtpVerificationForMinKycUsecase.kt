package com.isu.authentication.domain.usecases

import com.isu.common.customcomposables.AuthTokenData
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.common.utils.encryptdecrypt.EncryptedResponse
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OtpVerificationForMinKycUsecase @Inject constructor(val repository: AuthRepository) :
    ApiUseCase<AuthTokenData<EncryptedData>, EncryptedResponse> {
    override fun invoke(request: AuthTokenData<EncryptedData>): Flow<NetworkResource<EncryptedResponse>> {
        val apiRequest = request.request ?: EncryptedData()
        val authToken = request.authorization
        return repository.otpVerificationForMinKycUpdate(apiRequest, authToken)
    }
}