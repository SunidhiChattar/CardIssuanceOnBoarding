package com.isu.prepaidcard.domain.usecase.statement_usecases

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.MiniStatementRequest
import com.isu.prepaidcard.data.response.MiniStatementResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MiniStatementApiImpl @Inject constructor(
    private val repository: PrepaidCardRepository
): MiniStatementApiUseCase {
    override fun invoke(request: MiniStatementRequest): Flow<NetworkResource<MiniStatementResponse>> {
        return repository.miniStatement(request = request)
    }
}