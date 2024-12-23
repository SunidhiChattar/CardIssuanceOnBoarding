package com.isu.prepaidcard.domain.usecase.viewcvv_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.ViewCardCvvRequest
import com.isu.prepaidcard.data.response.ViewCardCvvResponse
import kotlinx.coroutines.flow.Flow

interface ViewCardCvvUseCase {
    operator fun invoke(request: ViewCardCvvRequest):
            Flow<NetworkResource<ViewCardCvvResponse>>
}