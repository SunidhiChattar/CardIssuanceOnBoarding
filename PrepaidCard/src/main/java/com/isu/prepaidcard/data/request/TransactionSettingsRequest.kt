package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TransactionSettingsRequest(
    @SerializedName("atm_limit")
    val atmLimit: Double?, // 110.0
    @SerializedName("cardRefNo")
    val cardRefNo: Long?, // 21110711188
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("contactless_limit")
    val contactlessLimit: Double?, // 3.0
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("ecom_limit")
    val ecomLimit: Double?, // 2.0
    @SerializedName("isAtmAllowed")
    val isAtmAllowed: Boolean?, // true
    @SerializedName("isContactlessAllowed")
    val isContactlessAllowed: Boolean?, // false
    @SerializedName("isEcomAllowed")
    val isEcomAllowed: Boolean?, // false
    @SerializedName("isPosAllowed")
    val isPosAllowed: Boolean?, // false
    @SerializedName("latLong")
    val latLong: String?, // 64,898
    @SerializedName("pos_limit")
    val posLimit: Double?, // 10.0
    // CUST7436945405
)