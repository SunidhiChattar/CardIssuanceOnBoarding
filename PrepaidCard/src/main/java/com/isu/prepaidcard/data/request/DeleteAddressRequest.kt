package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class DeleteAddressRequest(
    @SerializedName("channel")
    val channel: String? = "ANDROID", // ANDROID
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("id")
    val id: String?, // 1729757545700
    @SerializedName("isDelete")
    val isDelete: Boolean? = true, // true
    @SerializedName("latLong")
    val latLong: String? // 64:898
)