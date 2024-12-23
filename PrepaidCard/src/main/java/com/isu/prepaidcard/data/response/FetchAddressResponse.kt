package com.isu.prepaidcard.data.response

import com.google.gson.annotations.SerializedName
import com.isu.common.customcomposables.ShippingAddressItem

data class FetchAddressResponse(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
	val data: FetchAddressData? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)



data class FetchAddressData(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("shipping_address")
	val shippingAddress: List<ShippingAddressItem?>? = null
)
