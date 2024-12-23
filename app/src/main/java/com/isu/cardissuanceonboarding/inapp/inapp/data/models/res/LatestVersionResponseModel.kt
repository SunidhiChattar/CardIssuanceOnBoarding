package com.isu.cardissuanceonboarding.inapp.inapp.data.models.res

import com.google.gson.annotations.SerializedName

data class LatestVersionResponseModel(
    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: List<Data>? = null
)

data class Data(
    @SerializedName("app_package_name")
    val appPackageName: String? = null,

    @SerializedName("latest_updated_date")
    val latestUpdatedDate: String? = null,

    @SerializedName("version")
    val version: String? = null,

    @SerializedName("app_icon_url")
    val appIconUrl: String? = null,

    @SerializedName("app_name")
    val appName: String? = null,

    @SerializedName("appurl")
    val appUrl: String? = null,

    @SerializedName("forced_update")
    val forcedUpdate: Boolean? = null,

    @SerializedName("os_type")
    val osType: String? = null,

    @SerializedName("uniqueid")
    val uniqueId: String? = null,

    @SerializedName("updated_on")
    val updatedOn: String? = null,

    @SerializedName("user_name")
    val userName: String? = null,

    @SerializedName("versioncode")
    val versioncode: String? = null
)