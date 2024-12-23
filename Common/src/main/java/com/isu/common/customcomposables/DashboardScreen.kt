package  com.isu.common.customcomposables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.utils.IconFeature

import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.navigation.ProfileScreen
import com.isu.common.ui.theme.authTextColor


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.layout.ContentScale
import com.isu.common.R
import com.isu.common.customcomposables.CustomTopBar
import com.isu.common.events.LogOutEvent
import kotlinx.coroutines.launch


@Composable
fun NotificationScreen(modifier: Modifier = Modifier) {
    val scope= rememberCoroutineScope()
    Scaffold(bottomBar = {
        // Bottom navigation bar
//        CustomBottomBar(){
//            // Handle bottom bar item click
//        }
    }, topBar = {
        CustomTopBar(text="Notification", trailingIcon = { Icon(painter = painterResource(R.drawable.notification_settings),"") })
    },containerColor = Color.White) { padding ->
        padding
        BackHandler {
            scope.launch {
                LogOutEvent.logOut()
            }
        }

        KeyBoardAwareScreen(Modifier.padding(padding), shouldScroll = false) {
            Image(painter = painterResource(com.isu.common.R.drawable.img),"",Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        }
    }
}
@Composable
fun CardManagementScreen(modifier: Modifier = Modifier) {
    val scope= rememberCoroutineScope()
    Scaffold(bottomBar = {
        // Bottom navigation bar
//        CustomBottomBar(){
//            // Handle bottom bar item click
//        }
    }, topBar = {
        CustomTopBar(text="Card Management")
    },containerColor = Color.White) { padding ->
        padding
        BackHandler {
            scope.launch {
                LogOutEvent.logOut()
            }
        }

        KeyBoardAwareScreen(Modifier.padding(padding), shouldScroll = false) {
            Image(painter = painterResource(com.isu.common.R.drawable.img_2),"",Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Image(painter = painterResource(com.isu.common.R.drawable.img_3),"",Modifier.fillMaxSize(), contentScale = ContentScale.Crop)

        }
    }
}

@Preview
@Composable
fun DashboardScreen() {
    val scope= rememberCoroutineScope()
    val listOfBarItems = listOf(
        IconFeature(R.drawable.home, "Home", ProfileScreen.DashBoardScreen),
        IconFeature( R.drawable.card, "Card", ProfileScreen.DashBoardScreen),
        IconFeature( R.drawable.notification, "Notification", ProfileScreen.DashBoardScreen),
        IconFeature( R.drawable.avatarimage, "Avatar", ProfileScreen.DashBoardScreen)
    )

    Scaffold(bottomBar = {
        // Bottom navigation bar
        /*CustomBottomBar(){
            // Handle bottom bar item click
        }*/
    }, containerColor = Color.White,
        topBar = {
            CustomTopBar(text="Dashboard")
        }) { padding ->
        padding

        BackHandler {
            scope.launch {
                LogOutEvent.logOut()
            }
        }

        KeyBoardAwareScreen {
            Spacer(modifier = Modifier.height(50.dp))
            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource( R.drawable.emptycard), "")
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(0.7f).padding(5.dp), verticalArrangement = Arrangement.spacedBy(25.dp)) {
                    CustomText(text = "No Card Found", fontSize = 16.sp.noFontScale(), textAlign = TextAlign.Center)
                    CustomText(text = "Link your card or request for another Card.", fontSize = 14.sp.noFontScale(), textAlign = TextAlign.Center)
                }
                Row(
                    modifier = Modifier.wrapContentSize().padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = {},
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                        border = BorderStroke(
                            1.dp,
                            authTextColor
                        ),
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(5.dp)) {
                        Row {
                            Icon(painterResource(com.isu.common.R.drawable.link), "")
                            CustomText(text = "Link Card")
                        }
                    }
                    Button(onClick = {},
                        colors = ButtonDefaults.buttonColors(Color(0xff1D242D)),
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(5.dp)) {
                        Row {
                            Icon(Icons.Default.Add, "", tint = Color.White)
                            CustomText(text = "Request Card", color = Color.White, fontSize = 14.sp.noFontScale())
                        }
                    }
                }

            }

        }


    }
}