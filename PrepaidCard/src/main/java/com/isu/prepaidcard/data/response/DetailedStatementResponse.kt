package com.isu.prepaidcard.data.response

import com.google.gson.annotations.SerializedName

data class DetailedStatementResponse(

	@field:SerializedName("length")
	val length: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class UpdatedDate(

	@field:SerializedName("value")
	val value: String? = null
)

data class ResultsItem(

	@field:SerializedName("transaction_id")
	val transactionId: String? = null,

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("openingbalance")
	val openingbalance: Int? = null,

	@field:SerializedName("status_desc")
	val statusDesc: String? = null,

	@field:SerializedName("param_b")
	val paramB: Any? = null,

	@field:SerializedName("transactionMode")
	val transactionMode: String? = null,

	@field:SerializedName("param_c")
	val paramC: Any? = null,

	@field:SerializedName("CardRefId")
	val cardRefId: String? = null,

	@field:SerializedName("operationPerformed")
	val operationPerformed: Any? = null,

	@field:SerializedName("updatedDate")
	val updatedDate: UpdatedDate? = null,

	@field:SerializedName("userName")
	val userName: String? = null,

	@field:SerializedName("rrn")
	val rrn: String? = null,

	@field:SerializedName("closingbalance")
	val closingbalance: Int? = null,

	@field:SerializedName("adminName")
	val adminName: String? = null,

	@field:SerializedName("Type")
	val type: Any? = null,

	@field:SerializedName("createdDate")
	val createdDate: CreatedDate? = null,

	@field:SerializedName("param_a")
	val paramA: Any? = null
)

data class CreatedDate(

	@field:SerializedName("value")
	val value: String? = null
)
