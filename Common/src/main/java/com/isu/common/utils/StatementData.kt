package com.isu.common.utils

import androidx.compose.runtime.mutableStateListOf

data class StatementData(
    val txnName: String,
    val txnDate: String,
    val amount: String
)
val cardStatement = mutableStateListOf(null)
