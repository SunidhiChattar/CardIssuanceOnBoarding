package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class ViewCardBalanceRequest(

	@field:SerializedName("cardRefNo")
	val cardRefNo: String? = null,

	@field:SerializedName("channel")
	val channel: String? = null
)
