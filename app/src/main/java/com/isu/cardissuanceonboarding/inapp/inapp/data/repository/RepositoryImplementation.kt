package com.isu.cardissuanceonboarding.inapp.inapp.data.repository

import com.isu.cardissuanceonboarding.inapp.inapp.common.NetworkResource
import com.isu.cardissuanceonboarding.inapp.inapp.common.handleFlowResponse
import com.isu.cardissuanceonboarding.inapp.inapp.data.ApiService
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.req.InsertRequestModel
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.req.UserAppDetails
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.res.InsertResponseModel
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.res.LatestVersionResponseModel
import com.isu.cardissuanceonboarding.inapp.inapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(
    private val apiService: ApiService
) : Repository {

    override fun getLatestAPKVersion(userAppDetails: UserAppDetails): Flow<NetworkResource<LatestVersionResponseModel>> =
        handleFlowResponse(
            call = {
                apiService.getLatestAPKVersion(
                    userAppDetails
                )
            }, mapFun = {
                it
            }
        )

    override fun sendAppDetailsToApi(insertRequestModel: InsertRequestModel): Flow<NetworkResource<InsertResponseModel>> {
        return handleFlowResponse(
            call = {
                apiService.sendAppDetailsToApi(
                    insertRequestModel
                )
            }, mapFun = {
                it
            }

        )
    }
}