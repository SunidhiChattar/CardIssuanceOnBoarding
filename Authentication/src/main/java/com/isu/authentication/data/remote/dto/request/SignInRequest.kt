package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SignInRequest(
    @SerializedName("channel")
    val channel: String = "ANDROID", // WEB
    @SerializedName("city")
    val city: String?, // bbsr
    @SerializedName("country")
    val country: String = "India", // India
    @SerializedName("createdBy")
    val createdBy: String = "PROG9345247257", // PROG9999879898
    @SerializedName("dateOfBirth")
    val dateOfBirth: String?, // 30111998
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("email")
    val email: String?, // sonu@gmail.com
    @SerializedName("firstName")
    val firstName: String?, // soni
    @SerializedName("gender")
    val gender: String?, // M
    @SerializedName("kyctype")
    val kyctype: String?, // MIN-KYC
    @SerializedName("lastName")
    val lastName: String?, // Parida
    @SerializedName("latLong")
    val latLong: String?, // 64:898
    @SerializedName("mobileNumber")
    val mobileNumber: String?, // 8909093456
    @SerializedName("ovdId")
    val ovdId: String?, // 383092349837983
    @SerializedName("ovdType")
    val ovdType: String?, // Adhar
    @SerializedName("pinCode")
    val pinCode: String?, // 729091
    @SerializedName("referalCode")
    val referalCode: String?,
    @SerializedName("state")
    val state: String?, // Odisa
)