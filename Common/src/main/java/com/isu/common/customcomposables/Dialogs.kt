package com.isu.common.customcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.isu.common.R
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.errorColor
import com.isu.common.ui.theme.ticketTextLightColor

@Composable
fun StatusComponent(status: TicketStatus) {
    val color = status.color
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        CustomText(text = "Status", color = ticketTextLightColor, fontSize = 13.sp.noFontScale())
        StatusColorComponent(status = status.status, color = color, textColor = status.textColor)

    }
}

@Composable
fun StatusColorComponent(
    modifier: Modifier = Modifier,
    status: String,
    color: Color,
    withCircle: Boolean = true,
    textColor: Color,
    circleSize: Int = 10,
) {
    Row(
        modifier = modifier.height(30.dp).wrapContentHeight()
            .background(color = color, shape = RoundedCornerShape(100.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(withCircle){
            Box(
                modifier = Modifier.size(circleSize.dp).background(
                    shape = CircleShape,
                    color = if (color == Color.LightGray) Color.Gray else textColor
                )
            )
        }

        CustomText(
            text = status,
            color = if (color == Color.LightGray) Color.Gray else textColor,
            fontSize = 12.sp.noFontScale(),
            fontWeight = FontWeight(550),
        )
    }
}


@Composable
fun OpenTicketDialog(ticketData: TicketData, dismiss: () -> Unit, proceed: () -> Unit) {
    Dialog(onDismissRequest = {dismiss()}){
        Column ( modifier = Modifier.width(540.dp).wrapContentHeight().background(
            Color.White, RoundedCornerShape(10.dp)
        )) {
            Column (modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
                Row (modifier = Modifier.fillMaxWidth().padding(0.dp), verticalAlignment = Alignment.CenterVertically){
                    Spacer(Modifier.size(50.dp))
                    Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center){
                        CustomText(text="Reopen Ticket?", fontSize = 22.sp.noFontScale(), color = Color.Black, fontWeight = FontWeight(550))
                    }

                    Row (modifier = Modifier.size(50.dp).clickable { dismiss() }, horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically){
                        Icon(Icons.Default.Close,"")
                    }
                }
                Box(
                    Modifier.fillMaxWidth().border(1.dp, Color.LightGray,
                    RoundedCornerShape(5.dp)
                ).padding(10.dp).wrapContentHeight()){
                    Column (verticalArrangement = Arrangement.spacedBy(10.dp)){
                        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)){
                            Icon(painter = painterResource(R.drawable.ticket_flag),"", tint = authTextColor)
                            CustomText(text="Ticket ID: ${ticketData.ticketId}", fontSize = 13.sp.noFontScale(), fontWeight = FontWeight(500))
                        }
                        CardDetailComponent(s = "Request Title", s1 = ticketData.requestTitle, statusComponent = { CardDetailTitleComponent(s = "Raised Date", s1 = "01 Mar, 2024") })

                    }
                }
                Box(
                    Modifier.fillMaxWidth().border(1.dp, Color.LightGray,
                    RoundedCornerShape(5.dp)
                ).padding(10.dp)){
                    CardDetailComponent(s = "Category", s1 = ticketData.category, statusComponent = { StatusComponent(ticketData.status) })
                }
                Box(
                    Modifier.fillMaxWidth().border(1.dp, Color.LightGray,
                    RoundedCornerShape(5.dp)
                ).padding(10.dp)){
                    Column(modifier = Modifier.fillMaxWidth().height(70.dp).padding(end = 20.dp), verticalArrangement = Arrangement.spacedBy(5.dp)){
                        CustomText(text = "Description", color = ticketTextLightColor, fontSize = 14.sp.noFontScale())
                        CustomText(
                            text = "The user \\\"ReviewUser101\\\" is requesting a review of their ban, citing new evidence that was not considered during the initial investigation. They believe this evidence will demonstrate their compliance with community guidelines and justify the lifting of the ban.",
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp.noFontScale(),
                            fontWeight = FontWeight(500),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomResizeButton(
                    onClick = {
                        proceed()
                    },
                    color = Color(0xff0E8345),
                    modifier = Modifier.height(40.dp).fillMaxWidth(0.9f),
                    innerComponent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CustomText(text = "Reopen", color = Color.White)
                        }
                    }
                )
                CustomResizeButton(
                    onClick = {
                            dismiss()
                    },
                    color = Color.White,
                    modifier = Modifier.height(35.dp).fillMaxWidth(0.9f).border(1.dp,
                        authTextColor, shape = RoundedCornerShape(5.dp)
                    ),
                    innerComponent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CustomText(text = "Cancel", color = authTextColor)
                        }
                    }
                )

            }
        }
    }
}

