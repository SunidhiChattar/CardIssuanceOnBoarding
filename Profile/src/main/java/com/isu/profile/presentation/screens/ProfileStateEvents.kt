package com.isu.profile.presentation.screens

import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.StateEvent
import com.isu.profile.presentation.screens.basicInfo.BasicInfoEditState
import com.isu.profile.presentation.screens.basicInfo.BasicInfoState
import com.isu.profile.presentation.screens.changepassword.ChangePasswordUiState
import com.isu.profile.presentation.screens.customersupport.presentation.screens.getintouch.GetInTouchUiState
import com.isu.profile.presentation.screens.customersupport.presentation.screens.profile.ProfileState

/**
 * @author-karthik
 * Data class representing the state events for various profile-related screens.
 *
 * @property homeScreen State event for the home screen with associated state and events.
 * @property basicInfoScreen State event for the basic info screen with associated state and events.
 * @property basicInfoEditScreen State event for the basic info edit screen with associated state and events.
 * @property changePasswordScreenStateEvents State event for the change password screen with associated state and events.
 * @property getInTouchStateEvent State event for the get in touch screen with associated state and events.
 */
data class ProfileStateEvents(
    val homeScreen: StateEvent<ProfileState, CommonScreenEvents>,
    val basicInfoScreen: StateEvent<BasicInfoState, CommonScreenEvents>,
    val basicInfoEditScreen: StateEvent<BasicInfoEditState, CommonScreenEvents>,
    val changePasswordScreenStateEvents: StateEvent<ChangePasswordUiState, CommonScreenEvents>,
    val getInTouchStateEvent: StateEvent<GetInTouchUiState, CommonScreenEvents>
)

