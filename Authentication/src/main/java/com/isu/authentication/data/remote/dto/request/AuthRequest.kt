package com.isu.authentication.data.remote.dto.request


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AuthRequest(
    @SerializedName("deviceId")
    val deviceId: String?, // 1234
    @SerializedName("userName")
    val userName: String?, // SWAGAT_R18
)