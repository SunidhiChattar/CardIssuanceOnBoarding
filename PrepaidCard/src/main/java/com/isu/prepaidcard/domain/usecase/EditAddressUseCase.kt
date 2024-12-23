package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.EditAddressRequest
import com.isu.prepaidcard.data.response.EditAddressResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditAddressUseCase @Inject constructor(private val repository: PrepaidCardRepository) :
    ApiUseCase<EditAddressRequest, EditAddressResponse> {
    override fun invoke(request: EditAddressRequest): Flow<NetworkResource<EditAddressResponse>> {
        return repository.editAddress(request)
    }
}