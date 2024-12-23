package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.VerifySignInRequest
import com.isu.authentication.data.remote.dto.response.VerifySelfSignInResponse
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptedResponse
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifySelfSignInUseCase @Inject constructor(val repository: AuthRepository) {
    fun invoke(request: VerifySignInRequest): Flow<NetworkResource<VerifySelfSignInResponse>> {
        return repository.verifySelfSignUp(request)
    }
}