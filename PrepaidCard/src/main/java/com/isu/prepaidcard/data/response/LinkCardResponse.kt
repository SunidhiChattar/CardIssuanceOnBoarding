package com.isu.prepaidcard.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LinkCardResponse(
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String?, // Card linked Successfully to your registered mobile number ending with *******9090 !!
)