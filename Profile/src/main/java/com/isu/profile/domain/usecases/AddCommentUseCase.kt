package com.isu.profile.domain.usecases

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.request.AddCommentsRequest
import com.isu.profile.data.remote.model.request.AuthTokenData
import com.isu.profile.data.remote.model.response.AddCommentsResponse
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author-karthik
 * Use case for adding comments to a ticket.
 *
 * @param profileRepository The repository for profile-related operations.
 */
class AddCommentUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : ApiUseCase<AuthTokenData<AddCommentsRequest>, AddCommentsResponse> {

    /**
     * Executes the use case to add comments to a ticket.
     *
     * @param request The request containing authorization token, token properties, and comment details.
     * @return A [Flow] emitting a [NetworkResource] containing [AddCommentsResponse].
     */
    override fun invoke(
        request: AuthTokenData<AddCommentsRequest>,
    ): Flow<NetworkResource<AddCommentsResponse>> {
        return profileRepository.addCommentsToTicket(
            authorization = request.authorization,
            tokenProperties = request.tokenProperties,
            request = request.request!!
        )
    }
}
