package com.isu.authentication.domain.usecases

import com.google.gson.Gson
import com.isu.authentication.data.remote.dto.request.VerifyDeviceBindingOTP
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.common.utils.encryptdecrypt.EncryptedResponse
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyBindingOTPUseCase @Inject constructor(val repository: AuthRepository) :
    ApiUseCase<VerifyDeviceBindingOTP, EncryptedResponse> {
    override fun invoke(request: VerifyDeviceBindingOTP): Flow<NetworkResource<EncryptedResponse>> {
        val encrRequest =
            EncryptDecrypt.aesGcmEncryptToEncryptedDataClass(data = Gson().toJson(request))
        return repository.verifyDeviceBindingOTP(request = encrRequest ?: EncryptedData())
    }
}