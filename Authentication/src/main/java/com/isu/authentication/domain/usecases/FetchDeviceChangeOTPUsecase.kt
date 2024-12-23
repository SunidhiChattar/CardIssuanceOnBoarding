package com.isu.authentication.domain.usecases

import com.isu.authentication.data.remote.dto.request.DeviceChangeOTPRequest
import com.isu.authentication.data.remote.dto.response.DeviceChangeOTPResponse
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.authentication.domain.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchDeviceChangeOTPUsecase @Inject constructor(private val repository: AuthRepository) :
    ApiUseCase<DeviceChangeOTPRequest, DeviceChangeOTPResponse> {
    override fun invoke(request: DeviceChangeOTPRequest): Flow<NetworkResource<DeviceChangeOTPResponse>> {
        return repository.fetchDeviceChangeOTP(request)
    }
}