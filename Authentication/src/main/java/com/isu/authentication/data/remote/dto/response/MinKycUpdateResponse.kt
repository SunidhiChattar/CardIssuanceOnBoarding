package com.isu.authentication.data.remote.dto.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MinKycUpdateResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String?, // OTP Generate
) {
    @Keep
    data class Data(
        @SerializedName("Message")
        val message: String?, // OTP Generate
        @SerializedName("OTP_RefID")
        val oTPRefID: String?, // 11501
        @SerializedName("Response_Code")
        val responseCode: String?, // 1000
    )
}