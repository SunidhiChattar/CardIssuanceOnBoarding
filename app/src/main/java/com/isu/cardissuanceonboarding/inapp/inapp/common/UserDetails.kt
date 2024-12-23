package com.isu.cardissuanceonboarding.inapp.inapp.common

import android.os.Build

object UserDetails {
    var appVersionName = ""
   var appVersionCode = 0
    var appPackageName  = ""
    var androidId = ""
    var userIpAddress = ""
    var fcmToken = ""
    val osVersion = Build.VERSION.SDK_INT
    const val USER_NAME = "---- App User Name -----"
    val userDeviceModel: String = Build.MODEL
}