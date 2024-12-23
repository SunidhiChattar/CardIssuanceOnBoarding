package com.isu.common.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.isu.common.R

data class MerchantData(
    val merchant: String,
    val merchantProfile: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: String,
    val amountType: String,
    val frequency: String,
    val siHubId: String,
    var status: String
)
val merchants = mutableStateListOf(
    MerchantData(
    merchant = "Firefly",
    merchantProfile = R.drawable.profile_img,
    startDate = "6th March 2024" ,
    endDate = "6th March 2025",
    maxAmount = "₹ 890.00",
    amountType = "variable",
    frequency = "Monthly",
    siHubId = "XCWk9210",
    status = "Active"),
    MerchantData(
        merchant = "Open AI",
        merchantProfile = R.drawable.open_ai,
        startDate = "6th March 2024" ,
        endDate = "6th March 2025",
        maxAmount = "₹ 890.00",
        amountType = "variable",
        frequency = "Monthly",
        siHubId = "XCWk9210",
        status = "Active" )
)