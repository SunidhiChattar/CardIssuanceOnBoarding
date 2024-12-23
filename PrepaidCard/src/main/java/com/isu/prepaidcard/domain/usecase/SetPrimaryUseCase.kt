package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.SetPrimaryRequest
import com.isu.prepaidcard.data.response.SetPrimaryResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetPrimaryUseCase @Inject constructor(private val repository: PrepaidCardRepository) :
    ApiUseCase<SetPrimaryRequest, SetPrimaryResponse> {
    override fun invoke(request: SetPrimaryRequest): Flow<NetworkResource<SetPrimaryResponse>> {
        return repository.setPrimary(request)
    }

}