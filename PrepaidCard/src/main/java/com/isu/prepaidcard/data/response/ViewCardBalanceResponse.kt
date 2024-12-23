package com.isu.prepaidcard.data.response

import com.google.gson.annotations.SerializedName

data class ViewCardBalanceResponse(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
	val data: ViewCardBalanceData? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ViewCardBalanceData(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("balance")
	val balance: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
data class CardBalanceState(
	val requireCardBalanceFetching:Boolean=true
)