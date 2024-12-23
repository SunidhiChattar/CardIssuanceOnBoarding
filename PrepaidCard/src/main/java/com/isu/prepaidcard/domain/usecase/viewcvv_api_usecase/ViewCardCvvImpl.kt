package com.isu.prepaidcard.domain.usecase.viewcvv_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.ViewCardCvvRequest
import com.isu.prepaidcard.data.response.ViewCardCvvResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViewCardCvvImpl @Inject constructor(
    private val prepaidCardRepository: PrepaidCardRepository
): ViewCardCvvUseCase {
    override fun invoke(request: ViewCardCvvRequest): Flow<NetworkResource<ViewCardCvvResponse>> {
        return prepaidCardRepository.viewCardCvv(viewCardCvvRequest = request)
    }
}