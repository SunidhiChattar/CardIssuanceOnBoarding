package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.KitToKitRequest
import com.isu.prepaidcard.data.response.KitToKitResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KitToKitUseCase @Inject constructor(private val repository: PrepaidCardRepository) :
    ApiUseCase<KitToKitRequest, KitToKitResponse> {
    override fun invoke(request: KitToKitRequest): Flow<NetworkResource<KitToKitResponse>> {
        return repository.kitToKitTransfer(request)

    }
}