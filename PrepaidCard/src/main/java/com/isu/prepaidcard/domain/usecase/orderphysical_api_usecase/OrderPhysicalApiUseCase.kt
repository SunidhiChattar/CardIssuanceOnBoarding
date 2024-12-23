package com.isu.prepaidcard.domain.usecase.orderphysical_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.OrderPhysicalCardRequest
import com.isu.prepaidcard.data.response.OrderPhysicalCardResponse
import kotlinx.coroutines.flow.Flow

/**
 * This use case handles the process of placing a physical order.
 *
 * @param request The authentication token data containing the order physical request.
 * @return A Flow representing the network resource of the order physical response.
 *         The response will contain information about the order placement status,
 *         any error messages, and the order details if successful.
 */
interface OrderPhysicalApiUseCase {
    operator fun invoke(request: OrderPhysicalCardRequest): Flow<NetworkResource<OrderPhysicalCardResponse>>
}