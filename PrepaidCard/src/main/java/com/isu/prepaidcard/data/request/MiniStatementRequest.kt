package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class MiniStatementRequest(

	@field:SerializedName("cardRefNumber")
	val cardRefNumber: String? = null,

	@field:SerializedName("latLong")
	val latLong: String? = null,

	@field:SerializedName("channel")
	val channel: String? = null,

	@field:SerializedName("deviceId")
	val deviceId: String? = null
)
