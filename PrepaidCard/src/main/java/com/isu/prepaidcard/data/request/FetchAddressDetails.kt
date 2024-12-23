package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FetchAddressDetails(
    @SerializedName("requestedUserName")
    val requestedUserName: String?, // CUST9348096397
)