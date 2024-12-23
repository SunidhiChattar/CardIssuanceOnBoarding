package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AddonCardForChildRequest(
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 1234
    @SerializedName("isApproved")
    val isApproved: Boolean?, // true
    @SerializedName("latLong")
    val latLong: String?, // 56278199
)