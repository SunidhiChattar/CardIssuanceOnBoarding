package com.isu.prepaidcard.presentation.screens.dashboard

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.noFontScale
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.errorColor
import com.isu.common.ui.theme.plaeholderColor
import com.isu.prepaidcard.data.response.ViewCardDataByRefIdResponse
import com.isu.prepaidcard.presentation.viewmodels.DashboardViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.roundToInt

@Composable
fun MccCategories(modifier: Modifier = Modifier, viewModel: DashboardViewModel) {


    val mutableHashSet = viewModel.mutableHashSet
    val scope = rememberCoroutineScope()
    val config = LocalConfiguration.current
    val listOfMCC: SnapshotStateList<String> = remember {
        mutableStateListOf()
    }
    Box {
        Scaffold(topBar = {
            CustomProfileTopBar(text = "MCC")
        }, containerColor = White) {
            Column(
                modifier = Modifier.fillMaxSize().padding(
                    top = it.calculateTopPadding(), start = 22.dp, end = 22.dp, bottom = 22.dp
                ), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.heightIn(config.screenHeightDp.dp - 200.dp)) {


                    val category = remember {
                        mutableStateOf("")
                    }
                    CustomInputField(

                        label = "Select a Category to Add",
                        placeholder = "Select a Category to Add",
                        state = category.value,
                        modifier = Modifier.onGloballyPositioned {

                        }) {
                        category.value = it
                    }
                    CustomText(
                        text = "Selected Category ", modifier = Modifier.padding(bottom = 10.dp)
                    )
                    LazyColumn {
                        items(viewModel.mccCategoryCodeMap.keys.toList().filter {
                            it.lowercase().trim()
                                .contains(category.value.lowercase().trim().toRegex())
                        }) {
                            CustomInputField(
                                enabled = false,
                                labelRequired = false,
                                state = it,
                                modifier = Modifier.clickable {
                                    viewModel.selectedMccCategory.value = it

                                    scope.launch {
                                        NavigationEvent.helper.navigateTo(CardManagement.MCCCategoryCodeScreen)
                                    }

                                },
                                color = TextFieldDefaults.colors(
                                    unfocusedIndicatorColor = LightGray,
                                    focusedIndicatorColor = DarkGray,
                                    disabledIndicatorColor = LightGray.copy(0.5f),
                                    focusedContainerColor = White,
                                    unfocusedContainerColor = White,
                                    disabledContainerColor = White,
                                    disabledTextColor = plaeholderColor,
                                    errorIndicatorColor = errorColor,
                                    errorContainerColor = White
                                ),
                                trailingIcon = {
                                    CustomText(
                                        text = "${
                                            viewModel.mccCategoryCodeMap.get(it)
                                                ?.filter { it?.applicable == true }?.size
                                        }/${viewModel.mccCategoryCodeMap.get(it)?.size}",
                                        fontSize = 13.sp.noFontScale(),
                                        fontFamily = "Lato",
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(end = 10.dp)

                                    )
                                })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))


            }
        }
    }
}

