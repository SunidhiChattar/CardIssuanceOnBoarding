package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LinkCarrdRequest(
    @SerializedName("cardRefNo")
    val cardRefNo: String?, // 1500000281
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("latLong")
    val latLong: String?, // 64,898
    @SerializedName("otp")
    val otp: String?, // 1234
    @SerializedName("params")
    val params: String?, // Testing
//    @SerializedName("requestedUserName")
//    val requestedUserName: String?, // CUST7436945405
)