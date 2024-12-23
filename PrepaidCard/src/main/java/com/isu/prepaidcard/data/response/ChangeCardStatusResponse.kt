package com.isu.prepaidcard.data.response

import com.google.gson.annotations.SerializedName

data class ChangeCardStatusResponse(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
	val data: ChangeCardStatusData? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ChangeCardStatusData(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
