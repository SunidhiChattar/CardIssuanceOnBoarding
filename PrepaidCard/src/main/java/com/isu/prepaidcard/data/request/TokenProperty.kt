package com.isu.prepaidcard.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * This data class represents token properties, specifically the user name.
 *
 * @param userName The user name associated with the token.
 */
@Keep
data class TokenProperty(
    @SerializedName("userName")
    val userName: String?
)