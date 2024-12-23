package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CardStatusChange(
    @SerializedName("cardRefNumber")
    val cardRefNumber: String?, // 1500000230
    @SerializedName("channel")
    val channel: String?, // WEB
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("latLong")
    val latLong: String?, // 64,898
    @SerializedName("requestedStatus")
    val requestedStatus: String?, // REISSUANCE
    @SerializedName("requestedUserName")
    val requestedUserName: String?, // CUST9876757565
    @SerializedName("shippingAddress1")
    val shippingAddress1: String?, // test
    @SerializedName("shippingAddress2")
    val shippingAddress2: String?, // test
    @SerializedName("shippingCity")
    val shippingCity: String?, // test
    @SerializedName("shippingCountry")
    val shippingCountry: String?, // test
    @SerializedName("shippingPinCode")
    val shippingPinCode: String?, // 752030
    @SerializedName("shippingState")
    val shippingState: String?, // test
)