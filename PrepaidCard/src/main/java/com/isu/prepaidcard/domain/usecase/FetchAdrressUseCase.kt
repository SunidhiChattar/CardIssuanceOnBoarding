package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.FetchAddressRequest
import com.isu.prepaidcard.data.response.FetchAddressResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchAdrressUseCase @Inject constructor(private val prepaidCardRepository: PrepaidCardRepository) :
    ApiUseCase<FetchAddressRequest, FetchAddressResponse> {
    override fun invoke(request: FetchAddressRequest): Flow<NetworkResource<FetchAddressResponse>> {
        return prepaidCardRepository.fetchAddress(request)
    }
}