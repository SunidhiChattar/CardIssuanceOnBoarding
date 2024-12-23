package com.isu.profile.data.remote.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ChangePasswordUsingOldPasswordResponse(
    @SerializedName("message")
    val message: String?, // Your Password has been successfully changed! New Password has been sent to your mobile number +91-XXX-XXX-XX72
    @SerializedName("roleId")
    val roleId: Int?, // 0
    @SerializedName("roleName")
    val roleName: Any?, // null
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
)