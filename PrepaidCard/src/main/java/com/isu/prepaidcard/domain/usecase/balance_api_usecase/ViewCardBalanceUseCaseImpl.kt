package com.isu.prepaidcard.domain.usecase.balance_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.ViewCardCvvRequest
import com.isu.prepaidcard.data.response.ViewCardBalanceResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * This class implements the `ViewCardBalanceUseCase` interface, providing the actual implementation for fetching card balance.
 * Fetches card balance by delegating the call to the underlying `PrepaidCardRepository`.
 *
 * @param request The authentication token data containing the card balance request.
 * @return A Flow representing the network operation result, containing either a success response, an error, or a loading state.
 */
class ViewCardBalanceUseCaseImpl @Inject constructor(
    private val repository: PrepaidCardRepository
): ViewCardBalanceUseCase{
    override fun invoke(request: ViewCardCvvRequest): Flow<NetworkResource<ViewCardBalanceResponse>> {

        return repository.getCardBalance(viewCardBalanceRequest = request)
    }
}