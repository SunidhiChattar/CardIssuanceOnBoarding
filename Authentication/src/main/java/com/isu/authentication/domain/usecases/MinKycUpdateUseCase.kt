package com.isu.authentication.domain.usecases

import com.google.gson.Gson
import com.isu.authentication.data.remote.dto.request.MinKycUpdateRequest
import com.isu.authentication.data.remote.dto.response.MinKycUpdateResponse
import com.isu.common.customcomposables.AuthTokenData
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MinKycUpdateUseCase @Inject constructor(val repository: AuthRepository) :
    ApiUseCase<AuthTokenData<MinKycUpdateRequest>, MinKycUpdateResponse> {
    override fun invoke(request: AuthTokenData<MinKycUpdateRequest>): Flow<NetworkResource<MinKycUpdateResponse>> {
        val apiRequest = request.request
        val authToken = request.authorization
        val encryptedRequest =
            EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(apiRequest))
                ?: EncryptedData()
        return repository.minKycUpdate(encryptedRequest, authToken)
    }
}