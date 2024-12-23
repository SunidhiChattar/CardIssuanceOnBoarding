package com.isu.profile.domain.usecases

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.request.AuthTokenData
import com.isu.profile.data.remote.model.request.GenerateOTPChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.response.GenerateOTPChangePasswordUsingOldPasswordResponse
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

/**
 * @author-karthik
 * Use case for generating an OTP to change the password using the old password.
 *
 * @property profileRepository The repository for profile-related operations.
 * @constructor Creates an instance of [GenerateOTPToChangePasswordUsecase] with the given [profileRepository].
 */
class GenerateOTPToChangePasswordUsecase(
    private val profileRepository: ProfileRepository
) : ApiUseCase<AuthTokenData<GenerateOTPChangePasswordUsingOldPasswordRequest>, GenerateOTPChangePasswordUsingOldPasswordResponse> {

    /**
     * Executes the use case to generate an OTP for password change.
     *
     * @param request The request containing authorization token, token properties, and the OTP generation request details.
     * @return A [Flow] emitting a [NetworkResource] containing [GenerateOTPChangePasswordUsingOldPasswordResponse].
     */
    override fun invoke(
        request: AuthTokenData<GenerateOTPChangePasswordUsingOldPasswordRequest>
    ): Flow<NetworkResource<GenerateOTPChangePasswordUsingOldPasswordResponse>> {
        val tokenProperties = request.tokenProperties
        val authorization = request.authorization
        val otpRequest = request.request
        return profileRepository.generateOTPChangePasswordUsingOldPassword(
            tokenProperties = tokenProperties,
            authorization = authorization,
            request = otpRequest!!
        )
    }
}
