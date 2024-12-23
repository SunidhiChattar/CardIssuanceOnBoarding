package com.isu.common.customcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.ticketTextDarkColor

@Composable
fun TicketCard(ticketData: TicketData, onClick: () -> Unit, onDropDownClick: (String) -> Unit) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier.wrapContentHeight()
            .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(5.dp))
            .clickable { onClick() }
            .padding(start = 14.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Icon(
                        painter = painterResource(com.isu.common.R.drawable.ticket_flag),
                        "",
                        tint = authTextColor
                    )
                    CustomText(
                        text = "Ticket ID: ${ticketData.ticketId}",
                        fontSize = 13.sp.noFontScale(),
                        fontWeight = FontWeight(500),
                        color = ticketTextDarkColor
                    )
                }

                Box(contentAlignment = Alignment.CenterStart) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "",
                        tint = authTextColor,
                        modifier = Modifier.size(20.dp).clickable {
                            showDialog.value = !showDialog.value
                        })
                    DropdownMenu(
                        expanded = showDialog.value,
                        onDismissRequest = { showDialog.value = false },
                        Modifier.background(
                            Color.White
                        ).width(170.dp)
                    ) {
                        when (ticketData.status) {
                            is TicketStatus.Escalated -> {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Re-open",
                                            fontSize = 14.sp.noFontScale(),
                                            color = TicketStatus.Open().textColor
                                        )
                                    },
                                    onClick = {
                                        showDialog.value = false
                                        onDropDownClick("Re-open")
                                    },
                                    modifier = Modifier.height(30.dp).fillMaxWidth(),
                                )
                            }

                            is TicketStatus.Closed -> {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Re-open",
                                            fontSize = 14.sp.noFontScale(),
                                            color = TicketStatus.Open().textColor
                                        )
                                    },
                                    onClick = {
                                        showDialog.value = false
                                        onDropDownClick("Re-open")
                                    },
                                    modifier = Modifier.height(30.dp).fillMaxWidth(),
                                )
                            }

                            is TicketStatus.InProgress -> {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Close",
                                            fontSize = 14.sp.noFontScale(),
                                            color = Color.Red
                                        )
                                    },
                                    onClick = {
                                        showDialog.value = false
                                        onDropDownClick("Close")
                                    },
                                    modifier = Modifier.height(30.dp).fillMaxWidth(),
                                )
                            }

                            is TicketStatus.Open -> {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Close",
                                            fontSize = 14.sp.noFontScale(),
                                            color = Color.Red
                                        )
                                    },
                                    onClick = {
                                        showDialog.value = false
                                        onDropDownClick("Close")
                                    },
                                    modifier = Modifier.height(30.dp).fillMaxWidth(),
                                )
                            }

                            else -> {}
                        }

                    }
                }

            }
            Spacer(modifier = Modifier.height(0.dp))

            CardDetailComponent(
                s = "Category",
                s1 = ticketData.category,
                statusComponent = { StatusComponent(ticketData.status) })

        }
    }
}