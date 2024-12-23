package com.isu.prepaidcard.data.response

import com.google.gson.annotations.SerializedName

data class OrderPhysicalResponse(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
	val data: OrderPhysicalData? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class OrderPhysicalData(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
