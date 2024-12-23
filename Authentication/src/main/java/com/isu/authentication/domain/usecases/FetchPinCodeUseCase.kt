package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.FetchPinCodeDataRequest
import com.isu.authentication.data.remote.dto.response.FetchPinCodeDataResponse
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.authentication.domain.repo.AuthRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchPinCodeUseCase @Inject constructor(val repository: AuthRepository) :
    ApiUseCase<FetchPinCodeDataRequest, FetchPinCodeDataResponse> {
    override fun invoke(request: FetchPinCodeDataRequest): Flow<NetworkResource<FetchPinCodeDataResponse>> {
        return repository.fetchPinCodeData(request)
    }
}