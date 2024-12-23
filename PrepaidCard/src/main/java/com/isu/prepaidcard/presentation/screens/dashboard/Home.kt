package com.isu.prepaidcard.presentation.screens.dashboard

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iserveu.permission.multiplepermission.MyActivityResultCallback
import com.isu.common.R
import com.isu.common.customcomposables.CircularBoxIcon
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.CustomTopBar
import com.isu.common.customcomposables.DashboardBottomFrame
import com.isu.common.customcomposables.DashboardTopFrame
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.getLatLong
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.ShowBottomBarEvent
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.CardBlue
import com.isu.common.ui.theme.CardGreen
import com.isu.common.ui.theme.Pink40
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.CardFeature
import com.isu.common.utils.FontProvider
import com.isu.common.utils.payment
import com.isu.common.utils.quickAccess
import com.isu.prepaidcard.data.response.CardDataItem
import com.isu.prepaidcard.data.response.ViewCardDataByRefIdResponse
import com.isu.prepaidcard.presentation.components.CardDetailsItem
import com.isu.prepaidcard.presentation.components.CustomCardinDashboard
import com.isu.prepaidcard.presentation.viewmodels.DashboardViewModel
import kotlinx.coroutines.launch


/**
 * This composable function represents the home screen of the dashboard.
 *
 * @param dashboardViewModel ViewModel containing the dashboard related data and logic.
 *
 * @Composable This annotation indicates that the function is composable and can be used within a Jetpack Compose composable hierarchy.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardHomeScreen(dashboardViewModel: DashboardViewModel) {

    val config = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val nextScrollState = rememberScrollState()
    val lazyListState= rememberLazyListState()
    val cardList = dashboardViewModel.cardList

    val cvv=remember{
        mutableStateOf("")
    }
    val primaryCard: MutableState<CardDataItem?> = remember(cardList.size) {
        mutableStateOf(null)

    }
    val pagerState = rememberPagerState(pageCount = { cardList.size })
    val showDetails = remember(pagerState.currentPage) { mutableStateOf(false) }
    val showBalance = remember(pagerState.currentPage) { mutableStateOf(false) }
    val showCvv = remember(pagerState.currentPage) { mutableStateOf(false) }
    val balance = remember { mutableStateOf("") }
//    val cardList = allCards
    val seeAllwidth = remember {
        mutableStateOf(0)
    }
//    val cardDetailsState = dashboardViewModel.cardDashBoardDetails.collectAsState()
    val clickedCard : MutableState<CardFeature?> = remember { mutableStateOf(null) }


    val context= LocalContext.current
    val multiplePermissioin =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            MyActivityResultCallback.onActivityResult(permissions)
        }
    val intentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            MyActivityResultCallback.onActivityResult(result)
        }
    LaunchedEffect(Unit) {
        ShowBottomBarEvent.show()
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

    BackHandler {
        (context as Activity).finishAffinity()
    }
    val gprFilterCard = cardList.filter {
        val cardType = it?.productCategory
        cardType == "GPR"
    }
    val giftFilterCard = cardList.filter {
        val cardType = it?.productCategory
        cardType == "GIFT"
    }
    val clipBoardManager = LocalClipboardManager.current
    val primaryCardData: MutableState<ViewCardDataByRefIdResponse.Data?> = remember {
        mutableStateOf(null)
    }
    LaunchedEffect(Unit) {
        dashboardViewModel.getCardDataByMobileNo{resp ->
            scope.launch {

                primaryCard.value = resp.data?.data?.filter { it.isPrimary == true }?.getOrNull(0)
                dashboardViewModel.cardRefid.value = primaryCard.value?.cardRefId
                dashboardViewModel.getCardDataByRefId {
                    primaryCardData.value = it
                }
            }

        }
//        if (cardDetailsState.value.requireCardDataFetching) {
//
//        }
    }
    LaunchedEffect(Unit) {
        ShowBottomBarEvent.show()
    }
    LaunchedEffect(cardList.size) {
        primaryCard.value = cardList.filter { it?.isPrimary == true }.getOrNull(0)
    }
    LaunchedEffect(pagerState.currentPage) {
        showBalance.value = false
        showCvv.value = false
        showDetails.value = false
        Log.d("pager", "DashboardHomeScreen: ${pagerState.currentPage}")

    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CustomTopBar(text = "Dashboard")
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(top = padding.calculateTopPadding()).background(Color.White)
        ) {
            Column(
                verticalArrangement =
                if (cardList.isEmpty())
                    Arrangement.Center
                else
                    Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedVisibility(visible = cardList.isEmpty(), exit = fadeOut())
                {
                    KeyBoardAwareScreen(shouldScroll = false, backHandler = {
                        scope.launch {
                            (context as Activity).finishAffinity()
                        }
                    }) {
                        Spacer(modifier = Modifier.height(50.dp))
                        Column(
                            modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(painter = painterResource(R.drawable.emptycard), "")
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth(0.7f).padding(5.dp),
                                verticalArrangement = Arrangement.spacedBy(25.dp)
                            ) {
                                CustomText(
                                    text = "No Card Found",
                                    fontSize = 16.sp.noFontScale(),
                                    textAlign = TextAlign.Center
                                )
                                CustomText(
                                    text = "Link your card or request for another Card.",
                                    fontSize = 14.sp.noFontScale(),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Row(
                                modifier = Modifier.wrapContentSize().padding(top = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        scope.launch {
                                            NavigationEvent.helper.navigateTo(com.isu.common.navigation.CardManagement.LinkCard)
                                        }
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                                    border = BorderStroke(
                                        1.dp,
                                        authTextColor
                                    ),
                                    modifier = Modifier.height(34.dp),
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    Row {
                                        Icon(
                                            painterResource(R.drawable.link),
                                            ""
                                        )
                                        CustomText(text = "Link Card")
                                    }
                                }
                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(Color(0xff1D242D)),
                                    modifier = Modifier.height(34.dp),
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    Row {
                                        Icon(Icons.Default.Add, "", tint = Color.White)
                                        CustomText(
                                            text = "Request Card",
                                            color = Color.White,
                                            fontSize = 14.sp.noFontScale()
                                        )
                                    }
                                }
                            }

                        }

                    }
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = cardList.isNotEmpty(),
                    enter = fadeIn()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight().verticalScroll(
                            rememberScrollState()
                        ),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(modifier = Modifier.padding(22.dp)) {


                            CustomCardinDashboard(
                                isPrimary = true,
                                showDetails = showDetails,
                                cardDataByRefId = primaryCardData.value,
                                onCardClick = {
                                    scope.launch {

                                    }
                                },

                                )
                        }

//                        else {
//                            HorizontalPager(
//                                state = pagerState,
//                                contentPadding = PaddingValues(
//                                    horizontal = 30.dp,
//                                    vertical = 10.dp
//                                ),
//                                pageSpacing = 10.dp
//                            ) {
//                                CustomCardinDashboard(
//                                    cardDetails = cardList[it],
//                                    showDetails = showDetails
//                                ) {
//                                    scope.launch {
//                                        dashboardViewModel.onCardClick(cardList[pagerState.currentPage]?.cardRefId)
//                                        NavigationEvent.helper.navigateTo(ProfileScreen.CardManagementScreen)
//                                    }
//                                }
//                            }
//                            }

                        CardDetailsItem(
                            cardData = primaryCardData.value,
                            cardDatByMobileNumber = primaryCard.value,
                            showDetails = showDetails,
                            cvv = cvv.value,
                            cvvToggleState = showCvv,
                            onCvvToggle = {
                                    onSuccess->
                                scope.launch {
                                    primaryCard.value =
                                        cardList.filter { it?.isPrimary == true }.getOrNull(0)
                                    if (!showCvv.value) {
                                        dashboardViewModel.viewCardCvv(cardReferenceId = primaryCard.value?.cardRefId) {
                                            Log.d("CARDCVV", "CardManagement: ${it.data}")
                                            cvv.value = it.data?.cvv ?: ""
                                        }
                                    }
                                    onSuccess()
                                }
                            },
                            onShowDetails = {
                                scope.launch {
                                    primaryCard.value =
                                        cardList.filter { it?.isPrimary == true }.getOrNull(0)
                                    if (!showBalance.value) {
                                        dashboardViewModel.getCardBalance(
                                            cardReferenceId = primaryCard.value?.cardRefId
                                        ) { resp ->

                                            balance.value = resp.data?.balance.toString()
                                        }
                                    }
                                    it.invoke()

                                }
                            },
                            onCopyCardNumberr = {
                                if (showDetails.value) {
                                    clipBoardManager.setText(buildAnnotatedString {
                                        append(primaryCard.value?.decrypted)
                                    })
                                }

                            },
                            showBalance = showBalance,
                            balance = balance.value,
                            setPrimary = {
                                dashboardViewModel.setPrimary(dashboardViewModel.cardRefid.value.toString()) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(0)
                                    }
                                }
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomText(
                                text = "Quick Actions",
                                fontFamily = FontProvider.INTER,
                                fontSize = 13.5.sp.noFontScale(),
                                fontWeight = FontWeight(500)
                            )
                            CustomText(
                                text = "See all",
                                fontSize = 14.sp.noFontScale(),
                                fontWeight = FontWeight(500),
                                color = Color.LightGray,
                                fontFamily = FontProvider.INTER,
                                modifier = Modifier
                                    .clickable {
                                        scope.launch {
                                            scrollState.animateScrollTo(scrollState.maxValue)
                                        }
                                    }
                                    .onGloballyPositioned {
                                        seeAllwidth.value = it.positionInWindow().y.toInt() + 780
                                    },
                            )
                        }
                        Row(
                            modifier = Modifier.widthIn(config.screenWidthDp.dp)
                                .horizontalScroll(scrollState)
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            quickAccess.forEach {
                                CircularBoxIcon(
                                    modifier = Modifier.width(IntrinsicSize.Max),
                                    icon = it.icon,
                                    iconSize = it.iconSize,
                                    text = it.text
                                ){
                                    dashboardViewModel.handleFeatureClick(it.text)
                                }
                            }

                        }
                        Spacer(Modifier.height(6.dp))
                            CustomText(
                                text = "Brand Vouchers",
                                fontWeight = FontWeight(500),
                                fontFamily = FontProvider.INTER,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                                fontSize = 13.5.sp.noFontScale()
                            )
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(modifier = Modifier.height(100.dp).width(150.dp),
                                colors = CardDefaults.cardColors(CardBlue.copy(0.5f)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()){
                                    Image(modifier = Modifier.fillMaxSize(),painter = painterResource(R.drawable.brand_voucher), contentDescription = "")
                                }
                            }
                            Card(modifier = Modifier.height(100.dp).width(150.dp),
                                colors = CardDefaults.cardColors(Pink40.copy(0.5f)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()){
                                    Image(modifier = Modifier.fillMaxSize(),painter = painterResource(R.drawable.brand_voucher), contentDescription = "")
                                }
                            }
                            Card(modifier = Modifier.height(100.dp).width(150.dp),
                                colors = CardDefaults.cardColors(CardGreen.copy(0.5f)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()){
                                    Image(modifier = Modifier.fillMaxSize(), painter = painterResource(R.drawable.brand_voucher), contentDescription = "")
                                }
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomText(
                                text = "Payments",
                                fontFamily = FontProvider.INTER,
                                fontWeight = FontWeight(500),
                                fontSize = 13.5.sp.noFontScale()
                            )
                            CustomText(text = "See all",
                                fontSize = 13.5.sp.noFontScale(),
                                color = Color.LightGray,
                                fontFamily = FontProvider.INTER,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        scope.launch {
                                            nextScrollState.animateScrollTo(nextScrollState.maxValue)
                                        }
                                    }
                                    .onGloballyPositioned {
                                        seeAllwidth.value = it.positionInWindow().y.toInt() + 780
                                    })
                        }
                        Row(
                            modifier = Modifier.widthIn(config.screenWidthDp.dp)
                                .horizontalScroll(nextScrollState)
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            payment.forEach {
                                CircularBoxIcon(
                                    modifier = Modifier.width(IntrinsicSize.Max),
                                    icon = it.icon,
                                    iconSize = it.iconSize,
                                    text = it.text
                                ) {
                                    dashboardViewModel.handleFeatureClick(it.text)
                                }
                            }
                        }
                        DashboardBottomFrame()
                        DashboardTopFrame()
                    }
                }
            }
        }
    }
}