package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class ChangeMccRequestBody(

	@field:SerializedName("cardRefNumber")
	val cardRefNumber: Int? = null,
    @field:SerializedName("channel")
    val channel: String = "ANDROID",
    @field:SerializedName("latLong")
    val latLong: String? = null,
    @field:SerializedName("deviceId")
    val deviceId: String? = null,

	@field:SerializedName("restrictMccWrapperRequests")
	val restrictMccWrapperRequests: List<RestrictMccWrapperRequestsItem?>? = null
)

data class RestrictMccWrapperRequestsItem(

	@field:SerializedName("mccCode")
	val mccCode: String? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null
)
