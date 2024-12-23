package com.isu.prepaidcard.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MiniStatementResponse(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
@Keep
data class Data(

	@field:SerializedName("balance")
	val balance: Int? = null,

	@field:SerializedName("statements")
	val statements: List<StatementsItem?>? = null
)
@Keep
data class StatementsItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("transactionType")
	val transactionType: String? = null,

	@field:SerializedName("isCredit")
	val isCredit: Boolean? = null,

	@field:SerializedName("amount")
	val amount: Int? = null
)
