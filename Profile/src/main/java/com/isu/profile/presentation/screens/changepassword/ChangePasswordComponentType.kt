package com.isu.profile.presentation.screens.changepassword

import com.isu.common.events.APIType
import com.isu.common.events.Clickables
import com.isu.common.events.CommonTextField

/**
 * @author-karthik
 * Represents the different text fields in the Change Password screen.
 */
sealed interface ChangePasswordTextField : CommonTextField {

    /** Text field for confirming the new password. */
    data object ConfirmPassword : ChangePasswordTextField

    /** Text field for entering the old password. */
    data object OldPassword : ChangePasswordTextField

    /** Text field for entering the new password. */
    data object NewPassword : ChangePasswordTextField

    /** Text field for entering the otp. */
    data object Otp : ChangePasswordTextField
}

/**
 * Represents the different clickable actions in the Change Password screen.
 */
sealed interface ChangePasswordClickables : Clickables {

    /** Action to initiate the password change process. */
    data object ChangePassword : ChangePasswordClickables

    /** Action to hide or show the old password. */
    data object OldPasswordHide : ChangePasswordClickables

    /** Action to hide or show the confirmation password. */
    data object ConfirmPasswordHide : ChangePasswordClickables

    /** Action to hide or show the new password. */
    data object NewPasswordHide : ChangePasswordClickables

    /** Action to hide or show the new password. */
    data object Cancel : ChangePasswordClickables

    /** Action to hide or show the new password. */
    data object VeifyOTP : ChangePasswordClickables

    /** Action to hide or show the new password. */
    data object ResendOTP : ChangePasswordClickables
}

sealed interface ChangePasswordApiType : APIType {
    data object OtpToChangePassword : ChangePasswordApiType
}
