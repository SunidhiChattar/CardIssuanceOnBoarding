package com.isu.common.models

data class DataForCardSDK(
    val clientID:String?="",
    val clientSecret:String="",
    val userMobileNumber:String="",
    val jwtToken:String?="",
    val deviceChanged:Boolean=false
)