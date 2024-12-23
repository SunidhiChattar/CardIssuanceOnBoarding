package com.isu.profile.domain.usecases

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.request.FetchPinCodeDataRequest
import com.isu.profile.data.remote.model.response.FetchPinCodeDataResponse
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchPinCodeUseCase @Inject constructor(val repository: ProfileRepository) :
    ApiUseCase<FetchPinCodeDataRequest, FetchPinCodeDataResponse> {
    override fun invoke(request: FetchPinCodeDataRequest): Flow<NetworkResource<FetchPinCodeDataResponse>> {
        return repository.fetchPinCodeData(request)
    }
}