package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StatusCheckRequestModel(
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567ef234
    @SerializedName("latLong")
    val latLong: String?, // 12345678
    @SerializedName("mobileNumber")
    val mobileNumber: String?, // 8777778881
)
