package com.isu.prepaidcard.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NewUpiResponse
    (
    @SerializedName("amount")
    val amount: String?, // 1
    @SerializedName("clientRefId")
    val clientRefId: String?, // ISU1949959116
    @SerializedName("intentData")
    val intentData: String?, // upi://pay?pa=bankit.tanishatest@fin&pn=Tanisha%20Test&mc=5045&tr=539910035872742262&tn=SchedulerTest&am=1.00&cu=INR&mode=05&orgid=159771&catagory=01&url=https://www.finobank.com/&sign=MEUCICin47sRqcA9srusN6TkKqhaHWH21MH4kZ6emZPZ4o/FAiEA6Iswgazji8W61DqUi7Nwb+W0gGsptZ4ZSBc0FUoL6DE=
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
    val statusDesc: String?, // Intent Generation Success
    @SerializedName("txnId")
    val txnId: String?, // 539910035872742262
)