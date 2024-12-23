package com.isu.profile.presentation.screens.customersupport.presentation.screens.getintouch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.R
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileDropDown
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.navigation.CustomerSupportScreen
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.errorColor
import com.isu.common.ui.theme.plaeholderColor
import com.isu.common.utils.FontProvider.LATO_FONT
import kotlinx.coroutines.launch

/**
 * @author-karthik
 * Composable function for the "Get in Touch" screen.
 *
 * @param modifier Modifier to be applied to the screen.
 * @param state The UI state containing all the necessary data for the screen.
 * @param event Callback to handle screen events.
 */
@Composable
fun GetInTouchScreen(
    modifier: Modifier = Modifier,
    state: GetInTouchUiState,
    event: (CommonScreenEvents) -> Unit
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { CustomProfileTopBar(stringResource(com.isu.profile.R.string.get_in_touch)) },
        containerColor = Color.White
    ) {
        KeyBoardAwareScreen(
            modifier = Modifier.padding(
                start = 22.dp,
                end = 22.dp,
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            ),
            shouldScroll = false
        ) {
            // First Name Input Field
            CustomInputField(
                label = stringResource(com.isu.profile.R.string.first_name),
                labelRequired = true,
                labelColor = Color.Black,
                placeholder = stringResource(com.isu.profile.R.string.e_g_john),
                state = state.firstName,
                isError = state.firstNameError,
                errorMessage = state.firstNameErrorMessage,

            ) {
                event(CommonScreenEvents.OnTextChanged(it,GetInTouchInput.FirstName))
            }

            // Last Name Input Field
            CustomInputField(
                label = stringResource(com.isu.profile.R.string.last_name),
                labelRequired = true,
                placeholder = stringResource(com.isu.profile.R.string.smith),
                state = state.lastName,
                isError = state.lastNameError,
                errorMessage = state.lastNameErrorMessage,
                labelColor = Color.Black
            ) {
                event(CommonScreenEvents.OnTextChanged(it,GetInTouchInput.LastName))
            }

            // Company Name Input Field
            CustomInputField(
                label = stringResource(com.isu.profile.R.string.company_name),
                labelRequired = true,
                placeholder = stringResource(com.isu.profile.R.string.abc_companyname_com),
                state = state.companyName,
                isError = state.companyNameError,
                errorMessage = state.companyNameErrorMessage,
                labelColor = Color.Black
            ) {
                event(CommonScreenEvents.OnTextChanged(it,GetInTouchInput.CompanyName))
            }

            // Phone Number Input Field
            CustomInputField(
                label = stringResource(com.isu.profile.R.string.phone_number),
                labelRequired = true,
                placeholder = stringResource(com.isu.profile.R.string._91_0000_000_000),
                state = state.phoneNumber,
                isError = state.phoneNumberError,
                errorMessage = state.phoneNumberErrorMessage.asString(),
                labelColor = Color.Black
            ) {
                event(CommonScreenEvents.OnTextChanged(it,GetInTouchInput.Phone))
            }

            // Company Size Dropdown
            CustomProfileDropDown(
                items = arrayOf("0-500", "500-1000", "1000+"),
                label = AnnotatedString(stringResource(com.isu.profile.R.string.company_size)),
                placeholder = stringResource(com.isu.profile.R.string.select),
                labelRequired = true,
                state = state.companySize,
                isError = state.companySizeError,
                errorMessage = state.companySizeErrorMessage,
                labelColor = Color.Black
            ) {
                event(CommonScreenEvents.OnTextChanged(it,GetInTouchInput.CompanySize))
            }

            // Country Dropdown
            CustomProfileDropDown(
                label = AnnotatedString(stringResource(com.isu.profile.R.string.country)),
                state = state.country,
                placeholder = stringResource(com.isu.profile.R.string.country),
                labelRequired = true,
                isError = state.countryError,
                errorMessage = state.countryErrorMessage,
                labelColor = Color.Black
            ) {
                event(CommonScreenEvents.OnTextChanged(it,GetInTouchInput.Country))
            }

            // Point of Discussion Input Field
            CustomInputField(
                label = stringResource(com.isu.profile.R.string.what_would_you_like_to_discuss),
                modifier = Modifier.height(150.dp),
                state = state.pointOfDiscussion,
                isError = state.pointOfDiscussionError,
                errorMessage = state.pointOfDiscussionErrorMessage,
                placeholderComposable = {
                    CustomText(
                        text = stringResource(com.isu.profile.R.string.point_of_discussion),
                        color = if (state.pointOfDiscussionError) errorColor else plaeholderColor,
                        fontSize = 13.sp.noFontScale(),
                        textAlign = TextAlign.Start
                    )
                },
                labelRequired = true,
                labelColor = Color.Black
            ) {
                event(CommonScreenEvents.OnTextChanged(it,GetInTouchInput.PointOfDiscussion))
            }

            // Submit Button
            CustomButton(
                text = stringResource(com.isu.profile.R.string.submit),
                onClick = {

                }
            )

            Spacer(modifier.height(20.dp))

            // Support Row
            Row(modifier = Modifier.clickable {
                scope.launch {
                    NavigationEvent.helper.navigateTo(CustomerSupportScreen.CustomerSupportHomeScreen)
                }

            }) {
                CustomText(
                    text = stringResource(com.isu.profile.R.string.looking_for_support),
                    color = appMainColor
                )
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "", tint = appMainColor)
            }

            Spacer(Modifier.height(20.dp))

            // Copyright Text
            CustomText(
                text = stringResource(R.string.copyright),
                modifier = Modifier.padding(horizontal = 38.dp),
                fontFamily = LATO_FONT,
                fontSize = 14.sp.noFontScale(),
                lineHeight = 16.sp.noFontScale(),
                textAlign = TextAlign.Center,
                color = Color.LightGray
            )

            Spacer(Modifier.height(25.dp))
        }
    }
}
