package com.isu.profile.presentation.screens.changepassword

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.isu.common.R
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomCancelButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.PasswordStrengthBar
import com.isu.common.events.CommonScreenEvents
import com.isu.common.ui.theme.appMainColor
import com.isu.common.utils.calculatePasswordStrength

/**
 * @author-karthik
 * Composable function for the Change Password screen.
 *
 * @param modifier Modifier for styling and layout.
 * @param state UI state of the Change Password screen.
 * @param onEvent Lambda function for handling screen events.
 */
@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    state: ChangePasswordUiState,
    onEvent: (CommonScreenEvents) -> Unit
) {
    val oldPassword = state.oldPassword
    val oldPasswordError = state.oldPasswordError
    val oldPasswordErrorMessage = state.oldPasswordErrorMessage
    val oldPasswordHideState = state.oldPasswordHideState

    val newPassword = state.newPassword
    val newPasswordError = state.newPasswordError
    val newPasswordErrorMessage = state.newPasswordErrorMessage
    val newPasswordHideState = state.newPasswordHideState

    val confirmPassword = state.confirmPassword
    val confirmPasswordError = state.confirmPasswordError
    val confirmPasswordErrorMessage = state.confirmPasswordErrorMessage
    val confirmPasswordHideState = state.confirmPasswordHideState

    val oldPasswordTransformation = remember(state) {
        mutableStateOf(if (oldPasswordHideState) PasswordVisualTransformation('*') else VisualTransformation.None)
    }
    val newPasswordTransformation = remember(state) {
        mutableStateOf(if (newPasswordHideState) PasswordVisualTransformation('*') else VisualTransformation.None)
    }
    val confirmPasswordTransformation = remember(state) {
        mutableStateOf(if (confirmPasswordHideState) PasswordVisualTransformation('*') else VisualTransformation.None)
    }

    val screenHeight: Int = LocalConfiguration.current.screenHeightDp

    Scaffold(
        topBar = { CustomProfileTopBar(stringResource(com.isu.profile.R.string.change_password_hd)) },
        containerColor = Color.White
    ) {
        LaunchedEffect(Unit){
            onEvent(CommonScreenEvents.ClearFields)
        }
        KeyBoardAwareScreen(modifier = Modifier.padding(it), shouldScroll = false) {
            Column(
                modifier = Modifier
                    .heightIn(screenHeight.dp - it.calculateTopPadding() - it.calculateBottomPadding() - 80.dp)
                    .padding(horizontal = 22.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    // Old Password Input Field
                    CustomInputField(
                        labelColor = Color.Black,
                        label = stringResource(R.string.current_password),
                        state = oldPassword,
                        placeholder = stringResource(R.string.enter_current_password),
                        onValueChange = {
                            onEvent(CommonScreenEvents.OnTextChanged(it, ChangePasswordTextField.OldPassword))
                        },
                        isError = oldPasswordError,
                        errorMessage = oldPasswordErrorMessage.asString(),
                        visualTransformation = oldPasswordTransformation.value,
                        trailingIcon = {}
                    )

                    // New Password Input Field
                    CustomInputField(
                        labelColor = Color.Black,
                        label = stringResource(R.string.new_password),
                        state = newPassword,
                        placeholder = stringResource(R.string.set_new_password),
                        onValueChange = {
                            onEvent(CommonScreenEvents.OnTextChanged(it, ChangePasswordTextField.NewPassword))
                        },
                        isError = newPasswordError,
                        errorMessage = newPasswordErrorMessage.asString(),
                        visualTransformation = newPasswordTransformation.value,
                        trailingIcon = {
                            CustomText(
                                text = if (newPasswordHideState) stringResource(R.string.show) else stringResource(R.string.hide),
                                color = appMainColor,
                                modifier = Modifier.clickable {
                                    onEvent(CommonScreenEvents.OnClick<Any>(ChangePasswordClickables.NewPasswordHide))
                                },
                                fontWeight = FontWeight(700)
                            )
                        }
                    )

                    // Password Strength Bar
                    PasswordStrengthBar(
                        barCount = 5,
                        strength = calculatePasswordStrength(newPassword).toFloat()
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    // Confirm Password Input Field
                    CustomInputField(
                        labelColor = Color.Black,
                        label = stringResource(R.string.confirm_password_labrl),
                        placeholder = stringResource(R.string.confirm_password_labrl),
                        state = confirmPassword,
                        onValueChange = {
                            onEvent(CommonScreenEvents.OnTextChanged(it, ChangePasswordTextField.ConfirmPassword))
                        },
                        isError = confirmPasswordError,
                        errorMessage = confirmPasswordErrorMessage.asString(),
                        visualTransformation = confirmPasswordTransformation.value,
                        trailingIcon = {
                            CustomText(
                                text = if (confirmPasswordHideState) stringResource(R.string.show) else stringResource(R.string.hide),
                                color = appMainColor,
                                modifier = Modifier.clickable {
                                    onEvent(CommonScreenEvents.OnClick<Any>(ChangePasswordClickables.ConfirmPasswordHide))
                                },
                                fontWeight = FontWeight(700)
                            )
                        }
                    )
                }

                Column {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CustomButton(
                            text = stringResource(R.string.reset_password),
                            shape = RoundedCornerShape(5.dp),
                            onClick = {
                                onEvent(CommonScreenEvents.OnClick<Any>(ChangePasswordClickables.ChangePassword))
                            }
                        )
                        CustomCancelButton(text = stringResource(R.string.cancel), onClick = {
                            onEvent(CommonScreenEvents.OnClick<Any>(ChangePasswordClickables.Cancel))
                        })
                    }
                }
            }
        }
    }
}
