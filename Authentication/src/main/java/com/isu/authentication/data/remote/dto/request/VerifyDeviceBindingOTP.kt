package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VerifyDeviceBindingOTP(
    @SerializedName("channel")
    val channel: String = "ANDROID", // ANDROID
    @SerializedName("deviceId")
    val deviceId: String?, // 62d8c603603f6349
    @SerializedName("latLong")
    val latLong: String?, // 12.789.65.65
    @SerializedName("mobileNumber")
    val mobileNumber: String?, // 9853537086
    @SerializedName("otp")
    val otp: String?, // 1234
    @SerializedName("params")
    val params: String?, // OIGFDFGHJKJ43456789D
)