package com.isu.prepaidcard.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class KitToKitResponse(
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String?, // kit to kit transfered successfully
)