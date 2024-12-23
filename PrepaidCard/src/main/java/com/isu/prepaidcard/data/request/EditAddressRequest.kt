package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class EditAddressRequest(
    @SerializedName("channel")
    val channel: String? = "ANDROID", // ANDROID
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("id")
    val id: String?, // 1729757545700
    @SerializedName("isDelete")
    val isDelete: Boolean = false, // false
    @SerializedName("latLong")
    val latLong: String?, // 64:898
    @SerializedName("shippingAddress")
    val shippingAddress: ShippingAddress?
) {
    @Keep

    data class ShippingAddress(
        @SerializedName("address1")
        val address1: String?, // theirbbsr
        @SerializedName("address2")
        val address2: String?, // bbsr
        @SerializedName("addressType")
        val addressType: String?, // Home
        @SerializedName("city")
        val city: String?, // bbsr
        @SerializedName("country")
        val country: String?, // bhubaneswar
        @SerializedName("pinCode")
        val pinCode: String?, // 752410
        @SerializedName("state")
        val state: String? // odisha
    )
}