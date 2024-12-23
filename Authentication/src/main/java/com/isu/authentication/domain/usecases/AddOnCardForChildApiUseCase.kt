package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.AddonCardForChildRequest
import com.isu.authentication.data.remote.dto.response.AddOnCardForChildResponse
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddOnCardForChildApiUseCase @Inject constructor(private val repository: AuthRepository) :
    ApiUseCase<AddonCardForChildRequest, AddOnCardForChildResponse> {
    override fun invoke(request: AddonCardForChildRequest): Flow<NetworkResource<AddOnCardForChildResponse>> {
        return repository.addOnCardForChild(request)
    }
}