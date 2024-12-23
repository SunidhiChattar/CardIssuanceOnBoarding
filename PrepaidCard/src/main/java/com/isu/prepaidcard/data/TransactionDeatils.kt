package com.isu.prepaidcard.data

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.ProfileText
import com.isu.common.customcomposables.ShippingAddressItem
import com.isu.common.customcomposables.noFontScale
import com.isu.common.ui.theme.CardRed
import com.isu.common.ui.theme.TextGreen
import com.isu.prepaidcard.presentation.screens.dashboard.orderhistory.OrderDetails
import com.isu.prepaidcard.presentation.screens.dashboard.statement.convertDate
import com.isu.prepaidcard.presentation.viewmodels.TransactionType

data class TransactionDeatils(
    val id: String = "",
    val transactionType: TransactionType,
    val balance: String,
    val fontColor: Color,
    val amount: String = "",
    val date: String,
    val transactionMode: String = "",
    val rrn: String = "",

)


@Composable
fun OrderStatementCard(
    selectedTransaction: TransactionDeatils,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.height(80.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.Transparent),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(10f),
    ){
        Column(
            modifier = modifier
                .padding(start = 10.dp, end = 10.dp)
                .clickable {
                    onClick()
                }
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().height(80.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


                    Column(verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        CustomText(
                            text = if (selectedTransaction.transactionType == TransactionType.CREDIT) "Credit" else "Debit",
                            fontSize = 14.sp.noFontScale(),
                            fontWeight = FontWeight(450)
                        )
                        CustomText(
                            text = convertDate(
                                selectedTransaction.date,
                                inputFormat = "yyyy-MM-dd hh:mm",
                                outputDateFormat = "dd MMM YY hh:mm"
                            ).toString(),
                            fontSize = 14.sp.noFontScale(),
                            color = Color.LightGray,
                            fontWeight = FontWeight(450)
                        )
                    }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomText(
                        text = if (selectedTransaction.fontColor == TextGreen) "+₹${selectedTransaction.amount}" else "-₹${selectedTransaction.amount}",
                        fontSize = 16.sp.noFontScale(),
                        color = if (selectedTransaction.fontColor == TextGreen) TextGreen else CardRed,
                        fontWeight = FontWeight.Bold
                        )

                }

            }
        }
    }
}

@Composable
fun DetailOrderStatementCard(
    radioValue: TransactionDeatils?,
    showRadioButton: Boolean = false,
    selectedTransaction: TransactionDeatils,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onRadioClick: (TransactionDeatils) -> Unit = {}
) {
    val selected = remember {
        mutableStateOf(radioValue)
    }
    Card(
        modifier = Modifier.height(80.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.Transparent),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(10f),
    ) {
        Column(
            modifier = modifier
                .padding(start = 10.dp, end = 10.dp)
                .clickable {
                    onClick()
                }
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().height(80.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showRadioButton) {
                    RadioButton(selected = radioValue == selectedTransaction, onClick = {

                        onRadioClick(selectedTransaction)
                    })
                }


                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                        CustomText(
                            text = selectedTransaction.date,
                            fontSize = 14.sp.noFontScale(),
                            fontWeight = FontWeight.Normal
                        )
                    CustomText(
                        text = selectedTransaction.transactionMode + "/" + selectedTransaction.rrn,
                            fontSize = 15.sp.noFontScale(),
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CustomText(
                            text = if (selectedTransaction.fontColor == TextGreen) "+₹${selectedTransaction.amount}.00" else "-₹${selectedTransaction.amount}.00" + if (selectedTransaction.transactionType == TransactionType.CREDIT) "Cred" else "Deb",
                            fontSize = 16.sp.noFontScale(),
                            color = if (selectedTransaction.fontColor == TextGreen) TextGreen else CardRed,
                            fontWeight = FontWeight.Bold
                        )
                    }

            }
        }
    }
}

@Composable
fun OrderHistoryCard(orderDetail: OrderDetails, onClick: () -> Unit) {
    Card(
        modifier = Modifier.height(130.dp).fillMaxWidth().clickable(
            interactionSource = remember { MutableInteractionSource() },
        indication = null){
        onClick.invoke()
    },
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(Color.Transparent),
        border = BorderStroke(1.dp, Color.LightGray)) {

        Row {
            CustomText(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                text = "Id",
                fontSize = 14.sp.noFontScale(),
                fontWeight = FontWeight(600)
            )
            CustomText(
                modifier = Modifier.padding(top = 16.dp, start = 6.dp),
                text = orderDetail.orderID,
                fontSize = 14.sp.noFontScale(),
                fontWeight = FontWeight(400)
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp).height(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {


            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Card(
                modifier = Modifier.size(55.dp),
                colors = CardDefaults.cardColors(Color.Transparent),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {

                Column(modifier = Modifier.size(55.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(com.isu.common.R.drawable.small_card),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp))
                    }
            }
            Column {
                CustomText(
                    text = orderDetail.userName + "(" + orderDetail.cardReferenceNumber.takeLast(4) + ")",
                    fontSize = 15.sp.noFontScale(),
                    fontWeight = FontWeight(600)
                )
                CustomText(
                    text = orderDetail.cardType,
                    fontSize = 14.sp.noFontScale(),
                    color = Color.Gray,
                    fontWeight = FontWeight(400)
                )
                CustomText(
                    text = orderDetail.orderDate,
                    fontSize = 14.sp.noFontScale(),
                    color = Color.Gray,
                    fontWeight = FontWeight(400)
                )
            }
        }

            Icon(painter = painterResource(com.isu.common.R.drawable.round_arrow_forward_ios_24), contentDescription = "",
                tint = Color.Gray
            )
        }

    }
}

@Composable
fun ShippingAddressForOrderCard(
    addressItem: ShippingAddressItem,
    modifier: Modifier = Modifier.fillMaxWidth().padding(),
    headingText: String = "Shipping Address"
) {
    val openOptions= remember {
        mutableStateOf(false)
    }
    Card(modifier.border(width = 1.dp, color = Color.LightGray, RoundedCornerShape(5.dp)).heightIn(200.dp), shape = RoundedCornerShape(5.dp), colors = CardDefaults.cardColors(
        Color.White)) {
        Column(modifier=Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                ProfileText(text = headingText, fontSize = 16.sp.noFontScale(), color = Color.Black)
            }
            Spacer(Modifier.height(2.dp))
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(modifier=Modifier.weight(1f),verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.Start) {
                    ProfileText(text = "Rode name, Area")
                    ProfileText(
                        text = addressItem.address1.toString(),
                        color = Color.Black,
                        fontWeight = FontWeight(490),
                        fontSize = 12.sp.noFontScale()
                    )
                }
                Column(modifier=Modifier.weight(1f),verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.Start) {
                    ProfileText(text = "House No, Building Name")
                    ProfileText(
                        text = addressItem.address2.toString(),
                        color = Color.Black,
                        fontWeight = FontWeight(490),
                        fontSize = 12.sp.noFontScale()
                    )
                }

            }
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(modifier=Modifier.weight(1f),verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.Start) {
                    ProfileText(text = "Pin Code")
                    ProfileText(
                        text = "${addressItem.pinCode}",
                        color = Color.Black,
                        fontWeight = FontWeight(490),
                        fontSize = 12.sp.noFontScale()
                    )
                }
                Column(modifier=Modifier.weight(1f),verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.Start) {
                    ProfileText(text = "state")
                    ProfileText(
                        text = "${addressItem.state}",
                        color = Color.Black,
                        fontWeight = FontWeight(490),
                        fontSize = 12.sp.noFontScale()
                    )
                }

            }

        }



    }
}