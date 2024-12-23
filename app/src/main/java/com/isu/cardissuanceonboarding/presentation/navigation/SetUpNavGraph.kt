package com.isu.cardissuanceonboarding.presentation.navigation

//import com.isu.forexcard.presentation.navigation.forexCardNavGraph

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.isu.authentication.AuthModuleStateEvent
import com.isu.authentication.presentation.screens.newflow.EnterMobileNumberScreen
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationViewModel
import com.isu.cardissuanceonboarding.R
import com.isu.common.customcomposables.CustomBottomBar
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomLoaderFootball
import com.isu.common.customcomposables.CustomSuccessSnackBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.CustomTopBar
import com.isu.common.customcomposables.CustomWarningSnackBar
import com.isu.common.customcomposables.ErrorCard
import com.isu.common.customcomposables.LogOutDialog
import com.isu.common.customcomposables.noFontScale
import com.isu.common.customcomposables.signIn.DemoSignInViewModel
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.LogOutEvent
import com.isu.common.events.ShowAddCard
import com.isu.common.events.ShowBottomBarEvent
import com.isu.common.events.ShowNewAddedCard
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.events.StateEvent
import com.isu.common.navigation.AuthenticationScreens
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.Screen
import com.isu.common.navigation.SplashScreens
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.ConnectionState
import com.isu.common.utils.GlobalVariables
import com.isu.common.utils.LocalNavController
import com.isu.common.utils.SmsReceiver
import com.isu.common.utils.UiText
import com.isu.common.utils.connectivityManager
import com.isu.common.utils.datastore.PreferenceData
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.navigateTo
import com.isu.common.utils.observeConnectivityAsFlow
import com.isu.permission.presentation.PermissionViewModel

import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import presentation.navigation.authenticationNavGraph


/**
 * @author-anand
 * @author-karthik
 * Sets up the navigation graph for the application.
 */
