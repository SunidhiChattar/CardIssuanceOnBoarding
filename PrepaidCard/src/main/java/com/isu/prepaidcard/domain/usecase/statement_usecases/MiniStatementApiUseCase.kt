package com.isu.prepaidcard.domain.usecase.statement_usecases

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.MiniStatementRequest
import com.isu.prepaidcard.data.response.MiniStatementResponse
import kotlinx.coroutines.flow.Flow

interface MiniStatementApiUseCase {
    operator fun invoke(request: MiniStatementRequest): Flow<NetworkResource<MiniStatementResponse>>
}