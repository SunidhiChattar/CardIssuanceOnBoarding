package com.isu.profile.domain.usecases

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.request.AuthTokenData
import com.isu.profile.data.remote.model.request.RaiseAtTicketRequest
import com.isu.profile.data.remote.model.response.RaiseATicketResponse
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author-karthik
 * Use case for raising a support ticket.
 *
 * @property profileRepository The repository for profile-related operations.
 * @constructor Creates an instance of [RaiseATicketUseCase] with the given [profileRepository].
 */
class RaiseATicketUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : ApiUseCase<AuthTokenData<RaiseAtTicketRequest>, RaiseATicketResponse> {

    /**
     * Executes the use case to raise a support ticket.
     *
     * @param request The request containing authorization token, token properties, and the ticket details.
     * @return A [Flow] emitting a [NetworkResource] containing [RaiseATicketResponse].
     */
    override fun invoke(
        request: AuthTokenData<RaiseAtTicketRequest>
    ): Flow<NetworkResource<RaiseATicketResponse>> {
        val authorization = request.authorization
        val apiRequest = request.request
        val tokenProperty = request.tokenProperties

        return profileRepository.raiseATicket(

            request = apiRequest!!
        )
    }
}
