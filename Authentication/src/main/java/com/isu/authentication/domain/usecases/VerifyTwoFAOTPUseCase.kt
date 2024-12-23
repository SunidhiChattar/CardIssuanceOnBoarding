package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.VerifyTwoFAOTPRequest
import com.isu.authentication.data.remote.dto.response.VerifyTwoFAOTPResponse
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyTwoFAOTPUseCase @Inject constructor(val repository: AuthRepository) :
    ApiUseCase<VerifyTwoFAOTPRequest, VerifyTwoFAOTPResponse> {
    override fun invoke(request: VerifyTwoFAOTPRequest): Flow<NetworkResource<VerifyTwoFAOTPResponse>> {
        return repository.verifyTwoFaOtp(request)
    }
}