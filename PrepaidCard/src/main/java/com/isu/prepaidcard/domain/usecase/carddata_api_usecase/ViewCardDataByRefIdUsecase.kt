package com.isu.prepaidcard.domain.usecase.carddata_api_usecase

import com.isu.common.utils.NetworkResource
import com.isu.prepaidcard.data.request.AuthTokenData
import com.isu.prepaidcard.data.request.ViewCardDataByCardRefNumberRequest
import com.isu.prepaidcard.data.response.ViewCardDataByRefIdResponse
import kotlinx.coroutines.flow.Flow

interface ViewCardDataByRefIdUsecase {
    operator fun invoke(
        request: AuthTokenData<ViewCardDataByCardRefNumberRequest>
    ): Flow<NetworkResource<ViewCardDataByRefIdResponse>>
}