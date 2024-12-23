package com.isu.prepaidcard.domain.usecase.carddata_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.AuthTokenData
import com.isu.prepaidcard.data.request.ViewCardDataByCardRefNumberRequest
import com.isu.prepaidcard.data.response.ViewCardDataByRefIdResponse
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViewCardDataByRefIdImpl @Inject constructor(private val repository: PrepaidCardRepository): ViewCardDataByRefIdUsecase{
    override fun invoke(request: AuthTokenData<ViewCardDataByCardRefNumberRequest>): Flow<NetworkResource<ViewCardDataByRefIdResponse>> {
        return repository.getCardDataByCardRefNo(
            viewCardDataByCardRefNumberRequest = request.request!!,
            token = request.authorization,
            tokenProperties = request.tokenProperties
        )
    }
}