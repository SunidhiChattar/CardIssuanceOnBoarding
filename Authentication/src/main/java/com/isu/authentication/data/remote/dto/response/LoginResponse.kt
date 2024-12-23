package  data.remote.dto.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response for a login request.
 *
 * @property accessToken The access token for the session.
 * @property adminName The name of the admin user.
 * @property bankCode The bank code associated with the user.
 * @property created The timestamp when the token was created.
 * @property expiresIn The expiration time of the token in seconds.
 * @property is2FAEnabled Indicates if two-factor authentication is enabled.
 * @property isBioAuthRequired Indicates if biometric authentication is required.
 * @property isPasswordResetRequired Indicates if a password reset is required.
 * @property jti The JWT ID.
 * @property mobileNumber The mobile number associated with the user.
 * @property privileges A list of user privileges.
 * @property redirectUri The URI to redirect after login.
 * @property refreshToken The refresh token for the session.
 * @property scope The scope of the token.
 * @property tokenType The type of token (e.g., bearer).
 */
@Keep
data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String?, // eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...

    @SerializedName("adminName")
    val adminName: String?, // fino_admin

    @SerializedName("bankCode")
    val bankCode: String?, // fino

    @SerializedName("created")
    val created: String?, // 2024-05-27T11:58:48.508+00:00

    @SerializedName("expires_in")
    val expiresIn: Int?, // 1798

    @SerializedName("is2FAEnabled")
    val is2FAEnabled: Boolean?, // false

    @SerializedName("isBioAuthRequired")
    val isBioAuthRequired: Boolean?, // false

    @SerializedName("isPasswordResetRequired")
    val isPasswordResetRequired: Boolean?, // false

    @SerializedName("jti")
    val jti: String?, // cyr--tL51YSTO8FXWVPqyxZOqDI

    @SerializedName("mobileNumber")
    val mobileNumber: Long?, // 8456034894

    @SerializedName("privileges")
    val privileges: List<String?>?,

    @SerializedName("redirectUri")
    val redirectUri: String?, // http://localhost:4568/#/v1/dashboard/analytics

    @SerializedName("refresh_token")
    val refreshToken: String?, // eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...

    @SerializedName("scope")
    val scope: String?, // read write

    @SerializedName("token_type")
    val tokenType: String? // bearer
)
