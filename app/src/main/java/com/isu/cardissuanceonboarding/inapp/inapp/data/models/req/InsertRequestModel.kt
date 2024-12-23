package com.isu.cardissuanceonboarding.inapp.inapp.data.models.req

import com.google.gson.annotations.SerializedName


data class InsertRequestModel(
    @SerializedName("app_package_name")
    var app_package_name: String? = null,

    @SerializedName("app_version")
    var app_version: String? = null,

    @SerializedName("device_id")
    var device_id: String? = null,

    @SerializedName("device_details")
    var device_details: String? = null,

    @SerializedName("fcm_token")
    var fcm_token: String? = null,

    @SerializedName("user_name")
    var user_name: String? = null
)


