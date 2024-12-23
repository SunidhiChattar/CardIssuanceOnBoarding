package com.isu.common.navigation

import kotlinx.serialization.Serializable

interface Screen

sealed interface AuthenticationScreens:Screen {
    @Serializable
    data object EnterMobileNumberDeviceBindingScreen : AuthenticationScreens

    @Serializable
    data object KycScreen : AuthenticationScreens

    @Serializable
    data object PersonalDetailsScreen : AuthenticationScreens

    @Serializable
    data object PhoneVerificationScreen : AuthenticationScreens

    @Serializable
    data object PermissionScreen : AuthenticationScreens

    @Serializable
    data object TwoFAScreen : Screen

    @Serializable
    data object DeviceChangeScreen : AuthenticationScreens

    @Serializable
    data object NewCardAddedBySomeOneScreen : AuthenticationScreens


    /*
    @Serializable
    data object DeviceVerificationScreen : AuthenticationScreens

    @Serializable
    data object DeviceVerificationOTPScreen : AuthenticationScreens

    @Serializable
    data object EmailVerificationOTPScreen : AuthenticationScreens

    @Serializable
    data object EmailVerificationSuccessScreen : AuthenticationScreens

    @Serializable
    data object AssignedCardSelectionScreen : AuthenticationScreens


    @Serializable
    data object LoginScreen : AuthenticationScreens


    @Serializable
    data object LoginSuccessScreen : AuthenticationScreens


    @Serializable
    data object SignUpScreen : AuthenticationScreens

    @Serializable
    data object ForgotPasswordScreen : AuthenticationScreens

    @Serializable
    data object ForgotPasswordOTPScreen : AuthenticationScreens

    @Serializable
    data object SetNewPasswordScreen : AuthenticationScreens

    @Serializable
    data object SetPasswordSuccessScreen : AuthenticationScreens

    @Serializable
    data object PermissionScreen : AuthenticationScreens

    @Serializable
    data object EmailVerificationScreen : AuthenticationScreens

    @Serializable
    data object DataBindingScreen : AuthenticationScreens



    @Serializable
    data object KycOtpVerificationScreen : AuthenticationScreens

*/
}

sealed interface PrepaidCardScreens {
    @Serializable
    data object DemoScreen : PrepaidCardScreens
    @Serializable
    data class SecondScreen(val text: String) : PrepaidCardScreens

}
sealed interface SplashScreens:Screen{
    @Serializable
    data object SplashDashboard: SplashScreens
    @Serializable
    data object SplashScreen: SplashScreens
    @Serializable
    data object LoadingScreen: SplashScreens

}
sealed interface CustomerSupportScreen:Screen{
    @Serializable
    data object CustomerSupportHomeScreen:CustomerSupportScreen
    @Serializable
    data object TicketDetailsScreen:CustomerSupportScreen
    @Serializable
    data object RaiseTicketScreen:CustomerSupportScreen
    @Serializable
    data object AllTicketsScreen : CustomerSupportScreen
}
sealed interface ProfileScreen:Screen{


    @Serializable
    data object BasicInfoScreen:ProfileScreen

    @Serializable
    data object ChangePaswordScreenOTP : ProfileScreen

    @Serializable
    data object BasicInfoEditScreen:ProfileScreen
    @Serializable
    data object NotificationSettingsScreen:ProfileScreen
    @Serializable
    data object ChangePaswordScreen:ProfileScreen
    @Serializable
    data object CustomerSupportScreen:ProfileScreen
    @Serializable
    data object KYC:ProfileScreen
    @Serializable
    data object AboutUs:ProfileScreen
    @Serializable
    data object PrivacyPolicy:ProfileScreen
    @Serializable
    data object TermsAndCondition:ProfileScreen
    @Serializable
    data object GetInTouch:ProfileScreen
    @Serializable
    data object HomeScreen:ProfileScreen
    @Serializable
    data object CardManagementScreen : ProfileScreen
    @Serializable
    data object NotificationScreen : ProfileScreen
    @Serializable
    data object DashBoardScreen : ProfileScreen


}

sealed interface CardManagement : Screen {
    @Serializable
    data object DetailedStatementList : CardManagement

    @Serializable
    data object DetailedStatement : CardManagement

    @Serializable
    data object OrderPhyicalCardSelectScreen : CardManagement

    @Serializable
    data object OrderPhyicalCardDetailsScreen : CardManagement

    @Serializable
    data object OrderPhyicalCardShippingAddressScreen : CardManagement

    @Serializable
    data object AddOnCarSelfd : CardManagement

    @Serializable
    data object AddOnCardSomeoneElse : CardManagement

    @Serializable
    data object AddAddress : CardManagement

    @Serializable
    data object CompleteKYC : CardManagement

    @Serializable
    data object Mandate : CardManagement

    @Serializable
    data object MandateHistory : CardManagement

    @Serializable
    data object Statement : CardManagement

    @Serializable
    data object Tokens : CardManagement

    @Serializable
    data object CardStatus : CardManagement

    @Serializable
    data object LoadCardResultScreen : CardManagement

    @Serializable
    data object KitToKitTransfer : CardManagement

    @Serializable
    data object LoadCardScreen : CardManagement

    @Serializable
    data object ModifyPinScreen : CardManagement

    @Serializable
    data object LinkCard : CardManagement

    @Serializable
    data object LinkCardOtpScreen : CardManagement

    @Serializable
    data object EditAddressScreen : CardManagement


    @Serializable
    data object CardReissuance : CardManagement

    @Serializable
    data object AddCardScreen : CardManagement

    @Serializable
    data object TransactionSettings : CardManagement

    @Serializable
    data object AddonCardForSomeoneElseSuccess : CardManagement

    @Serializable
    data object SetPinScreen : CardManagement

    @Serializable
    data object AddonCardSelfSuccess : CardManagement

    @Serializable
    data object AddOnCardPhoneVerificationSCreen : CardManagement

    @Serializable
    data object ModifyPinOTPScreen : CardManagement

    @Serializable
    data object OrderHistoryDetails: CardManagement
    @Serializable
    data object OrderHistory: CardManagement

    @Serializable
    data object PaymentModeSelectionScreen : CardManagement

    @Serializable
    data object BeneSelectionScreen : CardManagement

    @Serializable
    data object BeneAmountPaymentScreen : CardManagement

    @Serializable
    data object AddBeneficiaryScreen : CardManagement

    @Serializable
    data object MCCCategoryScreen : CardManagement

    @Serializable
    data object MCCCategoryCodeScreen : CardManagement

    @Serializable
    data object VIEW_PDF_SCREEN : CardManagement {

    }
}