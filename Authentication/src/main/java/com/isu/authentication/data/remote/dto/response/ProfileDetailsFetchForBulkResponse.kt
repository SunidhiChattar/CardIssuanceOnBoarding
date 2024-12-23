package com.isu.authentication.data.remote.dto.response


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep

data class ProfileDetailsFetchForBulkResponse(
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String?, // Successfully fetch profile details of 8249652456
    @SerializedName("userDetails")
    val userDetails: UserDetails?
) {
    @Keep

    data class UserDetails(
        @SerializedName("email")
        val email: String?, // 8249652456@gmail.com
        @SerializedName("firstName")
        val firstName: String?, // Sonalii
        @SerializedName("lastName")
        val lastName: String?, // Parida
        @SerializedName("mobileNumber")
        val mobileNumber: String? // 8249652456
    )
}