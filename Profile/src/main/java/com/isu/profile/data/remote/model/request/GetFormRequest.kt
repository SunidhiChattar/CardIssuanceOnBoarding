package com.isu.profile.data.remote.model.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class GetFormRequest(
    @SerializedName("ids")
    val ids: List<Long?>? = listOf(31755579551257)
)