package com.isu.prepaidcard.domain.usecase.changecardstatus_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.ChangeCardStatusRequest
import com.isu.prepaidcard.data.response.ChangeCardStatusResponse
import kotlinx.coroutines.flow.Flow

interface ChangeCardStatusUseCase{
    operator fun invoke(request: ChangeCardStatusRequest):
            Flow<NetworkResource<ChangeCardStatusResponse>>
}