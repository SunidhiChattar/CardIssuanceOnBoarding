package com.isu.prepaidcard.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SetPrimaryResponse(
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String?, // The primary card has been updated successfully.
)