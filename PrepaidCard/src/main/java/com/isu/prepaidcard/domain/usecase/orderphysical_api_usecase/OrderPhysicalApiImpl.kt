package com.isu.prepaidcard.domain.usecase.orderphysical_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.OrderPhysicalCardRequest
import com.isu.prepaidcard.data.response.OrderPhysicalCardResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of the OrderPhysicalApiUseCase that delegates the order physical card operation to the PrepaidCardRepository.
 *
 * @param prepaidCardRepository The repository responsible for handling prepaid card operations.
 */
class OrderPhysicalApiImpl @Inject constructor(private val prepaidCardRepository: PrepaidCardRepository): OrderPhysicalApiUseCase {
    override fun invoke(request: OrderPhysicalCardRequest): Flow<NetworkResource<OrderPhysicalCardResponse>> {
        return prepaidCardRepository.orderPhysicalCard(request)
    }

}