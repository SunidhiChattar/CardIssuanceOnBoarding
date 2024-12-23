package com.isu.profile.data.remote.model.response

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @author-karthik
 * Response model for the Generate OTP request to change the password using the old password.
 *
 * @property message The response message indicating the result of the OTP generation.
 * @property roleId The ID of the role associated with the user.
 * @property roleName The name of the role associated with the user (nullable).
 * @property status The status of the request (e.g., SUCCESS).
 * @property statusCode The status code representing the result of the request.
 */
@Keep
data class GenerateOTPChangePasswordUsingOldPasswordResponse(
    @SerializedName("message")
    val message: String?, // Successful! A temporary OTP has been sent to your mobile number +91-XXX-XXX-XX65

    @SerializedName("roleId")
    val roleId: Int?, // 0

    @SerializedName("roleName")
    val roleName: Any?, // null

    @SerializedName("status")
    val status: String?, // SUCCESS

    @SerializedName("statusCode")
    val statusCode: Int? // 0
)
