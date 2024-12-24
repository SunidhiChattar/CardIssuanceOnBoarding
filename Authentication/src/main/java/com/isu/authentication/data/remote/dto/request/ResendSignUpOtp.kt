package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResendSignUpOtp(
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("latLong")
    val latLong: String?, // 64:898
    @SerializedName("mobileNumber")
    val mobileNumber: String?, // 6390055440
    @SerializedName("otpRefId")
    val otpRefId: String?, // 11586
)