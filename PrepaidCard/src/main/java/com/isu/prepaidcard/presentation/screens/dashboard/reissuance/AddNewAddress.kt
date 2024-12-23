package com.isu.prepaidcard.presentation.screens.dashboard.reissuance

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomCancelButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.ui.theme.CardBlue
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.LocalNavController
import kotlinx.coroutines.launch

@Composable
fun AddNewAddress(viewModel: CardReIssuanceViewModel) {
    val homeSelected = viewModel.homeSelected
    val workSelected = viewModel.workSelected
    val othersSelected = viewModel.othersSelected

    val addressLine1 = viewModel.addressLine1
    val addressLine2 = viewModel.addressLine2
    val pinCode = viewModel.pinCode
    val state = viewModel.state
    val city = viewModel.city
    val addressLine1Error = viewModel.addressLine1Error
    val addressLine2Error = viewModel.addressLine2Error
    val pinCodeError = viewModel.pinCodeError
    val stateError = viewModel.stateError
    val cityError = viewModel.cityError
    val addressLine1ErrorMessage = viewModel.addressLine1ErrorMessage
    val addressLine2ErrorMessage = viewModel.addressLine2ErrorMessage
    val pinCodeErrorMessage = viewModel.pinCodeErrorMessage
    val stateErrorMessage = viewModel.stateErrorMessage
    val cityErrorMessage = viewModel.cityErrorMessage
    val config = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        addressLine1.value = ""
        addressLine2.value = ""
        state.value = ""
        pinCode.value = ""
        city.value = ""

    }
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Add New Address"
            )
        },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding())
                .heightIn(config.screenHeightDp.dp).verticalScroll(
                    rememberScrollState()
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(vertical = 10.dp,
                horizontal = 20.dp)) {
                CustomText(modifier = Modifier.padding(vertical = 10.dp), text = "Address Line 1",
                    )
                CustomInputField(placeholder = "Enter Address Line 1",
                    state = addressLine1.value,
                    labelRequired = false,
                    onValueChange = {
                        addressLine1.value = it
                        addressLine1Error.value = false
                        addressLine1ErrorMessage.value = ""
                    },
                    isError = addressLine1Error.value,
                    errorMessage = addressLine1ErrorMessage.value
                )
                CustomText(modifier = Modifier.padding(vertical = 10.dp),text = "Address Line 2",
                    )
                CustomInputField(placeholder = "Enter Address Line 2",labelRequired = false,
                    onValueChange = {
                        addressLine2.value = it
                        addressLine2Error.value = false
                        addressLine1ErrorMessage.value = ""
                    },
                    state = addressLine2.value,
                    isError = addressLine2Error.value,
                    errorMessage = addressLine2ErrorMessage.value
                )
                CustomText(modifier = Modifier.padding(vertical = 10.dp),text = "Enter PIN Code",
                    )
                CustomInputField(placeholder = "Enter PIN Code",labelRequired = false,
                    onValueChange = {
                        if (it.isDigitsOnly() && it.length <= 6) {
                            pinCode.value = it
                            pinCodeError.value = false
                            pinCodeErrorMessage.value = ""
                            if (it.length == 6) {
                                viewModel.fetchPinCodeData(it) {
                                    state.value = it?.data?.data?.state ?: ""
                                    city.value = it?.data?.data?.city ?: ""
                                }
                            }
                        }

                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    state = pinCode.value,
                    isError = pinCodeError.value,
                    errorMessage = pinCodeErrorMessage.value
                    )
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                    Column(modifier = Modifier.weight(1f)){
                        CustomText(modifier = Modifier.padding(vertical = 10.dp),text = "State",
                            )
                        CustomInputField(
                            placeholder = "Enter your State",
                            labelRequired = false,
                            onValueChange = {
                                state.value = it
                                stateError.value = false
                                stateErrorMessage.value = ""
                            },
                            isError = stateError.value,
                            errorMessage = stateErrorMessage.value,
                            state = state.value)
                    }
                    Column(modifier = Modifier.weight(1f)){
                        CustomText(
                            modifier = Modifier.padding(vertical = 10.dp), text = "City/Town"
                            )
                        CustomInputField(
                            placeholder = "Enter City/Town",
                            labelRequired = false,
                            state = city.value,
                            errorMessage = cityErrorMessage.value,
                            isError = cityError.value
                        ) {
                            city.value = it
                            cityError.value = false
                            cityErrorMessage.value = ""
                        }
                    }
                }
                CustomText(modifier = Modifier.padding(vertical = 10.dp),text = "Address Type",
                    )

                Row { Row (modifier = Modifier.padding(horizontal = 0.dp)
                    .wrapContentHeight().wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    RadioButton(
                        selected = homeSelected.value,
                        onClick = {
                            homeSelected.value = true
                            workSelected.value = false
                            othersSelected.value = false
                        },
                        colors = RadioButtonColors(
                            selectedColor = appMainColor,
                            unselectedColor = Color.LightGray,
                            disabledSelectedColor = Color.Gray,
                            disabledUnselectedColor = Color.Gray
                        )
                    )
                    CustomText(text = "Home", color = authTextColor,
                        )
                }
                    Row (modifier = Modifier.padding(horizontal = 0.dp)
                        .wrapContentHeight().wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically){
                        RadioButton(
                            selected = workSelected.value,
                            onClick = {
                                homeSelected.value = false
                                workSelected.value = true
                                othersSelected.value = false
                            },
                            colors = RadioButtonColors(
                                selectedColor = appMainColor,
                                unselectedColor = Color.LightGray,
                                disabledSelectedColor = Color.Gray,
                                disabledUnselectedColor = Color.Gray
                            )
                        )
                        CustomText(text = "Work", color = authTextColor,
                            )
                    }
                    Row (modifier = Modifier.padding(horizontal = 0.dp)
                        .wrapContentHeight().wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically){
                        RadioButton(
                            selected = othersSelected.value,
                            onClick = {
                                homeSelected.value = false
                                workSelected.value = false
                                othersSelected.value = true
                            },
                            colors = RadioButtonColors(
                                selectedColor = appMainColor,
                                unselectedColor = Color.LightGray,
                                disabledSelectedColor = Color.Gray,
                                disabledUnselectedColor = Color.Gray
                            )
                        )
                        CustomText(text = "Others", color = authTextColor)
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                val navController = LocalNavController.current
                val context = LocalContext.current
                CustomButton(color = CardBlue, text = "Add",
                    modifier = Modifier.weight(
                        1f
                    ),
                    onClick = {
                        Toast.makeText(
                            context,
                            navController.previousBackStackEntry?.destination?.route,
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.addAddress {
                            if (navController.previousBackStackEntry?.destination?.route?.contains("OrderPhyical".toRegex()) == true) {
                                scope.launch {
                                    NavigationEvent.helper.navigateTo(
                                        CardManagement.OrderPhyicalCardShippingAddressScreen,
                                        CardManagement.OrderPhyicalCardDetailsScreen
                                    )
                                }
                            } else {
                                scope.launch {
                                    NavigationEvent.helper.navigateTo(
                                        CardManagement.CardReissuance,
                                        ProfileScreen.CardManagementScreen
                                    )
                                }
                            }
                        }
                    })
                CustomCancelButton(text = "Cancel",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            NavigationEvent.helper.emit(NavigationEvent.NavigateBack)
                        }
                    })
            }
    }
}
}