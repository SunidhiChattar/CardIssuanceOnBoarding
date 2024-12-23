package com.isu.prepaidcard.domain.usecase.balance_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.ViewCardCvvRequest
import com.isu.prepaidcard.data.response.ViewCardBalanceResponse
import kotlinx.coroutines.flow.Flow

/**
 * This use case defines a method for fetching card balance.
 * Invokes the card balance fetching operation.
 *
 * @param request The authentication token data containing the card balance request.
 * @return A Flow representing the network operation result, containing either a success response, an error, or a loading state.
 */
interface ViewCardBalanceUseCase {
    operator fun invoke(request: ViewCardCvvRequest): Flow<NetworkResource<ViewCardBalanceResponse>>
}