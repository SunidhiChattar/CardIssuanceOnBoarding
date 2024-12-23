package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class UpdateAddressRequest(

	@field:SerializedName("requestedUserName")
	val requestedUserName: String? = null,

	@field:SerializedName("shippingAddress")
	val shippingAddress: ShippingAddress? = null
)

data class ShippingAddress(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("address2")
	val address2: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("address1")
	val address1: String? = null,

	@field:SerializedName("pinCode")
	val pinCode: String? = null,

	@field:SerializedName("state")
	val state: String? = null
)
