package com.isu.prepaidcard.data.request


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class KitToKitRequest(
    @SerializedName("deviceId")
    val deviceId: String?, // 1234567
    @SerializedName("latLong")
    val latLong: String?, // 64,898
    @SerializedName("channel")
    val channel: String = "ANDROID",
    @SerializedName("parentCardRefNumber")
    val parentCardRefNumber: String?, // 1500000161
    @SerializedName("reissuedEncryptedCard")
    val reissuedEncryptedCard: String?, // TTRuamNJSnJja3Vo|TOnvpyoFEPxWy0iHNZ/qIiE0|GiutBUKF1BWTL0f4WE8KpA==

)