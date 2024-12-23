package com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets

import com.isu.common.customcomposables.TicketData

/**
 *@author-karthik
 * Data class representing the state of customer support tickets.
 *
 * @param listOfTicketData The list of ticket data to display.
 * @param requireReloading Indicates whether the data needs to be reloaded.
 * @param search The current search query for filtering tickets.
 */
data class CustomerSupportData(
    /**
     * The list of ticket data to display.
     */
    val listOfTicketData: List<TicketData> = listOf(),

    /**
     * Indicates whether the data needs to be reloaded.
     */
    val requireReloading: Boolean = true,

    /**
     * The current search query for filtering tickets.
     */
    val search: String = ""
)
