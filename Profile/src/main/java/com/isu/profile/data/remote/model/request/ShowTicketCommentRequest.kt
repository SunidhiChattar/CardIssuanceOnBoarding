package com.isu.profile.data.remote.model.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @author-karthik
 * Data class representing a request to show comments for a specific ticket.
 *
 * @property ticketId The ID of the ticket for which comments are being requested.
 */
@Keep
data class ShowTicketCommentRequest(
    @SerializedName("ticket_id")
    val ticketId: Int? // Example: 239907
)
