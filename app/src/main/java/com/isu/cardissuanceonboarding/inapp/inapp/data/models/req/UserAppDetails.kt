package com.isu.cardissuanceonboarding.inapp.inapp.data.models.req

import com.google.gson.annotations.SerializedName

data class UserAppDetails(
    @field:SerializedName("app_package_name")
    val appPackageName: String? = null,

    @field:SerializedName("is_enabled")
    val isEnabled: Boolean? = null
)
