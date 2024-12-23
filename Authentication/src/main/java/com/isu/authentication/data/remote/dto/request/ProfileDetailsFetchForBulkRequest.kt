package com.isu.authentication.data.remote.dto.request


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep

data class ProfileDetailsFetchForBulkRequest(
    @SerializedName("channel")
    val channel: String="ANDROID", // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 6587987
    @SerializedName("latLong")
    val latLong: String?, // 56789967
    @SerializedName("mobileNo")
    val mobileNo: String? // 8249652456
)