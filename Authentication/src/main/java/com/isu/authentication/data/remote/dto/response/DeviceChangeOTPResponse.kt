package com.isu.authentication.data.remote.dto.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DeviceChangeOTPResponse(
    @SerializedName("message")
    val message: String?, // Successful! A temporary OTP has been sent to your mobile number +91-XXX-XXX-XX71
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
)