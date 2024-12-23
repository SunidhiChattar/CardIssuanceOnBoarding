package com.isu.authentication.domain.usecases

import com.google.gson.Gson
import com.isu.authentication.data.remote.dto.request.DeviceBindigOTPRequest
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.common.utils.encryptdecrypt.EncryptedResponse
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerateDeviceBindingOTPUseCase @Inject constructor(private val authRepository: AuthRepository) :
    ApiUseCase<DeviceBindigOTPRequest, EncryptedResponse> {
    override fun invoke(request: DeviceBindigOTPRequest): Flow<NetworkResource<EncryptedResponse>> {
        val encryptedRequest =
            EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
        return authRepository.generateDeviceBindingOTP(encryptedRequest ?: EncryptedData())
    }
}