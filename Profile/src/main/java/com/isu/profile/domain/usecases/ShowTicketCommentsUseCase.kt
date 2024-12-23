package com.isu.profile.domain.usecases

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.request.AuthTokenData
import com.isu.profile.data.remote.model.request.ShowTicketCommentRequest
import com.isu.profile.data.remote.model.response.ShowTicketCommentResponse
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author-karthik
 * Use case for retrieving comments on a specific ticket.
 *
 * @property profileRepository The repository for profile-related operations.
 * @constructor Creates an instance of [ShowTicketCommentsUseCase] with the given [profileRepository].
 */
class ShowTicketCommentsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : ApiUseCase<AuthTokenData<ShowTicketCommentRequest>, ShowTicketCommentResponse> {

    /**
     * Executes the use case to show comments for a given ticket.
     *
     * @param request The request containing authorization token, token properties, and the ticket details.
     * @return A [Flow] emitting a [NetworkResource] containing [ShowTicketCommentResponse].
     */
    override fun invoke(
        request: AuthTokenData<ShowTicketCommentRequest>
    ): Flow<NetworkResource<ShowTicketCommentResponse>> {
        val authorization = request.authorization
        val apiRequest = request.request
        val tokenProperty = request.tokenProperties

        return profileRepository.showTicketComments(

            request = apiRequest!!
        )
    }
}