@Composable
fun CloseTicketDialog(ticketData: TicketData,dismiss:()->Unit,proceed:()->Unit) {
    Dialog(onDismissRequest = {
        dismiss()
    }){
        Column ( modifier = Modifier.width(540.dp).wrapContentHeight().background(
            Color.White, RoundedCornerShape(10.dp)
        )) {
            Column (modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
                Row (modifier = Modifier.fillMaxWidth().padding(0.dp), verticalAlignment = Alignment.CenterVertically){
                    Spacer(Modifier.size(50.dp))
                    Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center){
                        CustomText(text="Close Ticket?", fontSize = 22.sp.noFontScale(), color = Color.Black, fontWeight = FontWeight(550))
                    }

                    Row(
                        modifier = Modifier.size(50.dp).clickable { dismiss() },
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Close,"")
                    }
                }
                Box(
                    Modifier.fillMaxWidth().border(1.dp, Color.LightGray,
                    RoundedCornerShape(5.dp)
                ).padding(10.dp).wrapContentHeight()){
                    Column (verticalArrangement = Arrangement.spacedBy(10.dp)){
                        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)){
                            Icon(painter = painterResource(R.drawable.ticket_flag),"", tint = authTextColor)
                            CustomText(text="Ticket ID: ${ticketData.ticketId}", fontSize = 13.sp.noFontScale(), fontWeight = FontWeight(500))
                        }
                        CardDetailComponent(s = "Request Title", s1 = ticketData.requestTitle, statusComponent = { CardDetailTitleComponent(s = "Raised Date", s1 = "01 Mar, 2024") })

                    }
                }
                Box(
                    Modifier.fillMaxWidth().border(1.dp, Color.LightGray,
                    RoundedCornerShape(5.dp)
                ).padding(10.dp)){
                    CardDetailComponent(s = "Category", s1 = ticketData.category, statusComponent = { StatusComponent(ticketData.status) })
                }
                Box(
                    Modifier.fillMaxWidth().border(1.dp, Color.LightGray,
                    RoundedCornerShape(5.dp)
                ).padding(10.dp)){
                    Column(modifier = Modifier.fillMaxWidth().height(70.dp).padding(end = 20.dp), verticalArrangement = Arrangement.spacedBy(5.dp)){
                        CustomText(text = "Description", color = Color.LightGray, fontSize = 14.sp.noFontScale())
                        CustomText(
                            text = "The user \\\"ReviewUser101\\\" is requesting a review of their ban, citing new evidence that was not considered during the initial investigation. They believe this evidence will demonstrate their compliance with community guidelines and justify the lifting of the ban.",
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp.noFontScale(),
                            fontWeight = FontWeight(500),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomResizeButton(
                    onClick = {
                        proceed.invoke()
                    },
                    color = errorColor,
                    modifier = Modifier.height(40.dp).fillMaxWidth(0.9f),
                    innerComponent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CustomText(text = "Close", color = Color.White)
                        }
                    }
                )
                CustomResizeButton(
                    onClick = {
                        dismiss.invoke()
                    },
                    color = Color.White,
                    modifier = Modifier.height(35.dp).fillMaxWidth(0.9f).border(1.dp,
                        authTextColor, shape = RoundedCornerShape(5.dp)
                    ),
                    innerComponent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CustomText(text = "Cancel", color = authTextColor)
                        }
                    }
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogOutDialog(modifier: Modifier = Modifier,onProceed:()->Unit,onCancel:()->Unit) {
    BasicAlertDialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier.wrapContentHeight().background(
            Color.White, RoundedCornerShape(10.dp)
            ).padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column (Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End){ IconButton(onClick = {onCancel()}){
                Icon(Icons.Default.Close,"", modifier = Modifier.align(Alignment.End))
            } }
            Image(painter = painterResource(R.drawable.logout_bg), "", Modifier.size(230.dp))
            Column (Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally){
                CustomText(textAlign = TextAlign.Center, text = "You Sure You Want to Log Out?", fontWeight = FontWeight(600), color = Color.Black)
                CustomText(text = "Worried About Unauthorized Charges? Do You Want to Block Your Card Now for Enhanced Security and Financial Protection?", textAlign = TextAlign.Center)
            }
            Column(
                modifier = Modifier.wrapContentHeight().padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CustomButton(text = "Logout", shape = RoundedCornerShape(5.dp), color = errorColor, onClick = {onProceed()})
                CustomCancelButton(text = "Cancel", onClick = {onCancel()})
            }
        }
    }
}

