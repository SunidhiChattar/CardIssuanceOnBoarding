package com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails

import com.isu.common.customcomposables.TicketPriority
import com.isu.common.customcomposables.TicketStatus

/**
 * Data class representing the details of a ticket.
 *
 * @property ticketId The unique identifier of the ticket.
 * @property requestTitle The title of the support request.
 * @property category The category of the ticket.
 * @property description A description of the issue or request.
 * @property priority The priority level of the ticket.
 * @property status The current status of the ticket.
 * @property comments A list of messages associated with the ticket.
 * @property userComment The user's comment or message.
 * @property showLoader Flag indicating whether a loading indicator should be displayed.
 */
data class TicketDetailUIData(
    /**
     * The unique identifier of the ticket.
     */
    val ticketId: String = "",

    /**
     * The title of the support request.
     */
    val requestTitle: String = "",

    /**
     * The category of the ticket.
     */
    val category: String = "",

    /**
     * A description of the issue or request.
     */
    val description: String = "",

    /**
     * The priority level of the ticket.
     */
    val priority: TicketPriority = TicketPriority.Low(),

    /**
     * The current status of the ticket.
     */
    val status: TicketStatus = TicketStatus.Open(),

    /**
     * A list of messages associated with the ticket.
     */
    val comments: List<MessageDetails> = emptyList(),

    /**
     * The user's comment or message.
     */
    val userComment: String = "",

    /**
     * Flag indicating whether a loading indicator should be displayed.
     */
    val showLoader: Boolean = false,
    val date: String = "",
)
