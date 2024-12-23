package com.isu.authentication.data.remote.dto.response


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep

data class VerifySelfSignInResponse(
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String? // Customer onboarded successfully
)