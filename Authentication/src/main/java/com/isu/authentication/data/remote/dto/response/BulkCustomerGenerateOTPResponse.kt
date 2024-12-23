package com.isu.authentication.data.remote.dto.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.isu.common.utils.encryptdecrypt.EncryptedData

@Keep

data class BulkCustomerGenerateOTPResponse(
    @SerializedName("data")
    val `data`: EncryptedData?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String? // Otp generation Successfully
) {
    @Keep

    data class Data(
        @SerializedName("authTag")
        val authTag: String?, // XHIRG6uRpXrpnZVAOukzAg==
        @SerializedName("encryptedMessage")
        val encryptedMessage: String?, // 1WJSI+Uz5Me41DbikGV/dXN+lmlbQYhhi6+bz9k2PPxC9JB1g8s0V7P+6OD3gsafxv+4fXwb6TpBUrpOQDzFgGHn+qnR
        @SerializedName("iv")
        val iv: String? // +sXDnJQM12/9rz7p
    )

}