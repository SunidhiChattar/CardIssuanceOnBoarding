package com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails

import com.isu.common.events.APIType
import com.isu.common.events.Clickables
import com.isu.common.events.CommonTextField

/**
 * Sealed interface representing different API types related to ticket details.
 */
sealed interface TicketDetailsApiType : APIType {

    /**
     * API type for fetching ticket comments.
     */
    data object FetchTicketComments : TicketDetailsApiType
}

/**
 * Sealed interface representing different clickable actions related to ticket details.
 */
sealed interface TicketDetailsClickables : Clickables {

    /**
     * Clickable action for ticket status.
     */
    data object TicketStatus : TicketDetailsClickables

    /**
     * Clickable action for adding a comment.
     */
    data object AddComment : TicketDetailsClickables
}

/**
 * Sealed interface representing different text fields related to ticket comments.
 */
sealed interface TicketDetailsInputComment : CommonTextField {

    /**
     * Text field for entering a ticket comment.
     */
    data object TicketComment : TicketDetailsInputComment
}
