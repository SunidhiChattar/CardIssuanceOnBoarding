package com.isu.common.utils.encryptdecrypt

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
@Serializable
data class EncryptedData(
        @SerializedName("authTag")
        var authTag: String?=null,
        @SerializedName("encryptedMessage")
        var encryptedMessage: String?=null,
        @SerializedName("iv")
        var iv: String?=null
)
@Serializable
data class EncryptedResponse(
        @SerializedName("data")
        val `data`: EncryptedData,
        @SerializedName("status")
        val status: String?, // SUCCESS
        @SerializedName("statusCode")
        val statusCode: Int?, // 0
        @SerializedName("statusDesc")
        val statusDesc: String? // OTP G
)
@Serializable
data class EncryptedResponseCBC(
        @SerializedName("ResponseData")
        val responseData: String? // GP7ixnCpZd3XullYc9lh4TZVnyJEYUx7G7Alclr+MQ7fcxHr0gu/Z+9g3cpixBqcHcr7hyitoJP8W47it8oO+HZnfeBRaCj2RCqdSMRwaGrbgcEDhn0maNWMKq8ZCAGMXhgbZtLRRtIdL1cSklnVoNfWrhIXrNUeRP/RCtnOaNz8Bn6ReAPLyYaHRz09V4aCPPk4hhU1zJVerukuPFqCOQTZaMe3/fPmeQUQbRSmueyOTysvUncKVhm9oCgHfqs3hL7XC29KSQhwwkph0qkqOQ==
)