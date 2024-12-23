package com.isu.cardissuanceonboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationViewModel
import com.isu.cardissuanceonboarding.presentation.screens.LoadingScreen
import com.isu.cardissuanceonboarding.presentation.screens.SplashDashboard
import com.isu.cardissuanceonboarding.presentation.screens.SplashScreen
import com.isu.common.customcomposables.CustomComposable
import com.isu.common.navigation.SplashScreens

fun NavGraphBuilder.splashNavGraph(
    navHostController: NavHostController,
    registrationViewModel: RegistrationViewModel,
) {
    CustomComposable<SplashScreens.LoadingScreen> {
        LoadingScreen(navHostController = navHostController, viewModel = registrationViewModel)
        /*CardReissuance()*/
        /* LoadCard()*/
        /*ModifyPin()*/

    }
    CustomComposable<SplashScreens.SplashDashboard> {
        SplashDashboard(navHostController = navHostController)
    }
    CustomComposable<SplashScreens.SplashScreen> {
        SplashScreen(navHostController = navHostController)
    }

}

