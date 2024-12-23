package com.isu.prepaidcard.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NewUpiIntentResponse(
    @SerializedName("amount")
    val amount: String?, // 1
    @SerializedName("clientRefId")
    val clientRefId: String?, // KKRT5686339839943
    @SerializedName("intentData")
    val intentData: String?, // upi://pay?pa=bankit.tanishatest@fin&pn=silpa&mc=5816&tr=530197871662462447&tn=SchedulerTest&am=1&cu=INR&mode=04&orgid=159771&catagory=01&url=https://www.finobank.com/&sign=&tid=530197871662462447
    @SerializedName("merchantId")
    val merchantId: String?,
    @SerializedName("payeeVPA")
    val payeeVPA: String?, // bankit.tanishatest@fin
    @SerializedName("payerVPA")
    val payerVPA: String?, // shalinipasumarthy@ybl
    @SerializedName("paymentState")
    val paymentState: String?, // INITIATED
    @SerializedName("qrData")
    val qrData: String?,
    @SerializedName("status")
    val status: String?, // INITIATED
    @SerializedName("statusCode")
    val statusCode: String?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String?, // Intent verified successfully(B)
    @SerializedName("txnId")
    val txnId: String?, // 530197871662462447
)