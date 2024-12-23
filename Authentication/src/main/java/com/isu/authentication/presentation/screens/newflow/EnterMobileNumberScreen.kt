package com.isu.authentication.presentation.screens.newflow

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iserveu.permission.multiplepermission.MyActivityResultCallback
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationButtonType
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationInputField
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationState
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationViewModel
import com.isu.common.R
import com.isu.common.customcomposables.CustomAuthTopSection
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.getLatLong
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.ShowBottomBarEvent
import com.isu.common.utils.FontProvider.LATO_FONT
import com.isu.common.utils.datastore.PreferenceData
import com.isu.common.utils.datastore.PreferencesKeys

sealed interface CardType {
    data object GPR : CardType
    data object GIFT : CardType
}

@Composable
fun EnterMobileNumberScreen(
    state: RegistrationState,
    onEvent: (CommonScreenEvents) -> Unit,
    viewModel: RegistrationViewModel
) {
    // Extract state variables
    val config = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val firstName = remember {
        mutableStateOf("")
    }

    val firstNameError = remember {
        mutableStateOf(false)
    }
    val firstNameErrorMessage = remember {
        mutableStateOf("")
    }
    val lastName = remember {
        mutableStateOf("")
    }
    val lastNameError = remember {
        mutableStateOf(false)
    }
    val lastNameErrorMessage = remember {
        mutableStateOf("")
    }
    val email = remember {
        mutableStateOf("")
    }
    val emailError = remember {
        mutableStateOf(false)
    }
    val emailErrorMessage = remember {
        mutableStateOf("")
    }
    val mobileNumber = state.phoneNumber
    val mobileNumberError = state.isPhoneNumberError
    val mobileNumberErrorMessage = state.phoneNumberErrorMessage

    val androidId = android.provider.Settings.Secure.getString(
        context.contentResolver,
        android.provider.Settings.Secure.ANDROID_ID
    )
    LaunchedEffect(Unit) {
        ShowBottomBarEvent.hide()
        viewModel.clearData()

    }

    val multiplePermissioin =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            MyActivityResultCallback.onActivityResult(permissions)
        }
    val intentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            MyActivityResultCallback.onActivityResult(result)
        }

    LaunchedEffect(Unit) {
        getLatLong(
            context = context,
            scope = scope,
            mlatLong = LatLongFlowProvider.latLongFlow,
            multiplePermissioin = multiplePermissioin,
            intentLauncher = intentLauncher
        )
    }
    // Handle password visual transformation


//    val googleAuthUiClient: GoogleAuthUiClient by lazy {
//        GoogleAuthUiClient(
//            context.applicationContext,
//            Identity.getSignInClient(context.applicationContext)
//        )
//    }

    LaunchedEffect(true) {
//        firstName.value = ""
//        lastName.value = ""
////        email.value = ""
//        mobileNumber.value = ""
        emailError.value = false
        emailErrorMessage.value = ""
        onEvent(
            CommonScreenEvents.SaveToDataStore(
                PreferenceData(
                    key = PreferencesKeys.ANDROID_ID,
                    androidId
                )
            )
        )
    }

