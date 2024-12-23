package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VerifyDeviceChangeOTPRequest(
    @SerializedName("deviceId")
    val deviceId: String?, // 12345
    @SerializedName("otp")
    val otp: String?, // 467141
    @SerializedName("userName")
    val userName: String?, // SWAGAT_R18
)