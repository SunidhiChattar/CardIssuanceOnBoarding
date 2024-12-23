package com.isu.prepaidcard.domain.usecase.carddata_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.ViewCardDataByMobileNumber
import com.isu.prepaidcard.data.response.ViewCardDataByMobileNumberResponse
import kotlinx.coroutines.flow.Flow

/**
 * This use case defines a method for fetching card data by mobile number.
 * Invokes the card data fetching operation by mobile number.
 *
 * @param request The authentication token data.
 * @return A Flow representing the network operation result, containing either a success response, an error, or a loading state.
 */
interface ViewCardDataByMobNoApiUseCase {
    operator fun invoke(request: ViewCardDataByMobileNumber): Flow<NetworkResource<ViewCardDataByMobileNumberResponse>>
}