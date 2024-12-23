package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class BulkVerifyOTPRequest(
    @SerializedName("cardType")
    val cardType: String? = "GPR", // GIFT
    @SerializedName("channel")
    val channel: String? = "ANDROID", // WEB
    @SerializedName("latLong")
    val latLong: String?, // 1234,56789
    @SerializedName("mobileno")
    val mobileno: Long?, // 9986745465
    @SerializedName("otp")
    val otp: String?, // 152397
    @SerializedName("otpRefId")
    val otpRefId: String?,// 11503
    @SerializedName("deviceId")
    val deviceId: String?, // 1234
)