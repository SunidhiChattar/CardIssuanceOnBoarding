package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class FetchAddressRequest(
    // 752410
    @SerializedName("deviceId")
    val deviceId: String?,
    @SerializedName("latLong")
    val latLong: String?,// odisha
    @SerializedName("channel")
    val channel: String = "ANDROID",
)
