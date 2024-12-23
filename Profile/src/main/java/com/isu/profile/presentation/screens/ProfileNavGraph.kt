package com.isu.profile.presentation.screens

import androidx.navigation.NavGraphBuilder
import com.isu.common.customcomposables.CustomComposable
import com.isu.common.navigation.ProfileScreen
import com.isu.profile.presentation.screens.basicInfo.BasicInfoEditScreen
import com.isu.profile.presentation.screens.basicInfo.BasicInfoScreen
import com.isu.profile.presentation.screens.changepassword.ChangePasswordScreen
import com.isu.profile.presentation.screens.changepassword.OtpScreenTOChangeOldPassword
import com.isu.profile.presentation.screens.customersupport.presentation.screens.getintouch.GetInTouchScreen
import com.isu.profile.presentation.screens.customersupport.presentation.screens.profile.ProfileScreen

/**
 * @author-karthik
 * Sets up the navigation graph for profile-related screens.
 *
 * @param profileStateEvents An instance of [ProfileStateEvents] that holds the state and events for various screens.
 */
fun NavGraphBuilder.setUpProfileNavGraph(profileStateEvents: ProfileStateEvents) {

    CustomComposable<ProfileScreen.HomeScreen> {
        ProfileScreen(
            state = profileStateEvents.homeScreen.state,
            event = profileStateEvents.homeScreen.event
        )
    }

    CustomComposable<ProfileScreen.BasicInfoScreen> {
        BasicInfoScreen(
            state = profileStateEvents.basicInfoScreen.state,
            event = profileStateEvents.basicInfoScreen.event
        )
    }

    CustomComposable<ProfileScreen.BasicInfoEditScreen> {
        BasicInfoEditScreen(
            state = profileStateEvents.basicInfoEditScreen.state,
            event = profileStateEvents.basicInfoEditScreen.event
        )
    }

    CustomComposable<ProfileScreen.NotificationSettingsScreen> {
        NotificationSettings()
    }

    CustomComposable<ProfileScreen.PrivacyPolicy> {
        PrivacyPolicyScreen()
    }

    CustomComposable<ProfileScreen.AboutUs> {
        AboutUsScreen()
    }

    CustomComposable<ProfileScreen.TermsAndCondition> {
        TermsAndConditionScreen()
    }

    CustomComposable<ProfileScreen.ChangePaswordScreen> {
        ChangePasswordScreen(
            state = profileStateEvents.changePasswordScreenStateEvents.state,
            onEvent = profileStateEvents.changePasswordScreenStateEvents.event
        )
    }
    CustomComposable<ProfileScreen.ChangePaswordScreenOTP> {
        OtpScreenTOChangeOldPassword(
            screenState = profileStateEvents.changePasswordScreenStateEvents.state,
            onEvent = profileStateEvents.changePasswordScreenStateEvents.event
        )
    }

    CustomComposable<ProfileScreen.GetInTouch> {
        GetInTouchScreen(
            state = profileStateEvents.getInTouchStateEvent.state,
            event = profileStateEvents.getInTouchStateEvent.event
        )
    }
}