@Composable
fun MccCodeCategories(modifier: Modifier = Modifier, viewModel: DashboardViewModel) {
    val mutableHashSet: SnapshotStateList<ViewCardDataByRefIdResponse.Data.MccDetail?> =
        remember(viewModel.mcclist) {
            mutableStateListOf()
        }
    val scope = rememberCoroutineScope()
    Log.d("Mutableset", "MccCodeCategories: ${viewModel.mccCategoryCodeMap}")
    val dropDownItem = viewModel.mccCategoryCodeMap.get(key = viewModel.selectedMccCategory.value)
    val selectedCategoryCode: SnapshotStateList<ViewCardDataByRefIdResponse.Data.MccDetail?> =
        remember {
            mutableStateListOf()
        }
    val showDropDown = remember {
        mutableStateOf(false)
    }
    val enableCLicked = remember {
        mutableStateOf(false)
    }
    val disableCLicked = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    LaunchedEffect(Unit, viewModel.mccCategoryCodeMap, enableCLicked.value, disableCLicked.value) {
        Log.d("DropDown", "MccCodeCategories: ${dropDownItem}")
        mutableHashSet.clear()
        mutableHashSet.addAll(
            viewModel.mccCategoryCodeMap.get(key = viewModel.selectedMccCategory.value)
                ?: emptyList()
        )
        Log.d("Mutableset", "MccCodeCategories: ${mutableHashSet.toList()}")

    }


    val localConfig = LocalConfiguration.current
    val screenHeight = localConfig.screenHeightDp
    Box {
        Scaffold(topBar = {
            CustomProfileTopBar(text = viewModel.selectedMccCategory.value)
        }, containerColor = White) {
            Column(
                modifier = Modifier.heightIn(screenHeight.dp).padding(
                    top = it.calculateTopPadding(), start = 22.dp, end = 22.dp, bottom = 22.dp
                    )
            ) {
                Column(Modifier.weight(1f)) {
                    val category = remember {
                        mutableStateOf("")
                    }
                    CustomInputField(
                        placeholder = "Search Category Codes",

                        label = "Select a Category to Add", state = category.value,

                        modifier = Modifier.onGloballyPositioned {

                        }) {
                        category.value = it
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(text = "Selected Category Codes ", modifier = Modifier)
                        Box {
                            Icon(imageVector = Icons.Default.MoreVert,
                                contentDescription = "",
                                modifier = Modifier.clickable {
                                    showDropDown.value = !showDropDown.value
                                })
                            DropdownMenu(showDropDown.value, onDismissRequest = {
                                showDropDown.value = false
                            }, Modifier.background(White)) {
                                DropdownMenuItem(text = {
                                    CustomText(text = "Enable All")
                                }, modifier = Modifier.height(30.dp), onClick = {

                                    viewModel.mccCategoryCodeMap.get(viewModel.selectedMccCategory.value)
                                        ?.forEach {
                                            viewModel.mcclistRequest.put(
                                                it?.mccCode.toString(),
                                                true
                                            )
                                    }
                                    viewModel.changeMccStatus(enableAll = true, onSuccess = {
                                        viewModel.getCardDataByRefId {
                                            enableCLicked.value = true
                                            disableCLicked.value = false
                                            mutableHashSet.clear()
                                            mutableHashSet.addAll(it?.mccDetails ?: emptyList())
                                            scope.launch {
                                                NavigationEvent.helper.navigateBack()
                                            }
                                        }
                                    })
                                    showDropDown.value = false

                                })
                                HorizontalDivider(
                                    thickness = 1.dp, modifier = Modifier.padding(horizontal = 5.dp)
                                )
                                DropdownMenuItem(text = {

                                    CustomText(text = "Disable All")

                                }, modifier = Modifier.height(30.dp), onClick = {

                                    viewModel.mccCategoryCodeMap.get(viewModel.selectedMccCategory.value)
                                        ?.forEach {
                                            viewModel.mcclistRequest.put(
                                                it?.mccCode.toString(),
                                                false
                                            )
                                    }
                                    viewModel.changeMccStatus(diableAll = true, onSuccess = {
                                        viewModel.getCardDataByRefId {

                                            disableCLicked.value = true
                                            enableCLicked.value = false
                                            mutableHashSet.clear()
                                            mutableHashSet.addAll(
                                                viewModel.mccCategoryCodeMap.get(key = viewModel.selectedMccCategory.value)
                                                    ?: emptyList()
                                            )
                                            scope.launch {
                                                NavigationEvent.helper.navigateBack()
                                            }


                                        }
                                    })
                                    showDropDown.value = false
                                })


                            }
                        }

                    }

                    LazyColumn {
                        items(
                            mutableHashSet.toList()
                                .filter { it?.mccCategory?.matches(viewModel.selectedMccCategory.value.toRegex()) == true }
                                .filter {
                                    it?.mccName.toString().lowercase()
                                        .contains(category.value.lowercase()) == true
                                }) {
                            val mccCheck = remember(it?.applicable) {
                                mutableStateOf(it?.applicable)
                            }
                            CustomInputField(
                                modifier = Modifier,
                                enabled = false,
                                labelRequired = false,
                                state = viewModel.mccNameCodeMap.get(it?.mccCode.toString())
                                    .toString(),
                                color = TextFieldDefaults.colors(
                                    unfocusedIndicatorColor = LightGray,
                                    focusedIndicatorColor = DarkGray,
                                    disabledIndicatorColor = LightGray.copy(0.5f),
                                    focusedContainerColor = White,
                                    unfocusedContainerColor = White,
                                    disabledContainerColor = White,
                                    disabledTextColor = plaeholderColor,
                                    errorIndicatorColor = errorColor,
                                    errorContainerColor = White
                                ),
                                trailingIcon = {
                                    Checkbox(
                                        checked = if (mccCheck.value == null) it?.applicable == true
                                        else mccCheck.value == true,

                                        onCheckedChange = { bool ->
                                            mccCheck.value = bool
                                            if (bool != it?.applicable) {


                                                viewModel.mcclistRequest[it?.mccCode.toString()] =
                                                    bool
                                                Log.d(
                                                    "LIST NOT",
                                                    "MccCodeCategories: ${viewModel.mcclistRequest}"
                                                )

                                            } else {
                                                Log.d(
                                                    "LIST",
                                                    "MccCodeCategories: ${viewModel.mcclistRequest}"
                                                )

                                                viewModel.mcclistRequest.remove(it.mccCode.toString())
                                                Log.d(
                                                    "LIST",
                                                    "MccCodeCategories: ${viewModel.mcclistRequest}"
                                                )


                                            }


                                        })
                                })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomButton(
                            text = "Submit", onClick = {
                                viewModel.changeMccStatus(onSuccess = {
                                    viewModel.getCardDataByRefId {
                                        enableCLicked.value = false
                                        disableCLicked.value = false
                                        mutableHashSet.clear()
                                        mutableHashSet.addAll(it?.mccDetails ?: emptyList())
                                        scope.launch {
                                            NavigationEvent.helper.navigateBack()
                                        }
                                    }
                                }, onError = {})
                            }, modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        CustomButton(
                            onClick = {
                                scope.launch {
                                    NavigationEvent.helper.navigateBack()
                                }
                            }, modifier = Modifier.weight(1f).border(
                                    BorderStroke(1.dp, LightGray.copy(0.6f)),
                                    shape = RoundedCornerShape(5.dp)
                            ), innerComponent = {
                                CustomText(
                                    text = "Cancel", color = appMainColor
                                )
                            }, color = White
                        )


                    }
                }


            }
        }
    }
}


fun getLast100Years(): List<String> {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    return (currentYear - 200..currentYear).map { it.toString() }
}

@Composable
fun AnalyticsMeter(
    modifier: Modifier = Modifier, maxValue: Float = 100f, value: Float = 50f, type: String = "IMPS"
) {
    val startAnim = remember {
        mutableStateOf(false)
    }
    val spentValue = animateFloatAsState(
        targetValue = if (startAnim.value) (0.75f / maxValue) * value else 0f,
        animationSpec = tween(1000),
        label = ""
    )
    LaunchedEffect(value) {
        delay(1000)
        startAnim.value = true

    }
    Card(
        modifier = modifier.padding(horizontal = 2.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = modifier.padding(10.dp)) {
                CircularProgressIndicator(
                    progress = { 0.75f },
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier.rotate(225f).size(90.dp),
                    color = LightGray
                )
                CircularProgressIndicator(
                    progress = { spentValue.value },
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier.rotate(225f).size(95.dp),
                    strokeWidth = 10.dp,
                    color = appMainColor
                )
                CustomText(text = "₹${(spentValue.value * (100 / 0.75)).roundToInt()}")
            }
            CustomText(text = type)
        }

    }


}

