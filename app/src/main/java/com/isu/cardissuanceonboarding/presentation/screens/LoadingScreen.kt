package com.isu.cardissuanceonboarding.presentation.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.iserveu.permission.multiplepermission.MyActivityResultCallback
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationViewModel
import com.isu.biometric_authentication_system.components.BiometricAuth.isBiometricAvailable
import com.isu.biometric_authentication_system.components.BiometricAuth.showBiometricPrompt
import com.isu.cardissuanceonboarding.R
import com.isu.cardissuanceonboarding.common.CustomText
import com.isu.cardissuanceonboarding.presentation.ui.theme.TextColorDark
import com.isu.cardissuanceonboarding.presentation.ui.theme.TextColorLight
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.ErrorCardWithCustomButtons
import com.isu.common.customcomposables.getLatLong
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.ShowBottomBarEvent
import com.isu.common.navigation.AuthenticationScreens
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.navigation.SplashScreens
import com.isu.common.utils.navigateTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    viewModel: RegistrationViewModel,

    ) {
    val context = LocalContext.current
    val showBiometricPrompt = remember {
        mutableStateOf(false)
    }
    val showDeviceUnidentifiedError = remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        ShowBottomBarEvent.hide()
    }

    val multiplePermissioin =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            MyActivityResultCallback.onActivityResult(permissions)
        }
    val intentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            MyActivityResultCallback.onActivityResult(result)
        }
    LaunchedEffect(key1 = Unit) {
        delay(1000)
       getLatLong(context=context, multiplePermissioin = multiplePermissioin, intentLauncher = intentLauncher,mlatLong = LatLongFlowProvider.latLongFlow, scope = scope)

        val hasData = viewModel.hasData()
        if (hasData.isNullOrBlank()) {

            navHostController.navigateTo(SplashScreens.SplashDashboard)


        }else{
            viewModel.callStatusCheckApi{}
           /* {
                val status= it?.data?.get(0)?.cardDetails?.get(0)?.card?.get(0)?.status
                val statusCode=it?.data?.get(0)?.cardDetails?.get(0)?.card?.get(0)?.statusCode

                if(status=="SUCCESS" && statusCode==0){
                    viewModel.fetctAuthToken(
                        onSuccess = {
                            showBiometricPrompt.value = true

                        },
                        onFailure = {

                            showDeviceUnidentifiedError.value = true

                        },
                    )

                }else if(status=="FAILURE"){
                    navHostController.navigateTo(SplashScreens.SplashDashboard)
                }else{
                    viewModel.resendOtp(onSuccess = {
                        navHostController.navigateTo(   AuthenticationScreens.PhoneVerificationScreen)
                    })

                }



            }*/
        }


    }
    if (showDeviceUnidentifiedError.value) {
        ErrorCardWithCustomButtons(
            text = "Rgistered mobile number has logged in another device",
            onOkClick = {
                CustomButton(text = "Continue with another number", color = Color.Red, onClick = {
                    scope.launch {
                        NavigationEvent.helper.navigateTo(AuthenticationScreens.EnterMobileNumberDeviceBindingScreen)
                    }
                })
            }, onRetry = {
                CustomButton(color = Color.White, modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp, Color.Red,
                        RoundedCornerShape(5.dp)
                    ), innerComponent = {
                    com.isu.common.customcomposables.CustomText(
                        text = "Cancel",
                        color = Color.Red,

                        )
                }, onClick = {
                    (context as Activity).finish()
                })
            }
        )
    }
    if (showBiometricPrompt.value) {
        if (isBiometricAvailable(context)) {

            showBiometricPrompt(
                context = context as FragmentActivity,
                promptTitle = "Biometric Login ",
                promptSubtitle = "Unlock to continue",
                promptDescription = "Please unlock the app using your biometric",
                confirmationRequiredforFaceRecognition = false,
                onSuccess = {
                    scope.launch(Dispatchers.Main) {
                        navHostController.navigateTo(ProfileScreen.DashBoardScreen)
                        showBiometricPrompt.value = false
//                        viewModel.fetctAuthToken(onSuccess = {
//                            navHostController.navigateTo(ProfileScreen.DashBoardScreen)
//                            showBiometricPrompt.value = false
//                        }, onFailure = {
//                           ( context as Activity).finish()
//                        })

                    }

                },

                onError = { error ->

                    showBiometricPrompt.value = false

                }
            )
        } else {
            viewModel.onEvent(CommonScreenEvents.NavigateTo(ProfileScreen.DashBoardScreen))

        }
    }
    Box(modifier =Modifier ){
        Scaffold(
            topBar = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {
                    Image(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(),
                        painter = painterResource(id = com.isu.common.R.drawable.odishafclogo),
                        contentDescription = stringResource(R.string.iserveu)
                    )
                }
            }
        ) {
            Column (modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxWidth()
                .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally){
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(580.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(330.dp),
                        painter = painterResource(id = R.drawable.all_cards),
                        contentDescription = stringResource(R.string.splash)
                    )
                    CustomText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        text = stringResource(R.string.unlock_the_world_of_possibilities),
                        color = TextColorDark, size = 34.sp.noFontScale(),
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center,
                        lineHeight = 43.sp
                    )
                    CustomText(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.purchase_add_modify_or_share_your_cards_seamlessly_with_the_card_issuance_app),
                        color = TextColorLight,
                        size = 18.sp.noFontScale(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W400,
                        lineHeight = 25.sp
                    )
                }

            }
        }
        val config= LocalConfiguration.current
        Image(painter = painterResource(com.isu.common.R.drawable.splash), modifier = Modifier.fillMaxWidth().height((config.screenHeightDp+700).dp), contentScale = ContentScale.FillBounds, contentDescription = "")
        Row (
            Modifier
                .height(50.dp)
                .fillMaxWidth(0.9f)

                .align(Alignment.BottomCenter), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
            LinearProgressIndicator(color = Color.White)
        }
    }
}
