package com.isu.profile.data.remote.model.response

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

/**
 * @author-karthik
 * Data class representing the response for adding a comment to a ticket.
 *
 * @property data The data object containing details about the comment and associated ticket.
 * @property status The status of the response (e.g., "SUCCESS").
 * @property statusCode The status code of the response (e.g., 0).
 * @property statusDesc A description of the status (e.g., "Comment created successfully").
 */
@Keep
data class AddCommentsResponse(
    @SerializedName("data")
    val data: Data?,

    @SerializedName("status")
    val status: String?, // Example: "SUCCESS"

    @SerializedName("statusCode")
    val statusCode: Int?, // Example: 0

    @SerializedName("statusDesc")
    val statusDesc: String? // Example: "Comment created successfully"
) {
    /**
     * Data class representing the details of the comment and associated ticket.
     *
     * @property audit The audit details of the comment.
     * @property ticket The details of the associated ticket.
     */
    @Keep
    data class Data(
        @SerializedName("audit")
        val audit: Audit?,

        @SerializedName("ticket")
        val ticket: Ticket?
    ) {
        /**
         * Data class representing the audit details of the comment.
         *
         * @property authorId The ID of the author of the comment.
         * @property createdAt The creation timestamp of the comment.
         * @property events The list of events associated with the comment.
         * @property id The ID of the audit.
         * @property metadata The metadata associated with the audit.
         * @property ticketId The ID of the ticket.
         * @property via The source/channel through which the comment was created.
         */
        @Keep
        data class Audit(
            @SerializedName("author_id")
            val authorId: Long?, // Example: 903170352406

            @SerializedName("created_at")
            val createdAt: String?, // Example: "2024-08-23T06:28:21Z"

            @SerializedName("events")
            val events: List<Event?>?,

            @SerializedName("id")
            val id: Long?, // Example: 36754422791321

            @SerializedName("metadata")
            val metadata: Metadata?,

            @SerializedName("ticket_id")
            val ticketId: Int?, // Example: 239907

            @SerializedName("via")
            val via: Via?
        ) {
            /**
             * Data class representing an event in the audit.
             *
             * @property attachments The list of attachments in the comment.
             * @property auditId The ID of the audit.
             * @property authorId The ID of the author of the comment.
             * @property body The body of the comment.
             * @property htmlBody The HTML formatted body of the comment.
             * @property id The ID of the event.
             * @property public Whether the comment is public.
             * @property recipients The list of recipients of the comment.
             * @property subject The subject of the comment.
             * @property type The type of event (e.g., "Comment").
             * @property via The source/channel through which the comment was created.
             */
            @Keep
            data class Event(
                @SerializedName("attachments")
                val attachments: List<Any?>?,

                @SerializedName("audit_id")
                val auditId: Long?, // Example: 36754422791321

                @SerializedName("author_id")
                val authorId: Long?, // Example: 36666159839641

                @SerializedName("body")
                val body: String?, // Example: "nmbmmnmnm,,,,,"

                @SerializedName("html_body")
                val htmlBody: String?, // Example: "<div class=\"zd-comment\" dir=\"auto\"><p dir=\"auto\">nmbmmnmnm,,,,,</p></div>"

                @SerializedName("id")
                val id: Long?, // Example: 36754422792473

                @SerializedName("plain_body")
                val plainBody: String?, // Example: "nmbmmnmnm,,,,,"

                @SerializedName("public")
                val `public`: Boolean?, // Example: true

                @SerializedName("recipients")
                val recipients: List<Long?>?,

                @SerializedName("subject")
                val subject: String?, // Example: "[{{ticket.account}}] Re: {{ticket.title}}"

                @SerializedName("type")
                val type: String?, // Example: "Comment"

                @SerializedName("via")
                val via: Via?
            ) {
                /**
                 * Data class representing the source/channel through which the event was created.
                 *
                 * @property channel The channel through which the event was created (e.g., "rule").
                 * @property source The source details of the event.
                 */
                @Keep
                data class Via(
                    @SerializedName("channel")
                    val channel: String?, // Example: "rule"

                    @SerializedName("source")
                    val source: Source?
                ) {
                    /**
                     * Data class representing the source details of the event.
                     *
                     * @property from The details of the sender.
                     * @property rel The relationship of the event (e.g., "trigger").
                     */
                    @Keep
                    data class Source(
                        @SerializedName("from")
                        val from: From?,

                        @SerializedName("rel")
                        val rel: String? // Example: "trigger"
                    ) {
                        /**
                         * Data class representing the sender's details.
                         *
                         * @property deleted Whether the sender is deleted.
                         * @property id The ID of the sender.
                         * @property title The title of the sender.
                         */
                        @Keep
                        data class From(
                            @SerializedName("deleted")
                            val deleted: Boolean?, // Example: false

                            @SerializedName("id")
                            val id: Long?, // Example: 900045792383

                            @SerializedName("title")
                            val title: String? // Example: "Notify requester and CCs of comment update"
                        )
                    }
                }
            }

            /**
             * Data class representing the metadata associated with the audit.
             *
             * @property custom The custom metadata associated with the audit.
             * @property system The system metadata associated with the audit.
             */
            @Keep
            data class Metadata(
                @SerializedName("custom")
                val custom: Custom?,

                @SerializedName("system")
                val system: System?
            ) {
                /**
                 * Empty class representing custom metadata.
                 */
                @Keep
                class Custom

                /**
                 * Data class representing system metadata.
                 *
                 * @property client The client used to create the event (e.g., "axios/1.7.0").
                 * @property ipAddress The IP address of the client.
                 * @property latitude The latitude of the client's location.
                 * @property location The location of the client.
                 * @property longitude The longitude of the client's location.
                 */
                @Keep
                data class System(
                    @SerializedName("client")
                    val client: String?, // Example: "axios/1.7.0"

                    @SerializedName("ip_address")
                    val ipAddress: String?, // Example: "34.100.136.45"

                    @SerializedName("latitude")
                    val latitude: Double?, // Example: 19.0748

                    @SerializedName("location")
                    val location: String?, // Example: "Mumbai, MH, India"

                    @SerializedName("longitude")
                    val longitude: Double? // Example: 72.8856
                )
            }

            /**
             * Data class representing the source/channel through which the audit was created.
             *
             * @property channel The channel through which the audit was created (e.g., "api").
             * @property source The source details of the audit.
             */
            @Keep
            data class Via(
                @SerializedName("channel")
                val channel: String?, // Example: "api"

                @SerializedName("source")
                val source: Source?
            ) {
                /**
                 * Data class representing the source details of the audit.
                 *
                 * @property from The sender's details.
                 * @property rel The relationship of the event (e.g., "null").
                 * @property to The recipient's details.
                 */
                @Keep
                data class Source(
                    @SerializedName("from")
                    val from: From?,

                    @SerializedName("rel")
                    val rel: Any?, // Example: null

                    @SerializedName("to")
                    val to: To?
                ) {
                    @Keep
                    class From

                    /**
                     * Data class representing the recipient's details.
                     *
                     * @property address The email address of the recipient.
                     * @property name The name of the recipient.
                     */
                    @Keep
                    data class To(
                        @SerializedName("address")
                        val address: String?, // Example: "7008656872@yopmail.com"

                        @SerializedName("name")
                        val name: String? // Example: "CUST7008656872"
                    )
                }
            }
        }

        /**
         * Data class representing the details of the associated ticket.
         *
         * @property allowAttachments Whether attachments are allowed on the ticket.
         * @property allowChannelback Whether channelback is allowed on the ticket.
         * @property assigneeId The ID of the assignee of the ticket.
         * @property brandId The brand ID associated with the ticket.
         * @property collaboratorIds The list of collaborator IDs on the ticket.
         * @property createdAt The creation timestamp of the ticket.
         * @property customFields The list of custom fields on the ticket.
         * @property description The description of the ticket.
         * @property dueAt The due date of the ticket (if any).
         * @property emailCcIds The list of email CC IDs on the ticket.
         * @property externalId The external ID of the ticket (if any).
         * @property fields The list of fields on the ticket.
         * @property followerIds The list of follower IDs on the ticket.
         * @property followupIds The list of follow-up IDs on the ticket.
         * @property forumTopicId The forum topic ID associated with the ticket (if any).
         * @property fromMessagingChannel Whether the ticket is from a messaging channel.
         * @property generatedTimestamp The generated timestamp of the ticket.
         * @property groupId The group ID associated with the ticket.
         * @property hasIncidents Whether the ticket has incidents.
         * @property id The ID of the ticket.
         * @property isPublic Whether the ticket is public.
         * @property organizationId The organization ID associated with the ticket (if any).
         * @property priority The priority of the ticket.
         * @property problemId The problem ID associated with the ticket (if any).
         * @property rawSubject The raw subject of the ticket.
         * @property recipient The recipient of the ticket (if any).
         * @property requesterId The ID of the requester of the ticket.
         * @property satisfactionRating The satisfaction rating of the ticket.
         * @property sharingAgreementIds The list of sharing agreement IDs on the ticket.
         * @property status The current status of the ticket.
         * @property subject The subject of the ticket.
         * @property submitterId The ID of the submitter of the ticket.
         * @property tags The list of tags associated with the ticket.
         * @property ticketFormId The ID of the ticket form used to create the ticket.
         * @property type The type of ticket (if any).
         * @property updatedAt The last update timestamp of the ticket.
         * @property url The URL of the ticket.
         * @property via The source/channel through which the ticket was created.
         */
        @Keep
        data class Ticket(
            @SerializedName("allow_attachments")
            val allowAttachments: Boolean?, // Example: true

            @SerializedName("allow_channelback")
            val allowChannelback: Boolean?, // Example: false

            @SerializedName("assignee_id")
            val assigneeId: Long?, // Example: 903170352406

            @SerializedName("brand_id")
            val brandId: Long?, // Example: 6089514090649

            @SerializedName("collaborator_ids")
            val collaboratorIds: List<Any?>?,

            @SerializedName("created_at")
            val createdAt: String?, // Example: "2024-08-22T04:54:20Z"

            @SerializedName("custom_fields")
            val customFields: List<CustomField?>?,

            @SerializedName("description")
            val description: String?, // Example: "easrdftghjk"

            @SerializedName("due_at")
            val dueAt: Any?, // Example: null

            @SerializedName("email_cc_ids")
            val emailCcIds: List<Any?>?,

            @SerializedName("external_id")
            val externalId: Any?, // Example: null

            @SerializedName("fields")
            val fields: List<Field?>?,

            @SerializedName("follower_ids")
            val followerIds: List<Any?>?,

            @SerializedName("followup_ids")
            val followupIds: List<Any?>?,

            @SerializedName("forum_topic_id")
            val forumTopicId: Any?, // Example: null

            @SerializedName("from_messaging_channel")
            val fromMessagingChannel: Boolean?, // Example: false

            @SerializedName("generated_timestamp")
            val generatedTimestamp: Int?, // Example: 1724393752

            @SerializedName("group_id")
            val groupId: Long?, // Example: 900007924403

            @SerializedName("has_incidents")
            val hasIncidents: Boolean?, // Example: false

            @SerializedName("id")
            val id: Int?, // Example: 239907

            @SerializedName("is_public")
            val isPublic: Boolean?, // Example: true

            @SerializedName("organization_id")
            val organizationId: Any?, // Example: null

            @SerializedName("priority")
            val priority: String?, // Example: "low"

            @SerializedName("problem_id")
            val problemId: Any?, // Example: null

            @SerializedName("raw_subject")
            val rawSubject: String?, // Example: "hjhj"

            @SerializedName("recipient")
            val recipient: Any?, // Example: null

            @SerializedName("requester_id")
            val requesterId: Long?, // Example: 36666159839641

            @SerializedName("satisfaction_rating")
            val satisfactionRating: SatisfactionRating?,

            @SerializedName("sharing_agreement_ids")
            val sharingAgreementIds: List<Any?>?,

            @SerializedName("status")
            val status: String?, // Example: "open"

            @SerializedName("subject")
            val subject: String?, // Example: "hjhj"

            @SerializedName("submitter_id")
            val submitterId: Long?, // Example: 36666159839641

            @SerializedName("tags")
            val tags: List<String?>?,

            @SerializedName("ticket_form_id")
            val ticketFormId: Long?, // Example: 31755579551257

            @SerializedName("type")
            val type: Any?, // Example: null

            @SerializedName("updated_at")
            val updatedAt: String?, // Example: "2024-08-23T06:28:21Z"

            @SerializedName("url")
            val url: String?, // Example: "https://iserveuhelp.zendesk.com/api/v2/tickets/239907.json"

            @SerializedName("via")
            val via: Via?
        ) {
            /**
             * Data class representing a custom field in the ticket.
             *
             * @property id The ID of the custom field.
             * @property value The value of the custom field.
             */
            @Keep
            data class CustomField(
                @SerializedName("id")
                val id: Long?, // Example: 900012614646

                @SerializedName("value")
                val value: String? // Example: "7008656872"
            )

            /**
             * Data class representing a field in the ticket.
             *
             * @property id The ID of the field.
             * @property value The value of the field.
             */
            @Keep
            data class Field(
                @SerializedName("id")
                val id: Long?, // Example: 900012614646

                @SerializedName("value")
                val value: String? // Example: "7008656872"
            )

            /**
             * Data class representing the satisfaction rating of the ticket.
             *
             * @property score The satisfaction score of the ticket (e.g., "unoffered").
             */
            @Keep
            data class SatisfactionRating(
                @SerializedName("score")
                val score: String? // Example: "unoffered"
            )

            /**
             * Data class representing the source/channel through which the ticket was created.
             *
             * @property channel The channel through which the ticket was created (e.g., "api").
             * @property source The source details of the ticket.
             */
            @Keep
            data class Via(
                @SerializedName("channel")
                val channel: String?, // Example: "api"

                @SerializedName("source")
                val source: Source?
            ) {
                /**
                 * Data class representing the source details of the ticket.
                 *
                 * @property from The sender's details.
                 * @property rel The relationship of the event (e.g., "null").
                 * @property to The recipient's details.
                 */
                @Keep
                data class Source(
                    @SerializedName("from")
                    val from: From?,

                    @SerializedName("rel")
                    val rel: Any?, // Example: null

                    @SerializedName("to")
                    val to: To?
                ) {
                    @Keep
                    class From

                    /**
                     * Data class representing the recipient's details.
                     *
                     * @property address The email address of the recipient.
                     * @property name The name of the recipient.
                     */
                    @Keep
                    class To(
                        @SerializedName("address")
                        val address: String?, // Example: "7008656872@yopmail.com"

                        @SerializedName("name")
                        val name: String? // Example: "CUST7008656872"
                    )
                }
            }
        }
    }
}