//    val rememberResult =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { data ->
//            scope.launch {
//                val signInResult =
//                    googleAuthUiClient.signInWithIntent(intent = data.data ?: return@launch)
//
//            }
//
//
//        }

    Scaffold(containerColor = White) { paddingValues ->
        KeyBoardAwareScreen(
            modifier = Modifier
                .padding(
                    start = 22.dp,
                    top = paddingValues.calculateTopPadding(),
                    end = 22.dp
                )
                .fillMaxSize(),
            shouldScroll = false,
            keyBoardAware = true,
            horizontalAlignment = Alignment.CenterHorizontally,
            backHandler = {
                (context as Activity).finish()
            }
        ) {
            Column(modifier = Modifier.heightIn(config.screenHeightDp.dp)) {
                CustomAuthTopSection(
                    headingText = "Enter your Mobile Number",
                    infoText = "",
                    imageVector = R.drawable.regidter
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    // Username Input Field
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
                        /*       CustomInputField(

                                   annotatedLabel = buildAnnotatedString {
                                       append(stringResource(com.isu.authentication.R.string.first_name))
                                       withStyle(
                                           SpanStyle(color = Color.Red)
                                       ) {
                                           append("*")
                                       }
                                   },
                                   placeholder = stringResource(com.isu.authentication.R.string.enter_first_name),
                                   state = firstName.value,
                                   onValueChange = {
                                       if (Validation.containsOnlyAlphabetic(it)) {
                                           firstName.value = it
                                           firstNameError.value = false
                                           firstNameErrorMessage.value = ""
                                       }
                                   },
                                   errorMessage = firstNameErrorMessage.value,
                                   isError = firstNameError.value
                               )
                               CustomInputField(
                                   annotatedLabel = buildAnnotatedString {
                                       append(stringResource(com.isu.authentication.R.string.last_name))
                                       withStyle(
                                           SpanStyle(color = Color.Red)
                                       ) {
                                           append("*")
                                       }
                                   },
                                   placeholder = stringResource(com.isu.authentication.R.string.enter_last_name),
                                   state = lastName.value,
                                   onValueChange = {
                                       if (Validation.containsOnlyAlphabetic(it)) {
                                           lastName.value = it
                                           lastNameError.value = false
                                           lastNameErrorMessage.value = ""
                                       }
                                   },
                                   errorMessage = lastNameErrorMessage.value,
                                   isError = lastNameError.value
                               )*/
                        CustomInputField(
                            annotatedLabel = buildAnnotatedString {
                                append("Mobile Number")
                                withStyle(
                                    SpanStyle(color = Color.Red)
                                ) {
                                    append("*")
                                }
                            },
                            placeholder = "Enter Your Mobile Number",
                            state = mobileNumber,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            onValueChange = {
                                onEvent(
                                    CommonScreenEvents.OnTextChanged(
                                        it,
                                        RegistrationInputField.Mobile
                                    )
                                )
                            },

                            errorMessage = mobileNumberErrorMessage.asString(),
                            isError = mobileNumberError
                        )
                        /*  // Password Input Field
                          CustomInputField(
                              annotatedLabel = buildAnnotatedString {
                                  append("Email")
                                  withStyle(
                                      SpanStyle(color = Color.Red)
                                  ) {
                                      append("*")
                                  }
                              },
                              enabled = !viewModel.emailVerificationDone.value,
                              color = TextFieldDefaults.colors(
                                  unfocusedIndicatorColor = LightGray,
                                  focusedIndicatorColor = Color.DarkGray,
                                  disabledIndicatorColor = if (emailError.value) errorColor else {
                                      if (viewModel.emailVerificationDone.value) Green else LightGray
                                  },
                                  focusedContainerColor = White,
                                  unfocusedContainerColor = White,
                                  disabledContainerColor = White,
                                  disabledTextColor = Black,
                                  errorIndicatorColor = errorColor,
                                  errorContainerColor = White
                              ),
                              state = email.value,
                              placeholder = "Enter Your Email ID",
                              onValueChange = {
                                  if (Validation.isValidEmail(it)) {
                                      email.value = it
                                      emailError.value = false
                                      emailErrorMessage.value = ""

                                  } else {
                                      email.value = it
                                      emailError.value = true
                                      emailErrorMessage.value = "Please enter a valid email"
                                  }
                              },

                              errorMessage = emailErrorMessage.value,
                              isError = emailError.value,
                              trailingIcon = {
                                  if (viewModel.emailVerificationDone.value) {
                                      Icon(Icons.Outlined.CheckCircle, "", tint = Green)
                                  } else {

                                      TextButton(
                                          onClick = {
                                              Toast.makeText(
                                                  context,
                                                  viewModel.latLong.value,
                                                  Toast.LENGTH_LONG
                                              ).show()
                                              scope.launch {
  //                                            val signIn = googleAuthUiClient.signIn()
  //                                            rememberResult.launch(
  //                                                IntentSenderRequest.Builder(
  //                                                    signIn ?: return@launch
  //                                                ).build()
  //                                            )
                                                  viewModel.generateEmailVerificationOTP()
                                              }
                                          },
                                          interactionSource = NoRippleInteractionSource()
                                      ) {
                                          CustomText(
                                              text = "Verify",
                                              color = if (Validation.isValidEmail(email.value)) appMainColor else LightGray,
                                              fontWeight = FontWeight(700)
                                          )
                                      }
                                  }
                              }
                          )
                      }
                      if (viewModel.emailVerificationDone.value) {
                          CustomText(text = "Please verify email before registering")
                      }
  */
                    Spacer(Modifier.height(20.dp))

                    Column(
                        modifier = Modifier.wrapContentHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {


                        // Sign In Button
                        CustomButton(onClick = {
                            focusManager.clearFocus()
                            onEvent(CommonScreenEvents.OnClick<Any>(RegistrationButtonType.EnterPhoneNumberSubmitButton))





                        }, text = stringResource(R.string.sign_in_btn))
                        Spacer(Modifier.height(20.dp))
                        /*      val annotatedString = buildAnnotatedString {
                                  append(stringResource(R.string.register_pre))
                                  pushStringAnnotation(
                                      "Sign In",
                                      "Sign In",
                                  )
                                  withStyle(style = SpanStyle(color = appMainColor)) {
                                      append("Sign In")
                                  }

                                  pop()
                              }
                              AnnotatedClickableText(
                                  modifier = Modifier.padding(vertical = 10.dp)
                                      .graphicsLayer { translationY = -20f },
                                  text = annotatedString.text,
                                  linkText = "Sign In",
                                  onClick = {
                                      scope.launch {
                                          NavigationEvent.helper.emit(
                                              NavigationEvent.NavigateToNextScreen(
                                                  AuthenticationScreens.LoginScreen
                                              )
                                          )
                                      }
                                  },
                                  fontFamily = LATO_FONT,
                                  color = authTextColor
                              )*/



                        CustomText(
                            text = stringResource(R.string.copyright),
                            modifier = Modifier.padding(horizontal = 38.dp),
                            fontFamily = LATO_FONT,
                            fontSize = 14.sp.noFontScale(),
                            lineHeight = 16.sp.noFontScale(),
                            textAlign = TextAlign.Center,
                            color = LightGray
                        )
                        Spacer(Modifier.height(20.dp))
                    }
                }

            }
        }
    }
}
}




