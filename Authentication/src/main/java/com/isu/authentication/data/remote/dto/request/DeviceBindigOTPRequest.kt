package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DeviceBindigOTPRequest(
    @SerializedName("channel")
    val channel: String? = "ANDROID", // ANDROID
    @SerializedName("deviceId")
    val deviceId: String?, // 62d8c603603f6349
    @SerializedName("expiryTime")
    val expiryTime: String?, // 10
    @SerializedName("latLong")
    val latLong: String?, // 12.789.65.65
    @SerializedName("mobileNumber")
    val mobileNumber: String?, // 9853537086
    @SerializedName("params")
    val params: String?, // OIGFDFGHJKJ43456789D
    @SerializedName("purpose")
    val purpose: String?, // Device binding
)