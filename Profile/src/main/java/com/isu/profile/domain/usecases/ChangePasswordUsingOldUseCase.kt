package com.isu.profile.domain.usecases

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.request.AuthTokenData
import com.isu.profile.data.remote.model.request.ChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.response.ChangePasswordUsingOldPasswordResponse
import com.isu.profile.data.remote.model.response.GenerateOTPChangePasswordUsingOldPasswordResponse
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author-karthik
 * Use case for changing the password using the old password.
 *
 * @property profileRepository The repository for profile-related operations.
 * @constructor Creates an instance of [ChangePasswordUsingOldUseCase] with the given [profileRepository].
 */
class ChangePasswordUsingOldUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ApiUseCase<AuthTokenData<ChangePasswordUsingOldPasswordRequest>, ChangePasswordUsingOldPasswordResponse> {

    /**
     * Executes the use case to change the password using the old password.
     *
     * @param request The request containing authorization token, token properties, and password change details.
     * @return A [Flow] emitting a [NetworkResource] containing [GenerateOTPChangePasswordUsingOldPasswordResponse].
     */
    override fun invoke(
        request: AuthTokenData<ChangePasswordUsingOldPasswordRequest>,
    ): Flow<NetworkResource<ChangePasswordUsingOldPasswordResponse>> {
        return profileRepository.changePasswordUsingOldPassword(
            authorization = request.authorization,
            tokenProperties = request.tokenProperties,
            request = request.request!!
        )
    }
}
