package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MinKycUpdateRequest(
    @SerializedName("cardType")
    val cardType: String = "GPR", // GPR
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("deviceId")
    val deviceId: String?, // 25364758697
    @SerializedName("latLong")
    val latLong: String?, // 8738713
) {
    @Keep
    data class Data(
        @SerializedName("address")
        val address: String?, // bbsr
        @SerializedName("address1")
        val address1: String?, // bbsr
        @SerializedName("city")
        val city: String?, // bbsr
        @SerializedName("country")
        val country: String?, // india
        @SerializedName("dateOfBirth")
        val dateOfBirth: String?, // 1999-10-12
        @SerializedName("gender")
        val gender: String?, // Male
        @SerializedName("kycNumber")
        val kycNumber: String?, // 123456723125
        @SerializedName("kycType")
        val kycType: String?, // AADHAR
        @SerializedName("pincode")
        val pincode: String?, // 752030
        @SerializedName("state")
        val state: String?, // odisha
    )
}