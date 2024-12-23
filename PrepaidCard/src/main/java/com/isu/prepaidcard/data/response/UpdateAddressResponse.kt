package com.isu.prepaidcard.data.response

import com.google.gson.annotations.SerializedName

data class UpdateAddressResponse(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
	val data: UpdateAddressData? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UpdateAddressData(
	val any: Any? = null
)
