package com.isu.authentication.data.remote.dto.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StatusCheckResponse(
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String?, // Operation completed successfully.
    @SerializedName("userDetails")
    val userDetails: UserDetails?
) {
    @Keep
    data class UserDetails(
        @SerializedName("status")
        val status: String?, // SUCCESS
        @SerializedName("statusCode")
        val statusCode: Int?, // 0
        @SerializedName("statusDesc")
        val statusDesc: String? // USER ONBOARDED SUCCESSFULLY
    )
}