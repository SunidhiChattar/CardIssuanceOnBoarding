package com.isu.prepaidcard.presentation.screens.dashboard.orderhistory

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.ShippingAddressItem
import com.isu.common.customcomposables.StatusColorComponent
import com.isu.common.customcomposables.noFontScale
import com.isu.common.ui.theme.ActiveTextBackground
import com.isu.common.ui.theme.TextGreen
import com.isu.prepaidcard.data.ShippingAddressForOrderCard

data class OrderDetails(
    val cardReferenceNumber: String = "",
    val orderID: String = "",
    val userName: String = "",
    val cardType: String = "",
    val orderAmmount: String = "",
    val orderDate: String = "",
    val deliveryDate: String = "",
    val orderStatus: OrderStatus = OrderStatus.CONFIRMED,
    val orderAddress: ShippingAddressItem = ShippingAddressItem()
)

sealed interface OrderStatus {
    data object CONFIRMED : OrderStatus
    data object IN_TRANSIT : OrderStatus
    data object DELIVERED : OrderStatus
}
@Composable
fun OrderHistoryDetails(viewModel: OrderHistoryViewModel) {

    val config = LocalConfiguration.current
    val orderDetails = viewModel.selectedOrderDetails.value
        Scaffold(
            containerColor = Color.White,
            topBar = {
                CustomProfileTopBar(text = "Order History")
            },
        ) { padding ->
            KeyBoardAwareScreen(
                shouldScroll = false,
                modifier = Modifier.padding(top = padding.calculateTopPadding())
            ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
            ) {
                Column(
                    verticalArrangement =
                    Arrangement.spacedBy(15.dp),
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            .padding(horizontal = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.wrapContentWidth().padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                CustomText(
                                    text = orderDetails?.userName?.capitalize(Locale.current) ?: "",
                                    fontSize = 14.sp.noFontScale(),
                                    fontWeight = FontWeight(400),
                                    color = Color.Black
                                )
                                CustomText(
                                    text = (orderDetails?.cardType
                                        ?: "") + "(" + orderDetails?.cardReferenceNumber?.takeLast(4) + ")",
                                    fontSize = 14.sp.noFontScale(),
                                    color = Color.Gray,
                                    fontWeight = FontWeight(400)
                                )
                                CustomText(
                                    text = "₹${orderDetails?.orderAmmount}",
                                    fontSize = 18.sp.noFontScale(),
                                    fontWeight = FontWeight(550),
                                    color = Color.Black
                                )
                            }
                            Card(
                                modifier = Modifier.size(75.dp).align(Alignment.CenterVertically),
                                colors = CardDefaults.cardColors(Color.Transparent),
                                border = BorderStroke(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Column(
                                    modifier = Modifier.size(75.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(com.isu.common.R.drawable.small_card),
                                        contentDescription = "",
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }
                        Column(
                            modifier = Modifier.padding(5.dp),
                            verticalArrangement = Arrangement.spacedBy(13.dp)
                        ) {
                            Row(modifier = Modifier.wrapContentWidth()) {
                                CustomText(
                                    text = "Order Id: ",
                                    fontSize = 14.sp.noFontScale(),
                                    fontWeight = FontWeight(400)
                                )
                                CustomText(
                                    text = orderDetails?.orderID ?: "",
                                    fontSize = 14.sp.noFontScale(),
                                    color = Color.Gray,
                                    fontWeight = FontWeight(400)
                                )
                            }
                            Row(modifier = Modifier.wrapContentWidth()) {
                                CustomText(
                                    text = "Order Date: ",
                                    fontSize = 14.sp.noFontScale(),
                                    fontWeight = FontWeight(400)
                                )
                                CustomText(
                                    text = orderDetails?.orderDate ?: "",
                                    fontSize = 14.sp.noFontScale(),
                                    color = Color.Gray,
                                    fontWeight = FontWeight(400)
                                )
                            }

                            Row(modifier = Modifier.wrapContentWidth()) {
                                CustomText(
                                    text = "Expected Delivery: ",
                                    fontSize = 14.sp.noFontScale(),
                                    fontWeight = FontWeight(400)
                                )
                                CustomText(
                                    text = orderDetails?.deliveryDate ?: "",
                                    fontSize = 14.sp.noFontScale(),
                                    color = Color.Gray,
                                    fontWeight = FontWeight(400)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (orderDetails?.orderStatus == OrderStatus.CONFIRMED) {
                                    Icon(
                                        modifier = Modifier.size(25.dp),
                                        painter = painterResource(com.isu.common.R.drawable.success_tick),
                                        contentDescription = "",
                                        tint = TextGreen
                                    )
                                }

                                Box(
                                    modifier = Modifier.padding(top = 10.dp).height(30.dp)
                                        .width(1.dp).background(Color.Gray)
                                )
                                if (orderDetails?.orderStatus == OrderStatus.IN_TRANSIT) {
                                    Icon(
                                        modifier = Modifier.size(25.dp),
                                        painter = painterResource(com.isu.common.R.drawable.success_tick),
                                        contentDescription = "",
                                        tint = TextGreen
                                    )
                                } else {
                                    RadioButton(
                                        selected = true,
                                        onClick = {},
                                        colors = RadioButtonDefaults.colors(Color.Gray)
                                    )
                                }

                                Box(
                                    modifier = Modifier.height(30.dp).width(1.dp)
                                        .background(Color.Gray)
                                )
                                if (orderDetails?.orderStatus == OrderStatus.DELIVERED) {

                                    Icon(
                                        modifier = Modifier.size(25.dp),
                                        painter = painterResource(com.isu.common.R.drawable.success_tick),
                                        contentDescription = "",
                                        tint = TextGreen
                                    )
                                } else {

                                    RadioButton(
                                        selected = true,
                                        onClick = {},
                                        colors = RadioButtonDefaults.colors(Color.Gray)
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                Column {
                                    CustomText(text = "Sit back & relax, we are preparing your package",
                                        fontWeight = FontWeight(400),
                                        fontSize = 14.sp.noFontScale()
                                        )
                                    StatusColorComponent(
                                        withCircle = true,
                                        status = "Confirmed",
                                        color = if (orderDetails?.orderStatus == OrderStatus.CONFIRMED) {
                                            ActiveTextBackground
                                        } else {
                                            ActiveTextBackground.copy(0.1f)
                                        },
                                        textColor = if (orderDetails?.orderStatus == OrderStatus.CONFIRMED) {
                                            TextGreen
                                        } else {
                                            Color.DarkGray.copy(0.5f)
                                        }

                                    )
                                }
                                Column {
                                    CustomText(
                                        text = "Your order is on the way. It will reach you shortly",
                                        fontSize = 14.sp.noFontScale(),
                                        color = Color.Gray,
                                        fontWeight = FontWeight(400)
                                    )
                                    StatusColorComponent(
                                        withCircle = true,
                                        status = "In Transit",
                                        color = if (orderDetails?.orderStatus == OrderStatus.IN_TRANSIT) {
                                            ActiveTextBackground
                                        } else {
                                            Color.LightGray.copy(0.5f)
                                        },
                                        textColor = if (orderDetails?.orderStatus == OrderStatus.IN_TRANSIT) {
                                            TextGreen
                                        } else {
                                            Color.DarkGray.copy(0.5f)
                                        }
                                    )
                                }
                                Column {
                                    CustomText(
                                        text = "Your order has been delivered. Activate your card and start using it.",
                                        fontSize = 14.sp.noFontScale(),
                                        color = Color.Gray,
                                        fontWeight = FontWeight(400)
                                    )
                                    StatusColorComponent(
                                        withCircle = true,
                                        status = "Delivered",
                                        color = if (orderDetails?.orderStatus == OrderStatus.DELIVERED) {
                                            ActiveTextBackground
                                        } else {
                                            Color.LightGray.copy(0.5f)
                                        },
                                        textColor = if (orderDetails?.orderStatus == OrderStatus.DELIVERED) {
                                            TextGreen
                                        } else {
                                            Color.DarkGray.copy(0.5f)
                                        }
                                    )
                                }
                            }
                        }
                        ShippingAddressForOrderCard(
                            addressItem = orderDetails?.orderAddress ?: ShippingAddressItem()
                        )
                    }
                }
            }
        }
    }
}