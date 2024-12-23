package com.isu.profile.presentation.screens.basicInfo

import com.isu.common.events.Clickables
import com.isu.common.events.CommonTextField

/**
 * @author-karthik
 * classes representing screen components on which events are triggered
 */
/**
 * Sealed interface representing the clickable elements in the Basic Info screen.
 */
sealed interface BasicInfoClickable : Clickables {

    /**
     * Represents the "Edit" clickable element.
     */
    data object Edit : BasicInfoClickable

    /**
     * Represents the "Update Info" clickable element.
     */
    data object UpdateInfo : BasicInfoClickable

    /**
     * Represents the "Cancel Update" clickable element.
     */
    data object CancelUpdate : BasicInfoClickable
}

/**
 * Sealed interface representing the text fields in the Basic Info screen.
 */
sealed interface BasicInfoTextField : CommonTextField {

    /**
     * Represents the ID text field.
     */
    data object ID : BasicInfoTextField

    /**
     * Represents the Name text field.
     */
    data object Name : BasicInfoTextField

    /**
     * Represents the City text field.
     */
    data object City : BasicInfoTextField

    /**
     * Represents the State text field.
     */
    data object State : BasicInfoTextField

    /**
     * Represents the Country text field.
     */
    data object Country : BasicInfoTextField

    /**
     * Represents the GSTIN text field.
     */
    data object Gstin : BasicInfoTextField

    /**
     * Represents the Email text field.
     */
    data object Email : BasicInfoTextField

    /**
     * Represents the Invoicing Address text field.
     */
    data object InvoicingAddress : BasicInfoTextField

    /**
     * Represents the Pincode text field.
     */
    data object Pincode : BasicInfoTextField

    /**
     * Represents the KYC Status text field.
     */
    data object KycStatus : BasicInfoTextField

    /**
     * Represents the Mobile Number text field.
     */
    data object MobileNumber : BasicInfoTextField
}
