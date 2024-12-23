package com.isu.prepaidcard.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class FetchOrderCardHistoryResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String? // Card order history fetched successfully
) {
    @Keep

    data class Data(
        @SerializedName("amount")
        val amount: String?, // 50
        @SerializedName("cardType")
        val cardType: String?, // GPR
        @SerializedName("customerName")
        val customerName: String?, // CUST7008656872
        @SerializedName("expectedDeliveryDate")
        val expectedDeliveryDate: String?, // 2024-10-26T10:20:09.000Z
        @SerializedName("nameOnCard")
        val nameOnCard: String?, // TEST NAME
        @SerializedName("orderDate")
        val orderDate: String?, // 2024-10-19T10:20:09.000Z
        @SerializedName("orderId")
        val orderId: String?, // 161024162146933
        @SerializedName("orderStatus")
        val orderStatus: String?, // CONFIRMED
        @SerializedName("paymentType")
        val paymentType: String?, // SELF
        @SerializedName("productName")
        val productName: String?, // GPRPMINVENTORYOWN
        @SerializedName("shippingAddress")
        val shippingAddress: ShippingAddress?,
        @SerializedName("cardReferenceNumber")
        val cardReferenceNumber: String?
    ) {
        @Keep

        data class ShippingAddress(
            @SerializedName("address1")
            val address1: String?, // infocity
            @SerializedName("address2")
            val address2: String?, // burla
            @SerializedName("city")
            val city: String?, // puri
            @SerializedName("country")
            val country: String?, // India
            @SerializedName("pincode")
            val pincode: Int?, // 751024
            @SerializedName("state")
            val state: String? // odisha
        )
    }
}