package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.FetchOrderCardHistoryRequest
import com.isu.prepaidcard.data.response.FetchOrderCardHistoryResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchOrderHistoryUseCase @Inject constructor(val repository: PrepaidCardRepository) :
    ApiUseCase<FetchOrderCardHistoryRequest, FetchOrderCardHistoryResponse> {
    override fun invoke(request: FetchOrderCardHistoryRequest): Flow<NetworkResource<FetchOrderCardHistoryResponse>> {
        return repository.fetchOrderPhysicalCardHistory(request)
    }
}