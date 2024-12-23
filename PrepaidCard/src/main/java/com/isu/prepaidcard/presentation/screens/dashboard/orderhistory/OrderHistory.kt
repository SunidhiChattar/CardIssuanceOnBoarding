package com.isu.prepaidcard.presentation.screens.dashboard.orderhistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomTopBar
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.prepaidcard.data.OrderHistoryCard
import kotlinx.coroutines.launch

@Composable
fun OrderHistory(viewModel: OrderHistoryViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchOrderHistory {

        }
    }

    val scope = rememberCoroutineScope()
    val listOfOrderDetails = viewModel.listOfOrderDetails
    Scaffold(
        containerColor = Color.White,
        topBar = {
           CustomProfileTopBar(text = "Order History")
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(top = padding.calculateTopPadding()).background(Color.White)
        ) {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(listOfOrderDetails) {
                            OrderHistoryCard(orderDetail = it) {
                                viewModel.selectedOrderDetails.value = it
                                scope.launch {
                                    NavigationEvent.helper.navigateTo(CardManagement.OrderHistoryDetails)
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}