package com.isu.prepaidcard.presentation.screens.dashboard.mandate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.MerchantStatusCard
import com.isu.prepaidcard.presentation.viewmodels.MandateViewModel

/**
 * This composable function represents the mandates history screen of the application.
 *
 * @param mandateViewModel ViewModel containing the mandate related data and logic.
 *
 * @Composable This annotation indicates that the function is composable and can be used within a Jetpack Compose composable hierarchy.
 */
@Composable
fun MandateHistory(mandateViewModel: MandateViewModel){
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Mandates History"
            )
        },
        containerColor = Color.White
    ) {  Column(modifier = Modifier.padding(top = it.calculateTopPadding()).background(Color.White)) {

        LazyColumn(verticalArrangement =
        if (mandateViewModel.canceledList.isEmpty())
            Arrangement.Center
        else
            Arrangement.spacedBy(15.dp),
            modifier = Modifier.fillMaxWidth()
               .padding(20.dp)
        ) {
           items(mandateViewModel.canceledList){
               MerchantStatusCard(it){

               }
           }
            if (mandateViewModel.canceledList.isEmpty()) {
                item {
                    Image(
                        painter = painterResource(com.isu.common.R.drawable.no_data),
                        "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

        }
    } }
}