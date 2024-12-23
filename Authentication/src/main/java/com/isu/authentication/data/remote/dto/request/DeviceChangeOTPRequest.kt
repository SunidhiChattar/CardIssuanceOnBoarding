package com.isu.authentication.data.remote.dto.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DeviceChangeOTPRequest(
    @SerializedName("userName")
    val userName: String?, // SWAGAT_R7
)