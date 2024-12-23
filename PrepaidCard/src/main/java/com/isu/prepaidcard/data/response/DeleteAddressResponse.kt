package com.isu.prepaidcard.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class DeleteAddressResponse(
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String? // Address deleted successfully
)