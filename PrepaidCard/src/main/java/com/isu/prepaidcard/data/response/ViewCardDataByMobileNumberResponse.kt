package com.isu.prepaidcard.data.response

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import com.google.gson.annotations.SerializedName
import com.isu.common.ui.theme.CardBlue
import com.isu.common.ui.theme.CardDarkGreen
import com.isu.common.ui.theme.CardGreen
import com.isu.common.ui.theme.CardLightGreen
import com.isu.common.ui.theme.CardRed
import com.isu.common.ui.theme.CreditCardGreen


data class ViewCardDataByMobileNumberResponse(

	@field:SerializedName("statusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
    val data: ViewCardDataItem? = null,



	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ViewCardDataItem(

    @field:SerializedName("statusDesc")
    val statusDesc: String? = null,

    @field:SerializedName("cardData")
    val data: List<CardDataItem>? = null,

    @field:SerializedName("kycStatus")
    val kycStatus: String? = null,


    @field:SerializedName("status")
    val status: Int? = null
)

data class  CardDataItem(

	@field:SerializedName("expiryDate")
	val expiryDate: String? = null,

	@field:SerializedName("encryptedCard")
	val decrypted: String? = null,

	@field:SerializedName("addOnStatus")
	val addOnStatus: Any? = null,

	@field:SerializedName("nameOnCard")
	val nameOnCard: String? = null,

	@field:SerializedName("childCard")
	val childCard: Boolean? = null,

	@field:SerializedName("cardRefId")
	val cardRefId: Int? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null,

	@field:SerializedName("isPrimary")
	val isPrimary: Boolean? = null,

	@field:SerializedName("productCategory")
	val productCategory: String? = null,

	@field:SerializedName("encrypted")
	val encryptedCard: String? = null,

	@field:SerializedName("isReissued")
	val isReissued: Boolean? = null,

	@field:SerializedName("isNew")
	val isNew: Boolean? = null,


	val cardColor: Color = getRandomColor(),
)

data class CardDashBoardDetails(
	val requireCardDataFetching:Boolean=true
)

fun getRandomColor(): Color {
	val colors = listOf(
		Black,
		CreditCardGreen,
		CardRed,
		CardGreen,
		CardDarkGreen,
		CardLightGreen,
		CardBlue
	)
	return colors.random()
}