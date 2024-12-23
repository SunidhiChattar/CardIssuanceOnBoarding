package com.isu.cardissuanceonboarding.presentation.screens

import android.content.Context
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.isu.cardissuanceonboarding.R
import com.isu.cardissuanceonboarding.common.CustomButton
import com.isu.cardissuanceonboarding.common.CustomSplash
import com.isu.cardissuanceonboarding.common.CustomText
import com.isu.cardissuanceonboarding.common.GradientHorizontalPagerIndicator
import com.isu.cardissuanceonboarding.common.SplashContent
import com.isu.cardissuanceonboarding.presentation.ui.theme.IsuGradOne
import com.isu.cardissuanceonboarding.presentation.ui.theme.IsuGradTwo
import com.isu.cardissuanceonboarding.presentation.ui.theme.TextColorLight
import com.isu.cardissuanceonboarding.presentation.ui.theme.WhiteBackground
import com.isu.common.customcomposables.noFontScale
import com.isu.common.navigation.AuthenticationScreens
import com.isu.common.navigation.Screen
import com.isu.common.utils.PERMISSION_FOR_TWELVE
import com.isu.common.utils.STORAGE_PERMISSION_THIRTY_THREE
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navHostController: NavHostController) {

    val splashList = listOf(
        SplashContent(title = stringResource(R.string.unlock_the_world_of_possibilities), subTitle = stringResource(R.string.purchase_add_modify_or_share_your_cards_seamlessly_with_the_card_issuance_app), image = R.drawable.credit_card),
        SplashContent(title = stringResource(R.string.seamlessly_manage_your_prepaid_cards), subTitle = stringResource(R.string.view_balances_track_spending_and_update_card_information_with_ease), image = R.drawable.bank_pay),
        SplashContent(title = stringResource(R.string.stay_on_top_of_your_prepaid_cards) , subTitle = stringResource(R.string.stay_on_top_of_your_prepaid_cards), image = R.drawable.credit_card),
        SplashContent(title = stringResource(R.string.efficiently_oversee_your_prepaid_cards), subTitle = stringResource(R.string.monitor_transactions_check_balances_and_manage_card_settings_from_one_central_hub), image = R.drawable.cash_flow)
    )

    val pagerState = rememberPagerState(pageCount = remember(splashList) { { splashList.size } })

    val disableGrad = Brush.verticalGradient(
        colors = listOf(Color.LightGray,
            Color.Gray)
    )
    val indicatorGrad = Brush.verticalGradient(
        colors = listOf(
            IsuGradOne, IsuGradTwo
        )
    )
    val backgroundGrad = Brush.horizontalGradient(
        colors = listOf(Color.White, WhiteBackground)
    )
    val context:Context= LocalContext.current
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % splashList.size
            pagerState.animateScrollToPage(nextPage)
            if (nextPage == splashList.size - 1) {
                delay(Long.MAX_VALUE)
            }
        }
    }
    Scaffold(topBar = {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .height(60.dp),
                painter = painterResource(id = com.isu.common.R.drawable.odishafclogo),
                contentDescription = stringResource(R.string.iserveu)
            )
        }

    }) {
        Column(modifier = Modifier
            .padding(top = it.calculateTopPadding())
            .fillMaxWidth()
            .background(Color.White)
            .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {

                HorizontalPager(state = pagerState) {
                    CustomSplash(splashContent = splashList[it])
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GradientHorizontalPagerIndicator(
                        pagerState = pagerState,
                        activeColor = indicatorGrad,
                        inactiveColor = disableGrad,
                        indicatorHeight = 8.dp,
                        indicatorWidth = 8.dp)

                    Column(modifier = Modifier.height(78.dp)) {
                        if (pagerState.currentPage == 3) {
                            CustomButton {
                                navHostController.navigate(shouldShowPermissionPage(context =context))
                            }
                        } else {
                            CustomText(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(indication = null,
                                        interactionSource = remember {
                                            MutableInteractionSource()
                                        }) {
                                        navHostController.navigate(
                                            shouldShowPermissionPage(
                                                context
                                            )
                                        )
                                    },
                                text = stringResource(R.string.skip),
                                color = TextColorLight,
                                size = 20.sp.noFontScale(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.W400,
                                lineHeight = 80.sp.noFontScale()
                            )
                        }
                    }
                }
        }
    }

}


/**
 * Should show permission page
 * -- this function  decides where a navigation should be done
 * if user freshly installs, the splash screens will be visible
 * else dashboard will be visible
 * @param context
 * @return
 */
fun shouldShowPermissionPage(context: Context): Screen {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        PERMISSION_FOR_TWELVE
    } else {
        STORAGE_PERMISSION_THIRTY_THREE
    }.forEach {
        if (context.checkSelfPermission(it) == android.content.pm.PackageManager.PERMISSION_DENIED) {
            return AuthenticationScreens.PermissionScreen
        }
    }
    return AuthenticationScreens.EnterMobileNumberDeviceBindingScreen
}

