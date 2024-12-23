package com.isu.prepaidcard.domain.usecase.carddata_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.ViewCardDataByMobileNumber
import com.isu.prepaidcard.data.response.ViewCardDataByMobileNumberResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * This class implements the `ViewCardDataByMobNoApiUseCase` interface, providing the actual implementation for fetching card data by mobile number.
 * Fetches card data by mobile number by delegating the call to the underlying `PrepaidCardRepository`.
 *
 * @param request The authentication token data.
 * @return A Flow representing the network operation result, containing either a success response, an error, or a loading state.
 */
class ViewCardDataByMobNoApiUseCaseImpl @Inject constructor(
    private val repository: PrepaidCardRepository
) : ViewCardDataByMobNoApiUseCase {
    override fun invoke(request: ViewCardDataByMobileNumber): Flow<NetworkResource<ViewCardDataByMobileNumberResponse>> {

        return repository.getCardDataByMobileNumber(request)
    }
}