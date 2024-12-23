package com.isu.common.customcomposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.isu.common.R
import com.isu.common.ui.theme.authTextColor

@Composable
fun StatementComponent(
    barText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.border(
            BorderStroke(1.dp, authTextColor.copy(0.5f)),
            RoundedCornerShape(50.dp)
        ).clip(
            RoundedCornerShape(50.dp)
        )
    ) {
        Row(
            modifier = Modifier.wrapContentWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CustomText(
                modifier = Modifier, text = barText,
                color = authTextColor
            )
            Icon(
                painter = painterResource(R.drawable.arrow_down),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onClick.invoke() },
                tint = authTextColor
            )
        }
    }
}

data class DetailStatement(
    val id: String = ""
)

@Composable
fun DetailStatementComponent(
    detailStatement: DetailStatement,
    showRadio: Boolean,
    barText: String,
    onClick: () -> Unit,
    onRadioCick: (Boolean) -> Unit
) {
    val selected = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.border(
            BorderStroke(1.dp, authTextColor.copy(0.5f)),
            RoundedCornerShape(50.dp)
        ).clip(
            RoundedCornerShape(50.dp)
        )
    ) {
        Row(
            modifier = Modifier.wrapContentWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            RadioButton(selected = selected.value, onClick = {
                selected.value = !selected.value
                onRadioCick(selected.value)

            })
            CustomText(
                modifier = Modifier, text = barText,
                color = authTextColor
            )
            Icon(
                painter = painterResource(R.drawable.arrow_down),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onClick.invoke() },
                tint = authTextColor
            )
        }
    }
}

@Composable
fun StatementComponentWithDropDown(
    barText: String,
    onClick: () -> Unit,
    dropDownList: List<String> = listOf(
        "Jan",
        "Feb ",
        "Mar",
        "Apr",
        "May",
        "June",
        "July",
        "Aug",
        "Sept",
        "Oct",
        "Nov",
        "Dec",
    )
) {
    val textState = remember {
        mutableStateOf(barText)
    }
    val showDropDown = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.border(
            BorderStroke(1.dp, authTextColor.copy(0.5f)),
            RoundedCornerShape(5.dp)
        ).clip(
            RoundedCornerShape(50.dp)
        ).heightIn(0.dp, 400.dp)
    ) {
        Row(
            modifier = Modifier.wrapContentWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CustomText(
                modifier = Modifier, text = textState.value,
                color = authTextColor
            )
            Icon(
                painter = painterResource(R.drawable.arrow_down),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onClick.invoke()
                        showDropDown.value = true
                    },
                tint = authTextColor
            )
        }
        if (showDropDown.value) {
            DropdownMenu(expanded = showDropDown.value, onDismissRequest = {
                showDropDown.value = false
            }, modifier = Modifier.background(Color.White)) {
                Column(
                    Modifier.background(Color.White).padding(10.dp).heightIn(0.dp, 200.dp)
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {
                    dropDownList.forEach {
                        DropdownMenuItem(
                            text = {
                                CustomText(text = it)
                            }, onClick = {
                                textState.value = it
                                showDropDown.value = false
                            },
                            modifier = Modifier.wrapContentWidth()
                        )
                    }
                }
            }
        }

    }
}