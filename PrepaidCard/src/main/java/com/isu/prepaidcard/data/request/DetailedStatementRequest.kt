package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class DetailedStatementRequest(

	@field:SerializedName("$4")
    val start_date: String? = null,

	@field:SerializedName("$5")
    val end_date: String? = null,

	@field:SerializedName("$6")
    val txn_type: List<String?>? = listOf(
        "ECOM", "LOADCARD", "CREDIT", "DEBIT", "POS", "ATM", "credit"
    ),

	@field:SerializedName("$7")
    val cardReference: String? = null,

	@field:SerializedName("$1")
    val reportName: String? = "txn-deatiles-report",

	@field:SerializedName("$2")
    val username: String? = null,

	@field:SerializedName("$3")
    val status: List<String?>? = listOf(
        "SUCCESS", "FAILED"
    )
)
