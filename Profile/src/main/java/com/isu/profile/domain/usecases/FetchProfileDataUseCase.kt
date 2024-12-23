package com.isu.profile.domain.usecases

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.request.AuthTokenData
import com.isu.profile.data.remote.model.response.FetchProfileData
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author-karthik
 * Use case for fetching profile data.
 *
 * @property profileRepository The repository for profile-related operations.
 * @constructor Creates an instance of [FetchProfileDataUseCase] with the given [profileRepository].
 */
class FetchProfileDataUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : ApiUseCase<AuthTokenData<Any>, FetchProfileData> {

    /**
     * Executes the use case to fetch profile data.
     *
     * @param request The request containing authorization token and token properties.
     * @return A [Flow] emitting a [NetworkResource] containing [FetchProfileData].
     */
    override fun invoke(
        request: AuthTokenData<Any>
    ): Flow<NetworkResource<FetchProfileData>> {
        val authorization = request.authorization
        val tokenProperty = request.tokenProperties
        return profileRepository.fetchProfileData(

        )
    }
}
