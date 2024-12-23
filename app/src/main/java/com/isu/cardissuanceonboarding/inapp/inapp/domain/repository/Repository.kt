package com.isu.cardissuanceonboarding.inapp.inapp.domain.repository

import com.isu.cardissuanceonboarding.inapp.inapp.common.NetworkResource
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.req.InsertRequestModel
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.req.UserAppDetails
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.res.InsertResponseModel
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.res.LatestVersionResponseModel
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getLatestAPKVersion(
        userAppDetails: UserAppDetails
    ): Flow<NetworkResource<LatestVersionResponseModel>>

    fun sendAppDetailsToApi(insertRequestModel: InsertRequestModel): Flow<NetworkResource<InsertResponseModel>>

}