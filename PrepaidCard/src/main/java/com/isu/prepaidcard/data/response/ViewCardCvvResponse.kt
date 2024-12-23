package com.isu.prepaidcard.data.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class ViewCardCvvResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String? // cvv successfully fetched
) {
    @Keep

    data class Data(
        @SerializedName("cvv")
        val cvv: String? // TTRuamNJSnJja3Vo|TObmrjk=|IGQ45IbeHlxlf+tJsQxTsA==
    )
}