package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.AddNewAddressRequest
import com.isu.prepaidcard.data.response.AddAddressResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddAddressUseCase @Inject constructor(private val repository: PrepaidCardRepository) :
    ApiUseCase<AddNewAddressRequest, AddAddressResponse> {
    override fun invoke(request: AddNewAddressRequest): Flow<NetworkResource<AddAddressResponse>> {
        return repository.addAddress(request)
    }
}

