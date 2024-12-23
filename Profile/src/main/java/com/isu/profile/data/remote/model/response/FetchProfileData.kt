package com.isu.profile.data.remote.model.response

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @author-karthi
 * Data class representing the response structure for fetching profile data.
 *
 * @property data The decrypted profile data.
 * @property status The status of the request (e.g., "SUCCESS").
 * @property statusCode The status code (e.g., 0 for success).
 * @property statusDesc The description of the status (e.g., "Successfully verified the user for view").
 */
@Keep
data class FetchProfileData(
    @SerializedName("data")
    val `data`: FetchProfileDecryptedData?,

    @SerializedName("status")
    val status: String?, // SUCCESS

    @SerializedName("statusCode")
    val statusCode: Int?, // 0

    @SerializedName("statusDesc")
    val statusDesc: String? // Successfully verified the user for view
) {

    /**
     * Data class representing the decrypted profile data.
     *
     * @property email The email address associated with the profile.
     * @property kycType The type of KYC (Know Your Customer).
     * @property mobileNumber The mobile number associated with the profile.
     * @property name The name of the user.
     * @property userId The user ID associated with the profile.
     * @property userName The username associated with the profile.
     */
    @Keep
    data class FetchProfileDecryptedData(
        @SerializedName("email")
        val email: String?, // 7008656872@yopmail.com

        @SerializedName("kycType")
        val kycType: String?, // MIN-KYC

        @SerializedName("mobileNumber")
        val mobileNumber: String?, // 7008656872

        @SerializedName("name")
        val name: String?, // Test Customer

        @SerializedName("userId")
        val userId: String?, // 4444450000030768

        @SerializedName("userName")
        val userName: String? // CUST7008656872
    )
}
