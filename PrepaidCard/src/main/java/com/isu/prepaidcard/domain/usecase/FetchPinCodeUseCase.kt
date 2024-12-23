package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.FetchPinCodeDataRequest
import com.isu.prepaidcard.data.response.FetchPinCodeDataResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchPinCodeUseCase @Inject constructor(val repository: PrepaidCardRepository) :
    ApiUseCase<FetchPinCodeDataRequest, FetchPinCodeDataResponse> {
    override fun invoke(request: FetchPinCodeDataRequest): Flow<NetworkResource<FetchPinCodeDataResponse>> {
        return repository.fetchPinCodeData(request)
    }
}