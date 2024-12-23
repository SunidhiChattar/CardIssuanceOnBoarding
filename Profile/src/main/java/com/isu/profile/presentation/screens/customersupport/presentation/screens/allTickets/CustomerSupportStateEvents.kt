package com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets

import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.StateEvent
import com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket.RaiseTicketData
import com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails.TicketDetailUIData

/**
 * @author-karthik
 * Data class representing the state events for various customer support screens.
 *
 * @param customerSupportScreen The state event for the Customer Support screen.
 * @param raiseTicketScreen The state event for the Raise Ticket screen.
 * @param ticketDetailsScreen The state event for the Ticket Details screen.
 */
data class CustomerSupportStateEvents(
    /**
     * The state event for the Customer Support screen.
     * Contains the data and events related to the Customer Support screen.
     */
    val customerSupportScreen: StateEvent<CustomerSupportData, CommonScreenEvents>,

    /**
     * The state event for the Raise Ticket screen.
     * Contains the data and events related to the Raise Ticket screen.
     */
    val raiseTicketScreen: StateEvent<RaiseTicketData, CommonScreenEvents>,

    /**
     * The state event for the Ticket Details screen.
     * Contains the data and events related to the Ticket Details screen.
     */
    val ticketDetailsScreen: StateEvent<TicketDetailUIData, CommonScreenEvents>
)
