package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.TransactionSettingsRequest
import com.isu.prepaidcard.data.response.OrderPhysicalCardResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetTransactionLimitApiUseCase @Inject constructor(private val repository: PrepaidCardRepository) :
    ApiUseCase<TransactionSettingsRequest, OrderPhysicalCardResponse> {
    override fun invoke(request: TransactionSettingsRequest): Flow<NetworkResource<OrderPhysicalCardResponse>> {
        return repository.setTransactionLimit(request)
    }
}