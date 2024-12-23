package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VerifyTwoFAOTPRequest(
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 12345
    @SerializedName("latLong")
    val latLong: String?, // 12.23
    @SerializedName("mobileNumber")
    val mobileNumber: String?, // 7008656872
    @SerializedName("otp")
    val otp: String?, // 1234
    @SerializedName("params")
    val params: String?, // 6546f
    @SerializedName("purpose")
    val purpose: String = "LOGIN", // LOGIN
)