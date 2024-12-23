package com.isu.prepaidcard.customersupport.presentation.navigation

import androidx.navigation.NavGraphBuilder
import com.isu.common.customcomposables.CustomComposable
import com.isu.common.navigation.CustomerSupportScreen
import com.isu.profile.presentation.screens.customersupport.presentation.screens.CustomerSupportViewModel
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.AllTicketsScreen
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.CustomerSupportStateEvents
import com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket.RaiseTicketScreen
import com.isu.profile.presentation.screens.customersupport.presentation.screens.customesupportscreen.CustomerSupportScreen
import com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails.TicketDetailsScreen

fun NavGraphBuilder.SetUpCustomerSupportNavgraph(
    customerSupportStateEvents: CustomerSupportStateEvents,
    viewModel: CustomerSupportViewModel

    ) {
    CustomComposable<CustomerSupportScreen.AllTicketsScreen> {
        AllTicketsScreen(
            onEvent=customerSupportStateEvents.customerSupportScreen.event,
            state = customerSupportStateEvents.customerSupportScreen.state
        )
    }
    CustomComposable<CustomerSupportScreen.CustomerSupportHomeScreen> {
        CustomerSupportScreen(onEvent=customerSupportStateEvents.customerSupportScreen.event)

    }
    CustomComposable<CustomerSupportScreen.TicketDetailsScreen>  {
    TicketDetailsScreen(onEvent=customerSupportStateEvents.ticketDetailsScreen.event,state=customerSupportStateEvents.ticketDetailsScreen.state)
    }

    CustomComposable<CustomerSupportScreen.RaiseTicketScreen> {
     RaiseTicketScreen(onEvent=customerSupportStateEvents.raiseTicketScreen.event, hashMap = viewModel.mapOfListData, state=customerSupportStateEvents.raiseTicketScreen.state)
    }

}

