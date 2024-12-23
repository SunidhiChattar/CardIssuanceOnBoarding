package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/*"latLong": "64:898",
"deviceId": "1234567",
“channel”: “ANDROID”*/

@Keep
data class AddNewAddressRequest(

    @SerializedName("shippingAddress")
    val shippingAddress: ShippingAddress?,
    @SerializedName("latLong")
    val latLong: String?, // 752410
    @SerializedName("deviceId")
    val deviceId: String?,// odisha
    @SerializedName("channel")
    val channel: String = "ANDROID",
) {
    @Keep
    data class ShippingAddress(
        @SerializedName("address1")
        val address1: String?, // bbsr
        @SerializedName("address2")
        val address2: String?, // bbsr
        @SerializedName("city")
        val city: String?, // bbsr
        @SerializedName("country")
        val country: String?, // bhubaneswar
        @SerializedName("pinCode")
        val pinCode: String?, // 752410
        @SerializedName("state")
        val state: String?,// odisha
        @SerializedName("addressType")
        val addressType: String?,
    )

}