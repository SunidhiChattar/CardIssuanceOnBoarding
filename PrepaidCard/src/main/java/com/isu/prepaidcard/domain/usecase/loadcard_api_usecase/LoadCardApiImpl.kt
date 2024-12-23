package com.isu.prepaidcard.domain.usecase.loadcard_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.AuthTokenData
import com.isu.prepaidcard.data.request.LoadCardRequest
import com.isu.prepaidcard.data.response.LoadCardResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of the LoadCardApiUseCase interface.

 * @param repository The repository responsible for making the network call.
 */
class LoadCardApiImpl @Inject constructor(
    private  val repository: PrepaidCardRepository
): LoadCardApiUseCase{
    override fun invoke(request: AuthTokenData<LoadCardRequest>): Flow<NetworkResource<LoadCardResponse>> {
        return repository.loadCardApi(
            loadCardRequest = request.request!!,

            )
    }
}