package com.isu.prepaidcard.presentation.screens.dashboard.addoncard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.CardBlue
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.plaeholderColor
import com.isu.common.utils.FontProvider.LATO_FONT
import com.isu.prepaidcard.R
import com.isu.prepaidcard.presentation.viewmodels.DashboardViewModel
import kotlinx.coroutines.launch

@Composable
fun AddOnCardName(addOnCardViewModel: AddOnCardViewModel,
                  dashboardViewModel: DashboardViewModel){
    val cardList = dashboardViewModel.cardList
    val pagerState = rememberPagerState(pageCount = { dashboardViewModel.cardList.size })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val cardName = dashboardViewModel.userName
    val purpose = remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        dashboardViewModel.getSavedData()
    }
        Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Add Card"
            )
        },
        bottomBar = {
            CustomButton(
                color = if (cardName.value.isEmpty()) plaeholderColor else CardBlue,
                text = "Continue",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                onClick = {
                    if(cardName.value.isNotEmpty()){
                        scope.launch {
                            addOnCardViewModel.addOnCard(
                                cardReferenceId = try{
                                    cardList[pagerState.currentPage]?.cardRefId
                                }
                                catch (e:Exception) {
                                    0
                                },
                                nameOnCard = cardName.value,
                                addCardForSelf = true
                            ){
                                scope.launch {
                                    NavigationEvent.helper.navigateTo(CardManagement.AddonCardSelfSuccess)
                                }
                            }
                        }
                    }
                }
            )
        },
        containerColor = Color.White
    ) {
        KeyBoardAwareScreen(shouldScroll = false) {
            Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(Modifier.widthIn(400.dp, 450.dp).height(230.dp)) {
                            Image(
                                painterResource(com.isu.common.R.drawable.card_bg),
                                modifier = Modifier.widthIn(400.dp, 450.dp).height(250.dp)
                                    .aspectRatio(1f),
                                contentScale = ContentScale.FillWidth,
                                contentDescription = null
                            )
                            Column(
                                Modifier.fillMaxSize().padding(22.dp),
                                verticalArrangement = Arrangement.Center
                            ) {

                                Column {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = com.isu.common.R.drawable.chip),
                                            "",
                                            modifier = Modifier.size(50.dp)
                                        )
                                        Image(
                                            painter = painterResource(id = com.isu.common.R.drawable.network),
                                            "",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    CustomText(
                                        text = "XXXX XXXX XXXX XXXX",
                                        fontSize = 20.sp.noFontScale(),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.padding(
                            vertical = 10.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CustomInputField(
                            enabled = false,
                            labelRequired = true,
                            labelComponent = {
                                val label = buildAnnotatedString {
                                    append("UserName")
                                    withStyle(SpanStyle(color = Color.Red, letterSpacing = 10.sp)) {
                                        append(context.getString(R.string.astrisk))
                                    }
                                }
                                CustomText(
                                    text = label,
                                    fontFamily = LATO_FONT,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.height(25.dp).padding(horizontal = 5.dp),
                                    fontSize = 13.sp.noFontScale(),
                                    color = authTextColor
                                )
                            },
                            placeholder = "Enter UserName",
                            state = cardName.value,
                            onValueChange = {
                                cardName.value = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                        CustomInputField(
                            enabled = true,
                            labelRequired = true,
                            labelComponent = {
                                val label = buildAnnotatedString {
                                    append("Purpose")
                                    withStyle(SpanStyle(color = Color.Red, letterSpacing = 10.sp)) {
                                        append(context.getString(R.string.astrisk))
                                    }
                                }
                                CustomText(
                                    text = label,
                                    fontFamily = LATO_FONT,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.height(25.dp).padding(horizontal = 5.dp),
                                    fontSize = 13.sp.noFontScale(),
                                    color = authTextColor
                                )
                            },
                            placeholder = "Give the Card Purpose",
                            state = purpose.value,
                            onValueChange = {
                                purpose.value = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                    }
                }
            }
        }
    }
}