package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class LoadCardRequest(

	@field:SerializedName("cardRefNumber")
	val cardRefNumber: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("channel")
	val channel: String? = null
)
