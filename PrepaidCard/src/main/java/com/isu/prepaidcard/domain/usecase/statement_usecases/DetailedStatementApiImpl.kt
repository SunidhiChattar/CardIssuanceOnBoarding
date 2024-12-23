package com.isu.prepaidcard.domain.usecase.statement_usecases

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.DetailedStatementRequest
import com.isu.prepaidcard.data.response.DetailedStatementResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailedStatementApiImpl @Inject constructor(
    private val repository: PrepaidCardRepository
): DetailedStatementApiUseCase {
    override fun invoke(request: DetailedStatementRequest): Flow<NetworkResource<DetailedStatementResponse>> {
        return repository.detailedStatement(request = request)
    }
}