package com.isu.profile.presentation.screens.basicInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomCancelButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileDropDown
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.utils.FontProvider
import com.isu.common.utils.FontProvider.getFont
import com.isu.profile.R
import com.isu.profile.presentation.screens.customersupport.presentation.screens.profile.ProfileImageSection

/**
 * @author-karthik
 * Composable function for displaying the Basic Info Edit Screen.
 *
 * @param state The state holding the current values and error messages.
 * @param event Callback for handling events such as text changes and button clicks.
 */
@Composable
fun BasicInfoEditScreen(
    state: BasicInfoEditState,
    event: (CommonScreenEvents) -> Unit
) {
    val config = androidx.compose.ui.platform.LocalConfiguration.current
    val screenHeight = config.screenHeightDp

    Scaffold(
        topBar = { CustomProfileTopBar(stringResource(R.string.edit_basic_info)) },
        containerColor = Color.White
    ) { paddingValues ->
        KeyBoardAwareScreen(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .fillMaxHeight(),
            shouldScroll = false
        ) {
            Column(
                modifier = Modifier.heightIn(min = screenHeight.dp - 150.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    ProfileImageSection(state = state.name)
                    Column(Modifier.padding(horizontal = 5.dp)) {
                        CustomInputField(
                            label = "ID",
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                            ),
                            enabled = false,
                            state = state.id,
                            isError = state.idError,
                            errorMessage = state.idErrorMessage.asString(),
                            onValueChange = {
                                event(
                                    CommonScreenEvents.OnTextChanged(
                                        it,
                                        BasicInfoTextField.ID
                                    )
                                )
                            }
                        )
                        CustomInputField(
                            label = stringResource(R.string.name_edit),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                            ),
                            state = state.name,
                            enabled = false,
                            placeholder = stringResource(R.string.please_enter_name),
                            isError = state.nameError,
                            errorMessage = state.nameErrorMessage.asString(),
                            onValueChange = {

                            }
                        )
                        CustomInputField(
                            label = stringResource(R.string.invoicing_address_edit),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                            ),
                            state = state.invoicingAddress,
                            placeholder = stringResource(R.string.please_enter_address),
                            isError = state.invoicingAddressError,
                            errorMessage = state.invoicingAddressErrorMessage.asString(),
                            onValueChange = {
                                event(
                                    CommonScreenEvents.OnTextChanged(
                                        it,
                                        BasicInfoTextField.InvoicingAddress
                                    )
                                )
                            }
                        )
                        CustomInputField(
                            label = stringResource(R.string.email_edit),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                            ),
                            state = state.email,
                            isError = state.emailError,
                            errorMessage = state.emailErrorMessage.asString(),
                            placeholder = stringResource(R.string.please_enter_email),
                            onValueChange = {
                                event(
                                    CommonScreenEvents.OnTextChanged(
                                        it,
                                        BasicInfoTextField.Email
                                    )
                                )
                            }
                        )
                        CustomInputField(
                            label = stringResource(R.string.mobile_number_edit),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Number
                            ),
                            state = state.mobileNumber,
                            isError = state.mobileNumberError,
                            errorMessage = state.mobileNumberErrorMessage.asString(),
                            placeholder = stringResource(R.string.please_enter_mobile_number),
                            onValueChange = {
                                event(
                                    CommonScreenEvents.OnTextChanged(
                                        it,
                                        BasicInfoTextField.MobileNumber
                                    )
                                )
                            }
                        )
                    }
                    Column(modifier = Modifier.padding(horizontal = 5.dp)) {
                        CustomProfileDropDown(
                            modifier = Modifier.heightIn(64.dp),

                            labelColor = Black,
                            enabled = false,
                            label = AnnotatedString(stringResource(R.string.country_edit)),
                            state = state.country,
                            isError = state.countryError,
                            errorMessage = state.countryErrorMessage.asString(),
                            onSelected = {
                            },
                            labelRequired = true,
                            placeholder = stringResource(R.string.please_select_a_country),
                            textStyle = TextStyle(
                                fontSize = 13.sp.noFontScale(),
                                fontFamily = getFont(
                                    FontProvider.LATO_FONT,
                                    weight = FontWeight(400)
                                ),
                                color = Gray,
                                textAlign = TextAlign.Start
                            )
                        )

                        CustomProfileDropDown(
                            modifier = Modifier.heightIn(64.dp),
                            label = AnnotatedString(stringResource(R.string.state_edit)),
                            items = stringArrayResource(R.array.indian_states),
                            labelColor = Black,
                            textStyle = TextStyle(
                                fontSize = 13.sp.noFontScale(),
                                fontFamily = getFont(
                                    FontProvider.LATO_FONT,
                                    weight = FontWeight(400)
                                ),
                                color = Gray,
                                textAlign = TextAlign.Start
                            ),
                            placeholder = stringResource(R.string.please_select_a_state),
                            isError = state.stateError,
                            errorMessage = state.stateErrorMessage.asString(),
                            state = state.state,
                            onSelected = {
                                event(
                                    CommonScreenEvents.OnTextChanged(
                                        it,
                                        BasicInfoTextField.State
                                    )
                                )
                            }
                        )
                        CustomInputField(
                            modifier = Modifier.fillMaxWidth(),
                            label = stringResource(R.string.pin_label),
                            state = state.pincode,

                            onValueChange = {
                                event(
                                    CommonScreenEvents.OnTextChanged(
                                        it,
                                        BasicInfoTextField.Pincode
                                    )
                                )
                            },
                            placeholder = stringResource(R.string.please_enter_pincode),
                            isError = state.pincodeError,
                            errorMessage = state.pincodeErrorMessage.asString(),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Number
                            )
                        )
                        CustomProfileDropDown(
                            modifier = Modifier.heightIn(64.dp),
                            labelColor = Black,
                            label = AnnotatedString(stringResource(R.string.city_edit)),
                            state = state.city,
                            isError = state.cityError,
                            errorMessage = state.cityErrorMessage.asString(),
                            placeholder = stringResource(R.string.please_select_a_city),
                            onSelected = {
                                event(
                                    CommonScreenEvents.OnTextChanged(
                                        it,
                                        BasicInfoTextField.City
                                    )
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 13.sp.noFontScale(),
                                fontFamily = getFont(
                                    FontProvider.LATO_FONT,
                                    weight = FontWeight(400)
                                ),
                                color = Gray,
                                textAlign = TextAlign.Start
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomButton(
                        text = stringResource(R.string.update_btn),
                        shape = RoundedCornerShape(5.dp),
                        onClick = { event(CommonScreenEvents.OnClick<Any>(BasicInfoClickable.UpdateInfo)) }
                    )
                    CustomCancelButton(
                        text = stringResource(R.string.cancel_btn),
                        onClick = { event(CommonScreenEvents.OnClick<Any>(BasicInfoClickable.CancelUpdate)) }
                    )
                }
            }
        }
    }
}



