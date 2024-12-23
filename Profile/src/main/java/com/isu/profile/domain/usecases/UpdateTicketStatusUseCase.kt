package com.isu.profile.domain.usecases

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.request.AuthTokenData
import com.isu.profile.data.remote.model.request.UpdateTicketStatusRequest
import com.isu.profile.data.remote.model.response.UpdateTicketStatusResponse
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author-karthik
 * Use case for updating the status of a ticket.
 *
 * @property profileRepository The repository responsible for profile-related operations.
 * @constructor Creates an instance of [UpdateTicketStatusUseCase] with the specified [profileRepository].
 */
class UpdateTicketStatusUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : ApiUseCase<AuthTokenData<UpdateTicketStatusRequest>, UpdateTicketStatusResponse> {

    /**
     * Executes the use case to update the status of a ticket.
     *
     * @param request The request containing authorization token, token properties, and the status update details.
     * @return A [Flow] emitting a [NetworkResource] containing [UpdateTicketStatusResponse].
     */
    override fun invoke(
        request: AuthTokenData<UpdateTicketStatusRequest>
    ): Flow<NetworkResource<UpdateTicketStatusResponse>> {
        val tokenProperties = request.tokenProperties
        val authorization = request.authorization
        val statusUpdateRequest = request.request

        return profileRepository.updateTicketStatus(
            tokenProperties = tokenProperties,
            authorization = authorization,
            request = statusUpdateRequest!!
        )
    }
}
