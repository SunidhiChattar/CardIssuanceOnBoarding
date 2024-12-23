package com.isu.profile.presentation.screens.customersupport.presentation.screens.getintouch

import com.isu.common.events.CommonTextField

/**
 * @author-karthik
 * Interface representing the different types of input fields
 * for the "Get in Touch" screen.
 */
sealed interface GetInTouchInput : CommonTextField {

    /** Represents the "First Name" input field. */
    data object FirstName : GetInTouchInput

    /** Represents the "Last Name" input field. */
    data object LastName : GetInTouchInput

    /** Represents the "Company Name" input field. */
    data object CompanyName : GetInTouchInput

    /** Represents the "Company Size" input field. */
    data object CompanySize : GetInTouchInput

    /** Represents the "Phone Number" input field. */
    data object Phone : GetInTouchInput

    /** Represents the "Country" input field. */
    data object Country : GetInTouchInput

    /** Represents the "Point of Discussion" input field. */
    data object PointOfDiscussion : GetInTouchInput
}
