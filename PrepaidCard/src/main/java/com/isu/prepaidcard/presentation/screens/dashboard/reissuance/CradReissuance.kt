package com.isu.prepaidcard.presentation.screens.dashboard.reissuance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.ShippingAddressItem
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.FetchAllCards
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.authTextColor
import kotlinx.coroutines.launch

@Composable
fun AddressSelectionScreen(
    listOfAddress: SnapshotStateList<ShippingAddressItem?>,
    onAddAddress: () -> Unit,
    onDeleteAddress: (ShippingAddressItem) -> Unit,
    onUpdateAddress: (ShippingAddressItem) -> Unit,
    onSelect: (ShippingAddressItem) -> Unit,
) {
    var selectedAddress: MutableState<ShippingAddressItem?> = remember {
        mutableStateOf(null)
    }

    Column(modifier = Modifier.padding(0.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        listOfAddress.forEach {
            if (it != null) {
                AddressOption(
                    address = "${it.address1}, ${it.address2}, \n ${it.city}, ${it.state},\n ${it.pinCode}",
                    isSelected = selectedAddress.value == it,
                    onSelect = {
                        selectedAddress.value = it
                        onSelect(it)
                    },
                    onEdit = {
                        onUpdateAddress(it)
                    },
                    onDeleteClick = {
                        onDeleteAddress(it)
                    }

                )
            }

        }


        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "+ Add New Address",
            modifier = Modifier.clickable {
                onAddAddress()
            }
        )
    }
}

@Composable
fun AddressOption(
    address: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDeleteClick: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    val showDropDown = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) Color.LightGray else appMainColor.copy(0.1f),
                shape = RoundedCornerShape(7.dp)
            )
            .heightIn(70.dp)
            .clickable(onClick = onSelect)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onSelect() },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                checkmarkColor = Color.Black,
                uncheckedColor = Color.White
            ),
            modifier = Modifier.border(
                BorderStroke(1.dp, authTextColor),
                shape = RoundedCornerShape(4.dp)
            ).size(20.dp)

        )

        Spacer(modifier = Modifier.width(12.dp))

        CustomText(
            text = address,
            fontSize = 15.sp,
            modifier = Modifier

                .weight(1f)
                .padding(vertical = 15.dp)
        )
        Row(
            modifier = Modifier.padding(end = 0.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        showDropDown.value = true
                    },
                    tint = authTextColor
                )
                DropdownMenu(showDropDown.value, onDismissRequest = {
                    showDropDown.value = false
                }, Modifier.background(Color.White))
                {
                    DropdownMenuItem(text = {
                        CustomText(text = "Edit")
                    }, modifier = Modifier.height(30.dp), onClick = {
                        onEdit()
                        showDropDown.value = false

                    })
                    HorizontalDivider(
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                    DropdownMenuItem(text = {
                        CustomText(text = "Remove")

                    }, modifier = Modifier.height(30.dp), onClick = {
                        onDeleteClick()
                        showDropDown.value = false
                    })


                }
            }

        }
    }
}

@Composable
fun MCCOption(address: String, isSelected: Boolean, onSelect: (Boolean) -> Unit) {
    val checked = remember {
        mutableStateOf(isSelected)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) Color.LightGray else appMainColor.copy(0.1f),
                shape = RoundedCornerShape(7.dp)
            )
            .heightIn(50.dp)
            .clickable(onClick = { onSelect(isSelected) })
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {


        CustomText(
            text = address,
            fontSize = 14.sp.noFontScale(),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 5.dp)
        )
        Checkbox(
            checked = checked.value,
            onCheckedChange = {
                onSelect(it)
                checked.value = it
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                checkmarkColor = Color.Black,
                uncheckedColor = Color.White
            ),
            modifier = Modifier.border(
                BorderStroke(1.dp, authTextColor),
                shape = RoundedCornerShape(4.dp)
            ).size(20.dp)

        )
    }
}

@Composable
fun CardReissuance(
    modifier: Modifier = Modifier,
    cardReissuanceViewModel: CardReIssuanceViewModel,
) {

    val cardHolderName = cardReissuanceViewModel.cardHolderName
    val scope = rememberCoroutineScope()
    val cardHolderNameError = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        cardReissuanceViewModel.fetchAddress()
    }
    val cardHolderNameErrorMessage = remember {
        mutableStateOf("")
    }
    val cardNumber = cardReissuanceViewModel.cardNumber.value
    val cardNumberError = remember {
        mutableStateOf(false)
    }
    val cardNumberErrorMessage = remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        cardReissuanceViewModel.getSavedData()
    }

    val cardType = "GPR"
    val config = LocalConfiguration.current
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Reissuance"
            )
        },
        containerColor = Color.White
    ) {
        it
        val screenHeight = LocalConfiguration.current.screenHeightDp
        KeyBoardAwareScreen(
            shouldScroll = false,
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = 22.dp,
                bottom = 0.dp,
                end = 22.dp
            )
        ) {
            Column(
                modifier = Modifier.heightIn(config.screenHeightDp.dp - (it.calculateTopPadding() + 90.dp)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    CustomInputField(
                        enabled = false,
                        state = "XXXX XXXX XXXX ${
                            cardNumber.let {
                                if (it.length > 4) {
                                    it.takeLast(4)
                                } else {
                                    ""
                                }
                            }
                        }",
                        label = "Card Number"
                    )

                    CustomInputField(
                        enabled = false,
                        state = cardType,
                        placeholder = "Mobile Number",
                        label = "Card Type",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text(text = buildAnnotatedString {
                        append("Select Address")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append("*")
                        }
                    })
                    Spacer(Modifier.height(10.dp))
                    AddressSelectionScreen(
                        listOfAddress = cardReissuanceViewModel.addressList,
                        onAddAddress = {
                            scope.launch {
                                NavigationEvent.helper.navigateTo(CardManagement.AddAddress)
                            }
                        }, onDeleteAddress = {
                            cardReissuanceViewModel.deleteAddress(it)
                        }, onUpdateAddress = {

                            cardReissuanceViewModel.moveToEditAddress(it)
                        }) {
                        cardReissuanceViewModel.selectedAddress(it)
                    }

                }

                Column(modifier = Modifier.heightIn(20.dp)) {}
                Column {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        CustomButton(
                            text = "Continue",
                            color = appMainColor,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                cardReissuanceViewModel.reIssueCard(onSuccess = {
                                scope.launch {
                                    NavigationEvent.helper.navigateTo(CardManagement.LinkCardOtpScreen)
                                }
                            })

                        })
                        CustomButton(
                            innerComponent = {
                                CustomText(text = "Cancel")
                            }, modifier = Modifier.weight(1f).border(
                                1.dp,
                                Color.LightGray, shape = RoundedCornerShape(5.dp)
                            ), color = Color.White, onClick = {
                            scope.launch {

                                NavigationEvent.helper.navigateTo(
                                    ProfileScreen.CardManagementScreen,
                                    ProfileScreen.HomeScreen
                                )
                                FetchAllCards.fetch()
                                }


                        })
                    }

                }
            }


        }
    }
}