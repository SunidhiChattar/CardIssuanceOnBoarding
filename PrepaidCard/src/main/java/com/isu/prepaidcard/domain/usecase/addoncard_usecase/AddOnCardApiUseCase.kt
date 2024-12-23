package com.isu.prepaidcard.domain.usecase.addoncard_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.AddOnRequest
import com.isu.prepaidcard.data.request.ModifyPinRequestBody
import com.isu.prepaidcard.data.response.AddOnCardResponse
import com.isu.prepaidcard.data.response.ModifyPinResponse
import kotlinx.coroutines.flow.Flow

/**
 * Use case interface for loading a card.

 * @param request The request data containing authentication token and card information.
 * @return A Flow of NetworkResource representing the asynchronous network operation.
 *         - Success: NetworkResource holding the LoadCardResponse data.
 *         - Failure: NetworkResource with an appropriate error message.
 */
interface AddOnCardApiUseCase {
    operator fun invoke(request: AddOnRequest): Flow<NetworkResource<AddOnCardResponse>>
}