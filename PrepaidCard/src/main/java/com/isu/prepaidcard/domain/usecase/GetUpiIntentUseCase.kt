package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.UpiIntentRequest
import com.isu.prepaidcard.data.response.UpiIntentResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUpiIntentUseCase @Inject constructor(val repository: PrepaidCardRepository) :
    ApiUseCase<UpiIntentRequest, UpiIntentResponse> {
    override fun invoke(request: UpiIntentRequest): Flow<NetworkResource<UpiIntentResponse>> {
        return repository.getUpiIntentData(request)
    }

}