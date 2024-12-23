package com.isu.common.customcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.isu.common.R
import com.isu.common.ui.theme.FrameBlueBackground

/**
 * This composable function creates a bottom section for a dashboard UI.
 *
 * @Composable indicates that this function can be used to build UI elements.
 */
@Composable
fun DashboardBottomFrame(){
    val config = LocalConfiguration.current
    Card (modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(FrameBlueBackground)
    ) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()){
                Image(painterResource(id = R.drawable.affiliate_marketing),
                    contentDescription = "",
                    modifier = Modifier.padding(start = 20.dp).size(150.dp),
                    alignment = Alignment.BottomCenter)
            Column(modifier = Modifier.padding(end = 20.dp, bottom = 20.dp, top = 30.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(15.dp)) {
                CustomText(text = "Invite your friends and\nearn exciting goodies!", fontSize =  if (config.screenWidthDp.dp < 400.dp) 3.em else 2.5.em,
                    color = Color.White,
                    lineHeight = 1.3.em)
                ReferAndEarnButton(modifier = Modifier.align(Alignment.End),
                    text = "Refer & Earn"){}
            }
        }
    }
}