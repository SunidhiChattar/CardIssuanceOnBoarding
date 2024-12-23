package com.isu.common.customcomposables

import android.content.IntentFilter
import android.provider.Telephony
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.R
import com.isu.common.ui.theme.appMainColor
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.errorColor
import com.isu.common.ui.theme.plaeholderColor
import com.isu.common.ui.theme.ticketTextDarkColor
import com.isu.common.utils.FontProvider.LATO_FONT
import com.isu.common.utils.FontProvider.getFont
import com.isu.common.utils.SmsReceiver


@Composable
fun CustomInputField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    labelRequired: Boolean = true,
    labelColor: Color = authTextColor,
    labelComponent: (@Composable () -> Unit)? = null,
    label: String = "label",
    supportingText: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(5.dp),
    annotatedLabel: AnnotatedString? = null,
    placeholderComposable: (@Composable () -> Unit)? = null,
    placeholder: String = "placeholder",
    isError: Boolean = false,
    color: TextFieldColors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = LightGray,
        focusedIndicatorColor = DarkGray,
        disabledIndicatorColor = if (isError) errorColor else LightGray.copy(0.5f),
        focusedContainerColor = White,
        unfocusedContainerColor = White,
        disabledContainerColor = Color(0xffF5F7F9),
        disabledTextColor = plaeholderColor,
        errorIndicatorColor = errorColor,
        errorContainerColor = White
    ),
    textStyle: TextStyle = TextStyle(
        fontSize = 13.sp.noFontScale(),
        fontFamily = getFont("Lato", weight = FontWeight(550)),
        color = authTextColor,
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
            if (leadingIcon != null) {
                OutlinedTextField(
                    value = state,
                    leadingIcon = {
                        leadingIcon()
                    },
                    onValueChange = { onValueChange(it) },
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

                        } else {
                            supportingText?.invoke()
                        }
                    },
                    colors = color,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    trailingIcon = trailingIcon,
                    textStyle = textStyle,
                    keyboardOptions = keyboardOptions
                )
            } else {
                OutlinedTextField(
                    value = state,

                    onValueChange = { onValueChange(it) },
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

                        } else {
                            supportingText?.invoke()
                        }
                    },
                    colors = color,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    trailingIcon = trailingIcon,
                    textStyle = textStyle,
                    keyboardOptions = keyboardOptions
                )
            }

        }
    }
}

