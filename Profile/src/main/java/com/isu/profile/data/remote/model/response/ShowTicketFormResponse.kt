package com.isu.profile.data.remote.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class ShowTicketFormResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("status")
    val status: String?, // SUCCESS
    @SerializedName("statusCode")
    val statusCode: Int?, // 0
    @SerializedName("statusDesc")
    val statusDesc: String? // Ticket form fetched  successfully
) {
    @Keep

    data class Data(
        @SerializedName("active")
        val active: Boolean?, // true
        @SerializedName("end_user_conditions")
        val endUserConditions: List<Any?>?,
        @SerializedName("end_user_visible")
        val endUserVisible: Boolean?, // true
        @SerializedName("id")
        val id: Long?, // 31755579551257
        @SerializedName("name")
        val name: String?, // Raise a Ticket for Prepaid Card
        @SerializedName("ticket_fields")
        val ticketFields: List<TicketField?>?
    ) {
        @Keep

        data class TicketField(
            @SerializedName("active")
            val active: Boolean?, // true
            @SerializedName("custom_field_options")
            val customFieldOptions: List<CustomFieldOption?>?,
            @SerializedName("custom_statuses")
            val customStatuses: List<CustomStatuse?>?,
            @SerializedName("description")
            val description: String?, // Request status
            @SerializedName("editable_in_portal")
            val editableInPortal: Boolean?, // false
            @SerializedName("id")
            val id: Long?, // 900013326023
            @SerializedName("key")
            val key: Any?, // null
            @SerializedName("position")
            val position: Int?, // 3
            @SerializedName("regexp_for_validation")
            val regexpForValidation: String?, // \A[-+]?\d+\z
            @SerializedName("required")
            val required: Boolean?, // false
            @SerializedName("required_in_portal")
            val requiredInPortal: Boolean?, // false
            @SerializedName("sub_type_id")
            val subTypeId: Int?, // 0
            @SerializedName("title")
            val title: String?, // Status
            @SerializedName("type")
            val type: String?, // status
            @SerializedName("visible_in_portal")
            val visibleInPortal: Boolean? // false
        ) {
            @Keep

            data class CustomFieldOption(
                @SerializedName("default")
                val default: Boolean?, // false
                @SerializedName("id")
                val id: Long?, // 39654607181721
                @SerializedName("name")
                val name: String?, // Enquiry
                @SerializedName("raw_name")
                val rawName: String?, // Enquiry
                @SerializedName("value")
                val value: String? // enquiry
            )

            @Keep

            data class CustomStatuse(
                @SerializedName("active")
                val active: Boolean?, // true
                @SerializedName("agent_label")
                val agentLabel: String?, // New
                @SerializedName("created_at")
                val createdAt: String?, // 2021-10-07T11:56:27Z
                @SerializedName("default")
                val default: Boolean?, // true
                @SerializedName("description")
                val description: String?, // Ticket is awaiting assignment to an agent
                @SerializedName("end_user_description")
                val endUserDescription: String?, // We are working on a response for you
                @SerializedName("end_user_label")
                val endUserLabel: String?, // Open
                @SerializedName("id")
                val id: Int?, // 2140503
                @SerializedName("status_category")
                val statusCategory: String?, // new
                @SerializedName("updated_at")
                val updatedAt: String?, // 2021-10-07T11:56:27Z
                @SerializedName("url")
                val url: String? // https://iserveuhelp.zendesk.com/api/v2/custom_statuses/2140503.json
            )
        }
    }
}