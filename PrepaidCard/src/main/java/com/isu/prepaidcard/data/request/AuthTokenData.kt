package com.isu.prepaidcard.data.request

/**
 * This data class holds authentication token information and an optional request object.
 *
 * @param authorization The authentication token.
 * @param tokenProperties The token properties.
 * @param request The optional request object associated with the authentication token.
 * @param T The type of the request object.
 */
data class AuthTokenData<T>(
    val authorization: String,
    val tokenProperties:String,
    val request:T?=null
)