@Composable
fun SetUpNavGraph(requiredDataObj: JSONObject?) {
    val navHostController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navHostController) {
        val context = LocalContext.current
        val connectionState = context.connectivityManager.observeConnectivityAsFlow()
            .collectAsState(initial = ConnectionState.Available)
        val showSnackBar = remember { mutableStateOf(false) }
        val snackBarMsg: MutableState<UiText> =
            remember { mutableStateOf(UiText.StringResource(com.isu.common.R.string.empty)) }
        val showErrorSnackBar = remember { mutableStateOf(false) }
        val snackErrorBarMsg: MutableState<UiText> =
            remember { mutableStateOf(UiText.StringResource(com.isu.common.R.string.empty)) }
        val showLoading = remember { mutableStateOf(false) }
        val showError = remember { mutableStateOf(false) }
        val errorMsg = remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        val config = LocalConfiguration.current


        val showLogoutDialog = remember {
            mutableStateOf(false)
        }
        val showInvalidDataDialog= remember {
            mutableStateOf(false)
        }

        val smsReceiver = remember {
            SmsReceiver { sender, message ->
                // Handle the received SMS here
                Log.d("SMS", "From: $sender, Message: $message")
            }
        }
        val selectedAddCardFor = remember {
            mutableStateOf("")
        }

        // Register the BroadcastReceiver when the Composable enters the composition
        DisposableEffect(Unit) {
            val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
            context.registerReceiver(smsReceiver, intentFilter)

            // Unregister the BroadcastReceiver when the Composable leaves the composition
            onDispose {
                context.unregisterReceiver(smsReceiver)
            }
        }

        // ViewModels
        val permissionViewModel = hiltViewModel<PermissionViewModel>()
        val signInViewModel = hiltViewModel<DemoSignInViewModel>()
        val registrationViewModel = hiltViewModel<RegistrationViewModel>()



        // Collect state and events
        val permissionEvent = permissionViewModel::onEvent
        val signInEvent = signInViewModel::onEvent
        val cardInfoScreenState by signInViewModel.cardInfoState.collectAsState()
        val addressScreenState by signInViewModel.addressState.collectAsState()
        val personalInfoScreenState by signInViewModel.personalInfoState.collectAsState()
        val registrationState by registrationViewModel.registrationState.collectAsState()


        // Initialize AuthModuleStateEvent
        val authModuleStateEvent = AuthModuleStateEvent(
            permission = StateEvent(state = Any(), event = permissionEvent),
            cardInfo = StateEvent(state = cardInfoScreenState, event = signInEvent),
            personalInfo = StateEvent(state = personalInfoScreenState, event = signInEvent),
            addressInfo = StateEvent(state = addressScreenState, event = signInEvent),
            registrationState = StateEvent(
                state = registrationState,
                event = registrationViewModel::onEvent
            )
        )









        // Launch snack bar event collectors
        LaunchedEffect(Unit) {
            ShowSnackBarEvent.helper.showSnackBarEvent.collectLatest {
                handleSnackBarEvents(
                    it,
                    showSnackBar,
                    snackBarMsg,
                    showErrorSnackBar,
                    snackErrorBarMsg
                )
            }
        }


        // Launch loading and error event collectors
        LaunchedEffect(Unit) {
            scope.launch {
                LoadingErrorEvent.helper.loadingErrorEvent.collectLatest { event ->
                    handleLoadingErrorEvent(event, context, showError, errorMsg, showLoading)
                }
            }
        }
        // Launch navigation event collectors
        LaunchedEffect(key1 = Unit) {
            NavigationEvent.helper.navigationEvent.collectLatest {

                handleNavigationEvent(event = it, navHostController)
//                Log.d("SetUpNavGraph: ", navHostController.currentDestination.toString())

            }


        }





        // Main UI content
        Box(modifier = Modifier.fillMaxSize()) {
            if (showLoading.value) {
                CustomLoaderFootball()
            }
            if(showInvalidDataDialog.value){
                ErrorCard(text = "Required data could not be fetched.Try opening with whitelabel app", btnComposable = {
                    CustomButton(color = Red, modifier = Modifier
                        .padding(22.dp)
                        .fillMaxWidth(), onClick = {
                        (context as Activity).finish()
                    })
                })
            }
            if (showLogoutDialog.value) {
                LogOutDialog(onProceed = {
                    registrationViewModel.customerIsBulkOnBoarded.value = false
                    showLogoutDialog.value = false
                    registrationViewModel.clearDataStore()
                    GlobalVariables.AUTH_TOKEN = ""
                    scope.launch {
                        NavigationEvent.helper.emit(
                            NavigationEvent.NavigateToNextScreen(
                                AuthenticationScreens.EnterMobileNumberDeviceBindingScreen
                            )
                        )

                    }

//                    navHostController.navigateTo(AuthenticationScreens.LoginScreen)
                    navHostController.clearBackStack<Screen>()
                }) {
                    registrationViewModel.customerIsBulkOnBoarded.value = false
                    showLogoutDialog.value = false
                }
            }
            if (showError.value) {
                ErrorCard(text = errorMsg.value, btnComposable = {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                                color = Red.copy(0.1f)
                            ),
                        onClick = {
                            showError.value = false
                            errorMsg.value = ""
                        },
                        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White
                        ),
                    ) {
                        CustomText(text = "Close", color = Red, fontWeight = FontWeight(550))
                    }
                })
//                CustomErrorDialog(content = errorMsg.value, dismissAction = {
//                    showError.value = false
//                    errorMsg.value = ""
//                })
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(appMainColor.copy(0.1f))
                    ,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if(!showInvalidDataDialog.value){
                    NavHost(
                        modifier = Modifier.weight(1f),
                        navController = navHostController,
                        startDestination = AuthenticationScreens.PersonalDetailsScreen
                    ) {

                        authenticationNavGraph(authModuleStateEvent, registrationViewModel)
                        splashNavGraph(navHostController, registrationViewModel)

                    }
                }


                /*Log.d("SetUpNavGraph: ", navHostController.currentDestination.toString())*/



            }




            if (connectionState.value == ConnectionState.Unavailable) {
                CustomWarningSnackBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    warningMessage = stringResource(R.string.no_internet_message)
                )
            }

            if (showErrorSnackBar.value) {
                CustomWarningSnackBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    warning = showErrorSnackBar,
                    warningMessage = snackErrorBarMsg.value.asString()
                )
            }
            if (showSnackBar.value) {
                CustomSuccessSnackBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    success = showSnackBar,
                    successMsg = snackBarMsg.value.asString()
                )
            }





        }
    }
}


