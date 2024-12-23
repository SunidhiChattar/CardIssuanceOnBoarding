package com.isu.profile.data.remote.model.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @author-karthik
 * Data class representing a request to update the status of a ticket.
 *
 * @property status The new status of the ticket (e.g., "solved").
 * @property ticketId The unique identifier of the ticket to be updated.
 */
@Keep
data class UpdateTicketStatusRequest(
    @SerializedName("status")
    val status: String?, // Example: "solved"

    @SerializedName("ticket_id")
    val ticketId: Int? // Example: 239907
)
