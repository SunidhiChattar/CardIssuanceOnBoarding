package com.isu.profile.data.remote.model.request

/**
 * @author- karthik
 * Data class representing the authorization token data and associated request.
 *
 * @param T The type of the request data.
 * @property authorization The authorization token string.
 * @property tokenProperties Additional properties related to the token.
 * @property request The request data associated with the token, if any.
 */
data class AuthTokenData<T>(
    val authorization: String,//token for authorization passed as a header
    val tokenProperties: String,//class containing username property passed as a header
    val request: T? = null//request body for thr api call
)
