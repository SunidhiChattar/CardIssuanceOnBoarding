package com.isu.prepaidcard.domain.usecase.modifypin_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.ModifyPinRequestBody
import com.isu.prepaidcard.data.response.ModifyPinResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of the LoadCardApiUseCase interface.

 * @param repository The repository responsible for making the network call.
 */
class ModifyPinApiImpl @Inject constructor(
    private  val repository: PrepaidCardRepository
): ModifyPinApiUseCase{
    override fun invoke(request: ModifyPinRequestBody): Flow<NetworkResource<ModifyPinResponse>> {
        return repository.modifyPin(
            request = request
        )
    }
}