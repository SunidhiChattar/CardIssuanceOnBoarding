package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VerifySignInRequest(
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 123456789e2
    @SerializedName("latLong")
    val latLong: String?, // 12345,6789
    @SerializedName("mobileNo")
    val mobileNo: String?, // 9583014004
    @SerializedName("otp")
    val otp: String?, // 798051
    @SerializedName("otpRefId")
    val otpRefId: String?, // 11589
)