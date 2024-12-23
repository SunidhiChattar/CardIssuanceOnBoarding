package com.isu.prepaidcard.domain.usecase.changecardstatus_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.ChangeCardStatusRequest
import com.isu.prepaidcard.data.response.ChangeCardStatusResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangeCardStatusImpl @Inject constructor(
    private val prepaidCardRepository: PrepaidCardRepository
): ChangeCardStatusUseCase {
    override fun invoke(request: ChangeCardStatusRequest): Flow<NetworkResource<ChangeCardStatusResponse>> {
        return prepaidCardRepository.changeCardStatus(
            changeCardStatusRequest = request,
            token = "",
            tokenProperties = ""
        )
    }

}