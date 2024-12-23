package com.isu.profile.presentation.screens.customersupport.presentation.screens.profile

import com.isu.common.events.Clickables

/**
 * @author-karthik
 * Sealed interface representing various clickable items on the profile screen.
 */
sealed interface ProfileClickables : Clickables {

    /**
     * Represents a click event for the Basic Info section.
     */
    data object BasicInfo : ProfileClickables

    /**
     * Represents a click event for the Notification settings.
     */
    data object Notification : ProfileClickables

    /**
     * Represents a click event for accessing Customer Support.
     */
    data object CustomerSupport : ProfileClickables

    /**
     * Represents a click event for viewing the Privacy Policy.
     */
    data object PrivacyPolicy : ProfileClickables

    /**
     * Represents a click event for viewing the About Us section.
     */
    data object AboutUs : ProfileClickables

    /**
     * Represents a click event for viewing the Terms and Conditions.
     */
    data object TermsAndCondition : ProfileClickables

    /**
     * Represents a click event for changing the password.
     */
    data object OrderHistory : ProfileClickables

    /**
     * Represents a click event for the Get in Touch section.
     */
    data object GetInTouch : ProfileClickables
}
