package com.isu.authentication.data.remote.dto.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the request payload for fetching pin code data.
 *
 * @property channel The channel from which the request is made (e.g., WEB, MOBILE).
 * @property pin The pin code for which data is being requested.
 */
@Keep
data class FetchPinCodeDataRequest(
    @SerializedName("channel")
    val channel: String = "ANDROID", // Represents the channel making the request (e.g., WEB, MOBILE)

    @SerializedName("pin")
    val pin: Int?, // The pincode for which data is requested
)
