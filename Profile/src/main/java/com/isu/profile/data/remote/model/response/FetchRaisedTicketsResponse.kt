package com.isu.profile.data.remote.model.response

import android.util.Log
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.isu.common.customcomposables.TicketData
import com.isu.common.customcomposables.TicketPriority
import com.isu.common.customcomposables.TicketStatus

/**
 * @author-karthik
 * Data class representing the response structure for fetching raised tickets.
 *
 * @property data The list of raised ticket data.
 * @property status The status of the request (e.g., "SUCCESS").
 * @property statusCode The status code (e.g., 0 for success).
 * @property statusDesc The description of the status (e.g., "Tickets fetched successfully").
 */
@Keep
data class FetchRaisedTicketsResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,

    @SerializedName("status")
    val status: String?, // SUCCESS

    @SerializedName("statusCode")
    val statusCode: Int?, // 0

    @SerializedName("statusDesc")
    val statusDesc: String? // Tickets fetched successfully
) {

    /**
     * Data class representing a raised ticket.
     *
     * @property allowAttachments Indicates if attachments are allowed.
     * @property allowChannelback Indicates if channel back is allowed.
     * @property assigneeId The ID of the assignee.
     * @property brandId The ID of the brand.
     * @property collaboratorIds The list of collaborator IDs.
     * @property createdAt The timestamp when the ticket was created.
     * @property customFields The list of custom fields associated with the ticket.
     * @property description The description of the ticket.
     * @property dueAt The due date of the ticket.
     * @property emailCcIds The list of email CC IDs.
     * @property externalId The external ID of the ticket.
     * @property fields The list of fields associated with the ticket.
     * @property followerIds The list of follower IDs.
     * @property followupIds The list of follow-up IDs.
     * @property forumTopicId The ID of the forum topic associated with the ticket.
     * @property fromMessagingChannel Indicates if the ticket is from a messaging channel.
     * @property generatedTimestamp The timestamp when the ticket was generated.
     * @property groupId The ID of the group associated with the ticket.
     * @property hasIncidents Indicates if the ticket has incidents.
     * @property id The ID of the ticket.
     * @property isPublic Indicates if the ticket is public.
     * @property organizationId The ID of the organization associated with the ticket.
     * @property priority The priority of the ticket.
     * @property problemId The ID of the problem associated with the ticket.
     * @property rawSubject The raw subject of the ticket.
     * @property recipient The recipient of the ticket.
     * @property requesterId The ID of the requester.
     * @property resultType The type of the result (e.g., "ticket").
     * @property satisfactionRating The satisfaction rating of the ticket.
     * @property sharingAgreementIds The list of sharing agreement IDs.
     * @property status The status of the ticket.
     * @property subject The subject of the ticket.
     * @property submitterId The ID of the submitter.
     * @property tags The list of tags associated with the ticket.
     * @property ticketFormId The ID of the ticket form.
     * @property type The type of the ticket.
     * @property updatedAt The timestamp when the ticket was last updated.
     * @property url The URL of the ticket.
     * @property via The channel information of the ticket.
     */
    @Keep
    data class Data(
        @SerializedName("allow_attachments")
        val allowAttachments: Boolean?, // true

        @SerializedName("allow_channelback")
        val allowChannelback: Boolean?, // false

        @SerializedName("assignee_id")
        val assigneeId: Any?, // null

        @SerializedName("brand_id")
        val brandId: Long?, // 6089514090649

        @SerializedName("collaborator_ids")
        val collaboratorIds: List<Any?>?,

        @SerializedName("created_at")
        val createdAt: String?, // 2024-08-21T09:53:04Z

        @SerializedName("custom_fields")
        val customFields: List<CustomField?>?,

        @SerializedName("description")
        val description: String?, // easrdftghjk

        @SerializedName("due_at")
        val dueAt: Any?, // null

        @SerializedName("email_cc_ids")
        val emailCcIds: List<Any?>?,

        @SerializedName("external_id")
        val externalId: Any?, // null

        @SerializedName("fields")
        val fields: List<Field?>?,

        @SerializedName("follower_ids")
        val followerIds: List<Any?>?,

        @SerializedName("followup_ids")
        val followupIds: List<Any?>?,

        @SerializedName("forum_topic_id")
        val forumTopicId: Any?, // null

        @SerializedName("from_messaging_channel")
        val fromMessagingChannel: Boolean?, // false

        @SerializedName("generated_timestamp")
        val generatedTimestamp: Int?, // 1724233984

        @SerializedName("group_id")
        val groupId: Any?, // null

        @SerializedName("has_incidents")
        val hasIncidents: Boolean?, // false

        @SerializedName("id")
        val id: Int?, // 239599

        @SerializedName("is_public")
        val isPublic: Boolean?, // true

        @SerializedName("organization_id")
        val organizationId: Any?, // null

        @SerializedName("priority")
        val priority: String?, // low

        @SerializedName("problem_id")
        val problemId: Any?, // null

        @SerializedName("raw_subject")
        val rawSubject: String?, // hjhj

        @SerializedName("recipient")
        val recipient: Any?, // null

        @SerializedName("requester_id")
        val requesterId: Long?, // 36666159839641

        @SerializedName("result_type")
        val resultType: String?, // ticket

        @SerializedName("satisfaction_rating")
        val satisfactionRating: SatisfactionRating?,

        @SerializedName("sharing_agreement_ids")
        val sharingAgreementIds: List<Any?>?,

        @SerializedName("status")
        val status: String?, // open

        @SerializedName("subject")
        val subject: String?, // hjhj

        @SerializedName("submitter_id")
        val submitterId: Long?, // 36666159839641

        @SerializedName("tags")
        val tags: List<String?>?,

        @SerializedName("ticket_form_id")
        val ticketFormId: Long?, // 31755579551257

        @SerializedName("type")
        val type: Any?, // null

        @SerializedName("updated_at")
        val updatedAt: String?, // 2024-08-21T09:53:04Z

        @SerializedName("url")
        val url: String?, // https://iserveuhelp.zendesk.com/api/v2/tickets/239599.json

        @SerializedName("via")
        val via: Via?
    ) {

        /**
         * Data class representing a custom field in a ticket.
         *
         * @property id The ID of the custom field.
         * @property value The value of the custom field.
         */
        @Keep
        data class CustomField(
            @SerializedName("id")
            val id: Long?, // 900012614646

            @SerializedName("value")
            val value: String?, // false
        )

        /**
         * Data class representing a field in a ticket.
         *
         * @property id The ID of the field.
         * @property value The value of the field.
         */
        @Keep
        data class Field(
            @SerializedName("id")
            val id: Long?, // 900012614646

            @SerializedName("value")
            val value: String?, // false
        )

        /**
         * Data class representing the satisfaction rating of a ticket.
         *
         * @property score The score of the satisfaction rating.
         */
        @Keep
        data class SatisfactionRating(
            @SerializedName("score")
            val score: String? // unoffered
        )

        /**
         * Data class representing the channel information of a ticket.
         *
         * @property channel The channel through which the ticket was submitted.
         * @property source The source of the ticket.
         */
        @Keep
        data class Via(
            @SerializedName("channel")
            val channel: String?, // api

            @SerializedName("source")
            val source: Source?
        ) {

            /**
             * Data class representing the source information of a ticket.
             *
             * @property from The origin of the ticket.
             * @property rel The relationship information.
             * @property to The destination of the ticket.
             */
            @Keep
            data class Source(
                @SerializedName("from")
                val from: From?,

                @SerializedName("rel")
                val rel: Any?, // null

                @SerializedName("to")
                val to: To?
            ) {
                @Keep
                class From

                @Keep
                class To
            }
        }
    }
}

/**
 * Extension function to convert a FetchRaisedTicketsResponse to a list of TicketData.
 *
 * @return A list of TicketData created from the response.
 */
fun FetchRaisedTicketsResponse.toTicketData(): List<TicketData>? {

    return data?.map {
        val fields = it?.customFields?.filter { field -> field?.id == 31812447425945 }
        Log.d("KMap", "toTicketData:${fields} ")
        val category = if (fields.isNullOrEmpty()) "" else fields[0]?.value.toString()
        TicketData(
            ticketId = it?.id.toString(),
            requestTitle = it?.subject.toString(),
            description = it?.description.toString(),
            category = category,
            status = when (it?.status?.lowercase()) {
                "open" -> TicketStatus.Open()
                "solved" -> TicketStatus.Closed()
                else -> TicketStatus.Closed()
            },
            priority = when (it?.priority?.lowercase()) {
                "low" -> TicketPriority.Low()
                "medium" -> TicketPriority.Medium()
                else -> TicketPriority.High()
            },
            date = it?.createdAt ?: ""
        )
    }
}
