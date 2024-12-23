package com.isu.profile.presentation.screens.customersupport.presentation.screens

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.customcomposables.TicketData
import com.isu.common.customcomposables.TicketStatus
import com.isu.common.events.APIType
import com.isu.common.events.Clickables
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.CommonTextField
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.CustomerSupportScreen
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.Screen
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.profile.R
import com.isu.profile.data.remote.model.request.AddCommentsRequest
import com.isu.profile.data.remote.model.request.AuthTokenData
import com.isu.profile.data.remote.model.request.GetFormRequest
import com.isu.profile.data.remote.model.request.RaiseAtTicketRequest
import com.isu.profile.data.remote.model.request.ShowTicketCommentRequest
import com.isu.profile.data.remote.model.request.UpdateTicketStatusRequest
import com.isu.profile.data.remote.model.response.AddCommentsResponse
import com.isu.profile.data.remote.model.response.FetchRaisedTicketsResponse
import com.isu.profile.data.remote.model.response.RaiseATicketResponse
import com.isu.profile.data.remote.model.response.ShowTicketCommentResponse
import com.isu.profile.data.remote.model.response.ShowTicketFormResponse
import com.isu.profile.data.remote.model.response.UpdateTicketStatusResponse
import com.isu.profile.data.remote.model.response.toComments
import com.isu.profile.data.remote.model.response.toTicketData
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.CustomerSupportApiType
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.CustomerSupportClickables
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.CustomerSupportData
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.CustomerSupportTextField
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.DocumentDetails
import com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket.RaiseATicketAPIType
import com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket.RaiseATicketClickableTypes
import com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket.RaiseATicketTextInput
import com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket.RaiseTicketData
import com.isu.profile.presentation.screens.customersupport.presentation.screens.customesupportscreen.CustomerSupportComponentTypes
import com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails.TicketDetailUIData
import com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails.TicketDetailsApiType
import com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails.TicketDetailsClickables
import com.isu.profile.presentation.screens.customersupport.presentation.screens.ticketDetails.TicketDetailsInputComment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author-karthik
 * Customer support view model
 *
 * @property dataStoreManager
 * @property fetchRaisedTicketsUseCase
 * @property fetchTicketCommentsUseCase
 * @property raiseATicketUseCase
 * @property updateTicketStatusUseCase
 * @property addCommentUseCase
 * @constructor Create empty Customer support view model
 */
sealed interface InputType {
    data object FIELD_NUMBER:InputType
    data object FIELD_STRING:InputType
    data object DROPDOWN:InputType
}


data class FieldOptions (
    val id: String = "",
    val value: String = "",
    val fieldValue: String = "",

)


data class FormData(
    val type: InputType,
    val id: String = "",
    val label: String = "",
    val value: String = "",
    val possibleValues: List<FieldOptions> = listOf(),
    val required: Boolean = false,
    val regex: String = ""
)

