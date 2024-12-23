package com.isu.profile.data.remote.model.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @author- karthik
 * Data class representing a request to change a password using the old password.
 *
 * @property newPassword The new password to be set.
 * @property oldPassword The current/old password.
 * @property otp The One-Time Password (OTP) for verification.
 */
@Keep
data class ChangePasswordUsingOldPasswordRequest(
    @SerializedName("newPassword")
    val newPassword: String?, // Example: Itpl@8895

    @SerializedName("oldPassword")
    val oldPassword: String?, // Example: Q82@hit0M

    @SerializedName("otp")
    val otp: String? // Example: 111111
)
