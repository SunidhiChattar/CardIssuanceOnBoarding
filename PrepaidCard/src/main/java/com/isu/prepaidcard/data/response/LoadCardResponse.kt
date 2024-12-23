package com.isu.prepaidcard.data.response

import com.google.gson.annotations.SerializedName

data class LoadCardResponse(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
	val loadCardData: LoadCardData? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class LoadCardData(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
