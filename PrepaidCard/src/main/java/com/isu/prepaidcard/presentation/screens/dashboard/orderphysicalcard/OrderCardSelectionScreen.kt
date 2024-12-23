package com.isu.prepaidcard.presentation.screens.dashboard.orderphysicalcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.ShippingAddressItem
import com.isu.common.customcomposables.getLocationDetails
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.DeleteAddressEvent
import com.isu.common.events.EditAddressEvent
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.CardBlue
import com.isu.common.ui.theme.errorColor
import com.isu.common.utils.FontProvider.getFont
import com.isu.common.utils.UiText
import com.isu.prepaidcard.presentation.screens.dashboard.reissuance.AddressSelectionScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun OrderCardSelectionScreen(modifier: Modifier = Modifier, viewModel: OrderPhysicalCardViewModel) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.getSavedData()
    }
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Order Card"
            )
        },
        bottomBar = {
            CustomButton(
                color = CardBlue,
                text = "Select",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                onClick = {
                    scope.launch {
                        NavigationEvent.helper.navigateTo(CardManagement.OrderPhyicalCardDetailsScreen)
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
                        vertical = 0.dp,
                        horizontal = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.padding(),
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
                                        text = "XXXX XXXX XXXX ${
                                            viewModel.cardNumber.value.takeLast(
                                                4
                                            )
                                        }",
                                        fontSize = 20.sp.noFontScale(),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                    CustomText(
                        text = "Ordering card  for ${viewModel.userName.value}",
                        fontWeight = FontWeight(550),
                        fontSize = 15.sp.noFontScale()
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCardShippingDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderPhysicalCardViewModel,
) {
    val nameOnCard = viewModel.nameOnCard
    val nameOnCardError = viewModel.nameOnCardError
    val nameOnCardErrorMessage = viewModel.nameOnCardErrorMessage
    val selectedAddress = viewModel.selectedAddressForShipping
    val scope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Order Card"
            )
        },
        bottomBar = {
            CustomButton(
                color = CardBlue,
                text = "Proceed to Pay Rs. 49",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                onClick = {
                    if (selectedAddress.value == null) {
                        scope.launch {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.ErrorSnackBar,
                                    UiText.DynamicString("Please select address")
                                )
                            )
                        }
                    } else {
                        viewModel.orderPhysicalCard(onSuccess = {
                            nameOnCard.value = ""
                            selectedAddress.value = null
                        })
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
                        vertical = 10.dp,
                        horizontal = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CustomInputField(
                            enabled = false,
                            state = viewModel.userName.value,

                            annotatedLabel = buildAnnotatedString {
                                append("Name on card")
                                withStyle(SpanStyle(color = errorColor)) {
                                    append("*")
                                }
                            },
                            isError = nameOnCardError.value,
                            errorMessage = nameOnCardErrorMessage.value,
                            placeholder = "Enter Name"
                        ) {
                            nameOnCard.value = it
                            nameOnCardError.value = false
                            nameOnCardErrorMessage.value = ""
                        }
                        CustomInputField(
                            labelRequired = false,
                            state = if (selectedAddress.value == null) "Card delivery Address" else (selectedAddress.value?.address1
                                ?: "") + "," + (selectedAddress.value?.state
                                ?: "") + "," + (selectedAddress.value?.city
                                ?: "") + "," + (selectedAddress.value?.country
                                ?: "") + "," + (selectedAddress.value?.pinCode ?: ""),
                            enabled = false,
                            color = TextFieldDefaults.colors(
                                disabledTextColor = Color(0xff008F9F),
                                disabledContainerColor = Color(0xffE6F9FB)
                            ),
                            textStyle = TextStyle(
                                fontSize = 13.sp.noFontScale(),
                                fontFamily = getFont("Lato", weight = FontWeight(500)),
                                color = Color(0xff008F9F),
                                textAlign = TextAlign.Start
                            ),
                            trailingIcon = {
                                CustomText(
                                    text = if (selectedAddress.value == null) "Select" else "Change",
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable {
                                        scope.launch {
                                            NavigationEvent.helper.navigateTo(CardManagement.OrderPhyicalCardShippingAddressScreen)
                                        }
                                    }.padding(end = 10.dp)
                                )
                            }
                        ) {

                        }

                    }
                }
            }
        }
    }
}

@Composable
fun OrderCardShippingAddressScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderPhysicalCardViewModel,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchAddress()
    }
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Card Delivery Address"
            )
        },
        bottomBar = {
            CustomButton(
                color = CardBlue,
                text = "Select Address",

                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                onClick = {
                    scope.launch {

                        NavigationEvent.helper.navigateBack()
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
                        vertical = 10.dp,
                        horizontal = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        CustomInputField(
                            labelRequired = false,
                            textStyle = TextStyle(
                                fontSize = 13.sp.noFontScale(),
                                fontFamily = getFont("Lato", weight = FontWeight(500)),
                                color = Color(0xff008F9F),
                                textAlign = TextAlign.Start
                            ),
                            state = "Current location using GPS",
                            enabled = false,
                            color = TextFieldDefaults.colors(
                                disabledContainerColor = Color.White,
                                disabledTextColor = Color(0xff008F9F),
                                disabledIndicatorColor = Color(0xff008F9F)
                            ),
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(com.isu.common.R.drawable.gps),
                                    "",
                                    tint = Color(0xff008F9F),
                                    modifier = Modifier.size(20.dp)
                                )

                            },
                            modifier = Modifier.clickable {

                                scope.launch {
                                    val latLong = LatLongFlowProvider.latLongFlow.first()
                                    try {
                                        val lat = latLong.split(",")[0].toDouble()
                                        val long = latLong.split(",")[1].toDouble()
                                        val details = getLocationDetails(
                                            context = context,
                                            latitude = lat,
                                            longitude = long
                                        )
                                        viewModel.selectedAddressForShipping.value =
                                            ShippingAddressItem(
                                                country = details?.country ?: "",
                                                city = details?.city ?: "",
                                                address1 = details?.address1 ?: "",
                                                address2 = details?.address2 ?: "",
                                                state = details?.state ?: "",
                                                pinCode = details?.pinCode ?: "",
                                                type = "OTHERS",

                                                )
                                        scope.launch {
                                            NavigationEvent.helper.navigateBack()
                                        }
                                    } catch (_: Exception) {

                                    }

                                }

                            }
                        ) {

                        }
                        CustomText(text = "OR", color = Color(0xff008F9F))
                        AddressSelectionScreen(
                            listOfAddress = viewModel.availanleAddress,
                            onSelect = {
                                viewModel.selectedAddressForShipping.value = it
                            },
                            onAddAddress = {
                                scope.launch {
                                    NavigationEvent.helper.navigateTo(CardManagement.AddAddress)
                                }
                            }, onDeleteAddress = {

                            scope.launch {
                                    DeleteAddressEvent.deleteAddress(it)
                                }
                            }, onUpdateAddress = {


                                scope.launch {
                                    EditAddressEvent.editAddress(it)

                                }
                            })

                    }
                }
            }
        }
    }
}