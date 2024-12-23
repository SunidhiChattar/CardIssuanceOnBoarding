package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.ProfileDetailsFetchForBulkRequest
import com.isu.authentication.data.remote.dto.response.ProfileDetailsFetchForBulkResponse
import com.isu.authentication.domain.repo.AuthRepository
import com.isu.common.utils.NetworkResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileDetailsFetchForBulkUseCase @Inject constructor(val repository: AuthRepository){
    fun invoke(request:ProfileDetailsFetchForBulkRequest): Flow<NetworkResource<ProfileDetailsFetchForBulkResponse>> {
        return repository.profileDetailsFetchForBulk(request)
    }
}