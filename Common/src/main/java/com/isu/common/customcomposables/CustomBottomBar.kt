package com.isu.common.customcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.isu.common.R
import com.isu.common.navigation.AuthenticationScreens
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.ui.theme.CardBlue
import com.isu.common.utils.IconFeature
import com.isu.common.utils.LocalNavController
import com.isu.common.utils.canProceed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun CustomBottomBar(onClick: (IconFeature) -> Unit ) {
    val context = LocalContext.current
    val lifeCycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val recompose = remember {
        mutableStateOf(false)
    }
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {

                    recompose.value = !recompose.value
                }

                else -> {}
            }
        }
        lifeCycleOwner.lifecycle.addObserver(
            observer
        )
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
    when (lifeCycleOwner.lifecycle.currentState) {

        Lifecycle.State.RESUMED -> {

        }

        else -> {}
    }

    val listOfBarItems = remember(recompose.value) {
        listOf(
        IconFeature(R.drawable.home, "Home",destination=ProfileScreen.DashBoardScreen),
        IconFeature(R.drawable.card, "Card",destination=ProfileScreen.CardManagementScreen),
        IconFeature(R.drawable.notification, "Notification",destination=ProfileScreen.NotificationScreen),
        IconFeature(R.drawable.avatarimage, "Avatar",destination=ProfileScreen.HomeScreen)
        )
    }
    val navHostController= LocalNavController.current

    val clickedIcon: MutableState<IconFeature?> = remember {

        mutableStateOf(null)
    }
    val navEvent= remember {
        mutableStateOf(false)
    }
    val scope= rememberCoroutineScope()


    LaunchedEffect(navHostController.canProceed) {
        listOfBarItems.forEach {
            if (navHostController.currentDestination?.route?.contains(
                    it.destination.toString().toRegex()
                ) == true
            ) {
                clickedIcon.value = it
            }
        }
        NavigationEvent.helper.navigationEvent.collectLatest {
            listOfBarItems.forEach {
                if(navHostController.currentDestination?.route?.contains(it.destination.toString().toRegex())==true ){
                    clickedIcon.value=it
                }
            }
        }
        scope.launch {
        }

    }






    BottomAppBar(containerColor = Color.White) {
        Card(
            shape = RoundedCornerShape(topEnd = 5.dp, topStart = 5.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOfBarItems.forEach {
                    val iconColor = remember {
                        mutableStateOf(Color.Gray)
                    }
                    IconButton(onClick = {
                        onClick(it)

                        if(clickedIcon.value?.destination!=it.destination){

                            scope.launch {

                                when(it.feature){
                                    "Avatar"->{
                                        NavigationEvent.helper.emit(NavigationEvent.NavigateToNextScreen(ProfileScreen.HomeScreen))
                                        navEvent.value=!navEvent.value
                                    }
                                    "Home"->{
                                        NavigationEvent.helper.emit(NavigationEvent.NavigateToNextScreen(ProfileScreen.DashBoardScreen))
                                        navEvent.value=!navEvent.value
                                    }
                                    "Notification"->{
                                        NavigationEvent.helper.navigateTo(ProfileScreen.NotificationScreen)
                                        navEvent.value=!navEvent.value
                                    }
                                    "Card"->{
                                        NavigationEvent.helper.navigateTo(ProfileScreen.CardManagementScreen)
                                        navEvent.value=!navEvent.value
                                    }
                                    else->{
                                        NavigationEvent.helper.navigateTo(AuthenticationScreens.PhoneVerificationScreen)
                                    }

                                }

                            }

                        }


                    }) {
                        if (it.icon == R.drawable.avatarimage) {
                            Image(
                                painterResource(it.icon),
                                "",
                                modifier = Modifier.border(
                                    2.dp,
                                    if (clickedIcon.value == it) Color.Black else Color.Transparent,
                                    shape = CircleShape
                                ).padding(3.dp).size(22.dp).aspectRatio(1f),
                                contentScale = ContentScale.FillBounds
                            )
                        } else {
                            Icon(
                                painterResource(it.icon),
                                "",
                                modifier = Modifier.size(22.dp).aspectRatio(1f),
                                tint = if (clickedIcon.value == it) CardBlue else iconColor.value
                            )
                        }
                    }
                }
            }
        }


    }

}