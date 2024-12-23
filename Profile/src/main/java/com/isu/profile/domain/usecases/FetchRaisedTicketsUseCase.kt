package com.isu.profile.domain.usecases

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.response.FetchRaisedTicketsResponse
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author-karthik
 * Use case for fetching raised tickets.
 *
 * @property profileRepository The repository for profile-related operations.
 * @constructor Creates an instance of [FetchRaisedTicketsUseCase] with the given [profileRepository].
 */
class FetchRaisedTicketsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : ApiUseCase<com.isu.profile.data.remote.model.request.AuthTokenData<Any>, FetchRaisedTicketsResponse> {

    /**
     * Executes the use case to fetch raised tickets.
     *
     * @param request The request containing authorization token and token properties.
     * @return A [Flow] emitting a [NetworkResource] containing [FetchRaisedTicketsResponse].
     */
    override fun invoke(
        request: com.isu.profile.data.remote.model.request.AuthTokenData<Any>,
    ): Flow<NetworkResource<FetchRaisedTicketsResponse>> {
        return profileRepository.fetchRaisedTickets(

        )
    }
}
