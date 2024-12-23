package com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.isu.common.utils.UiText
import com.isu.profile.presentation.screens.customersupport.presentation.screens.FormData
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.DocumentDetails

/**
 * Data class representing the state of the Raise Ticket screen.
 *
 * @property search The search query entered by the user.
 * @property title The title of the ticket.
 * @property titleError Boolean indicating if there is an error with the title field.
 * @property titleErrorMessage Error message for the title field.
 * @property description The description of the ticket.
 * @property descriptionError Boolean indicating if there is an error with the description field.
 * @property descriptionErrorMessage Error message for the description field.
 * @property priority The priority level of the ticket.
 * @property priorityError Boolean indicating if there is an error with the priority field.
 * @property priorityErrorMessage Error message for the priority field.
 * @property category The category of the ticket.
 * @property categoryError Boolean indicating if there is an error with the category field.
 * @property categoryErrorMessage Error message for the category field.
 * @property attachment List of attached documents for the ticket.
 * @property attachmentError Boolean indicating if there is an error with attachments.
 * @property attachmentErrorMessage Error message for the attachments.
 */
data class RaiseTicketData(
    val search: String = "", // The search query entered by the user.
    val title: String = "", // The title of the ticket.
    val titleError: Boolean = false, // Indicates if there is an error with the title field.
    val titleErrorMessage: UiText = UiText.StringResource(com.isu.common.R.string.empty), // Error message for the title field.
    val description: String = "", // The description of the ticket.
    val descriptionError: Boolean = false, // Indicates if there is an error with the description field.
    val descriptionErrorMessage: UiText = UiText.StringResource(com.isu.common.R.string.empty), // Error message for the description field.
    val priority: String = "", // The priority level of the ticket.
    val priorityError: Boolean = false, // Indicates if there is an error with the priority field.
    val priorityErrorMessage: UiText = UiText.StringResource(com.isu.common.R.string.empty), // Error message for the priority field.
    val category: String = "", // The category of the ticket.
    val categoryError: Boolean = false, // Indicates if there is an error with the category field.
    val categoryErrorMessage: UiText = UiText.StringResource(com.isu.common.R.string.empty),// Error message for the category field.
    val attachment: SnapshotStateList<DocumentDetails> = mutableStateListOf(), // List of attached documents for the ticket.
    val attachmentError: Boolean = false, // Indicates if there is an error with attachments.
    val attachmentErrorMessage: UiText = UiText.StringResource(com.isu.common.R.string.empty),
    val listOfFormData:List<FormData> = mutableStateListOf(),
    val TransactionId: String? = null,

    // Error message for the attachments.
)
