package com.isu.prepaidcard.domain.usecase

import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.LinkCarrdRequest
import com.isu.prepaidcard.data.response.LinkCardResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LinkCardUseCase @Inject constructor(private val repository: PrepaidCardRepository) :
    ApiUseCase<LinkCarrdRequest, LinkCardResponse> {
    override fun invoke(request: LinkCarrdRequest): Flow<NetworkResource<LinkCardResponse>> {
        return repository.linkCard(request)
    }
}