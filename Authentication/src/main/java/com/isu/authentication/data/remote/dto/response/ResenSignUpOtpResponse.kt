package com.isu.authentication.data.remote.dto.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResenSignUpOtpResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String?, // OTP generated successfully and sent to your registered mobile number ending with ****9090
) {
    @Keep
    data class Data(
        @SerializedName("otpRefId")
        val otpRefID: String?, // 11579
    )
}