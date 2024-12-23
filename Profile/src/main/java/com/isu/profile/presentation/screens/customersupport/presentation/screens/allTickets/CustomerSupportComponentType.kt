package com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets

import com.isu.common.events.APIType
import com.isu.common.events.Clickables
import com.isu.common.events.CommonTextField

/**
 * @author-Karthik
 * Defines the text fields used in the Customer Support screen.
 */
sealed interface CustomerSupportTextField : CommonTextField {
    /**
     * Represents the search text field in Customer Support.
     */
    data object Search : CustomerSupportTextField
}

/**
 * Defines the clickable elements in the Customer Support screen.
 */
sealed interface CustomerSupportClickables : Clickables {
    /**
     * Represents a ticket card in Customer Support.
     */
    data object TicketCard : CustomerSupportClickables

    /**
     * Represents the ticket status in Customer Support.
     */
    data object TicketStatus : CustomerSupportClickables
}

/**
 * Defines the API types used in Customer Support interactions.
 */
sealed interface CustomerSupportApiType : APIType {
    /**
     * Represents the API call to fetch tickets.
     */
    data object FetchTickets : CustomerSupportApiType
}
