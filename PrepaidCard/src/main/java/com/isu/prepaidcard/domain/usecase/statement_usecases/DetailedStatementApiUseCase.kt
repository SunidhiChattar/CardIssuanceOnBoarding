package com.isu.prepaidcard.domain.usecase.statement_usecases

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.DetailedStatementRequest
import com.isu.prepaidcard.data.response.DetailedStatementResponse
import kotlinx.coroutines.flow.Flow

interface DetailedStatementApiUseCase {
    operator fun invoke(request: DetailedStatementRequest): Flow<NetworkResource<DetailedStatementResponse>>
}