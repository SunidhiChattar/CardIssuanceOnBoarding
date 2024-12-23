package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderPhysicalCardRequest(
    @SerializedName("addresType")
    val addresType: String?, // HOME
    @SerializedName("address")
    val address: String?, // infocity
    @SerializedName("address1")
    val address1: String?, // burla
    @SerializedName("cardRefId")
    val cardRefId: String?, // 1500000194
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("city")
    val city: String?, // puri
    @SerializedName("country")
    val country: String?, // India
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("latLong")
    val latLong: String?, // 64,898
    @SerializedName("nameOnCard")
    val nameOnCard: String?, // TEST NAME
    @SerializedName("personalizationMethod")
    val personalizationMethod: String?, // INDENT
    @SerializedName("pinType")
    val pinType: String?, // GREENPIN
    @SerializedName("pincode")
    val pincode: String?, // 751024

    @SerializedName("state")
    val state: String?, // odisha
)