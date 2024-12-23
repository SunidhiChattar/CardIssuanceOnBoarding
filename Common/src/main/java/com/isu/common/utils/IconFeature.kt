package com.isu.common.utils

import com.isu.common.navigation.Screen

data class IconFeature(
    val icon: Int,
    val feature: String,
    val destination: Screen? = null
)