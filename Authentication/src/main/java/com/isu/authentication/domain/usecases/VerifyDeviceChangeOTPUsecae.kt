package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.VerifyDeviceChangeOTPRequest
import com.isu.authentication.data.remote.dto.response.VerifyDeviceChangeOTPResponse
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyDeviceChangeOTPUsecae @Inject constructor(private val repository: AuthRepository) :
    ApiUseCase<VerifyDeviceChangeOTPRequest, VerifyDeviceChangeOTPResponse> {
    override fun invoke(request: VerifyDeviceChangeOTPRequest): Flow<NetworkResource<VerifyDeviceChangeOTPResponse>> {
        return repository.verifyDeviceChangeOTP(request)
    }
}