@Composable
fun CustomInputFieldWithDropDown(
    modifier: Modifier = Modifier,
    onDropDownClick: (String) -> Unit = {},
    dropDownItem: List<String> = listOf("Item1", "Item2", "Item3", "Item4"),
    enabled: Boolean = true,
    labelRequired: Boolean = true,
    labelColor: Color = authTextColor,
    labelComponent: (@Composable () -> Unit)? = null,
    label: String = "label",
    supportingText: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(5.dp),
    annotatedLabel: AnnotatedString? = null,
    placeholderComposable: (@Composable () -> Unit)? = null,
    placeholder: String = "Search MCC category",
    isError: Boolean = false,
    color: TextFieldColors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = LightGray,
        focusedIndicatorColor = DarkGray,
        disabledIndicatorColor = if (isError) errorColor else LightGray.copy(0.5f),
        focusedContainerColor = White,
        unfocusedContainerColor = White,
        disabledContainerColor = Color(0xffF5F7F9),
        disabledTextColor = plaeholderColor,
        errorIndicatorColor = errorColor,
        errorContainerColor = White
    ),
    textStyle: TextStyle = TextStyle(
        fontSize = 13.sp.noFontScale(),
        fontFamily = getFont("Lato", weight = FontWeight(550)),
        color = authTextColor,
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
    val mccCategory = remember {
        mutableStateOf("")
    }
    val filterList = remember {
        mutableStateListOf<String>()
    }
    val focusManager = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        filterList.addAll(dropDownItem)
    }
    val showDropDown = remember {
        mutableStateOf(false)
    }
    val focusManagerLocal = LocalFocusManager.current
    LaunchedEffect(key1 = mccCategory.value, key2 = showDropDown.value) {


            filterList.clear()
        filterList.addAll(dropDownItem.filter { mccCategory.value.contains(it.toRegex()) })



    }
    Column(modifier = Modifier.fillMaxWidth()) {
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
            Column {
                OutlinedTextField(
                    value = mccCategory.value,
                    onValueChange = {
                        onValueChange(it)
                        mccCategory.value = it
                    },
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
                    modifier = modifier.fillMaxWidth().padding(0.dp).focusRequester(focusManager)
                        .onFocusChanged {
                            showDropDown.value = it.isFocused || it.hasFocus

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

                        } else {
                            supportingText?.invoke()
                        }
                    },
                    colors = color,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    trailingIcon = trailingIcon,
                    textStyle = textStyle,
                    keyboardOptions = keyboardOptions
                )

            }

        }


        if (showDropDown.value) {
            androidx.compose.animation.AnimatedVisibility(
                visible = showDropDown.value,
                enter = slideInVertically(),
                exit = fadeOut() + slideOutVertically(),

                ) {
                Column(
                    modifier = Modifier.heightIn(40.dp, 400.dp).fillMaxWidth().background(
                        White, RoundedCornerShape(5.dp)
                    )
                ) {
                    Column(
                        Modifier.heightIn(0.dp, 400.dp).border(
                            width = 1.dp,
                            shape = RoundedCornerShape(5.dp),
                            color = LightGray.copy(0.5f)
                        )
                    ) {
                        LazyColumn {
                            if (dropDownItem.filter {
                                    it.lowercase().contains(mccCategory.value.lowercase().toRegex())
                                }.isEmpty()) {
                                item {
                                    DropdownMenuItem(text = {
                                        CustomText(text = "No match found")
                                    }, onClick = {

                                    })
                                }
                            }
                            items(dropDownItem.filter {
                                it.lowercase().contains(mccCategory.value.lowercase().toRegex())
                            }) {
                                DropdownMenuItem(text = {
                                    CustomText(text = it)
                                }, onClick = {
                                    onDropDownClick(it)
                                    showDropDown.value = false
                                    mccCategory.value = ""
                                    focusManager.freeFocus()
                                    focusManagerLocal.clearFocus()
                                })
                            }
                        }


                    }
                }

            }
        }


    }
}

@Composable
fun CustomAttachmentInputField(
    modifier: Modifier = Modifier,
    labelRequired: Boolean = true,
    labelColor: Color = authTextColor,
    label: AnnotatedString = AnnotatedString(""),
    placeholder: String = "placeholder",
    isError: Boolean = false,
    color: TextFieldColors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = LightGray,
        focusedIndicatorColor = DarkGray,
        focusedContainerColor = White,
        unfocusedContainerColor = White,
        errorIndicatorColor = errorColor,
        errorContainerColor = LightGray.copy(0.5f),
        disabledContainerColor = LightGray.copy(0.2f),
        disabledIndicatorColor = if (isError) errorColor else LightGray,

        ),

    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorMessage: String = "jhkkjffkklfkjl",
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth().clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) { onClick() }) {
        if (labelRequired) {
            CustomText(
                text = label,
                fontFamily = LATO_FONT,
                fontWeight = FontWeight(500),
                modifier = Modifier.height(25.dp),
                fontSize = 14.sp.noFontScale(),
                color = labelColor
            )
        }
        Box {
            OutlinedTextField(
                value = "",
                onValueChange = { },
                enabled = false,

                placeholder = {
                    Row(
                        modifier = Modifier.fillMaxHeight().padding(start = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(
                            text = placeholder,
                            color = plaeholderColor,
                            fontSize = 13.sp.noFontScale()
                        )
                    }
                },
                modifier = modifier.heightIn(65.dp).fillMaxWidth().padding(0.dp),
                isError = isError,
                supportingText = {
                    if (isError) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.graphicsLayer {
                                translationX = -30f
                            }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_circ_error),
                                "",
                                tint = errorColor
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            CustomText(
                                text = errorMessage,
                                color = errorColor,
                                fontSize = 13.sp.noFontScale()
                            )
                        }
                    }


                },
                colors = color,
                singleLine = true,
                visualTransformation = visualTransformation,
                trailingIcon = trailingIcon,
                textStyle = TextStyle(
                    fontSize = 13.sp.noFontScale(), fontFamily = getFont("Lato"), color = Black
                )
            )
        }
    }
}

