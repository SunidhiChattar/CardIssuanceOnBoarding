package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TwoFARequestModel(
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 123456
    @SerializedName("expiryTime")
    val expiryTime: String?, // 10
    @SerializedName("latLong")
    val latLong: String?, // 12.3.4,12.34.45
    @SerializedName("mobileNumber")
    val mobileNumber: String?, // 7008656872
    @SerializedName("paramA")
    val paramA: String?,
    @SerializedName("paramB")
    val paramB: String?,
    @SerializedName("paramC")
    val paramC: String?,
    @SerializedName("params")
    val params: String = "TESTNIYATI", // TESTNIYATI
    @SerializedName("purpose")
    val purpose: String = "LOGIN", // LOGIN
)