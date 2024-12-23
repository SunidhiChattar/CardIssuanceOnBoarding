package com.isu.authentication.data.remote.dto.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String?, // eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyZWRpcmVjdFVyaSI6Imh0dHA6Ly9sb2NhbGhvc3Q6NDU2OC8jL3YxL2Rhc2hib2FyZC9hbmFseXRpY3MiLCJiYW5rQ29kZSI6ImZpbm8iLCJwcml2aWxlZ2VzIjpbIjUwMCIsIjUwNCIsIjE5IiwiMTgiLCIxODAiLCIxODEiXSwiaXMyRkFFbmFibGVkIjp0cnVlLCJ1c2VyX25hbWUiOiJTV0FHQVRfUjE4IiwibW9iaWxlTnVtYmVyIjo5MzQ4MjAwMjcxLCJjcmVhdGVkIjoxNzI4MzY5MjU1OTMxLCJpc0Jpb0F1dGhSZXF1aXJlZCI6ZmFsc2UsInBhcmVudFVzZXJOYW1lIjoiZmlub19hZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfUkVUQUlMRVIiXSwiY2xpZW50X2lkIjoiaXN1LWZpbm8tY2xpZW50IiwiYWRtaW5OYW1lIjoiZmlub19hZG1pbiIsImlzUGFzc3dvcmRSZXNldFJlcXVpcmVkIjp0cnVlLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNzI4MzcxMDU1LCJqdGkiOiJ2dEk1NlQyamJaWnhmaXVBR2hIcTA4dUc2U00ifQ.Aqr1p28FliiutgB5ntL8E2ROz8dC0e3F9GPYFDSwg125Cr458i3ZZIYpzXHRgvaJlKYqPlV1VsxHOGZTwS3tjXV-wOZUyi_EkCvcneBoNmH7y7PBz-4FXxSmPJ8d61saZ3qNj8r17sZah8flbXkG8hLDX8Pk9xwXEcSdFzVby97nHOUPHfeDgYyObh55SgmQmuFXfuIfAZLIF5vqU-bNPHMKdU3EWfCtRpj2orjkP98h51okwC_UCCvuoVWEo2FOO1fkMixn1FF-NCHwEhnepE3-LYHtk03Tzez2XLukGxg9WwSDlSlVDYC4YCjlQNgbdXp_Q1AajZJpsZwTi7XAJw
    @SerializedName("adminName")
    val adminName: String?, // fino_admin
    @SerializedName("bankCode")
    val bankCode: String?, // fino
    @SerializedName("created")
    val created: Long?, // 1728369255931
    @SerializedName("expires_in")
    val expiresIn: Int?, // 1799
    @SerializedName("is2FAEnabled")
    val is2FAEnabled: Boolean?, // true
    @SerializedName("isBioAuthRequired")
    val isBioAuthRequired: Boolean?, // false
    @SerializedName("isPasswordResetRequired")
    val isPasswordResetRequired: Boolean?, // true
    @SerializedName("jti")
    val jti: String?, // vtI56T2jbZZxfiuAGhHq08uG6SM
    @SerializedName("mobileNumber")
    val mobileNumber: Long?, // 9348200271
    @SerializedName("parentUserName")
    val parentUserName: String?, // fino_admin
    @SerializedName("privileges")
    val privileges: List<String?>?,
    @SerializedName("redirectUri")
    val redirectUri: String?, // http://localhost:4568/#/v1/dashboard/analytics
    @SerializedName("refresh_token")
    val refreshToken: String?, // eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyZWRpcmVjdFVyaSI6Imh0dHA6Ly9sb2NhbGhvc3Q6NDU2OC8jL3YxL2Rhc2hib2FyZC9hbmFseXRpY3MiLCJiYW5rQ29kZSI6ImZpbm8iLCJwcml2aWxlZ2VzIjpbIjUwMCIsIjUwNCIsIjE5IiwiMTgiLCIxODAiLCIxODEiXSwiaXMyRkFFbmFibGVkIjp0cnVlLCJ1c2VyX25hbWUiOiJTV0FHQVRfUjE4IiwibW9iaWxlTnVtYmVyIjo5MzQ4MjAwMjcxLCJjcmVhdGVkIjoxNzI4MzY5MjU1OTMxLCJpc0Jpb0F1dGhSZXF1aXJlZCI6ZmFsc2UsInBhcmVudFVzZXJOYW1lIjoiZmlub19hZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfUkVUQUlMRVIiXSwiY2xpZW50X2lkIjoiaXN1LWZpbm8tY2xpZW50IiwiYWRtaW5OYW1lIjoiZmlub19hZG1pbiIsImlzUGFzc3dvcmRSZXNldFJlcXVpcmVkIjp0cnVlLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoidnRJNTZUMmpiWlp4Zml1QUdoSHEwOHVHNlNNIiwiZXhwIjoxNzI4MzcxMzU1LCJqdGkiOiI1SUtod2FCa3RHTW4wRUM2UW9NNHhIU3R4M0UifQ.BFTLkN3ULdotqP4jSv9GUW4aPycJyBh38lM_B6WraG_uY6auJPhxNrsErWNEFpjyGsyJAosuXzujigpGCpslPibuENAdsCpma_LvYdh4UEyE3ZXyIThN-693t4QEcIEWF1oaq4GP46fyTvOUKwaMl7NxxR4dbDxHqxSVVi9Qh51jYMl_vg3IiMZ6J8tf1iFLW0fIPDLKQiOJCSADEWBu-dk55MHwhfNRrT1wIP9-XYG-T5YXx7IrUCKbmMTKqY5pNWJW7xhWDE2EmG1U2fZNI1DI_JsR5uHTIXfceQqHJX5A08YZ5b6AxkbC9qAwCthWGljqCmSNhf9bVsXpuBQitQ
    @SerializedName("scope")
    val scope: String?, // read write
    @SerializedName("token_type")
    val tokenType: String?, // bearer
)