@HiltViewModel
class CustomerSupportViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val fetchRaisedTicketsUseCase: ApiUseCase<AuthTokenData<Any>, FetchRaisedTicketsResponse>,
    private val fetchTicketCommentsUseCase: ApiUseCase<AuthTokenData<ShowTicketCommentRequest>, ShowTicketCommentResponse>,
    private val raiseATicketUseCase: ApiUseCase<AuthTokenData<RaiseAtTicketRequest>, RaiseATicketResponse>,
    private val updateTicketStatusUseCase: ApiUseCase<AuthTokenData<UpdateTicketStatusRequest>, UpdateTicketStatusResponse>,
    private val addCommentUseCase: ApiUseCase<AuthTokenData<AddCommentsRequest>, AddCommentsResponse>,
    private val ticketFormUseCase: ApiUseCase<GetFormRequest, ShowTicketFormResponse>
) : ViewModel() {
    private val listOfUrl = mutableListOf("")

    // UI State for  ticketDetail
    private val _tickedDetailUIState = MutableStateFlow(TicketDetailUIData())
    val tickedDetailUIState: StateFlow<TicketDetailUIData> = _tickedDetailUIState

    // UI State for Customer Support Screen
    private val _customerSupportUIState = MutableStateFlow(CustomerSupportData())
    val customerSupportState: StateFlow<CustomerSupportData> = _customerSupportUIState

    val listOfFormData: SnapshotStateList<FormData> = mutableStateListOf()

    val mapOfListData: MutableMap<String, String> = mutableMapOf<String, String>()
    /**
     * Handles various events triggered on the Customer Support screen.
     *
     * @param events The event triggered on the screen.
     */
    fun onCustomerSupportScreenEvent(events: CommonScreenEvents) {
        when (events) {
            is CommonScreenEvents.CallApi<*> -> {
                handleCustomerSupportApiCall(events.apiType, onApiSuccess = events.onSuccess)
            }

            is CommonScreenEvents.ClearFields -> {
                _customerSupportUIState.update { it.copy(search = "") }
            }

            is CommonScreenEvents.OnClick<*> -> {
                handleCustomerSupportScreenClick(
                    events.type,
                    events.additionData
                ) { events.onComplete() }
            }

            is CommonScreenEvents.OnTextChanged -> {
                handleCustomerSupportScreenTextChange(events.type, events.text)
            }

            else -> {
                // Handle any other events if necessary
            }
        }
    }

    /**
     * Handles text change events on the Customer Support screen.
     *
     * @param type The type of text field that was changed.
     * @param text The new text entered in the text field.
     */
    private fun handleCustomerSupportScreenTextChange(type: CommonTextField, text: String) {
        when (type) {
            CustomerSupportTextField.Search -> {
                _customerSupportUIState.update { it.copy(search = text) }
            }
        }
    }

    /**
     * Handles click events on the Customer Support screen.
     *
     * @param type The clickable element that was clicked.
     * @param additionData Additional data associated with the click event.
     * @param onComplete A callback function to be executed after the click event is handled.
     */
    private fun handleCustomerSupportScreenClick(
        type: Clickables,
        additionData: Any?,
        onComplete: () -> Unit,
    ) {
        when (type) {
            is CustomerSupportClickables.TicketCard -> {
                handleTicketCardClick(additionData, onComplete)
            }

            is CustomerSupportClickables.TicketStatus -> {
                handleTicketStatusClick(additionData, onComplete)
            }
            is CustomerSupportComponentTypes.ToRaiseScreen->{
                viewModelScope.launch {
                    dataStoreManager.clearDataStoreSpecificData(PreferencesKeys.DISPUTED_TRANSACTION_ID)
                    NavigationEvent.helper.navigateTo(CustomerSupportScreen.RaiseTicketScreen)
                }
            }
            is CustomerSupportComponentTypes.ToAllTicketScreen->{
             viewModelScope.launch {
                 NavigationEvent.helper.navigateTo(CustomerSupportScreen.AllTicketsScreen)
             }
            }
        }
    }

    /**
     * Handles the API calls triggered by the Customer Support screen.
     *
     * @param apiType The type of API to be called.
     * @param onApiSuccess A callback function to be executed upon successful API call.
     */
    private fun handleCustomerSupportApiCall(apiType: APIType, onApiSuccess: () -> Unit) {
        when (apiType) {
            is CustomerSupportApiType.FetchTickets -> {
                fetchTickets(onSuccess = { onApiSuccess() })
            }
        }
    }

    /**
     * Handles click event on the ticket card, updating the UI state and navigating to the Ticket Details screen.
     *
     * @param additionData Additional data associated with the click event, expected to be of type [TicketData].
     * @param onComplete A callback function to be executed after the click event is handled.
     */
    private fun handleTicketCardClick(additionData: Any?, onComplete: () -> Unit) {
        val data = additionData as TicketData
        viewModelScope.launch {
            _tickedDetailUIState.update {
                it.copy(
                    ticketId = data.ticketId,
                    requestTitle = data.requestTitle,
                    category = data.category,
                    status = data.status,
                    showLoader = true,
                    description = data.description,
                    priority = data.priority,
                    date = data.date
                )
            }
            NavigationEvent.helper.emit(
                NavigationEvent.NavigateToNextScreen(
                    CustomerSupportScreen.TicketDetailsScreen
                )
            )
            onComplete()
        }
    }

    /**
     * Handles click event on the ticket status, updating the ticket status via an API call.
     *
     * @param additionData Additional data associated with the click event, expected to be of type [TicketData].
     * @param onComplete A callback function to be executed after the click event is handled.
     */
    private fun handleTicketStatusClick(additionData: Any?, onComplete: () -> Unit) {
        val data = additionData as TicketData
        val status = when (data.status) {
            TicketStatus.Open() -> "solved"
            else -> "open"
        }

        viewModelScope.launch {
            val authToken = dataStoreManager.getPreferenceValue(PreferencesKeys.AUTH_TOKEN)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
            val request = UpdateTicketStatusRequest(
                status = status,
                ticketId = try {
                    data.ticketId.toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    0
                }
            )
            val authTokenData =
                AuthTokenData(
                    authToken.toString(),
                    userName.toString(),
                    request
                )

            handleFlow(
                apiCall = { updateTicketStatusUseCase.invoke(authTokenData) },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                builder = {
                    onSuccess { resp ->
                        if (resp?.statusCode == 0) {
                            onComplete()
                            viewModelScope.launch {
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.SuccessSnackBar,
                                        UiText.DynamicString("Success")
                                    )
                                )
                            }
                            fetchTickets { }
                        }

                    }
                    onLoading { isLoading ->
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(isLoading))
                        }
                    }
                    onFailure { msg, _, _, resp ->
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(resp?.statusDesc ?: "Something went wrong")
                                )
                            )
                        }
                    }
                }
            )
        }
    }

    /**
     * Fetches tickets by making an API call.
     */
    private fun fetchTickets(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val authToken = dataStoreManager.getPreferenceValue(PreferencesKeys.AUTH_TOKEN)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
            val authTokenData = AuthTokenData<Any>(
                authToken.toString(),
                userName.toString()
            )
            handleFlow(
                apiCall = {
                    fetchRaisedTicketsUseCase.invoke(authTokenData)
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                builder = {
                    onSuccess { resp ->
                        viewModelScope.launch {
                            _customerSupportUIState.update {
                                it.copy(
                                    listOfTicketData = resp?.toTicketData() ?: emptyList(),
                                    requireReloading = false
                                )
                            }
                            if (tickedDetailUIState.value.ticketId.isNotEmpty()) {
                                resp?.toTicketData()?.forEach { tickets ->
                                    if (tickets.ticketId == tickedDetailUIState.value.ticketId) {
                                        _tickedDetailUIState.update {
                                            it.copy(
                                                status = tickets.status,
                                                priority = tickets.priority,
                                                ticketId = tickets.ticketId,
                                                description = tickets.description
                                            )
                                        }
                                    }
                                }
                                onSuccess()
                            }
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.DynamicString("Success")
                                )
                            )
                        }
                    }
                    onLoading {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                        }
                    }
                    onFailure { msg, _, _, resp ->
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        resp?.statusDesc ?: "Something went wrong"
                                    )
                                )
                            )
                        }
                    }
                }
            )
        }
    }


    // UI State for Raise a ticket screen

    private val _raiseTicketUIState = MutableStateFlow(RaiseTicketData())
    val raiseTicketUIState: StateFlow<RaiseTicketData> = _raiseTicketUIState
    val listOfExcludedIds: List<String> =
        listOf("11231832864281", "900013326103", "900013326083", "900013326023", "900013325983")


    /**
     * Handles various events triggered on the Raise Ticket screen.
     *
     * @param events The event triggered on the screen.
     */
    fun onRaiseTicketEvent(events: CommonScreenEvents) {
        when (events) {
            is CommonScreenEvents.ClearFields -> clearRaiseTicketScreenData(_raiseTicketUIState)
            is CommonScreenEvents.NavigateTo -> handleRaiseTicketNavigationEvent(events.screen)
            is CommonScreenEvents.OnClick<*> -> handleRaiseTicketButtonClick(
                events.type, data = events.additionData
            )

            is CommonScreenEvents.OnTextChanged -> handleRaisedTextChange(events.type, events.text)
            is CommonScreenEvents.CallApi<*> -> {
                when (events.apiType) {
                    RaiseATicketAPIType.FetchFormData -> {
                        callFetchFormData()
                    }
                }

            }

            else -> {

            }

        }
    }

    private fun callFetchFormData() {
        val request = GetFormRequest(

        )
           

        handleFlow(
            apiCall = {
                ticketFormUseCase.invoke(request)
            },
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            onSuccesss = {resp->
                listOfFormData.clear()
                viewModelScope.launch {
                    val transactionId =
                        dataStoreManager.getPreferenceValue(PreferencesKeys.DISPUTED_TRANSACTION_ID)
                    _raiseTicketUIState.update {
                        it.copy(
                            listOfFormData = resp?.data?.getOrNull(0)?.ticketFields?.filter { it?.visibleInPortal == true }
                                ?.filter { listOfExcludedIds.contains(it?.id.toString()) == false }
                                ?.map {
                                    FormData(
                                        type = if (it?.type == "tagger") InputType.DROPDOWN else if (it?.type?.contains(
                                                "number".toRegex()
                                            ) == true || it?.type?.contains("integer".toRegex()) == true
                                        ) InputType.FIELD_NUMBER else InputType.FIELD_STRING,
                                        id = it?.id.toString(),
                                        label = it?.title.toString(),
                                        value = "",
                                        possibleValues = it?.customFieldOptions?.map {
                                            FieldOptions(
                                                id = it?.id.toString(),
                                                value = it?.name.toString(),
                                                fieldValue = it?.value.toString()
                                            )
                                        } ?: emptyList()
                                    )
                                } ?: emptyList(),

                            )
                    }
                    _raiseTicketUIState.update {
                        Log.d("TRANSACTION_ID", "callFetchFormData:${transactionId} ")
                        it.copy(TransactionId = transactionId)
                    }
                }



            }
        )
    }

    /**
     * Handles text change events on the Raise Ticket screen.
     *
     * @param type The type of text field that was changed.
     * @param text The new text entered in the text field.
     */
    private fun handleRaisedTextChange(type: CommonTextField, text: String) {
        when (type) {
            is RaiseATicketTextInput.Search -> {
                _raiseTicketUIState.update { it.copy(search = text) }
            }

            is RaiseATicketTextInput.Category -> {
                _raiseTicketUIState.update {
                    it.copy(
                        category = text,
                        categoryError = false,
                        categoryErrorMessage = UiText.StringResource(com.isu.common.R.string.empty)
                    )
                }
            }

            is RaiseATicketTextInput.Priority -> {
                _raiseTicketUIState.update {
                    it.copy(
                        priority = text,
                        priorityError = false,
                        priorityErrorMessage = UiText.StringResource(com.isu.common.R.string.empty)
                    )
                }
            }

            is RaiseATicketTextInput.Title -> handleTitleTextChange(text)
            is RaiseATicketTextInput.Description -> handleDescriptionTextChange(text)
        }
    }

    /**
     * Handles title text change and manages the error state.
     *
     * @param text The new title text entered by the user.
     */
    private fun handleTitleTextChange(text: String) {
        if (text.length <= 50) {
            if (text.length == 50) {
                viewModelScope.launch {
                    _raiseTicketUIState.update {
                        it.copy(
                            title = text,
                            titleError = true,
                            titleErrorMessage = UiText.StringResource(com.isu.common.R.string.title_error)
                        )
                    }
                    delay(1000)
                    _raiseTicketUIState.update {
                        it.copy(
                            title = text,
                            titleError = false,
                            titleErrorMessage = UiText.StringResource(com.isu.common.R.string.empty)
                        )
                    }
                }
            } else {
                _raiseTicketUIState.update {
                    it.copy(
                        title = text,
                        titleError = false,
                        titleErrorMessage = UiText.StringResource(com.isu.common.R.string.empty)
                    )
                }
            }
        }
    }

    /**
     * Handles description text change and manages the error state.
     *
     * @param text The new description text entered by the user.
     */
    private fun handleDescriptionTextChange(text: String) {
        if (text.length <= 300) {
            if (text.length == 300) {
                viewModelScope.launch {
                    _raiseTicketUIState.update {
                        it.copy(
                            description = text,
                            descriptionErrorMessage = UiText.StringResource(com.isu.common.R.string.empty),
                            descriptionError = true
                        )
                    }
                    delay(1000)
                    _raiseTicketUIState.update {
                        it.copy(
                            description = text,
                            descriptionErrorMessage = UiText.StringResource(com.isu.common.R.string.empty),
                            descriptionError = false
                        )
                    }
                }
            } else {
                _raiseTicketUIState.update {
                    it.copy(
                        description = text,
                        descriptionError = false,
                        descriptionErrorMessage = UiText.StringResource(com.isu.common.R.string.empty)
                    )
                }
            }
        }
    }

    /**
     * Handles navigation events on the Raise Ticket screen.
     *
     * @param navigationScreen The screen to navigate to.
     */
    private fun handleRaiseTicketNavigationEvent(navigationScreen: Screen) {
        viewModelScope.launch {
            NavigationEvent.helper.navigateTo(navigationScreen)
        }
    }

    /**
     * Clears all the fields on the Raise Ticket screen.
     *
     * @param raiseTicketUIState The UI state of the Raise Ticket screen.
     */
    private fun clearRaiseTicketScreenData(raiseTicketUIState: MutableStateFlow<RaiseTicketData>) {
        raiseTicketUIState.value = RaiseTicketData()
    }

    /**
     * Handles button click events on the Raise Ticket screen.
     *
     * @param type The clickable element that was clicked.
     *
     */
    private fun handleRaiseTicketButtonClick(type: Clickables, data: Any?) {
        when (type) {
            is RaiseATicketClickableTypes.Submit -> {

                    callRaiseTicketApi()

            }

            is RaiseATicketClickableTypes.Cancel -> {
                handleCancelButtonClick()
            }

            RaiseATicketClickableTypes.UploadToFirebase -> {
                viewModelScope.launch {

                    val uri = data as Uri

                    if (raiseTicketUIState.value.attachment.size <= 5) {
//                        firebaseStorageManager.uploadFileToFirebaseStorage(uri).collectLatest {
//                            when (it) {
//                                is NetworkResource.Error -> {
//                                    LoadingErrorEvent.helper.emit(
//                                        LoadingErrorEvent.errorEncountered(
//                                            UiText.DynamicString(it.message ?: "")
//                                        )
//                                    )
//                                }
//
//                                is NetworkResource.Loading -> {
//                                    LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it.isLoading))
//                                }
//
//                                is NetworkResource.Success -> {
//
//                                    Log.d(
//                                        "KDATA",
//                                        "handleRaiseTicketButtonClick: ${raiseTicketUIState.value}"
//                                    )
//                                }
//                            }
//                        }
                        _raiseTicketUIState.update { data ->
                            data.attachment.add(DocumentDetails(url = ""))
                            data.copy(
                                attachment = data.attachment
                            )

                        }
                    } else {
                        _raiseTicketUIState.update {
                            it.copy(
                                attachmentError = true,
                                attachmentErrorMessage = UiText.StringResource(R.string.maximum_5_files_can_be_uploaded)
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Validates all the input fields on the Raise Ticket screen.
     *
     * @return True if all fields are valid, false otherwise.
     */
    private fun allFieldsAreValid(): Boolean {
        val validTitle = titleValid()
        val validDescription = descriptionValid()
        val validCategory = categoryValid()
        val validPriority = priorityValid()

        return validTitle && validDescription && validCategory && validPriority
    }

    /**
     * Initiates the API call to raise a ticket.
     */
    private fun callRaiseTicketApi() {
        viewModelScope.launch {
            val requestTitle = raiseTicketUIState.value.title
            val category = raiseTicketUIState.value.category
            val authToken = dataStoreManager.getPreferenceValue(PreferencesKeys.AUTH_TOKEN)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)

            val request = RaiseAtTicketRequest(
                assignee = "CUST" + userName,
                attachmentURL = if (raiseTicketUIState.value.attachment.isNullOrEmpty()) {
                    ""
                } else raiseTicketUIState.value.attachment[0].url,
                body = mapOfListData.get("900013326003"),
                customFields = mapOfListData.toList().map {
                    RaiseAtTicketRequest.CustomField(
                        id = it.first.toLong(),
                        value = it.second
                    )
                },
                priority = "high",
                subject = "PREPAID",
            )

            val authTokenData = AuthTokenData(
                authToken.toString(),
                userName.toString(),
                request
            )

            handleApiCall(authTokenData)
        }
    }

    /**
     * Handles the API call flow for raising a ticket.
     *
     * @param authTokenData The authorization token and request data for the API call.
     */
    private fun handleApiCall(authTokenData: AuthTokenData<RaiseAtTicketRequest>) {
        viewModelScope.launch {
            handleFlow(
                apiCall = { raiseATicketUseCase.invoke(authTokenData) },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                builder = {
                    onSuccess { resp ->
                        if (resp?.statusCode == 0) {
                            onTicketRaisedSuccessfully()
                        } else {
                            viewModelScope.launch {
                                LoadingErrorEvent.helper.emit(
                                    LoadingErrorEvent.errorEncountered(
                                        UiText.DynamicString(
                                            resp?.statusDesc ?: "Something went wrong"
                                        )
                                    )
                                )
                            }
                        }
                    }
                    onLoading { isLoading ->
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(isLoading))
                        }
                    }
                    onFailure { msg, _, _, resp ->
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        resp?.statusDesc ?: "Something went wrong"
                                    )
                                )
                            )
                        }
                    }
                }
            )
        }
    }

    /**
     * Handles successful ticket raising by showing a success message and navigating to the home screen.
     */
    private fun onTicketRaisedSuccessfully() {
        viewModelScope.launch {
            _customerSupportUIState.update { it.copy(requireReloading = true) }
            ShowSnackBarEvent.helper.emit(
                ShowSnackBarEvent.show(
                    SnackBarType.SuccessSnackBar,
                    UiText.StringResource(com.isu.common.R.string.ticket_raise_success)
                )
            )
            NavigationEvent.helper.emit(
                NavigationEvent.NavigateBack
            )
        }
    }

    /**
     * Handles the cancel button click event by stopping loading and navigating back.
     */
    private fun handleCancelButtonClick() {
        viewModelScope.launch {
            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(false))
            NavigationEvent.helper.emit(NavigationEvent.NavigateBack)
        }
    }

    // Add your validation methods here (titleValid, descriptionValid, categoryValid, priorityValid)
    /**
     * Validates the priority field.
     *
     * @return True if the priority field is valid, false otherwise.
     */
    private fun priorityValid(): Boolean {
        var isValid = true
        if (raiseTicketUIState.value.priority.isEmpty()) {
            _raiseTicketUIState.update {
                it.copy(
                    priorityError = true,
                    priorityErrorMessage = UiText.StringResource(R.string.priority_is_required)
                )
            }
            isValid = false
        }
        return isValid
    }

    /**
     * Validates the category field.
     *
     * @return True if the category field is valid, false otherwise.
     */
    private fun categoryValid(): Boolean {
        var isValid = true
        if (raiseTicketUIState.value.category.isEmpty()) {
            _raiseTicketUIState.update {
                it.copy(
                    categoryError = true,
                    categoryErrorMessage = UiText.StringResource(R.string.category_is_required)
                )
            }
            isValid = false
        }
        return isValid
    }

    /**
     * Validates the description field.
     *
     * @return True if the description field is valid, false otherwise.
     */
    private fun descriptionValid(): Boolean {
        var isValid = true
        if (raiseTicketUIState.value.description.isEmpty()) {
            _raiseTicketUIState.update {
                it.copy(
                    descriptionError = true,
                    descriptionErrorMessage = UiText.StringResource(R.string.description_is_required)
                )
            }
            isValid = false
        }
        return isValid
    }

    /**
     * Validates the title field.
     *
     * @return True if the title field is valid, false otherwise.
     */
    private fun titleValid(): Boolean {
        var isValid = true
        if (raiseTicketUIState.value.title.isEmpty()) {
            _raiseTicketUIState.update {
                it.copy(
                    titleError = true,
                    titleErrorMessage = UiText.StringResource(R.string.title_is_required)
                )
            }
            isValid = false
        }
        return isValid
    }

    /**
     * Handles events on the Ticket Detail Screen.
     *
     * @param events The event to be handled.
     */
    fun onTicketDetailScreenEvent(events: CommonScreenEvents) {
        when (events) {
            is CommonScreenEvents.CallApi<*> -> handleTicketDetailApiCall(
                events.apiType,
                events.onSuccess,
                events.additionalInfo
            )

            is CommonScreenEvents.OnClick<*> -> {
                handleTicketDetailsClickEvent(
                    events.type, events.additionData
                ) { events.onComplete() }
            }

            is CommonScreenEvents.OnTextChanged -> {
                handleTicketDetailTextChange(events.type, events.text)
            }

            else -> {
                /* No other events to handle */
            }
        }
    }

    /**
     * Handles text change events on the Ticket Detail Screen.
     *
     * @param type The type of text field.
     * @param text The new text value.
     */
    private fun handleTicketDetailTextChange(type: CommonTextField, text: String) {
        when (type) {
            TicketDetailsInputComment.TicketComment -> {
                _tickedDetailUIState.update {
                    it.copy(userComment = text)
                }
            }
        }
    }

    /**
     * Handles API calls for the Ticket Detail Screen.
     *
     * @param apiType The type of API call.
     * @param onSuccess A callback to be executed on successful API call.
     * @param additionalInfo Additional information required for the API call.
     */
    private fun handleTicketDetailApiCall(
        apiType: APIType,
        onSuccess: () -> Unit,
        additionalInfo: Any?,
    ) {
        when (apiType) {
            is TicketDetailsApiType.FetchTicketComments -> {
                val id = additionalInfo?.toString()?.toIntOrNull() ?: 0
                fetchTicketsComment(id) { onSuccess() }
            }
        }
    }

    /**
     * Fetches ticket comments based on the provided ticket ID.
     *
     * @param ticketId The ID of the ticket.
     * @param onComplete A callback to be executed upon completion.
     */
    private fun fetchTicketsComment(ticketId: Int, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val authToken = dataStoreManager.getPreferenceValue(PreferencesKeys.AUTH_TOKEN)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
            val request =
                ShowTicketCommentRequest(ticketId)
            val authTokenData = AuthTokenData(
                authToken.toString(),
                userName.toString(),
                request
            )

            handleFlow(
                apiCall = { fetchTicketCommentsUseCase.invoke(authTokenData) },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                builder = {
                    onSuccess { resp ->
                        viewModelScope.launch {
                            _tickedDetailUIState.update {
                                it.copy(
                                    comments = resp?.toComments()?.reversed() ?: emptyList(),
                                    showLoader = false
                                )
                            }
                            onComplete()
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.StringResource(R.string.comments_fetched)
                                )
                            )
                        }
                    }
                    onLoading {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                        }
                    }
                    onFailure { msg, _, _, resp ->
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(msg)
                                )
                            )
                        }
                    }
                }
            )
        }
    }

    /**
     * Handles click events on the Ticket Detail Screen.
     *
     * @param type The type of clickable element.
     * @param additionData Additional data required for the click event.
     * @param onComplete A callback to be executed upon completion.
     */
    private fun handleTicketDetailsClickEvent(
        type: Clickables,
        additionData: Any?,
        onComplete: () -> Unit,
    ) {
        when (type) {
            TicketDetailsClickables.TicketStatus -> handleTicketStatusChange(
                additionData,
                onComplete
            )

            TicketDetailsClickables.AddComment -> handleAddComment(
                additionData as String,
                onComplete
            )
        }
    }

    /**
     * Handles ticket status change events.
     *
     * @param additionData Additional data required for the status change.
     * @param onComplete A callback to be executed upon completion.
     */
    private fun handleTicketStatusChange(additionData: Any?, onComplete: () -> Unit) {
        val data = additionData as TicketData
        val status = if (data.status == TicketStatus.Open()) "solved" else "open"

        viewModelScope.launch {
            val authToken = dataStoreManager.getPreferenceValue(PreferencesKeys.AUTH_TOKEN)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
            val request = UpdateTicketStatusRequest(
                status = status,
                ticketId = data.ticketId.toIntOrNull() ?: 0
            )
            val authTokenData = AuthTokenData(
                authToken.toString(),
                userName.toString(),
                request
            )

            handleFlow(
                apiCall = { updateTicketStatusUseCase.invoke(authTokenData) },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                builder = {
                    onSuccess {
                        viewModelScope.launch {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.DynamicString("Success")
                                )
                            )
                            fetchTickets { onComplete() }
                        }
                    }
                    onLoading {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                        }
                    }
                    onFailure { msg, _, _, resp ->
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        resp?.statusDesc ?: "Something went wrong"
                                    )
                                )
                            )
                        }
                    }
                }
            )
        }
    }

    /**
     * Handles the addition of a comment to a ticket.
     *
     * @param ticketId The ID of the ticket to add a comment to.
     * @param onComplete A callback to be executed upon completion.
     */
    private fun handleAddComment(comment: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val authToken = dataStoreManager.getPreferenceValue(PreferencesKeys.AUTH_TOKEN)
            val userName = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
            val ticketId = _tickedDetailUIState.value.ticketId
            val request = AddCommentsRequest(
                attachmentURL = null,
                body = _tickedDetailUIState.value.userComment,
                ticketId = ticketId.toInt()
            )
            val authTokenData = AuthTokenData(
                authorization = authToken.toString(),
                tokenProperties = userName.toString(),
                request = request
            )

            handleFlow(
                apiCall = { addCommentUseCase.invoke(authTokenData) },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                builder = {
                    onSuccess {
                        viewModelScope.launch {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.StringResource(R.string.comments_updated)
                                )
                            )
                            fetchTicketsComment(ticketId.toInt(), onComplete)
                            _tickedDetailUIState.update {
                                it.copy(userComment = "")
                            }
                        }
                    }
                    onLoading {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                        }
                    }
                    onFailure { msg, _, _, resp ->
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(UiText.DynamicString(msg))
                            )
                        }
                    }
                }
            )
        }
    }


}