@Composable
fun CustomProfileDropDown(
    items: Array<String> = arrayOf("item1", "item2", "item3"),
    modifier: Modifier = Modifier,
    boxSize: Dp = 50.dp,
    enabled: Boolean = true,
    labelRequired: Boolean = true,
    labelColor: Color = authTextColor,
    labelComponent: (@Composable () -> Unit)? = null,
    label: AnnotatedString = AnnotatedString(""),
    placeholder: String = "placeholder",
    isError: Boolean = false,
    color: TextFieldColors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = LightGray,
        focusedIndicatorColor = DarkGray,
        focusedContainerColor = White,
        disabledContainerColor = White,
        disabledIndicatorColor = if (isError) errorColor else LightGray,
        disabledPlaceholderColor = LightGray,
        unfocusedContainerColor = White,
        errorIndicatorColor = errorColor,
        errorContainerColor = LightGray.copy(0.5f)
    ),
    state: String = "",
    textStyle: TextStyle = TextStyle(
        fontSize = 13.sp.noFontScale(),
        fontFamily = getFont("Lato", weight = FontWeight(400)),
        color = Gray,
        textAlign = TextAlign.Start
    ),

    errorMessage: String = "",
    focusRequester: FocusRequester? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onSelected: (String) -> Unit = {},
) {
    val showDropDown = remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp)) {
        if (labelRequired) {
            if (labelComponent != null) {
                labelComponent()
            } else {

                CustomText(
                    text = label,
                    fontFamily = LATO_FONT,
                    fontWeight = FontWeight(400),
                    modifier = Modifier.height(25.dp),
                    fontSize = 14.sp.noFontScale(),
                    color = labelColor
                )
            }
        }
        Box(contentAlignment = Alignment.Center) {
            OutlinedTextField(value = state, onValueChange = { }, placeholder = {
                CustomText(
                    text = placeholder,
                    color = if (isError) errorColor else plaeholderColor,
                    fontSize = 13.sp.noFontScale()
                )
            }, modifier = modifier.fillMaxWidth().padding(0.dp).apply {
                if (focusRequester != null) {
                    focusRequester(focusRequester)
                }
            }.clickable(enabled = enabled) {
                showDropDown.value = !showDropDown.value
                focusManager.clearFocus()
            }, isError = isError,

                supportingText = {
                    if (isError) {
                        Row(
                            modifier = Modifier.graphicsLayer {
                                translationX = -30f
                            }.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
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
                }, enabled = false, colors = color, singleLine = true, trailingIcon = {
                    if (enabled) {
                        Icon(
                            if (showDropDown.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            "",
                            tint = ticketTextDarkColor
                        )
                    }

                }, textStyle = TextStyle(
                    fontSize = 13.sp.noFontScale(),
                    fontFamily = getFont("Lato", weight = FontWeight(600)),
                    color = authTextColor
                )
            )
            DropdownMenu(
                expanded = enabled && showDropDown.value,
                onDismissRequest = { showDropDown.value = false },
                modifier = Modifier.align(Alignment.BottomCenter).heightIn(0.dp, 300.dp)
                    .fillMaxWidth(0.9f).background(White)
            ) {
                items.forEach {
                    DropdownMenuItem(text = { CustomText(text = it) }, onClick = {
                        onSelected(it)
                        showDropDown.value = false
                    }, modifier = Modifier.background(White))
                }
            }
        }
    }
}


@Composable
fun CustomOTPInputField(
    requireTimer: Boolean = true,
    label: String? = null,
    otpLength: Int,
    timer: Int = 30,
    boxSize: Dp = 50.dp,
    state: String = "123",
    isError: Boolean = false,
    errorMessage: String = "Invalid OTP! Please enter valid OTP.",
    onValueChange: (String) -> Unit = {},
    onAutoComplete: () -> Unit = {},
    textStyle: TextStyle = TextStyle(
        textAlign = TextAlign.Center,
        fontSize = 15.sp.noFontScale(),
        color = if (isError) MaterialTheme.colorScheme.error else appMainColor
    )

) {

    val context = LocalContext.current
    DisposableEffect(Unit) {
        val smsReceiver = SmsReceiver { address, msg ->
            try {
                val otpRegex = Regex("\\b\\d{4}(\\d{2})?\\b")


                val matchedOTP = otpRegex.findAll(msg).first().value
                if (state.isEmpty()) {

                    onValueChange(matchedOTP)
                    onAutoComplete()


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
        val intentFilter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        context.registerReceiver(smsReceiver, intentFilter)

        onDispose {
            context.unregisterReceiver(smsReceiver)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        label?.let {
            CustomText(
                text = label,
                fontFamily = LATO_FONT,
                fontWeight = FontWeight(500),
                modifier = Modifier.height(25.dp),
                fontSize = 14.sp.noFontScale()
            )
        }

        Column(modifier = Modifier.heightIn(80.dp)) {
            BasicTextField(
                value = state, modifier = Modifier, onValueChange = {
                    if (it.length <= otpLength) {
                        onValueChange(it)
                    }
                }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0..<otpLength) {
                        val char = if (state.length > i) {
                            state[i]
                        } else ""
                        OutlinedTextField(
                            value = char.toString(),
                            onValueChange = {},
                            enabled = false,
                            textStyle = textStyle,
                            modifier = Modifier.size(boxSize),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = appMainColor,
                                disabledIndicatorColor = if (isError) errorColor else if (i < state.length) appMainColor else Gray,
                                disabledContainerColor = White
                            ),
                        )
                    }
                }
            }
            Column {
                if (isError) {
                    Row(
                        modifier = Modifier.padding(
                            vertical = 5.dp, horizontal = 5.dp
                        ).fillMaxWidth()

                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.width(5.dp))
                        CustomText(
                            text = errorMessage,
                            modifier = Modifier,
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight(700),
                            color = errorColor
                        )
                    }

                }
                if (requireTimer) {
                    Row(
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp)
                            .fillMaxWidth().height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.width(5.dp))
                        CustomText(
                            text = "OTP will expire in ${formattedString(timer)} seconds.",
                            modifier = Modifier,
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight(700),
                            fontSize = 14.sp.noFontScale()
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun CustomOTPInputFieldWithBox(
    requireTimer: Boolean = true,
    label: String? = null,
    otpLength: Int,
    timer: Int = 30,
    boxSize: Dp = 50.dp,
    state: String = "123",
    isError: Boolean = false,
    errorMessage: String = "Invalid OTP! Please enter valid OTP.",
    onValueChange: (String) -> Unit = {},
    onAutoComplete: () -> Unit = {},
    textStyle: TextStyle = TextStyle(
        textAlign = TextAlign.Center,
        fontSize = 15.sp.noFontScale(),
        color = if (isError) MaterialTheme.colorScheme.error else appMainColor
    )

) {

    val context = LocalContext.current
    DisposableEffect(Unit) {
        val smsReceiver = SmsReceiver { address, msg ->
            try {
                val otpRegex = Regex("\\b\\d{4}(\\d{2})?\\b")


                val matchedOTP = otpRegex.findAll(msg).first().value
                if (matchedOTP != null && state.isEmpty()) {

                    onValueChange(matchedOTP)
                    onAutoComplete()


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
        val intentFilter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        context.registerReceiver(smsReceiver, intentFilter)

        onDispose {
            context.unregisterReceiver(smsReceiver)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        label?.let {
            CustomText(
                text = label,
                fontFamily = LATO_FONT,
                fontWeight = FontWeight(500),
                modifier = Modifier.height(25.dp),
                fontSize = 14.sp.noFontScale()
            )
        }

        Column(modifier = Modifier.heightIn(80.dp)) {
            BasicTextField(
                value = state, modifier = Modifier, onValueChange = {
                    if (it.length <= otpLength) {
                        onValueChange(it)
                    }
                }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0..<otpLength) {
                        val char = if (state.length > i) {
                            state[i]
                        } else ""
                        Box(
                            modifier = Modifier.size(boxSize).border(
                                1.5.dp, appMainColor, RoundedCornerShape(5.dp)
                            ), contentAlignment = Alignment.Center

                        ) {
                            CustomText(text = char.toString(), fontSize = textStyle.fontSize)
                        }
                    }
                }
            }
            Column {
                if (isError) {
                    Row(
                        modifier = Modifier.padding(
                            vertical = 5.dp, horizontal = 5.dp
                        ).fillMaxWidth()

                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.width(5.dp))
                        CustomText(
                            text = errorMessage,
                            modifier = Modifier,
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight(700),
                            color = errorColor
                        )
                    }

                }
                if (requireTimer) {
                    Row(
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp)
                            .fillMaxWidth().height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.width(5.dp))
                        CustomText(
                            text = "OTP will expire in ${formattedString(timer)} seconds.",
                            modifier = Modifier,
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight(400),
                            fontSize = 12.sp.noFontScale()
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun CustomSetPin(
    label: String? = null,
    pinLength: Int,
    state: String = "123",
    isError: Boolean = false,
    errorMessage: String = "Pin doesn't match",
    onValueChange: (String) -> Unit = {},
    onAutoComplete: () -> Unit = {},
) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val smsReceiver = SmsReceiver { address, msg ->
            try {
                val otpRegex = Regex("\\b\\d{4}(\\d{2})?\\b")


                val matchedOTP = otpRegex.findAll(msg).first().value
                if (matchedOTP != null && state.isEmpty()) {

                    onValueChange(matchedOTP)
                    onAutoComplete()


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
        val intentFilter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        context.registerReceiver(smsReceiver, intentFilter)

        onDispose {
            context.unregisterReceiver(smsReceiver)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        label?.let {
            CustomText(
                text = label,
                fontFamily = LATO_FONT,
                fontWeight = FontWeight(500),
                modifier = Modifier.height(25.dp),
                fontSize = 14.sp.noFontScale()
            )
        }

        Column(modifier = Modifier.heightIn(80.dp)) {
            BasicTextField(
                value = state, modifier = Modifier, onValueChange = {
                    if (it.length <= pinLength) {
                        onValueChange(it)
                    }
                }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0..<pinLength) {
                        val char = if (state.length > i) {
                            state[i]
                        } else ""
                        TextField(
                            value = char.toString(),
                            onValueChange = {},
                            enabled = false,
                            textStyle = TextStyle(
                                textAlign = TextAlign.Center,
                                fontSize = 15.sp.noFontScale(),
                                color = if (isError) MaterialTheme.colorScheme.error else authTextColor
                            ),
                            modifier = Modifier.size(50.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = appMainColor,
                                disabledIndicatorColor = if (isError) errorColor else if (i < state.length) authTextColor else Gray,
                                disabledContainerColor = White
                            ),
                        )
                    }
                }
            }
            Column {
                if (isError) {
                    Row(
                        modifier = Modifier.padding(
                            vertical = 5.dp, horizontal = 5.dp
                        ).fillMaxWidth()

                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.width(5.dp))
                        CustomText(
                            text = errorMessage,
                            modifier = Modifier,
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight(700),
                            color = errorColor
                        )
                    }
                }
            }
        }
    }
}

fun formattedString(timer: Int): String {
    val seconds = if (timer > 60) timer - 60 else timer
    val secondsString = if (seconds < 10) "0$seconds" else "$seconds"
    val minutesString = if (timer > 60) "01" else "00"
    return "$minutesString:$secondsString"
}


