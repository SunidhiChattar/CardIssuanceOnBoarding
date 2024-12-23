package com.isu.cardissuanceonboarding.inapp.inapp.data

import com.isu.cardissuanceonboarding.inapp.inapp.data.models.req.InsertRequestModel
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.req.UserAppDetails
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.res.InsertResponseModel
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.res.LatestVersionResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit API service interface defining methods for various network operations.
 */
interface ApiService {
    @POST("latestVersion")
    suspend fun getLatestAPKVersion(
        @Body userAppDetails: UserAppDetails
    ): Response<LatestVersionResponseModel>

    @POST("downloadInsertApi")
    suspend fun sendAppDetailsToApi(@Body insertRequestModel: InsertRequestModel):Response<InsertResponseModel>
}