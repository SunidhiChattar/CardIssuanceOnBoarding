package com.isu.profile.domain.usecases

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.request.GetFormRequest
import com.isu.profile.data.remote.model.response.ShowTicketFormResponse
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TicketFormUseCase @Inject constructor(private val profileRepository: ProfileRepository
    ) : ApiUseCase<GetFormRequest, ShowTicketFormResponse> {
    override fun invoke(request: GetFormRequest): Flow<NetworkResource<ShowTicketFormResponse>> {
        return profileRepository.showFormTicket(request)
    }
}