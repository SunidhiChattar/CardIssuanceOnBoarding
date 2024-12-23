package com.isu.common.customcomposables

import android.content.pm.ResolveInfo
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.isu.common.ui.theme.appMainColor
import com.isu.common.utils.getUpiApps


@Composable
fun CustomCheckBoxField(
    text: String = "",
    expand: MutableState<Boolean> = remember {
        mutableStateOf(false)
    },
    onSelected: () -> Unit = { expand.value = !expand.value },
    expandedComposable: @Composable () -> Unit,
) {

    val context = LocalContext.current
    LaunchedEffect(expand.value) {

    }
    Column(modifier = Modifier
        .fillMaxWidth()

        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(5.dp))
        .padding(horizontal = 15.dp, vertical = 5.dp)
        .clickable(interactionSource = remember {
            MutableInteractionSource()
        }, indication = null) {
            onSelected()

        }) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomText(text = text)
            RadioButton(selected = expand.value, onClick = {
                onSelected()

            })


        }

        AnimatedVisibility(expand.value) {

            expandedComposable()

        }
    }
}

@Composable
fun UpiExpandable(
    onUpiAppClick: (ResolveInfo, onSuccess: () -> Unit) -> Unit,
) {
    val clickedApp: MutableState<ResolveInfo?> = remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current
    val rememberUpiPayResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            it.data?.extras.let { bundle ->
                bundle?.keySet()?.forEach {
                    if (bundle["Status"] == "SUCCESS") {

                    }
                    {

                    }
                    Log.d("PaymentResult", "UpiExpandable:${it} : ${bundle[it]}")
                }
            }
        }
    Column {
        CustomText(text = "Pay by any UPI", fontSize = 13.sp.noFontScale())
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(getUpiApps(context)) {
                val icon = remember { it.loadIcon(context.packageManager).toBitmap() }
                val intentAddress = it
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(icon)
                        .allowHardware(false)
                        .transformations(CircleCropTransformation())  // Apply circular crop transformation
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build()
                )
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(10.dp),
                    modifier = Modifier.padding(10.dp).then(
                        if (it == clickedApp.value) {
                            Modifier.border(5.dp, appMainColor, CircleShape).padding(5.dp)
                                .scale(0.9f)
                        } else {
                            Modifier
                        }

                    ).clickable {

                        onUpiAppClick(it) {
                            clickedApp.value = it
                        }
//                        payUsingSelectedUpiApp(context, it, "7008656872@idfcfirst", ammountToLoad, "Test") {
//                            rememberUpiPayResultLauncher.launch(it)
//                        }
                    }
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(
                                CircleShape
                            )
                    )
                }

            }
        }

    }
}