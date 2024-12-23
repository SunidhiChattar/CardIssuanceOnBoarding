package com.isu.cardissuanceonboarding.inapp.inapp.domain.usecase


import com.isu.cardissuanceonboarding.inapp.inapp.common.NetworkResource
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.req.InsertRequestModel
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.res.InsertResponseModel
import com.isu.cardissuanceonboarding.inapp.inapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendAppDetailsToApiUseCase @Inject constructor(
    private val repository: Repository,
) {
    operator fun invoke(
        insertRequestModel: InsertRequestModel,
    ): Flow<NetworkResource<InsertResponseModel>> {
        return repository.sendAppDetailsToApi(insertRequestModel)
    }
}