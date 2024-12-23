package com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.isu.common.R
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.OrderCard
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.utils.BeneDetails
import kotlinx.coroutines.launch


@Composable
fun BeneficiaryListScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CustomProfileTopBar(text = "Beneficiary")
        },
        containerColor = White,

        ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(top = it.calculateTopPadding(), start = 22.dp, end = 22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(text = "Beneficiary List", fontWeight = FontWeight.SemiBold)
                Row(
                    Modifier.border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(5.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp).clickable {
                            scope.launch {
                                NavigationEvent.helper.navigateTo(CardManagement.AddBeneficiaryScreen)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(text = "Add Beneficiary")
                    Icon(
                        painter = painterResource(R.drawable.round_add),
                        contentDescription = "",
                        tint = Black,
                        modifier = Modifier.size(35.dp).padding(10.dp)
                            .border(1.dp, color = Black, shape = CircleShape)
                    )
                }
            }
            Spacer(Modifier.height(15.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(15) {
                    OrderCard(beneDetails = BeneDetails(), onCLick = {
                        scope.launch {
                            NavigationEvent.helper.navigateTo(CardManagement.BeneAmountPaymentScreen)
                        }
                    })
                }
            }
        }
    }
}