package com.isu.prepaidcard.domain.usecase.mccstatus_apiusecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.AuthTokenData
import com.isu.prepaidcard.data.request.ChangeMccRequestBody
import com.isu.prepaidcard.data.request.LoadCardRequest
import com.isu.prepaidcard.data.response.ChangeMccResponseBody
import com.isu.prepaidcard.data.response.LoadCardResponse
import kotlinx.coroutines.flow.Flow

/**
 * Use case interface for loading a card.

 * @param request The request data containing authentication token and card information.
 * @return A Flow of NetworkResource representing the asynchronous network operation.
 *         - Success: NetworkResource holding the LoadCardResponse data.
 *         - Failure: NetworkResource with an appropriate error message.
 */
interface MccStatusApiUseCase {
    operator fun invoke(request: ChangeMccRequestBody): Flow<NetworkResource<ChangeMccResponseBody>>
}