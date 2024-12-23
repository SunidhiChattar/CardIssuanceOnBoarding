package com.isu.common.customcomposables

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
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
import com.isu.common.R
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.BeneDetails

@Composable
fun OrderCard(
    beneDetails: BeneDetails,
    modifier: Modifier = Modifier.fillMaxWidth().padding(),
    headingText: String = "Krishna Das",
    onCLick: () -> Unit,

    ) {
    val openOptions = remember {
        mutableStateOf(false)
    }
    Card(
        modifier.border(width = 1.dp, color = Color.LightGray, RoundedCornerShape(5.dp))
            .heightIn(200.dp).clickable {
            openOptions.value = true
        }, shape = RoundedCornerShape(5.dp), colors = CardDefaults.cardColors(
            Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                ProfileText(text = headingText, fontSize = 16.sp.noFontScale())
                Box(Modifier.clickable { openOptions.value = !openOptions.value }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "",
                        tint = authTextColor
                    )
                    DropdownMenu(
                        expanded = openOptions.value, onDismissRequest = {
                            openOptions.value = false
                        }, Modifier.background(
                            Color.White
                        ).border(
                            1.dp, color = Color.LightGray,
                            RoundedCornerShape(5.dp)
                        )
                    ) {
                        Row(Modifier.width(200.dp).padding(10.dp).clickable {
                            onCLick()
                            openOptions.value = false
                        }, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(
                                painter = painterResource(R.drawable.card),
                                "",
                                tint = authTextColor
                            )
                            CustomText(
                                text = "Pay" +
                                        ""
                            )
                        }
                        Row(Modifier.width(200.dp).padding(10.dp).clickable {

                            openOptions.value = false
                        }, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(imageVector = Icons.Default.Delete, "", tint = authTextColor)
                            CustomText(
                                text = "Delete Bene" +
                                        ""
                            )
                        }
                    }
                }


            }
            Spacer(Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ProfileText(text = "Name")
                    ProfileText(
                        text = beneDetails.name,
                        color = Color.Black,
                        fontWeight = FontWeight(490)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ProfileText(text = "Bank Name")
                    ProfileText(
                        text = beneDetails.bankName,
                        color = Color.Black,
                        fontWeight = FontWeight(490)
                    )
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ProfileText(text = "Account Number")
                    ProfileText(
                        text = beneDetails.bankAccNumber,
                        color = Color.Black,
                        fontWeight = FontWeight(490)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ProfileText(text = "IFSC Code")
                    ProfileText(
                        text = beneDetails.IFSCCode,
                        color = Color.Black,
                        fontWeight = FontWeight(490)
                    )
                }

            }

        }


    }
}