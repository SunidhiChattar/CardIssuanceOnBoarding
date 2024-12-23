package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class ViewCardDataByCardRefNumberRequest(
    @SerializedName("cardRefNo")
    val cardRefNo: String?, // 1500000161
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("latLong")
    val latLong: String?, // 64:898
    @SerializedName("type")
    val type: String = "CUSTOMER", // CUSTOMER
)
