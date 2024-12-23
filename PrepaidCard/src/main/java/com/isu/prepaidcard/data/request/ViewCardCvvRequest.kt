package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class ViewCardCvvRequest(
	@SerializedName("cardRefNo")
	val cardRefNo: String?, // 1500000210
	@SerializedName("channel")
	val channel: String = "ANDROID", // WEB
	@SerializedName("deviceId")
	val deviceId: String?, // 1234567
	@SerializedName("latLong")
	val latLong: String?, // 64:898
)
