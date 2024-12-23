package com.isu.prepaidcard.domain.usecase.mccstatus_apiusecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.AuthTokenData
import com.isu.prepaidcard.data.request.ChangeMccRequestBody
import com.isu.prepaidcard.data.request.LoadCardRequest
import com.isu.prepaidcard.data.response.ChangeMccResponseBody
import com.isu.prepaidcard.data.response.LoadCardResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of the LoadCardApiUseCase interface.

 * @param repository The repository responsible for making the network call.
 */
class MccStatusApiImpl @Inject constructor(
    private  val repository: PrepaidCardRepository
): MccStatusApiUseCase{
    override fun invoke(request: ChangeMccRequestBody): Flow<NetworkResource<ChangeMccResponseBody>> {
        return repository.mccStatus(
            request = request
        )
    }
}