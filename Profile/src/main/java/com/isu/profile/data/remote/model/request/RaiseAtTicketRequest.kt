package com.isu.profile.data.remote.model.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @author-karthik
 * Data class representing a request to raise a ticket.
 *
 * @property assignee The ID of the person or group assigned to the ticket.
 * @property attachmentURL The URL of any attachment related to the ticket.
 * @property body The main content or description of the ticket.
 * @property customFields A list of custom fields associated with the ticket.
 * @property priority The priority level of the ticket (e.g., low, medium, high).
 * @property subject The subject or title of the ticket.
 * @property ticketFormId The ID of the ticket form used.
 */
@Keep
data class RaiseAtTicketRequest(
    @SerializedName("assignee")
    val assignee: String?, // Example: PROG6370886162

    @SerializedName("attachmentURL")
    val attachmentURL: String?,

    @SerializedName("body")
    val body: String?, // Example: easrdftghjk

    @SerializedName("custom_fields")
    val customFields: List<RaiseAtTicketRequest.CustomField?>?,

    @SerializedName("priority")
    val priority: String?, // Example: low

    @SerializedName("subject")
    val subject: String?, // Example: hjhj

    @SerializedName("ticket_form_id")
    val ticketFormId: Long? = 31755579551257 // Example: 31755579551257
) {
    /**
     * Data class representing a custom field in the RaiseAtTicketRequest.
     *
     * @property id The ID of the custom field.
     * @property value The value associated with the custom field.
     */
    @Keep
    data class CustomField(
        @SerializedName("id")
        val id: Long?, // Example: 31812447425945

        @SerializedName("value")
        val value: String? // Example: transactional_related
    )
}
