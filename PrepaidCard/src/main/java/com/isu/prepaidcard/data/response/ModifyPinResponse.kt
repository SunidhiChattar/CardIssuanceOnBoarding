package com.isu.prepaidcard.data.response

import com.google.gson.annotations.SerializedName

data class ModifyPinResponse(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
