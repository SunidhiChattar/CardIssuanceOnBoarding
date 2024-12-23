package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BulkCustomerGenerateOTP(
    @SerializedName("cardType")
    val cardType: String? = "GPR", // GIFT
    @SerializedName("channel")
    val channel: String? = "ANDROID", // WEB
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("deviceId")
    val deviceId: String?, // 1234
    @SerializedName("latLong")
    val latLong: String? // 12345,67890
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
        val kycNumber: String?, // 123451228825
        @SerializedName("kycType")
        val kycType: String?, // AADHAR
        @SerializedName("pincode")
        val pincode: String?, // 752030
        @SerializedName("state")
        val state: String? // odisha
    )
}