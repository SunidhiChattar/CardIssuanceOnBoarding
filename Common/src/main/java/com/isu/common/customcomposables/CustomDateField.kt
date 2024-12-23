package com.isu.common.customcomposables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.R
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.errorColor
import com.isu.common.ui.theme.plaeholderColor
import com.isu.common.utils.FontProvider.LATO_FONT
import com.isu.common.utils.FontProvider.getFont
import com.isu.common.utils.ZonedDateFormatter
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateField(
    pattern: String = "dd-MM-yyyy",
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    labelRequired: Boolean = true,
    labelColor: Color = authTextColor,
    labelComponent: (@Composable () -> Unit)? = null,
    label: String = "label",
    shape: Shape = TextFieldDefaults.shape,
    annotatedLabel: AnnotatedString? = null,
    placeholderComposable: (@Composable () -> Unit)? = null,
    placeholder: String = "placeholder",
    isError: Boolean = false,
    color: TextFieldColors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = LightGray,
        focusedIndicatorColor = Color.DarkGray,
        disabledIndicatorColor = if (isError) errorColor else LightGray,
        focusedContainerColor = White,
        unfocusedContainerColor = White,
        disabledContainerColor = White,
        disabledTextColor = Black,
        errorIndicatorColor = errorColor,
        errorContainerColor = White,

        ),
    textStyle: TextStyle = TextStyle(
        fontSize = 13.sp.noFontScale(),
        fontFamily = getFont("Lato", weight = FontWeight(400)),
        color = Black,
        textAlign = TextAlign.Start
    ),
    state: String = "",

    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorMessage: String = "",
    focusRequester: FocusRequester? = null,
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChange: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    val showDate = remember {
        mutableStateOf(false)
    }
    val dateState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()

        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun isSelectableYear(year: Int): Boolean {
            return year <= LocalDate.now().year

        }

    })
    if (showDate.value) {
        DatePickerDialog(
            onDismissRequest = { showDate.value = false },
            modifier = Modifier.padding(22.dp).scale(0.9f), colors = DatePickerDefaults.colors(
                containerColor = White,
                dayContentColor = appMainColor,
                selectedDayContainerColor = appMainColor,
                selectedDayContentColor = White,

                titleContentColor = appMainColor,
                headlineContentColor = appMainColor,
                weekdayContentColor = errorColor,
                subheadContentColor = appMainColor,
                todayDateBorderColor = appMainColor,
                todayContentColor = appMainColor,
                yearContentColor = appMainColor,


                dividerColor = appMainColor,
                dateTextFieldColors = null,

                ), confirmButton = {
                Button(onClick = {
                    showDate.value = false
                    onValueChange(
                        ZonedDateFormatter.formatMillisecondsToDate(
                            dateState.selectedDateMillis ?: 0L, pattern
                        )
                    )
                    focusManager.clearFocus()
                }, colors = ButtonDefaults.buttonColors(containerColor = appMainColor)) {
                    Text("OK")
                }
            }) {
            DatePicker(
                dateState,
                modifier = Modifier.padding(0.dp),
                colors = DatePickerDefaults.colors(
                    containerColor = White,
                    dayContentColor = appMainColor,
                    selectedDayContainerColor = appMainColor,
                    selectedDayContentColor = White,
                    selectedYearContentColor = White,
                    todayDateBorderColor = appMainColor,
                    todayContentColor = appMainColor,
                    yearContentColor = appMainColor,
                    selectedYearContainerColor = appMainColor,
                    dividerColor = appMainColor,


                    ),
                title = {
                    CustomText(text = "")
                },
                headline = {
                    CustomText(
                        text = "Select Date Of Birth",
                        color = appMainColor,
                        fontSize = 19.sp.noFontScale(),
                        modifier = Modifier.padding(start = 10.dp),
                        fontWeight = FontWeight(600),
                    )
                }
            )
        }

    }
    Column(modifier = modifier.fillMaxWidth()) {
        if (labelRequired) {
            if (labelComponent != null) {

                labelComponent()


            } else {
                if (annotatedLabel != null) {
                    CustomText(
                        text = annotatedLabel,
                        fontFamily = LATO_FONT,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.height(25.dp),
                        fontSize = 14.sp.noFontScale(),
                        color = labelColor
                    )
                } else {
                    CustomText(
                        text = label,
                        fontFamily = LATO_FONT,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.height(25.dp),
                        fontSize = 14.sp.noFontScale(),
                        color = labelColor
                    )
                }
            }
        }
        Box {
            OutlinedTextField(
                value = state,
                onValueChange = { state == it},
                placeholder = {
                    if (placeholderComposable == null) {

                        CustomText(
                            text = placeholder,
                            color = if (isError) errorColor else plaeholderColor,
                            fontSize = 13.sp.noFontScale(),
                            modifier = Modifier,
                            textAlign = TextAlign.Start
                        )

                    } else {
                        placeholderComposable.invoke()
                    }
                },
                enabled = enabled,
                modifier = modifier.fillMaxWidth().padding(0.dp).apply {
                    if (focusRequester != null) {
                        focusRequester(focusRequester)
                    }
                }.clickable {
                    showDate.value = true
                },
                shape = shape,
                isError = isError,
                supportingText = {
                    if (isError) {
                        Row(
                            modifier = Modifier.graphicsLayer {
                                translationX = -30f
                            }.padding(vertical = 5.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_circ_error),
                                "",
                                tint = errorColor
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            CustomText(
                                text = errorMessage,
                                modifier = Modifier,
                                textAlign = TextAlign.Start,
                                color = errorColor,
                                fontSize = 13.sp.noFontScale()
                            )
                        }

                    }
                },
                colors = color,
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                trailingIcon = {
                    Icon(Icons.Outlined.DateRange, "")
                },
                textStyle = textStyle, keyboardOptions = keyboardOptions
            )
        }
    }
}