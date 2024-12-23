package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.DeleteAddressRequest
import com.isu.prepaidcard.data.response.DeleteAddressResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(private val repository: PrepaidCardRepository) :
    ApiUseCase<DeleteAddressRequest, DeleteAddressResponse> {
    override fun invoke(request: DeleteAddressRequest): Flow<NetworkResource<DeleteAddressResponse>> {
        return repository.deleteAddress(request)
    }
}