fun handleNavigationEvent(event: NavigationEvent, navHostController: NavHostController) {
    when (event) {
        is NavigationEvent.NavigateBack -> navigateBack(navHostController)
        is NavigationEvent.NavigateToNextScreen -> handleNavigationToScreen(
            event,
            navHostController
        )

        is NavigationEvent.ClearStack -> {
            navHostController.clearBackStack<Screen>()
        }
    }
}

fun navigateBack(navHostController: NavHostController) {
    navHostController.popBackStack()
}

fun handleNavigationToScreen(
    event: NavigationEvent.NavigateToNextScreen,
    navHostController: NavHostController,
) {
    if (event.popUpTo != null) {
        navHostController.navigateTo(
            event.screen,
            popDestination = event.popUpTo,
            inclusive = event.inclusive == true
        )
    } else {
        navHostController.navigateTo(event.screen)
    }

}

fun handleSnackBarEvents(
    event: ShowSnackBarEvent,
    showSnackBar: MutableState<Boolean>,
    snackBarMsg: MutableState<UiText>,
    showErrorSnackBar: MutableState<Boolean>,
    snackErrorBarMsg: MutableState<UiText>,
) {
    when (event) {
        is ShowSnackBarEvent.show -> {
            handleSnackBarShow(
                event,
                showSnackBar,
                snackBarMsg,
                showErrorSnackBar,
                snackErrorBarMsg
            )
        }
    }
}

fun handleSnackBarShow(
    event: ShowSnackBarEvent.show,
    showSnackBar: MutableState<Boolean>,
    snackBarMsg: MutableState<UiText>,
    showErrorSnackBar: MutableState<Boolean>,
    snackErrorBarMsg: MutableState<UiText>,
) {
    when (event.type) {

        SnackBarType.ErrorSnackBar -> showErrorSnackBar(
            event,
            showErrorSnackBar,
            snackErrorBarMsg
        )

        SnackBarType.SuccessSnackBar -> showSuccessSnackBar(
            event,
            showSnackBar,
            snackBarMsg
        )
    }
}

fun showSuccessSnackBar(
    event: ShowSnackBarEvent.show,
    showSnackBar: MutableState<Boolean>,
    snackBarMsg: MutableState<UiText>,
) {
    showSnackBar.value = true
    snackBarMsg.value = event.msg
}

fun handleLoadingErrorEvent(
    event: LoadingErrorEvent,
    context: Context,
    showError: MutableState<Boolean>,
    errorMsg: MutableState<String>,
    showLoading: MutableState<Boolean>,
) {
    when (event) {
        is LoadingErrorEvent.errorEncountered -> showError(event, showError, errorMsg, context)
        is LoadingErrorEvent.isLoading -> showLoading(event, showLoading)
    }
}

fun showErrorSnackBar(
    event: ShowSnackBarEvent.show,
    showErrorSnackBar: MutableState<Boolean>,
    snackErrorBarMsg: MutableState<UiText>,
) {
    showErrorSnackBar.value = true
    snackErrorBarMsg.value = event.msg
}

fun showLoading(event: LoadingErrorEvent.isLoading, showLoading: MutableState<Boolean>) {
    showLoading.value = event.isLoading
}

fun showError(
    event: LoadingErrorEvent.errorEncountered,
    showError: MutableState<Boolean>,
    errorMsg: MutableState<String>,
    context: Context,
) {

    showError.value = true
    errorMsg.value = event.error.asString(context)

}


