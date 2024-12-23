/*
package com.isu.prepaidcard.data.response

import com.google.gson.annotations.SerializedName

data class ViewCardCvvResponse(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
	val data: ViewCvvData? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ViewCvvData(

	@field:SerializedName("cvv")
	val cvv: String? = null,

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class CardCvvDetails(
	val requireCvvFetching:Boolean=true
)*/
data class CardCvvDetails(
    val requireCvvFetching: Boolean = true
)
