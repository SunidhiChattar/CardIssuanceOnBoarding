package com.isu.profile.presentation.screens.basicInfo

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.events.APIType
import com.isu.common.events.Clickables
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.CommonTextField
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.CustomerSupportScreen
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.UiText
import com.isu.common.utils.Validation
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.profile.R
import com.isu.profile.data.remote.model.request.FetchPinCodeDataRequest
import com.isu.profile.data.remote.model.response.FetchPinCodeDataResponse
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.CustomerSupportData
import com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket.RaiseATicketAPIType
import com.isu.profile.presentation.screens.customersupport.presentation.screens.profile.ProfileClickables
import com.isu.profile.presentation.screens.customersupport.presentation.screens.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author- karthik
 * ViewModel class for handling Basic Information Screen-related operations.
 */
@HiltViewModel
class BasicInfoViewModel @Inject constructor(
    private val dataStore: DataStoreManager,
    private val profileDataUseCase: ApiUseCase<
            com.isu.profile.data.remote.model.request.AuthTokenData<Any>,
            com.isu.profile.data.remote.model.response.FetchProfileData
            >,
    private val fetchPinCodeUseCase: ApiUseCase<FetchPinCodeDataRequest, FetchPinCodeDataResponse>,
) : ViewModel() {

    // UI State for Customer Support Data
    private val _uiState = MutableStateFlow(CustomerSupportData())
    val state: StateFlow<CustomerSupportData> = _uiState

    // UI State for Basic Information
    private val _basicInfoUiState = MutableStateFlow(BasicInfoState())
    val basicInfoUiState: StateFlow<BasicInfoState> = _basicInfoUiState

    // UI State for Editing Basic Information
    private val _basicInfoEditUiState = MutableStateFlow(BasicInfoEditState())
    val basicInfoEditUiState: StateFlow<BasicInfoEditState> = _basicInfoEditUiState


    // UI State for Profile Data
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState

    /**
     * Handles various events that occur in the Basic Information Screen.
     */
    fun onEvent(events: CommonScreenEvents) {
        when (events) {
            is CommonScreenEvents.CallApi<*> -> handleApiCall(events.apiType, events.onSuccess)

            is CommonScreenEvents.OnClick<*> -> {
                handleButtonClick(events.type)
            }


            is CommonScreenEvents.OnTextChanged -> {
                handleTextChanged(events.type, events.text)
            }

            else -> {

            }
        }
    }

    fun setProfileFetchRequireMents(bool: Boolean) {
        _profileState.update {
            it.copy(requireProfileFetching = bool)
        }
    }

    /**
     * Handles API calls based on the type of API event.
     */
    private fun handleApiCall(apiType: APIType, onSuccess: () -> Unit) {
        when (apiType) {
            is RaiseATicketAPIType.FetchProfileData -> {
                viewModelScope.launch {
                    val authToken = dataStore.getPreferenceValue(PreferencesKeys.AUTH_TOKEN)
                    val userName = dataStore.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
                    handleFlow(
                        apiCall = {
                            profileDataUseCase.invoke(
                                com.isu.profile.data.remote.model.request.AuthTokenData(
                                    authorization = authToken.toString(),
                                    tokenProperties = userName.toString()
                                )
                            )
                        },
                        scope = viewModelScope,
                        dispatcher = Dispatchers.IO,
                        builder = {
                            onLoading {
                                viewModelScope.launch {
                                    LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                                }
                            }
                            onSuccess { resp ->
                                viewModelScope.launch {
                                    val profileData = resp?.data
                                    if (profileData != null) {
                                        _profileState.update {
                                            it.copy(
                                                requireProfileFetching = false,
                                                usernae = profileData.name ?: ""
                                            )
                                        }
                                        _basicInfoUiState.update {
                                            it.copy(
                                                id = profileData.userId ?: "",
                                                name = profileData.name ?: "",
                                                city = "",
                                                state = "",
                                                email = profileData.email ?: "",
                                                mobileNumber = profileData.mobileNumber ?: "",
                                                kycStatus = profileData.kycType ?: "",
                                                gstin = "",
                                                invoicingAddress = ""
                                            )
                                        }
                                        onSuccess()
                                    }
                                    ShowSnackBarEvent.helper.emit(
                                        ShowSnackBarEvent.show(
                                            SnackBarType.SuccessSnackBar,
                                            UiText.StringResource(R.string.profile_data_fetched_successfully)
                                        )
                                    )
                                }
                            }
                            onFailure { msg, _, _, resp ->
                                viewModelScope.launch {
                                    LoadingErrorEvent.helper.emit(
                                        LoadingErrorEvent.errorEncountered(
                                            UiText.DynamicString(
                                                resp?.statusDesc ?: ""
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    /**
     * Handles text changes in various input fields.
     */
    private fun handleTextChanged(type: CommonTextField, text: String) {
        when (type) {
            BasicInfoTextField.MobileNumber -> {
                Validation.isValidPhoneNumber(text, onError = { errorMsg ->
                    _basicInfoEditUiState.update {
                        it.copy(
                            mobileNumber = text,
                            mobileNumberError = true,
                            mobileNumberErrorMessage = errorMsg
                        )
                    }
                }, onValid = {
                    _basicInfoEditUiState.update {
                        it.copy(
                            mobileNumber = text,
                            mobileNumberErrorMessage = UiText.StringResource(
                                com.isu.common.R.string.empty
                            ),
                            mobileNumberError = false
                        )
                    }
                })


            }

            BasicInfoTextField.KycStatus -> {
                _basicInfoEditUiState.update {
                    it.copy(kycStatus = text)
                }
            }

            BasicInfoTextField.City -> {
                _basicInfoEditUiState.update {
                    it.copy(
                        city = text, cityError = false, cityErrorMessage = UiText.StringResource(
                            com.isu.common.R.string.empty
                        )
                    )
                }
            }

            BasicInfoTextField.Pincode -> {
                if (text.length <= 6 && text.isDigitsOnly()) {
                    if (text.length < 6) {
                        _basicInfoEditUiState.update {
                            it.copy(
                                pincode = text,
                                pincodeError = true,
                                pincodeErrorMessage = UiText.StringResource(
                                    R.string.pincode_should_be_6_digits
                                )
                            )
                        }
                    } else {
                        _basicInfoEditUiState.update {
                            it.copy(
                                pincode = text,
                                pincodeError = false,
                                pincodeErrorMessage = UiText.StringResource(
                                    com.isu.common.R.string.empty
                                )
                            )
                        }
                        val pin = try {
                            text.toInt()
                        } catch (e: Exception) {
                            0
                        }
                        val request = FetchPinCodeDataRequest(pin = pin)
                        fetchPinCodeDetails(request)
                    }

                }
            }


            BasicInfoTextField.Email -> {
                if (Validation.isValidEmail(text)) {
                    _basicInfoEditUiState.update {
                        it.copy(
                            email = text,
                            emailError = false,
                            emailErrorMessage = UiText.StringResource(
                                com.isu.common.R.string.empty
                            )
                        )
                    }
                } else {
                    _basicInfoEditUiState.update {
                        it.copy(
                            email = text,
                            emailError = true,
                            emailErrorMessage = UiText.StringResource(
                                R.string.please_enter_a_valid_email
                            )
                        )
                    }
                }

            }

            BasicInfoTextField.Gstin -> {
                _basicInfoEditUiState.update {
                    it.copy(gstin = text)
                }
            }


            BasicInfoTextField.InvoicingAddress -> {
                if (text.length <= 30) {
                    _basicInfoEditUiState.update {
                        it.copy(
                            invoicingAddress = text,
                            invoicingAddressError = false,
                            invoicingAddressErrorMessage = UiText.StringResource(
                                com.isu.common.R.string.empty
                            )
                        )
                    }
                }

            }

            BasicInfoTextField.Name -> {
                if (text.length <= 40) {


                    if (text.all { it.isLetter() }) {
                        _basicInfoEditUiState.update {
                            it.copy(
                                name = text,
                                nameError = false,
                                nameErrorMessage = UiText.StringResource(
                                    com.isu.common.R.string.empty
                                )
                            )
                        }


                    } else {
                        _basicInfoEditUiState.update {
                            it.copy(
                                name = text,
                                nameError = true,
                                nameErrorMessage = UiText.StringResource(
                                    R.string.please_enter_a_valid_name
                                )
                            )
                        }
                    }
                }


            }

            BasicInfoTextField.State -> {
                _basicInfoEditUiState.update {
                    it.copy(
                        state = text, stateError = false, stateErrorMessage = UiText.StringResource(
                            com.isu.common.R.string.empty
                        )
                    )
                }
            }
        }
    }

    private fun fetchPinCodeDetails(request: FetchPinCodeDataRequest) {
        handleFlow(
            apiCall = { fetchPinCodeUseCase.invoke(request) },
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            builder = {
                onLoading {
                    viewModelScope.launch {
                        LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                    }

                }
                onSuccess { resp ->
                    viewModelScope.launch {
                        if (resp?.statusCode == 0) {
                            setPinDetails(resp)

                        } else {

                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.ErrorSnackBar,
                                    UiText.DynamicString(resp?.data?.statusDesc ?: "")
                                )
                            )

                        }
                    }
                }
                onFailure { _, _, msg, resp ->
                    viewModelScope.launch {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.ErrorSnackBar,
                                UiText.DynamicString(msg.toString())
                            )
                        )
                    }
                }
            })
    }

    private fun setPinDetails(resp: FetchPinCodeDataResponse) {
        _basicInfoEditUiState.update {
            it.copy(
                city = resp.data?.data?.city ?: "",
                state = resp.data?.data?.state ?: "",
                country = "India"

            )
        }
    }

    /**
     * Handles button click events.
     */
    private fun handleButtonClick(type: Clickables) {
        when (type) {
            ProfileClickables.BasicInfo -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(ProfileScreen.BasicInfoScreen)
                }
            }

            ProfileClickables.Notification -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(ProfileScreen.NotificationSettingsScreen)
                }
            }

            BasicInfoClickable.Edit -> {
                viewModelScope.launch {
                    setEditState()
                    NavigationEvent.helper.navigateTo(ProfileScreen.BasicInfoEditScreen)
                }
            }

            ProfileClickables.CustomerSupport -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CustomerSupportScreen.CustomerSupportHomeScreen)
                }
            }

            ProfileClickables.TermsAndCondition -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(ProfileScreen.TermsAndCondition)
                }
            }

            ProfileClickables.PrivacyPolicy -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(ProfileScreen.PrivacyPolicy)
                }
            }

            ProfileClickables.OrderHistory -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.OrderHistory)
                }
            }

            ProfileClickables.AboutUs -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(ProfileScreen.AboutUs)
                }
            }

            ProfileClickables.GetInTouch -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(ProfileScreen.GetInTouch)
                }
            }

            BasicInfoClickable.UpdateInfo -> {
                if (allFieldsAreValid()) {
                    setState()

                    viewModelScope.launch {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.SuccessSnackBar, UiText.StringResource(

                                    R.string.profile_updated_successfully
                                )
                            )
                        )
                        NavigationEvent.helper.emit(NavigationEvent.NavigateBack)
                    }
                }

            }

            BasicInfoClickable.CancelUpdate -> {
                if (allFieldsAreValid()) {
                    viewModelScope.launch {
                        NavigationEvent.helper.emit(NavigationEvent.NavigateBack)
                    }
                }

            }
        }
    }

    private fun allFieldsAreValid(): Boolean {
        val nameIsValid = isNameValid()
        val emailIsValid = isEmailValid()
        val mobileNumberIsValid = isPhoneNumberValid()
        val cityIsValid = isCityValid()
        val countryIsValid = isCountryValid()
        val stateIsValid = isStateValid()
        val pincodeIsValid = isPincodeValid()
        val invoicingAddressIsValid = isInvoicingAddressValid()
        return nameIsValid && emailIsValid && mobileNumberIsValid && cityIsValid && countryIsValid && stateIsValid && pincodeIsValid && invoicingAddressIsValid
    }

    private fun isInvoicingAddressValid(): Boolean {
        if (basicInfoEditUiState.value.invoicingAddress.isEmpty()) {
            _basicInfoEditUiState.update {
                it.copy(
                    invoicingAddressError = true,
                    invoicingAddressErrorMessage = UiText.StringResource(
                        R.string.invoicing_address_cannot_be_empty
                    )
                )
            }
        }
        return (!basicInfoEditUiState.value.invoicingAddressError) && basicInfoEditUiState.value.invoicingAddress.isNotEmpty()
    }

    private fun isPincodeValid(): Boolean {
        if (basicInfoEditUiState.value.pincode.isEmpty()) {
            _basicInfoEditUiState.update {
                it.copy(
                    pincodeError = true, pincodeErrorMessage = UiText.StringResource(

                        R.string.pincode_cannot_be_empty
                    )
                )
            }
        }
        return (!basicInfoEditUiState.value.pincodeError) && basicInfoEditUiState.value.pincode.isNotEmpty()
    }

    private fun isStateValid(): Boolean {
        if (basicInfoEditUiState.value.state.isEmpty()) {
            _basicInfoEditUiState.update {
                it.copy(
                    stateError = true, stateErrorMessage = UiText.StringResource(
                        R.string.state_cannot_be_empty
                    )
                )
            }
        }
        return (!basicInfoEditUiState.value.stateError) && basicInfoEditUiState.value.state.isNotEmpty()
    }

    private fun isCountryValid(): Boolean {
        if (basicInfoEditUiState.value.country.isEmpty()) {
            _basicInfoEditUiState.update {
                it.copy(
                    countryError = true, countryErrorMessage = UiText.StringResource(
                        R.string.country_cannot_be_empty
                    )
                )
            }
        }
        return (!basicInfoEditUiState.value.countryError) && basicInfoEditUiState.value.country.isNotEmpty()
    }

    private fun isCityValid(): Boolean {
        if (basicInfoEditUiState.value.city.isEmpty()) {
            _basicInfoEditUiState.update {
                it.copy(
                    cityError = true, cityErrorMessage = UiText.StringResource(
                        R.string.city_cannot_be_empty
                    )
                )
            }
        }
        return (!basicInfoEditUiState.value.cityError) && basicInfoEditUiState.value.city.isNotEmpty()
    }

    private fun isPhoneNumberValid(): Boolean {
        if (basicInfoEditUiState.value.mobileNumber.isEmpty()) {
            _basicInfoEditUiState.update {
                it.copy(
                    mobileNumberError = true, mobileNumberErrorMessage = UiText.StringResource(

                        R.string.mobile_number_cannot_be_empty
                    )
                )
            }
        }
        return basicInfoEditUiState.value.mobileNumber.isNotEmpty() && (!basicInfoEditUiState.value.mobileNumberError)
    }

    private fun isEmailValid(): Boolean {
        if (basicInfoEditUiState.value.email.isEmpty()) {
            _basicInfoEditUiState.update {
                it.copy(
                    emailError = true, emailErrorMessage = UiText.StringResource(
                        R.string.email_cannot_be_empty
                    )
                )
            }
        }
        return basicInfoEditUiState.value.email.isNotEmpty() && (!basicInfoEditUiState.value.emailError)
    }

    private fun isNameValid(): Boolean {
        if (basicInfoEditUiState.value.name.isEmpty()) {
            _basicInfoEditUiState.update {
                it.copy(
                    nameError = true, nameErrorMessage = UiText.StringResource(

                        R.string.name_cannot_be_empty
                    )
                )
            }
        }
        return basicInfoEditUiState.value.name.isNotEmpty() && (!basicInfoEditUiState.value.nameError)
    }

    /**
     * Sets the edit state based on the current basic info state.
     */
    private fun setEditState() {
        _basicInfoEditUiState.update {
            it.copy(
                id = basicInfoUiState.value.id,
                city = basicInfoUiState.value.city,
                name = basicInfoUiState.value.name,
                email = basicInfoUiState.value.email,
                gstin = basicInfoUiState.value.gstin,
                mobileNumber = basicInfoUiState.value.mobileNumber,
                invoicingAddress = basicInfoUiState.value.invoicingAddress,
                state = basicInfoUiState.value.state,
                country = basicInfoUiState.value.country
            )
        }
    }

    /**
     * Updates the basic info state based on the current edit state.
     */
    private fun setState() {
        _basicInfoUiState.update {
            it.copy(
                city = basicInfoEditUiState.value.city,
                name = basicInfoEditUiState.value.name,
                email = basicInfoEditUiState.value.email,
                gstin = basicInfoEditUiState.value.gstin,
                invoicingAddress = basicInfoEditUiState.value.invoicingAddress,
                state = basicInfoEditUiState.value.state,
                country = basicInfoEditUiState.value.country
            )
        }
    }
}
