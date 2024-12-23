package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.CustomerInitiateRequest
import com.isu.authentication.data.remote.dto.response.CustomerInitiateResponse
import com.isu.authentication.domain.repo.AuthRepository
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomerInitiateApiUseCase @Inject constructor(val repository: AuthRepository) {

    fun invoke(
        request: CustomerInitiateRequest,
        clientId:String,
        clientSecret:String,

        ): Flow<NetworkResource<CustomerInitiateResponse>> {
        return repository.customerInitiateAPI(request, clientSecret = clientSecret, clientId = clientId)
    }

}