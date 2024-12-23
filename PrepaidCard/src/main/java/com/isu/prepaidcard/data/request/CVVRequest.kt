package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CVVRequest(
    @SerializedName("cardRefNo")
    val cardRefNo: String?, // 1500000210
    @SerializedName("channel")
    val channel: String?, // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("latLong")
    val latLong: String?, // 64:898
)