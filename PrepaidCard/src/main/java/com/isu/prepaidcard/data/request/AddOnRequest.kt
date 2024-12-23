package com.isu.prepaidcard.data.request

import com.google.gson.annotations.SerializedName

data class AddOnRequest(

    @field:SerializedName("nameonCard")
    val nameonCard: String? = null,

    @field:SerializedName("addoncardforself")
    val addoncardforself: Boolean? = null,

    @field:SerializedName("childMobileNumber")
    val childMobileNumber: String? = "",


    @field:SerializedName("latLong")
    val latLong: String? = null,

    @field:SerializedName("parentCardRefId")
    val parentCardRefId: String? = null,

    @field:SerializedName("channel")
    val channel: String? = null,

    @field:SerializedName("deviceId")
    val deviceId: String? = null,
)
