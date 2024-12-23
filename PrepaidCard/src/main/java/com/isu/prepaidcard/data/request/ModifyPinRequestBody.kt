package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class ModifyPinRequestBody(

	@field:SerializedName("encryptPin")
	val encryptPin: String? = null,

	@field:SerializedName("latLong")
	val latLong: String? = null,

	@field:SerializedName("cardRefId")
	val cardRefId: Int? = null,

	@field:SerializedName("channel")
	val channel: String? = null,

	@field:SerializedName("otp")
	val otp: String? = null,

	@field:SerializedName("params")
	val params: String? = null,

	@field:SerializedName("deviceId")
	val deviceId: String? = null
)
