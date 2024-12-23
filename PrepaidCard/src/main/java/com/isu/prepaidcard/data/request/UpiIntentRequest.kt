package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpiIntentRequest(
    @SerializedName("amount")
    val amount: String?, // 1
    @SerializedName("channelId")
    val channelId: String = "ANDROID", // WEBUSER
    @SerializedName("clientRefId")
    val clientRefId: String?, // KKRT5686339839943
    @SerializedName("isWalletTopUp")
    val isWalletTopUp: Boolean?, // false
    @SerializedName("merchantType")
    val merchantType: String?, // AGGREGATE
    @SerializedName("paramA")
    val paramA: String?,
    @SerializedName("paramB")
    val paramB: String?,
    @SerializedName("paramC")
    val paramC: String?,
    @SerializedName("paymentMode")
    val paymentMode: String?, // INTENT
    @SerializedName("remarks")
    val remarks: String?, // SchedulerTest
    @SerializedName("requestingUserName")
    val requestingUserName: String?, // silpa
    @SerializedName("virtualAddress")
    val virtualAddress: String?, // shalinipasumarthy@ybl
)