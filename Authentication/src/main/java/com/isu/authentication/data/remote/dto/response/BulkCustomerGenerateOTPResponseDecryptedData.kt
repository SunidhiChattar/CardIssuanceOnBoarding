package com.isu.authentication.data.remote.dto.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class BulkCustomerGenerateOTPResponseDecryptedData(
    @SerializedName("Message")
    val message: String?, // OTP Generate
    @SerializedName("OTP_RefID")
    val oTPRefID: String?, // 11481
    @SerializedName("Response_Code")
    val responseCode: String? // 1000
)