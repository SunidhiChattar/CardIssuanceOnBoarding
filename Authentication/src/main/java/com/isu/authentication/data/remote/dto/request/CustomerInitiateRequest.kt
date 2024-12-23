package com.isu.authentication.data.remote.dto.request


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep

data class CustomerInitiateRequest(
    @SerializedName("channel")
    val channel: String?="ANDROID", // ANDROID
    @SerializedName("city")
    val city: String?, // bbsr
    @SerializedName("country")
    val country: String?, // India
    @SerializedName("createdBy")
    val createdBy: String?, // PROG9999879898
    @SerializedName("dateOfBirth")
    val dateOfBirth: String?, // 1998-11-30
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("email")
    val email: String?, // sonu@gmail.com
    @SerializedName("firstName")
    val firstName: String?, // soni
    @SerializedName("gender")
    val gender: String?, // Male
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
    val ovdType: String?, // AADHAR
    @SerializedName("pinCode")
    val pinCode: String?, // 729091
    @SerializedName("referalCode")
    val referalCode: String?, // 1234
    @SerializedName("state")
    val state: String? // Odisa
)