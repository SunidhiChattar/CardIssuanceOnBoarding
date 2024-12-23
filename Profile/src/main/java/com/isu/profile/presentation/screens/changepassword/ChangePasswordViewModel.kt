package com.isu.profile.presentation.screens.changepassword

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.R
import com.isu.common.events.APIType
import com.isu.common.events.Clickables
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.CommonTextField
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.utils.ApiUseCase
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.profile.data.remote.model.request.AuthTokenData
import com.isu.profile.data.remote.model.request.ChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.request.GenerateOTPChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.response.ChangePasswordUsingOldPasswordResponse
import com.isu.profile.data.remote.model.response.GenerateOTPChangePasswordUsingOldPasswordResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    val otpToChangePasswordUsecase: ApiUseCase<AuthTokenData<GenerateOTPChangePasswordUsingOldPasswordRequest>, GenerateOTPChangePasswordUsingOldPasswordResponse>,
    val changePasswordViaOldPasswordUseCase: ApiUseCase<AuthTokenData<ChangePasswordUsingOldPasswordRequest>, ChangePasswordUsingOldPasswordResponse>,
) :
    ViewModel() {
    //ui state for change password screen
    private val _changePasswordUiState = MutableStateFlow(ChangePasswordUiState())
    val changePasswordUiState: StateFlow<ChangePasswordUiState> = _changePasswordUiState

    fun onEvent(event: CommonScreenEvents) {
        when (event) {
            is CommonScreenEvents.CallApi<*> -> {
                handleApiCall(event.apiType, onSuccess = { event.onSuccess() })
            }

            CommonScreenEvents.ClearFields -> {
                _changePasswordUiState.value = ChangePasswordUiState()
            }

            CommonScreenEvents.ClearStack -> {}
            is CommonScreenEvents.NavigateTo -> {}
            is CommonScreenEvents.OnCheckChanged -> {}
            is CommonScreenEvents.OnClick<*> -> handleClickEvents(
                event.type,
                onComplete = { event.onComplete() })

            is CommonScreenEvents.OnTextChanged -> handleInputChange(event.type, event.text)
            else -> {}
        }
    }

    private fun handleApiCall(apiType: APIType, onSuccess: () -> Unit) {
        when (apiType) {
            ChangePasswordApiType.OtpToChangePassword -> {
//                { callFetchOtpApi(onSuccess = { onSuccess() }) }
            }
        }
    }

    private fun callFetchOtpApi(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val username = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
            val authToken = dataStoreManager.getPreferenceValue(PreferencesKeys.AUTH_TOKEN)
            val otpRequest = GenerateOTPChangePasswordUsingOldPasswordRequest(
                newPassword = _changePasswordUiState.value.newPassword,
                oldPassword = _changePasswordUiState.value.oldPassword

            )
            val apiRequest = AuthTokenData<GenerateOTPChangePasswordUsingOldPasswordRequest>(
                authorization = authToken ?: "",
                tokenProperties = username ?: "",
                request = otpRequest
            )
            handleFlow(
                apiCall = { otpToChangePasswordUsecase.invoke(apiRequest) },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                builder = {
                    onLoading {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                        }

                    }
                    onSuccess {

                        viewModelScope.launch {
                            if (it?.status?.matches("SUCCESS".toRegex()) == true) {
                                onSuccess()
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.SuccessSnackBar,
                                        UiText.StringResource(R.string.otp_sent_successfully)
                                    )
                                )
                            } else {
                                LoadingErrorEvent.helper.emit(
                                    LoadingErrorEvent.errorEncountered(
                                        UiText.DynamicString(it?.message ?: "")
                                    )
                                )
                            }
                        }
                    }
                    onFailure { code, i, s, resp ->
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        resp?.message ?: ""
                                    )
                                )
                            )
                        }
                    }
                }

            )
        }
    }

    private fun handleClickEvents(type: Clickables, onComplete: () -> Unit) {
        when (type) {
            ChangePasswordClickables.Cancel -> {
                if (allFieldsAreValid()) {
                    viewModelScope.launch {
                        NavigationEvent.helper.navigateBack()
                    }
                }

            }

            ChangePasswordClickables.ChangePassword -> {
                Log.d("KAPIVALID", "handleClickEvents: ${allFieldsAreValid()}")
                if (allFieldsAreValid()) {
                    viewModelScope.launch {
                        callFetchOtpApi(onSuccess = {
                            viewModelScope.launch {
                                NavigationEvent.helper.navigateTo(ProfileScreen.ChangePaswordScreenOTP)
                            }
                        })

                    }
                }

            }

            ChangePasswordClickables.ConfirmPasswordHide -> {
                _changePasswordUiState.update {
                    it.copy(confirmPasswordHideState = !it.confirmPasswordHideState)
                }
            }

            ChangePasswordClickables.NewPasswordHide -> {
                _changePasswordUiState.update {
                    it.copy(newPasswordHideState = !it.newPasswordHideState)
                }
            }

            ChangePasswordClickables.OldPasswordHide -> {
                _changePasswordUiState.update {
                    it.copy(oldPasswordHideState = !it.oldPasswordHideState)
                }
            }

            ChangePasswordClickables.ResendOTP -> {
                callFetchOtpApi {
                    onComplete()
                }
            }

            ChangePasswordClickables.VeifyOTP -> {
                if (allOTPScreenFieldsAreValid()) {
                    changePassword(onsuccess = { onComplete() })
                }
            }
        }
    }

    private fun isOtpValid(): Boolean {
        if (changePasswordUiState.value.otp.isEmpty()) {
            _changePasswordUiState.update {
                it.copy(
                    otpError = true,
                    otpErrorMessage = UiText.StringResource(R.string.empty_field)
                )
            }
        }
        return (!changePasswordUiState.value.otpError) && changePasswordUiState.value.otp.isNotEmpty()
    }

    private fun allOTPScreenFieldsAreValid(): Boolean {
        val otpValid = isOtpValid()
        return allFieldsAreValid() && otpValid
    }

    private fun allFieldsAreValid(): Boolean {
        val oldPasswordIsValid = isOldPasswordValid()
        val newPasswordIsValid = isNewPasswordValid()
        val confirmPasswordIsValid = isConfirmPasswordValid()
        return oldPasswordIsValid && newPasswordIsValid && confirmPasswordIsValid
    }

    private fun isConfirmPasswordValid(): Boolean {
        if (changePasswordUiState.value.confirmPassword.isEmpty()) {
            _changePasswordUiState.update {
                it.copy(
                    confirmPasswordError = true,
                    confirmPasswordErrorMessage = UiText.StringResource(R.string.empty_field)
                )
            }
        }
        return changePasswordUiState.value.confirmPassword.isNotEmpty() && (!changePasswordUiState.value.confirmPasswordError)
    }

    private fun isNewPasswordValid(): Boolean {
        if (changePasswordUiState.value.newPassword.isEmpty()) {
            _changePasswordUiState.update {
                it.copy(
                    newPasswordError = true,
                    newPasswordErrorMessage = UiText.StringResource(R.string.empty_field)
                )
            }
        }
        return changePasswordUiState.value.newPassword.isNotEmpty() && (!changePasswordUiState.value.newPasswordError)
    }

    private fun isOldPasswordValid(): Boolean {
        if (changePasswordUiState.value.oldPassword.isEmpty()) {
            _changePasswordUiState.update {
                it.copy(
                    oldPasswordError = true,
                    oldPasswordErrorMessage = UiText.StringResource(R.string.empty_field)
                )
            }
        }
        return changePasswordUiState.value.oldPassword.isNotEmpty()
    }

    private fun changePassword(onsuccess: () -> Unit) {


        viewModelScope.launch {
            val username = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
            val authToken = dataStoreManager.getPreferenceValue(PreferencesKeys.AUTH_TOKEN)
            val changePasswordReques = ChangePasswordUsingOldPasswordRequest(
                newPassword = changePasswordUiState.value.newPassword,
                oldPassword = changePasswordUiState.value.oldPassword,
                otp = changePasswordUiState.value.otp
            )
            val apiRequest = AuthTokenData<ChangePasswordUsingOldPasswordRequest>(
                authorization = authToken ?: "",
                tokenProperties = username ?: "",
                request = changePasswordReques
            )
            handleFlow(
                apiCall = { changePasswordViaOldPasswordUseCase.invoke(apiRequest) },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                builder = {
                    onLoading {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                        }

                    }
                    onSuccess {

                        viewModelScope.launch {
                            if (it?.status?.matches("SUCCESS".toRegex()) == true) {
                                onsuccess()
                                _changePasswordUiState.value = ChangePasswordUiState()
                                NavigationEvent.helper.emit(
                                    NavigationEvent.NavigateToNextScreen(
                                        ProfileScreen.HomeScreen,
                                        popUpTo = ProfileScreen.HomeScreen,
                                        true
                                    )
                                )
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.SuccessSnackBar,
                                        UiText.StringResource(com.isu.profile.R.string.password_changed_successfully)
                                    )
                                )
                            } else {
                                LoadingErrorEvent.helper.emit(
                                    LoadingErrorEvent.errorEncountered(
                                        UiText.DynamicString(it?.message ?: "")
                                    )
                                )
                            }
                        }
                    }
                    onFailure { code, i, s, resp ->
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        resp?.message ?: "Something went wrong"
                                    )
                                )
                            )
                        }
                    }
                }
            )

        }
    }

    private fun handleInputChange(type: CommonTextField, text: String) {
        when (type) {
            ChangePasswordTextField.ConfirmPassword -> {
                handleConfirmPasswordInputChange(text)
            }

            ChangePasswordTextField.NewPassword -> {
                handleNewPasswordInputChange(text)
            }

            ChangePasswordTextField.OldPassword -> {
                _changePasswordUiState.update {
                    it.copy(
                        oldPassword = text,
                        oldPasswordError = false,
                        oldPasswordErrorMessage = UiText.StringResource(
                            R.string.empty
                        )
                    )
                }
            }

            ChangePasswordTextField.Otp -> {
                if (text.isDigitsOnly() && text.length <= 6) {
                    if (text.length < 6) {
                        _changePasswordUiState.update {
                            it.copy(
                                otp = text,
                                otpError = false,
                                otpErrorMessage = UiText.StringResource(R.string.otp_must_be_6_digits)
                            )
                        }
                    } else {
                        _changePasswordUiState.update {
                            it.copy(
                                otp = text,
                                otpError = false,
                                otpErrorMessage = UiText.StringResource(R.string.empty)
                            )
                        }
                    }

                }
            }
        }
    }

    private fun handleNewPasswordInputChange(text: String) {
        val confirmPassword = _changePasswordUiState.value.confirmPassword
        if (confirmPassword.isEmpty() || text.matches(confirmPassword.toRegex())) {
            _changePasswordUiState.update {
                it.copy(
                    newPassword = text,
                    confirmPasswordError = false,
                    confirmPasswordErrorMessage = UiText.StringResource(R.string.empty),
                    newPasswordError = false,
                    newPasswordErrorMessage = UiText.StringResource(R.string.empty)
                )
            }
        } else {
            _changePasswordUiState.update {
                it.copy(
                    newPassword = text,
                    confirmPasswordError = true,
                    confirmPasswordErrorMessage = UiText.StringResource(R.string.password_nomatch),
                    newPasswordError = true,
                    newPasswordErrorMessage = UiText.StringResource(R.string.password_nomatch)
                )
            }
        }
    }

    private fun handleConfirmPasswordInputChange(text: String) {
        val newPassword = _changePasswordUiState.value.newPassword
        if (!text.matches(newPassword.toRegex())) {
            _changePasswordUiState.update {
                it.copy(
                    confirmPassword = text,
                    confirmPasswordError = true,
                    confirmPasswordErrorMessage = UiText.StringResource(R.string.password_nomatch),
                    newPasswordError = true,
                    newPasswordErrorMessage = UiText.StringResource(R.string.password_nomatch)
                )
            }
        } else {
            _changePasswordUiState.update {
                it.copy(
                    confirmPassword = text,
                    confirmPasswordError = false,
                    confirmPasswordErrorMessage = UiText.StringResource(R.string.empty),
                    newPasswordError = false,
                    newPasswordErrorMessage = UiText.StringResource(R.string.empty)
                )
            }
        }
    }


}