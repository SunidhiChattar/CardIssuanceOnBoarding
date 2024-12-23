package com.isu.profile.data.remote.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails.MessageDetails
import com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails.MessageType


/**
 * @author-karthik
 * Show ticket comment response
 *
 * @property data
 * @property status
 * @property statusCode
 * @property statusDesc
 * @constructor Create empty Show ticket comment response
 */
@Keep
data class ShowTicketCommentResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String? // Comments fetched successfully
) {
    /**
     * Data
     *
     * @property attachments
     * @property body
     * @property createdAt
     * @property username
     * @constructor Create empty Data
     */
    @Keep
    data class Data(
        @SerializedName("attachments")
        val attachments: List<Any?>?,
        @SerializedName("body")
        val body: String?, // easrdftghjk
        @SerializedName("created_at")
        val createdAt: String?, // 2024-08-22T04:54:20Z
        @SerializedName("username")
        val username: String? // CUST7008656872
    )
}

/**
 * To comments
 * this is a mapper function to convert responses  to comments to domain based classes
 * @return
 */
fun ShowTicketCommentResponse.toComments(): List<MessageDetails> {
    return data?.map {
        MessageDetails(
            name = it?.username?:"",
            messageType = if (it?.username?.contains("CUST".toRegex()) == true) MessageType.User else MessageType.CustomerSupport,
            message = it?.body.toString(),
            time = it?.createdAt.toString()

        )
    }?: emptyList()
}