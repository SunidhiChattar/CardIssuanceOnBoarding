package com.isu.authentication.data.remote.dto.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class BulkVerifyOTPResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String? // Registration Successful
) {
    @Keep

    data class Data(
        @SerializedName("authTag")
        val authTag: String?, // O5ZPT+7z6Awxj7wYDdUvKw==
        @SerializedName("encryptedMessage")
        val encryptedMessage: String?, // hC0hzqAs9KBft4VcFYQ8IF8F7ZhWPeJMkHI7x2Ksj4pl+N/6lVo0U4VhN5b3xUSsnNkaKdISxDzNz+TL2doAdy8xL4oUzMIxRhqDuf5tuEGBX7qkzrjCBEsML8wzrleCmJ2glpiUQ6vTCt5wkc4aLTF/9+hxLkVdoy2N9lglZedVNl7WQOCLG4jFBg4OkmN/EDVHxGe9OYIxnZdrGz+GTTP9+IU=
        @SerializedName("iv")
        val iv: String? // 2s+n/jCTGvIXaDoE
    )
}