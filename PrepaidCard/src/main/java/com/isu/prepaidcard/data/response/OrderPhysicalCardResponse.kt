package com.isu.prepaidcard.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class OrderPhysicalCardResponse(
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String?, // Virtual card successfully upgraded to a physical card.
)