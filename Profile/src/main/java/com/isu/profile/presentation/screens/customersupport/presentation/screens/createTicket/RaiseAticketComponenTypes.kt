package com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket

import com.isu.common.events.APIType
import com.isu.common.events.Clickables
import com.isu.common.events.CommonTextField

/**
 * classes representing screen components where events can be encountered
 */
/**
 * Sealed interface representing the clickable types in the Raise a Ticket screen.
 */
sealed interface RaiseATicketClickableTypes : Clickables {

    /**
     * Represents the 'Submit' button click action in the Raise a Ticket screen.
     */
    data object Submit : RaiseATicketClickableTypes

    /**
     * Represents the 'Cancel' button click action in the Raise a Ticket screen.
     */
    data object Cancel : RaiseATicketClickableTypes
    data object UploadToFirebase : RaiseATicketClickableTypes
}

/**
 * Sealed interface representing the text input fields in the Raise a Ticket screen.
 */
sealed interface RaiseATicketTextInput : CommonTextField {

    /**
     * Represents the 'Category' text input field in the Raise a Ticket screen.
     */
    data object Category : RaiseATicketTextInput

    /**
     * Represents the 'Priority' text input field in the Raise a Ticket screen.
     */
    data object Priority : RaiseATicketTextInput

    /**
     * Represents the 'Title' text input field in the Raise a Ticket screen.
     */
    data object Title : RaiseATicketTextInput

    /**
     * Represents the 'Description' text input field in the Raise a Ticket screen.
     */
    data object Description : RaiseATicketTextInput

    /**
     * Represents the 'Search' text input field in the Raise a Ticket screen.
     */
    data object Search : RaiseATicketTextInput
}

/**
 * Sealed interface representing the API types used in the Raise a Ticket screen.
 */
sealed interface RaiseATicketAPIType : APIType {

    /**
     * Represents the API type for fetching profile data.
     */
    data object FetchProfileData : RaiseATicketAPIType

    data object FetchFormData: RaiseATicketAPIType
}
