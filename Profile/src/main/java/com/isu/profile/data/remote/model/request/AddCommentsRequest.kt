package com.isu.profile.data.remote.model.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @author- karthik
 * Data class representing the request body for adding comments to a ticket.
 *
 * @property attachmentURL URL of the attachment, if any.
 * @property body The body of the comment.
 * @property ticketId The ID of the ticket to which the comment is being added.
 */
@Keep
data class AddCommentsRequest(
    @SerializedName("attachmentURL")
    val attachmentURL: Any?, // null

    @SerializedName("body")
    val body: String?, // nmbmmnmnm,,,,,

    @SerializedName("ticket_id")
    val ticketId: Int? // 239907
)
