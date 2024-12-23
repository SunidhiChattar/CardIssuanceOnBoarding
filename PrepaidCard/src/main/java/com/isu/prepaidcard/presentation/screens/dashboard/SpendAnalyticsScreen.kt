package com.isu.prepaidcard.presentation.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.StatementComponentWithDropDown

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpendAnalyticsScreen(modifier: Modifier = Modifier) {
    Scaffold(topBar = {
        CustomProfileTopBar(text = "Spend Analytics") {

        }
    }, containerColor = Color.White) {
        KeyBoardAwareScreen(
            modifier.padding(
                top = it.calculateTopPadding(),
                start = 22.dp,
                end = 22.dp,
                bottom = 22.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Column {
                        CustomText(text = "Your Spending")
                        CustomText(
                            text = "₹1,640",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    StatementComponentWithDropDown(barText = "Oct", onClick = {})
                    StatementComponentWithDropDown(
                        dropDownList = getLast100Years().reversed(),
                        barText = "2023",
                        onClick = {})
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            FlowRow(
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                AnalyticsMeter(type = "POS", value = 25f)
                AnalyticsMeter(type = "IMPS", value = 40f)
                AnalyticsMeter(type = "ATM", value = 75f)
                AnalyticsMeter(type = "ECOM", value = 65f)
                AnalyticsMeter(type = "Subscription", value = 5f)


            }
        }
    }


}