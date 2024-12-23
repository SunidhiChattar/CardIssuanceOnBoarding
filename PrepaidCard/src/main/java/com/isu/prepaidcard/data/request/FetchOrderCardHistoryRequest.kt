package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FetchOrderCardHistoryRequest(
    @SerializedName("channel")
    val channel: String = "ANDROID", // ANDROID
    @SerializedName("deviceId")
    val deviceId: String?, // 12345
    @SerializedName("latLong")
    val latLong: String? // 34.56756
)