package com.isu.prepaidcard.domain.usecase.addoncard_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.AddOnRequest
import com.isu.prepaidcard.data.request.ModifyPinRequestBody
import com.isu.prepaidcard.data.response.AddOnCardResponse
import com.isu.prepaidcard.data.response.ModifyPinResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of the LoadCardApiUseCase interface.

 * @param repository The repository responsible for making the network call.
 */
class AddOnCardApiImpl @Inject constructor(
    private  val repository: PrepaidCardRepository
): AddOnCardApiUseCase{
    override fun invoke(request: AddOnRequest): Flow<NetworkResource<AddOnCardResponse>> {
        return repository.addOnCard(
            request = request
        )
    }
}