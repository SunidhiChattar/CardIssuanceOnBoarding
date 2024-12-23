package com.isu.authentication.domain.usecases.statuscheck_api_usecase

import com.isu.authentication.data.remote.dto.request.StatusCheckRequestModel
import com.isu.authentication.data.remote.dto.response.StatusCheckResponse
import com.isu.common.utils.NetworkResource
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StatusCheckImpl @Inject constructor(
    private val authRepoImpl: AuthRepository
)  {
     fun invoke(request: StatusCheckRequestModel): Flow<NetworkResource<StatusCheckResponse>> {
        return authRepoImpl.statusCheck(
            request = request,
        )
    }
}