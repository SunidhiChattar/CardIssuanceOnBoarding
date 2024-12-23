package com.isu.profile.data.remote.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author -karthik
 * Raise a ticket response
 *
 * @property data
 * @property status
 * @property statusCode
 * @property statusDesc
 * @constructor Create empty Raise a ticket response
 */
@Keep
data class RaiseATicketResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String? // Ticket created succesfully
) {
    /**
     * Data
     *
     * @property audit
     * @property ticket
     * @constructor Create empty Data
     */
    @Keep
    data class Data(
        @SerializedName("audit")
        val audit: Audit?,
        @SerializedName("ticket")
        val ticket: Ticket?
    ) {
        /**
         * Audit
         *
         * @property authorId
         * @property createdAt
         * @property events
         * @property id
         * @property metadata
         * @property ticketId
         * @property via
         * @constructor Create empty Audit
         */
        @Keep
        data class Audit(
            @SerializedName("author_id")
            val authorId: Long?, // 903170352406
            @SerializedName("created_at")
            val createdAt: String?, // 2024-08-22T04:54:20Z
            @SerializedName("events")
            val events: List<Any?>?,
            @SerializedName("id")
            val id: Long?, // 36700993843225
            @SerializedName("metadata")
            val metadata: Metadata?,
            @SerializedName("ticket_id")
            val ticketId: Int?, // 239907
            @SerializedName("via")
            val via: Via?,
        ) {
            /**
             * Event
             *
             * @property attachments
             * @property auditId
             * @property authorId
             * @property body
             * @property fieldName
             * @property htmlBody
             * @property id
             * @property plainBody
             * @property public
             * @property recipients
             * @property subject
             * @property type
             * @property value
             * @property via
             * @constructor Create empty Event
             */
            @Keep
            data class Event(
                @SerializedName("attachments")
                val attachments: List<Any?>?,
                @SerializedName("audit_id")
                val auditId: Long?, // 36700993843225
                @SerializedName("author_id")
                val authorId: Long?, // 36666159839641
                @SerializedName("body")
                val body: String?, // easrdftghjk
                @SerializedName("field_name")
                val fieldName: String?, // priority
                @SerializedName("html_body")
                val htmlBody: String?, // <div class="zd-comment" dir="auto"><p dir="auto">easrdftghjk</p></div>
                @SerializedName("id")
                val id: Long?, // 36700993843353
                @SerializedName("plain_body")
                val plainBody: String?, // easrdftghjk
                @SerializedName("public")
                val `public`: Boolean?, // true
                @SerializedName("recipients")
                val recipients: List<Long?>?,
                @SerializedName("subject")
                val subject: String?, // {{ticket.title}}
                @SerializedName("type")
                val type: String?, // Comment
                @SerializedName("value")
                val value: String?, // low
                @SerializedName("via")
                val via: Via?
            ) {
                @Keep
                data class Via(
                    @SerializedName("channel")
                    val channel: String?, // rule
                    @SerializedName("source")
                    val source: Source?
                ) {
                    @Keep
                    data class Source(
                        @SerializedName("from")
                        val from: From?,
                        @SerializedName("rel")
                        val rel: String? // trigger
                    ) {
                        @Keep
                        data class From(
                            @SerializedName("deleted")
                            val deleted: Boolean?, // false
                            @SerializedName("id")
                            val id: Long?, // 900045792363
                            @SerializedName("title")
                            val title: String? // Notify requester of new proactive ticket
                        )
                    }
                }
            }

            @Keep
            data class Metadata(
                @SerializedName("custom")
                val custom: Custom?,
                @SerializedName("system")
                val system: System?
            ) {
                @Keep
                class Custom

                @Keep
                data class System(
                    @SerializedName("client")
                    val client: String?, // axios/1.7.0
                    @SerializedName("ip_address")
                    val ipAddress: String?, // 34.100.136.45
                    @SerializedName("latitude")
                    val latitude: Double?, // 19.0748
                    @SerializedName("location")
                    val location: String?, // Mumbai, MH, India
                    @SerializedName("longitude")
                    val longitude: Double? // 72.8856
                )
            }

            @Keep
            data class Via(
                @SerializedName("channel")
                val channel: String?, // api
                @SerializedName("source")
                val source: Source?
            ) {
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
                    data class To(
                        @SerializedName("address")
                        val address: String?, // 7008656872@yopmail.com
                        @SerializedName("name")
                        val name: String? // CUST7008656872
                    )
                }
            }
        }

        /**
         * Ticket
         *
         * @property allowAttachments
         * @property allowChannelback
         * @property assigneeId
         * @property brandId
         * @property collaboratorIds
         * @property createdAt
         * @property customFields
         * @property description
         * @property dueAt
         * @property emailCcIds
         * @property externalId
         * @property fields
         * @property followerIds
         * @property followupIds
         * @property forumTopicId
         * @property fromMessagingChannel
         * @property generatedTimestamp
         * @property groupId
         * @property hasIncidents
         * @property id
         * @property isPublic
         * @property organizationId
         * @property priority
         * @property problemId
         * @property rawSubject
         * @property recipient
         * @property requesterId
         * @property satisfactionRating
         * @property sharingAgreementIds
         * @property status
         * @property subject
         * @property submitterId
         * @property tags
         * @property ticketFormId
         * @property type
         * @property updatedAt
         * @property url
         * @property via
         * @constructor Create empty Ticket
         */

        @Keep
        data class Ticket(
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
            val createdAt: String?, // 2024-08-22T04:54:20Z
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
            val generatedTimestamp: Int?, // 0
            @SerializedName("group_id")
            val groupId: Any?, // null
            @SerializedName("has_incidents")
            val hasIncidents: Boolean?, // false
            @SerializedName("id")
            val id: Int?, // 239907
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
            val updatedAt: String?, // 2024-08-22T04:54:20Z
            @SerializedName("url")
            val url: String?, // https://iserveuhelp.zendesk.com/api/v2/tickets/239907.json
            @SerializedName("via")
            val via: Via?
        ) {
            @Keep
            data class CustomField(
                @SerializedName("id")
                val id: Long?, // 900012614646
                @SerializedName("value")
                val value: Boolean? // false
            )

            @Keep
            data class Field(
                @SerializedName("id")
                val id: Long?, // 900012614646
                @SerializedName("value")
                val value: Boolean? // false
            )

            @Keep
            data class SatisfactionRating(
                @SerializedName("score")
                val score: String? // unoffered
            )

            @Keep
            data class Via(
                @SerializedName("channel")
                val channel: String?, // api
                @SerializedName("source")
                val source: Source?
            ) {
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
}