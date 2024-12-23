package com.isu.prepaidcard.domain.usecase


import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.TwoFARequestModel
import com.isu.prepaidcard.data.response.TwoFAOtpResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TwoFAUseCase @Inject constructor(val repository: PrepaidCardRepository) :
    ApiUseCase<TwoFARequestModel, TwoFAOtpResponse> {
    override fun invoke(request: TwoFARequestModel): Flow<NetworkResource<TwoFAOtpResponse>> {
        return repository.generateTwoFaOtp(request)
    }
}