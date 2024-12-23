package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.TwoFARequestModel
import com.isu.authentication.data.remote.dto.response.TwoFAOtpResponse
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TwoFAUseCase @Inject constructor(val repository: AuthRepository) :
    ApiUseCase<TwoFARequestModel, TwoFAOtpResponse> {
    override fun invoke(request: TwoFARequestModel): Flow<NetworkResource<TwoFAOtpResponse>> {
        return repository.generateTwoFaOtp(request)
    }
}