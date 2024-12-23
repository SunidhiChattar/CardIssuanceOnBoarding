package  presentation.navigation


import androidx.navigation.NavGraphBuilder
import com.isu.authentication.AuthModuleStateEvent
import com.isu.authentication.presentation.screens.newflow.ChangeDeviceBindingScreen
import com.isu.authentication.presentation.screens.newflow.EnterMobileNumberScreen
import com.isu.authentication.presentation.screens.newflow.NewCardAddedBySomeOneScreen
import com.isu.authentication.presentation.screens.newflow.PhoneVerificationScreen
import com.isu.authentication.presentation.screens.newflow.kyc.KycInfoScreen
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationScreen
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationViewModel
import com.isu.authentication.presentation.screens.newflow.twofa.TwoFactorOtpScreen
import com.isu.common.customcomposables.CustomComposable
import com.isu.common.navigation.AuthenticationScreens
import com.isu.permission.presentation.PermissionScreen


/**
 * Authentication nav graph
 * nav graph made for authentication screen only
 * @param authModuleStateEvent
 */
fun NavGraphBuilder.authenticationNavGraph(
    authModuleStateEvent: AuthModuleStateEvent,
    registartionViewModel: RegistrationViewModel,
) {
    CustomComposable<AuthenticationScreens.KycScreen> {
        KycInfoScreen(
            state = authModuleStateEvent.registrationState.state,
            onEvent = authModuleStateEvent.registrationState.event
        )
    }

    CustomComposable<AuthenticationScreens.EnterMobileNumberDeviceBindingScreen> {
        EnterMobileNumberScreen(
            state = authModuleStateEvent.registrationState.state,
            onEvent = authModuleStateEvent.registrationState.event,
            viewModel = registartionViewModel
        )
    }
    CustomComposable<AuthenticationScreens.PermissionScreen> {
        PermissionScreen(

            onEvent = authModuleStateEvent.permission.event
        )
    }
    CustomComposable<AuthenticationScreens.PersonalDetailsScreen> {

        RegistrationScreen(
            registrationState = authModuleStateEvent.registrationState.state,
            onEvent = authModuleStateEvent.registrationState.event
        )
    }
    CustomComposable<AuthenticationScreens.PhoneVerificationScreen> {
        PhoneVerificationScreen(
            state = authModuleStateEvent.registrationState.state,
            onEvent = authModuleStateEvent.registrationState.event,
            registartionViewModel
        )
    }
    CustomComposable<AuthenticationScreens.TwoFAScreen> {
        TwoFactorOtpScreen(
            state = authModuleStateEvent.registrationState.state,
            onEvent = authModuleStateEvent.registrationState.event
        )
    }
    CustomComposable<AuthenticationScreens.DeviceChangeScreen> {
        ChangeDeviceBindingScreen(
            state = authModuleStateEvent.registrationState.state,
            onEvent = authModuleStateEvent.registrationState.event
        )
    }
    CustomComposable<AuthenticationScreens.NewCardAddedBySomeOneScreen> {
        NewCardAddedBySomeOneScreen(viewModel = registartionViewModel)
    }
    /*  CustomComposable<AuthenticationScreens.SignUpScreen> {
          RegistrationScreen(
              registrationState = authModuleStateEvent.registrationState.state,
              onEvent = authModuleStateEvent.registrationState.event
          )
      }
      CustomComposable<AuthenticationScreens.PermissionScreen> {
          PermissionScreen(
              onEvent = authModuleStateEvent.permission.event
          )

      }
      CustomComposable<AuthenticationScreens.LoginScreen> {
  //        KycInfoScreen(viewModel = onBoardingViewModel)
          LogInScreen(
              screenState = authModuleStateEvent.loginScreen.state,
              onEvent = authModuleStateEvent.loginScreen.event
          )
      }
      CustomComposable<AuthenticationScreens.ForgotPasswordScreen> {
          ForgotPassword(
              screenState = authModuleStateEvent.forgotPassword.state,
              onEvent = authModuleStateEvent.forgotPassword.event
          )
      }
      CustomComposable<AuthenticationScreens.ForgotPasswordOTPScreen> {
          ForgotPasswordOTP(
              screenState = authModuleStateEvent.forgotPassword.state,
              onEvent = authModuleStateEvent.forgotPassword.event
          )
      }
      CustomComposable<AuthenticationScreens.SetNewPasswordScreen> {
          SetNewPasswordScreen(
              screenState = authModuleStateEvent.setPassword.state,
              onEvent = authModuleStateEvent.setPassword.event
          )
      }
      CustomComposable<AuthenticationScreens.SetPasswordSuccessScreen> {
          PasswordSuccess(
              onEvent = authModuleStateEvent.setPassword.event
          )
      }
      CustomComposable<AuthenticationScreens.LoginSuccessScreen> {
         LoginSuccess(onEvent=authModuleStateEvent.loginScreen.event)
      }
      CustomComposable<AuthenticationScreens.EmailVerificationOTPScreen> {
          EmailVerification(viewModel = onBoardingViewModel)
      }
      CustomComposable<AuthenticationScreens.DataBindingScreen> {
          DeviceBindingScreen(viewModel = onBoardingViewModel)
      }
      CustomComposable<AuthenticationScreens.DeviceVerificationScreen> {
          VerifySuccess()
      }
      CustomComposable<AuthenticationScreens.DeviceVerificationOTPScreen> {
          DeviceBindingOtpScreen(viewModel = onBoardingViewModel)
      }
      CustomComposable<AuthenticationScreens.AssignedCardSelectionScreen> {
          SelectAsssignedCardScreen(viewModel = onBoardingViewModel)
      }
      CustomComposable<AuthenticationScreens.KycOtpVerificationScreen> {
          MinKycUpdateOTPScreen(viewModel = onBoardingViewModel)
      }*/

}