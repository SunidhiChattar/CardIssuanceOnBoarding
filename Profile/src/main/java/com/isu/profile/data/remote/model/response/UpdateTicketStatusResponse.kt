package com.isu.profile.data.remote.model.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UpdateTicketStatusResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String? // Updated status succesfully!!
) {
    @Keep
    data class Data(
        @SerializedName("audit")
        val audit: Audit?,
        @SerializedName("ticket")
        val ticket: Ticket?
    ) {
        @Keep
        data class Audit(
            @SerializedName("author_id")
            val authorId: Long?, // 903170352406
            @SerializedName("created_at")
            val createdAt: String?, // 2024-08-22T06:50:45Z
            @SerializedName("events")
            val events: List<Event?>?,
            @SerializedName("id")
            val id: Long?, // 36706811098649
            @SerializedName("metadata")
            val metadata: Metadata?,
            @SerializedName("ticket_id")
            val ticketId: Int?, // 239907
            @SerializedName("via")
            val via: Via?
        ) {
            @Keep
            data class Event(
                @SerializedName("field_name")
                val fieldName: String?, // assignee_id
                @SerializedName("id")
                val id: Long?, // 36706811098777
                @SerializedName("previous_value")
                val previousValue: String?, // open
                @SerializedName("type")
                val type: String?, // Change
                @SerializedName("value")
                val value: String?, // 903170352406
                @SerializedName("via")
                val via: Via?
            ) {
                @Keep
                data class Via(
                    @SerializedName("channel")
                    val channel: String?, // system
                    @SerializedName("source")
                    val source: Source?
                ) {
                    @Keep
                    data class Source(
                        @SerializedName("from")
                        val from: From?,
                        @SerializedName("rel")
                        val rel: String?, // admin_setting
                        @SerializedName("to")
                        val to: To?
                    ) {
                        @Keep
                        data class From(
                            @SerializedName("deleted")
                            val deleted: Boolean?, // false
                            @SerializedName("id")
                            val id: Long?, // 900085693323
                            @SerializedName("title")
                            val title: String? // Assign Tickets Upon Solve
                        )

                        @Keep
                        class To
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
                    class To
                }
            }
        }

        @Keep
        data class Ticket(
            @SerializedName("allow_attachments")
            val allowAttachments: Boolean?, // true
            @SerializedName("allow_channelback")
            val allowChannelback: Boolean?, // false
            @SerializedName("assignee_id")
            val assigneeId: Long?, // 903170352406
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
            val generatedTimestamp: Int?, // 1724302568
            @SerializedName("group_id")
            val groupId: Long?, // 900007924403
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
            val status: String?, // solved
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
            val updatedAt: String?, // 2024-08-22T06:50:45Z
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
                val value: String? // 7008656872
            )

            @Keep
            data class Field(
                @SerializedName("id")
                val id: Long?, // 900012614646
                @SerializedName("value")
                val value: String? // 7008656872
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