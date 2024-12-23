package com.isu.prepaidcard.presentation.screens.dashboard

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.isu.common.R
import com.isu.common.customcomposables.CircularBoxIcon
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.ManageNfc
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.ShowAddCard
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.CircularBoxLayout
import com.isu.common.utils.FontProvider.INTER
import com.isu.common.utils.ManageFeatures
import com.isu.common.utils.UiText
import com.isu.prepaidcard.presentation.components.CardDetailsItem
import com.isu.prepaidcard.presentation.components.CustomCardinDashboard
import com.isu.prepaidcard.presentation.viewmodels.DashboardViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * This composable function represents the card management screen of the dashboard.
 *
 * @param dashboardViewModel ViewModel containing the card management related data and logic.
 *
 * @Composable This annotation indicates that the function is composable and can be used within a Jetpack Compose composable hierarchy.
 */
object ADD_CARD_FOR {
    const val SELF = "self"
    const val SOMEONE_ELSE = "someoneelse"
}

//{
//    "mccCode": "3549",
//    "isActive": false
//}
data class UpdateMcc(
    val mccCode: String,
    val isActive: Boolean,
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CardManagement(dashboardViewModel: DashboardViewModel) {

    val context = LocalContext.current
    val config = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val lazyListState= rememberLazyListState()
    val showMccCard = dashboardViewModel.showMccDialog
    val seeAllwidth= remember{
        mutableStateOf(0)
    }
    val addCardVisible = remember {
        mutableStateOf(false)
    }
    val mccList = dashboardViewModel.mcclist
    /*   val cardList = allCards
       val pagerState = rememberPagerState(pageCount = { cardList.size })*/
    val cardList = dashboardViewModel.cardList
    val pagerState = rememberPagerState(pageCount = { dashboardViewModel.cardList.size })
    val checked = remember{mutableStateOf(false)}
    val showCardAccess = dashboardViewModel.showCardAccess
    val showTransaction = dashboardViewModel.showTransaction
    val showAdditionalOption = dashboardViewModel.showAdditionalOption

    val cardAccess = dashboardViewModel.cardAccess
    val cardAccessII = dashboardViewModel.cardAccessII
    val flowList: MutableList<CircularBoxLayout> = mutableListOf()
    flowList.addAll(cardAccess)
    flowList.addAll((cardAccessII))
    flowList.addAll(dashboardViewModel.cardAccessIII)

    val cvv=remember{
        mutableStateOf("")
    }
    val clipBoardManager = LocalClipboardManager.current
    val balance = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        dashboardViewModel.getCardDataByMobileNo {}
    }
    LaunchedEffect(pagerState.currentPage) {

        dashboardViewModel.currentPage.value = pagerState.currentPage
        if (dashboardViewModel.cardList.isNotEmpty()) {
            dashboardViewModel.cardRefid.value = cardList[pagerState.currentPage]?.cardRefId

            dashboardViewModel.getCardDataByRefId(cardReferenceId = null) { resp ->
                dashboardViewModel.saveToPreference(data = resp)

                }
            dashboardViewModel.mutableHashSet.clear()

        }




    }
    val showDetails = remember { mutableStateOf(false) }
    val showBalance = remember { mutableStateOf(false) }
    val showCVV = remember { mutableStateOf(false) }
    val mccSearch = remember {
        mutableStateOf("")
    }



    LaunchedEffect(pagerState.currentPage) {
        showBalance.value = false
        showCVV.value = false
        showDetails.value = false
        pagerState.animateScrollToPage(dashboardViewModel.currentPage.value)
        Log.d("pager", "DashboardHomeScreen: ${pagerState.currentPage}")

    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

        /*  if (showMccCard.value) {
              Mcc(onDismiss = {
                  showMccCard.value = false
                  mccList.clear()
              }, onConfirm = {

                  scope.launch {
                      dashboardViewModel.changeMccStatus(
                          cardRefId = cardList[pagerState.currentPage]?.cardRefId,
                          onError = {
                          showMccCard.value = false
                          },
                          onSuccess = {
                              dashboardViewModel.getCardDataByRefId {

                              }
                              mccList.clear()
                              showMccCard.value = false
                          })
                  }
              }

              ) {
                  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                      LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                          items(
                              items = dashboardViewModel.currentcardData.value?.mccDetails
                                  ?: emptyList()
                          ) {

                              MCCOption(
                                  address = "${it?.mccName}",
                                  isSelected = it?.applicable == true,
                                  onSelect = { bool ->
                                      Log.d("MCC", "CardManagement:$bool ")
                                      if (bool == it?.applicable) {
                                          mccList.remove(
                                              UpdateMcc(
                                                  mccCode = it.mccCode.toString(),
                                                  isActive = !bool
                                              )
                                          )
                                      } else {
                                          mccList.add(
                                              UpdateMcc(
                                                  mccCode = it?.mccCode.toString(),
                                                  isActive = bool
                                              )
                                          )
                                      }


                                  }

                              )
                      }
                      }

                  }

              }
          }*/
        val bottomBarVisible = ShowAddCard.showAddCardModal.collectAsState(false)
        BackHandler {
            scope.launch {

                if (bottomBarVisible.value) {
                    ShowAddCard.hide()
                } else {
                    NavigationEvent.helper.navigateBack()
                }
            }
        }
    Scaffold(topBar = {
        CustomProfileTopBar("All Cards", trailinIcon = {
            if (!dashboardViewModel.isChildCustomer.value) {
                Box(
                    modifier = Modifier.size(50.dp)
                        .alpha(if ((dashboardViewModel.currentcardData.value?.isActive == false || dashboardViewModel.currentcardData.value?.isStolen == true || dashboardViewModel.currentcardData.value?.isLost == true || dashboardViewModel.currentcardData.value?.isHotlist == true)) 0.5f else 1f),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            if ((dashboardViewModel.currentcardData.value?.isActive == false || dashboardViewModel.currentcardData.value?.isStolen == true || dashboardViewModel.currentcardData.value?.isLost == true || dashboardViewModel.currentcardData.value?.isHotlist == true)) {
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.ErrorSnackBar,
                                        UiText.DynamicString("Card is not Active")
                                    )
                                )
                            } else {

                                ShowAddCard.show()
                            }
                        }
                    }) {
                        Image(
                            painter = painterResource(R.drawable.add_card),
                            "",
                            Modifier.size(40.dp)
                        )
                    }
                    if ((dashboardViewModel.currentcardData.value?.isActive == false || dashboardViewModel.currentcardData.value?.isStolen == true || dashboardViewModel.currentcardData.value?.isLost == true || dashboardViewModel.currentcardData.value?.isHotlist == true)) {
                        Icon(
                            painterResource(R.drawable.baseline_block_24),
                            "",
                            tint = Color.Red.copy(0.5f),
                            modifier = Modifier.align(
                                Alignment.TopEnd
                            ).size(15.dp)
                        )
                    }
                }
            }



        })
    },

        containerColor = Color.White) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding()).background(Color.White)) {

            Column(
                verticalArrangement =
                if (cardList.isEmpty())
                    Arrangement.Center
                else
                    Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding( if (cardList.isEmpty())
                        20.dp
                    else 0.dp)){
                if (cardList.isEmpty()){
                    KeyBoardAwareScreen(shouldScroll = false, screenScrollState = scrollState) {
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
                else{
                    Column(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight().verticalScroll(
                            scrollState
                        ),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        if (pagerState.pageCount == 1) {
                            Row {
                                CustomText(text = "<${1}/${cardList.size}>")
                            }
                            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                                CustomCardinDashboard(
                                    showDetails = showDetails,
                                    cardDataByRefId = dashboardViewModel.currentcardData.value,
                                    onCardClick = {
                                        dashboardViewModel.setPrimary(cardRefId = it) {
                                            scope.launch {
                                                pagerState.animateScrollToPage(0)
                                            }

                                        }
                                    },
                                    isPrimary = dashboardViewModel.cardList.getOrNull(0)?.isPrimary == true,
                                    newCard = dashboardViewModel.newCardRefId.value
                                )
                            }

                        } else {
                            HorizontalPager(
                                state = pagerState,
                                contentPadding = PaddingValues(
                                    horizontal = 30.dp,
                                ),
                                pageSize = PageSize.Fill,
                                pageSpacing = 10.dp
                            ) {
                                CustomCardinDashboard(
                                    color = dashboardViewModel.cardList[it]?.cardColor
                                        ?: appMainColor,
                                    newCard = dashboardViewModel.newCardRefId.value,
                                    isPrimary = dashboardViewModel.cardList[it]?.isPrimary == true,
                                    showDetails = showDetails,
                                    cardDataByRefId = dashboardViewModel.currentcardData.value
                                ) { refId ->
                                    if (cardList[it]?.isActive == true) {
                                        dashboardViewModel.setPrimary(cardRefId = refId) {
                                            scope.launch {
                                                pagerState.animateScrollToPage(0)
                                            }

                                        }
                                    } else {
                                        scope.launch {
                                            ShowSnackBarEvent.helper.emit(
                                                ShowSnackBarEvent.show(
                                                    type = SnackBarType.ErrorSnackBar,
                                                    UiText.DynamicString("Please Activate the card ")
                                                )
                                            )
                                        }
                                    }

                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                CustomText(text = "<<", modifier = Modifier.clickable {
                                    scope.launch {
                                        pagerState.animateScrollToPage(0)
                                    }

                                }, fontWeight = FontWeight(600), fontSize = 17.sp.noFontScale())
                                CustomText(text = "<${pagerState.currentPage + 1}/${cardList.size}>")
                                CustomText(text = ">>", modifier = Modifier.clickable {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.pageCount + 1)
                                    }

                                }, fontWeight = FontWeight(600), fontSize = 17.sp.noFontScale())
                            }
                        }
                        CardDetailsItem(

                            cardData = dashboardViewModel.currentcardData.value,
                            status = dashboardViewModel.cardStatus.value,
                            showDetails = showDetails,
                            cardDatByMobileNumber = cardList[pagerState.currentPage],
                            cvv = cvv.value,
                            onCvvToggle = {
                                    onSuccess->
                                scope.launch {
                                    if (!showCVV.value) {
                                        cvv.value=""
                                        dashboardViewModel.viewCardCvv(cardReferenceId = cardList[pagerState.currentPage]?.cardRefId) {
                                            Log.d("CARDCVV", "CardManagement: ${it.data}")
                                            cvv.value = it.data?.cvv ?: ""
                                        }
                                    }
                                    onSuccess()
                                }
                            },
                            onShowDetails = {
                                scope.launch {
                                    if (!showBalance.value) {
                                        dashboardViewModel.getCardBalance(
                                            cardReferenceId = cardList[pagerState.currentPage]?.cardRefId
                                        ) { resp ->

                                            balance.value = resp.data?.balance.toString()
                                        }
                                    }
                                    it.invoke()
                                }
                            },
                            onCopyCardNumberr = {
                                clipBoardManager.setText(
                                    buildAnnotatedString {
                                        append(cardList[pagerState.currentPage]?.decrypted)
                                    }
                                )
                            },
                            cvvToggleState = showCVV,
                            balance = balance.value,
                            showBalance = showBalance,
                            setPrimary = {
                                dashboardViewModel.setPrimary(dashboardViewModel.cardRefid.value.toString()) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(0)
                                    }
                                }
                            }
                        )
                        Spacer(Modifier.height(7.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    showCardAccess.value = !showCardAccess.value
                                    scope.launch {
                                        delay(200)
                                        scrollState.animateScrollTo(scrollState.maxValue)
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomText(
                                text = "Card Access",
                                fontWeight = FontWeight(500),
                                fontSize = 14.sp.noFontScale(),
                                fontFamily = INTER,
                            )
                            Icon(
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    showCardAccess.value = !showCardAccess.value
                                    scope.launch {
                                        delay(200)
                                        scrollState.animateScrollTo(scrollState.maxValue)
                                    }
                                }.onGloballyPositioned {
                                    seeAllwidth.value= it.positionInWindow().y.toInt()+780
                                },
                                imageVector = if (showCardAccess.value) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                        AnimatedVisibility(visible = showCardAccess.value) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
//                                FlowRow(
//                                    maxItemsInEachRow = 5,
//                                    verticalArrangement = Arrangement.spacedBy(28.dp),
//                                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
//                                    overflow = FlowRowOverflow.Clip
//                                ) {
//                                    flowList.forEach {
//
//                                        if (it.text == ManageFeatures.RE_ISSUANCE && dashboardViewModel.currentcardData.value?.isVirtual == true) {
//
//                                        } else if (it.text == ManageFeatures.KIT_TO_KIT && dashboardViewModel.currentcardData.value?.isVirtual == true) {
//
//                                        } else if (it.text == ManageFeatures.ORDER_PHYSICAL && dashboardViewModel.currentcardData.value?.isVirtual != true) {
//
//                                        } else {
//
//                                            CircularBoxIcon(
//                                                icon = it.icon,
//                                                iconSize = it.iconSize,
//                                                text = it.text
//                                            ) {
//                                                dashboardViewModel.handleFeatureClick(it.text)
//                                            }
//
//                                        }
//
//                                    }
//                                }
                                flowList.filter {
                                    shouldShowReissuance(
                                        it,
                                        dashboardViewModel
                                    )
                                }
                                    .filter {
                                        shouldShowKitToKit(
                                            it,
                                            dashboardViewModel
                                        )
                                    }
                                    .filter { !(it.text == ManageFeatures.ORDER_PHYSICAL && dashboardViewModel.currentcardData.value?.isReissued == true) }
                                    .filter { !(it.text == ManageFeatures.ORDER_PHYSICAL && dashboardViewModel.currentcardData.value?.isVirtual == false) }
                                    .chunked(4).forEach {
                                        Row(
                                            modifier = Modifier.fillMaxWidth()
                                                .padding(horizontal = 5.dp)
                                                .horizontalScroll(rememberScrollState()),
                                            horizontalArrangement = if (it.size < 3) Arrangement.spacedBy(
                                                10.dp
                                            ) else Arrangement.SpaceEvenly,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            it.forEach {
                                                if (it.text.lowercase()
                                                        .contains("status".toRegex())
                                                ) {
                                                    CircularBoxIcon(
                                                        isNotActive = (dashboardViewModel.currentcardData.value?.isStolen == true ||
                                                                dashboardViewModel.currentcardData.value?.isLost == true ||
                                                                dashboardViewModel.currentcardData.value?.isHotlist == true),
                                                        icon = it.icon,
                                                        iconSize = it.iconSize,
                                                        text = it.text
                                                    ) {
                                                        dashboardViewModel.handleFeatureClick(it.text)
                                                    }
                                                } else if (it.text.lowercase()
                                                        .contains("kit".toRegex())
                                                ) {
                                                    CircularBoxIcon(
                                                        isNotActive = (false),
                                                        icon = it.icon,
                                                        iconSize = it.iconSize,
                                                        text = it.text
                                                    ) {
                                                        dashboardViewModel.handleFeatureClick(it.text)
                                                    }
                                                } else {
                                                    CircularBoxIcon(
                                                        isNotActive = (dashboardViewModel.currentcardData.value?.isActive == false ||
                                                                dashboardViewModel.currentcardData.value?.isStolen == true ||
                                                                dashboardViewModel.currentcardData.value?.isLost == true ||
                                                                dashboardViewModel.currentcardData.value?.isHotlist == true),
                                                        icon = it.icon,
                                                        iconSize = it.iconSize,
                                                        text = it.text
                                                    ) {
                                                        dashboardViewModel.handleFeatureClick(it.text)
                                                    }
                                                }



                                            }
                                        }
                                    }
//                                val spaceType = remember {
//                                    mutableStateOf(false)
//                                }
//                                val size = remember {
//                                    mutableStateOf(cardAccess.size)
//                                }
//
//                                  Row(
//                                      modifier = Modifier.fillMaxWidth().padding(start = 10.dp)
//                                          .horizontalScroll(rememberScrollState()),
//                                      horizontalArrangement =if(size.value>2) Arrangement.SpaceEvenly else Arrangement.spacedBy(10.dp),
//                                      verticalAlignment = Alignment.Top
//                                  ) {
//                                      cardAccess.forEach {
//                                          if(it.text== ManageFeatures.RE_ISSUANCE&& dashboardViewModel?.currentcardData?.value?.isVirtual==true){
//                                             size.value--
//                                          }
//                                          else if(it.text== ManageFeatures.KIT_TO_KIT&& dashboardViewModel?.currentcardData?.value?.isVirtual==true){
//                                            size.value--
//                                          }else{
//                                              CircularBoxIcon(
//                                                  icon = it.icon,
//                                                  iconSize = it.iconSize,
//                                                  text = it.text
//                                              ) {
//                                                  dashboardViewModel.handleFeatureClick(it.text)
//                                              }
//                                          }
//
//                                      }
//                                  }
//
//                                  Row(
//                                      modifier = Modifier.fillMaxWidth().padding(start = if(config.screenWidthDp<400) 10.dp else 20.dp)
//                                          .horizontalScroll(rememberScrollState()),
//                                      horizontalArrangement = Arrangement.spacedBy(if(config.screenWidthDp < 400)10.dp else 20.dp),
//                                      verticalAlignment = Alignment.Top
//                                  ) {
//                                      cardAccessII.forEach {
//
//                                              CircularBoxIcon(
//                                                  icon = it.icon,
//                                                  iconSize = it.iconSize,
//                                                  text = it.text
//                                              ) {
//                                                  dashboardViewModel.handleFeatureClick(it.text)
//                                              }
//
//
//                                      }
//                                  }
//                                  Row(
//                                      modifier = Modifier.fillMaxWidth().padding(start = if(config.screenWidthDp<400) 10.dp else 20.dp)
//                                          .horizontalScroll(rememberScrollState()),
//                                      horizontalArrangement = Arrangement.spacedBy(if(config.screenWidthDp < 400)10.dp else 20.dp),
//                                      verticalAlignment = Alignment.Top
//                                  ) {
//                                      dashboardViewModel.cardAccessIII.forEach {
//                                          CircularBoxIcon(
//                                              icon = it.icon,
//                                              iconSize = it.iconSize,
//                                              text = it.text
//                                          ) {
//                                              dashboardViewModel.handleFeatureClick(it.text)
//                                          }
//                                      }
//                                  }
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    showTransaction.value = !showTransaction.value
                                    scope.launch {
                                        delay(200)
                                        scrollState.animateScrollTo(scrollState.maxValue)
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomText(
                                text = "Transaction",
                                fontWeight = FontWeight(500),
                                fontFamily = INTER,
                                fontSize = 14.sp.noFontScale()
                            )
                            Icon(
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    showTransaction.value = !showTransaction.value
                                    scope.launch {
                                        delay(200)
                                        scrollState.animateScrollTo(scrollState.maxValue)
                                    }
                                }
                                    .onGloballyPositioned {
                                        seeAllwidth.value= it.positionInWindow().y.toInt()+780
                                    },
                                imageVector = if (showTransaction.value) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                        AnimatedVisibility(visible = showTransaction.value, modifier = Modifier.wrapContentSize()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                Row(
                                    modifier =Modifier.fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    dashboardViewModel.transaction.forEach{

                                        CircularBoxIcon(
                                            isNotActive = if (it.text.lowercase()
                                                    .contains("load".toRegex())
                                            ) (dashboardViewModel.currentcardData.value?.isActive == false || dashboardViewModel.currentcardData.value?.isStolen == true || dashboardViewModel.currentcardData.value?.isLost == true || dashboardViewModel.currentcardData.value?.isHotlist == true) else false,
                                            icon = it.icon,
                                            iconSize = it.iconSize,
                                            text = it.text
                                        ){
                                            dashboardViewModel.handleFeatureClick(it.text)
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(start = if(config.screenWidthDp<400) 10.dp else 20.dp)
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(if(config.screenWidthDp < 400)10.dp else 20.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    dashboardViewModel.transactionII.forEach {
                                        CircularBoxIcon(
                                            icon = it.icon,
                                            iconSize = it.iconSize,
                                            text = it.text
                                        ){
                                            dashboardViewModel.handleFeatureClick(it.text)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (dashboardViewModel.manageNFC.value){
                        ManageNfc(checked = checked,
                            onDismiss = {dashboardViewModel.manageNFC.value = false},
                            onConfirm = {dashboardViewModel.manageNFC.value = false})
                    }

                }
            }

        }
    }
}

}

fun shouldShowKitToKit(it: CircularBoxLayout, value: DashboardViewModel): Boolean {

    val data = value.currentcardData.value
    val isChild = value.isChildCustomer.value
    return if (it.text == ManageFeatures.KIT_TO_KIT) {
        if (isChild) {
            false
        } else if (data?.isVirtual == true) {
            data.isHotlist == true || data.isBlock == true
        } else {
            data?.isStolen == true || data?.isDamage == true || data?.isHotlist == true || data?.isBlock == true || data?.isReissued == true
        }
    } else {
        true
    }

}

fun shouldShowReissuance(it: CircularBoxLayout, value: DashboardViewModel): Boolean {
    val data = value.currentcardData.value
    val isChild = value.isChildCustomer.value

    return if (it.text == ManageFeatures.RE_ISSUANCE) {
        data?.isVirtual == false && !isChild
    } else {
        true
    }

}
