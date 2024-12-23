package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ViewCardDataByMobileNumber(
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 123456789e2
    @SerializedName("latLong")
    val latLong: String?, // 12345,6789

)