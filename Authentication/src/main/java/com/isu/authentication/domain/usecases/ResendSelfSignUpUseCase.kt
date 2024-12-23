package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.ResendSignUpOtp
import com.isu.authentication.data.remote.dto.response.ResenSignUpOtpResponse
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResendSelfSignUpUseCase @Inject constructor(val repository: AuthRepository) {
    fun invoke(request: ResendSignUpOtp): Flow<NetworkResource<ResenSignUpOtpResponse>> {
        return repository.resendSelfSignUpOtp(request)
    }
}