package com.isu.authentication.domain.usecases.statuscheck_api_usecase

import com.isu.authentication.data.remote.dto.request.StatusCheckRequestModel
import com.isu.authentication.data.remote.dto.response.StatusCheckResponse
import com.isu.common.utils.NetworkResource
import kotlinx.coroutines.flow.Flow


interface StatusCheckUseCase {
    operator fun invoke(
        request: StatusCheckRequestModel,
    ): Flow<NetworkResource<StatusCheckResponse>>
}