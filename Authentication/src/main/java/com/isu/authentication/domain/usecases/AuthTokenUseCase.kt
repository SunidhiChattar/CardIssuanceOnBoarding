package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.AuthRequest
import com.isu.authentication.data.remote.dto.response.AuthResponse
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthTokenUseCase @Inject constructor(val repository: AuthRepository) :
    ApiUseCase<AuthRequest, AuthResponse> {
    override fun invoke(request: AuthRequest): Flow<NetworkResource<AuthResponse>> {
        return repository.authToken(request)
    }
}