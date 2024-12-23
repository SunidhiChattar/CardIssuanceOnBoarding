package com.isu.cardissuanceonboarding.inapp.inapp.data.models.res

import com.google.gson.annotations.SerializedName


data class InsertResponseModel(
    @SerializedName("status")
    var status: Int? = null,

    @SerializedName("message")
    var message: String? = null,
)

