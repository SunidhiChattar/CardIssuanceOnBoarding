package com.isu.profile.data.remote.model.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @author- karthik
 * Data class representing the token properties, including the user's name.
 *
 * @property userName The name of the user associated with the token.
 */
@Keep
data class TokenProperty(
    @SerializedName("userName")
    val userName: String? // Example: CUST7008656872
)
