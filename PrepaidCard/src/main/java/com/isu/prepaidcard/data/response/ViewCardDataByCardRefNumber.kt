package com.isu.prepaidcard.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ViewCardDataByCardRefNumber(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String?, // Card details fetched successfully
) {
    @Keep
    data class Data(
        @SerializedName("cardRefId")
        val cardRefId: Int?, // 1500000161
        @SerializedName("cardType")
        val cardType: String?, // CHIP
        @SerializedName("createdDate")
        val createdDate: String?, // 2024-09-03T11:12:45.730707
        @SerializedName("encryptedCard")
        val encryptedCard: String?, // TTRuamNJSnJja3Vo|TOnvpyoFEPxWy0iHNZPrJCQ0|GQ9PdqGb87ygpJ6zANW8CA==
        @SerializedName("expiryDate")
        val expiryDate: String?, // TTRuamNJSnJja3Vo|TOHnrSsOEe8=|wbFGxSfPXrsozvRIxaxmXQ==
        @SerializedName("isActive")
        val isActive: Boolean?, // false
        @SerializedName("isBlock")
        val isBlock: Boolean?, // false
        @SerializedName("isChild")
        val isChild: Boolean?, // false
        @SerializedName("isDamage")
        val isDamage: Boolean?, // true
        @SerializedName("isHotlist")
        val isHotlist: Boolean?, // false
        @SerializedName("isLost")
        val isLost: Boolean?, // true
        @SerializedName("isPinSet")
        val isPinSet: Boolean?, // false
        @SerializedName("isVirtual")
        val isVirtual: Boolean?, // false
        @SerializedName("lastModified")
        val lastModified: String?, // 2024-09-03T11:12:45.730724
        @SerializedName("lastfourDigit")
        val lastfourDigit: String?, // 4404
        @SerializedName("mccDetails")
        val mccDetails: List<MccDetail?>?,
        @SerializedName("mobileNumber")
        val mobileNumber: String?, // TTRuamNJSnJja3Vo|V+npqSwJE/hQzw==|EOUIINLZ9Xh3X+/f5nFX8g==
        @SerializedName("nameOnCard")
        val nameOnCard: String?, // HIMANSHU ROUT
        @SerializedName("preferredLanguage")
        val preferredLanguage: String?, // ENGLISH
        @SerializedName("productCategory")
        val productCategory: String?, // GPR
        @SerializedName("productId")
        val productId: Int?, // 7407
        @SerializedName("productName")
        val productName: String?, // GPRPMINVENTORYBANK
    ) {
        @Keep
        data class MccDetail(
            @SerializedName("applicable")
            val applicable: Boolean?, // true
            @SerializedName("mccCode")
            val mccCode: Int?, // 3377
            @SerializedName("mccName")
            val mccName: String?, // Car Rental
        )
    }
}
