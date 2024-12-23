package com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.*
import com.isu.common.events.CommonScreenEvents
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.ticketTextDarkColor
import com.isu.profile.R
import com.isu.profile.presentation.screens.customersupport.presentation.screens.InputType
import com.isu.profile.presentation.screens.customersupport.presentation.screens.allTickets.UploadedDocumentDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round

/**
 * Extension function to round a [Double] value to two decimal places.
 */
fun Double.roundToTwoDecimalPlaces(): Double {
    return round(this * 100) / 100
}

/**
 * Retrieves the file size in megabytes for a given [Uri] and [ContentResolver].
 *
 * @param uri The URI of the file.
 * @param contentResolver The content resolver used to query file metadata.
 * @return The file size in megabytes, rounded to two decimal places, or null if the size could not be determined.
 */
private fun getFileSize(uri: Uri, contentResolver: ContentResolver): Double? {
    val cursor = contentResolver.query(uri, null, null, null, null)
    return if (cursor != null && cursor.moveToFirst()) {
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        val size = cursor.getLong(sizeIndex)
        cursor.close()
        (size / (1024.0 * 1024.0)).roundToTwoDecimalPlaces()
    } else {
        null // Indicate an error
    }
}

/**
 * Get file type from uri
 *
 * @param context
 * @param uri
 * @return
 */
sealed class File {
    data class Image(val type: String = "Image") : File()
    data class Video(val type: String = "Video") : File()
    data class Audio(val type: String = "Audio") : File()
    data class PDF(val type: String = "PDF") : File()
    data class Unknown(val type: String = "Unknown") : File()

}

fun getFileTypeFromUri(context: Context, uri: Uri): File {
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(uri)

    return when {
        mimeType?.startsWith("image/") == true -> File.Image()
        mimeType?.startsWith("video/") == true -> File.Video()
        mimeType?.startsWith("audio/") == true -> File.Audio()
        mimeType == "application/pdf" -> File.PDF()
        else -> File.Unknown()
    }
}

/**
 * @author-karthik
 * Composable function for the Raise Ticket Screen.
 *
 * @param onEvent A lambda function to handle screen events.
 * @param state The state of the raise ticket form.
 */
@Composable
fun RaiseTicketScreen(
    onEvent: (CommonScreenEvents) -> Unit,
    state: RaiseTicketData,
    hashMap: MutableMap<String, String>
) {
    val scope = rememberCoroutineScope()
//    val files: SnapshotStateList<DocumentDetails> = remember { mutableStateListOf() }
    val attachmentError = remember { mutableStateOf(false) }
    val attachmentErrorMessage = remember { mutableStateOf("") }
    val contentResolver = LocalContext.current.contentResolver
    val context = LocalContext.current
    val config= LocalConfiguration.current

    // Launcher for file selection
    val uploadFile = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val size = getFileSize(it, contentResolver)
            val fileType = getFileTypeFromUri(context, it)

            if (size != null) {
                if (size > 5) {
                    scope.launch {
                        attachmentError.value = true
                        attachmentErrorMessage.value =
                            context.getString(R.string.maximum_file_size_is_5_mb)
                        delay(2000)
                        attachmentError.value = false
                        attachmentErrorMessage.value = ""
                    }
                } else {
                    onEvent(
                        CommonScreenEvents.OnClick<Uri>(
                            RaiseATicketClickableTypes.UploadToFirebase,
                            additionData = it
                        )
                    )

                    val icon = when (fileType) {
                        is File.Audio -> com.isu.common.R.drawable.music_file
                        is File.Image -> com.isu.common.R.drawable.jpg
                        is File.PDF -> com.isu.common.R.drawable.pdf
                        is File.Unknown -> com.isu.common.R.drawable.unknown_file
                        is File.Video -> com.isu.common.R.drawable.video_file
                    }

                }
            }
        }
    }

    LaunchedEffect(Unit) {
        onEvent(CommonScreenEvents.ClearFields)
        onEvent(CommonScreenEvents.CallApi<RaiseATicketAPIType.FetchFormData>(apiType =RaiseATicketAPIType.FetchFormData, onSuccess = {} ))
    }


    Scaffold(
        topBar = {
            CustomProfileTopBar(text = stringResource(R.string.raise_a_ticket))
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                start = 22.dp,
                top = innerPadding.calculateTopPadding(),
                end = 22.dp,
                bottom = innerPadding.calculateBottomPadding(),
            )
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            KeyBoardAwareScreen(
                modifier = Modifier.padding(
                    start = 0.dp,
                    top = 0.dp,
                    bottom = 0.dp,
                    end = 0.dp
                ),
                shouldScroll = false,
                scrollValue = { it / 2 }
            ) {
                Column(modifier = Modifier.heightIn(config.screenHeightDp.dp - 280.dp)) {
                   state.listOfFormData.forEach {
                       if(it.type == InputType.DROPDOWN){
                           val value= remember {
                               mutableStateOf("")
                           }
                           if (it.id == "31812447425945" && state.TransactionId != null) {
                               value.value = "Transaction Drop"
                               CustomProfileDropDown(
                                   label = buildAnnotatedString {
                                       append(it.label)
                                       withStyle(
                                           SpanStyle(color = Color.Red)
                                       ) {
                                           if (it.required) {
                                               append("*")
                                           }
                                       }
                                   },
                                   placeholder = it.label,
                                   state = value.value,
                                   items = it.possibleValues.map { it.value }.toTypedArray()
                               ) {
                               }
                           } else {
                               CustomProfileDropDown(
                                   label = buildAnnotatedString {
                                       append(it.label)
                                       withStyle(
                                           SpanStyle(color = Color.Red)
                                       ) {
                                           if (it.required) {
                                               append("*")
                                           }
                                       }
                                   },
                                   placeholder = it.label,
                                   state = value.value,
                                   items = it.possibleValues.map { it.value }.toTypedArray()
                               ) {valu->
                                   hashMap.put(it.id.toString(), valu)
                                   value.value = valu
                               }
                           }

                       }else{

                           val value= remember {
                               mutableStateOf("")
                           }


                           if (it.id == "900012621786" && state.TransactionId != null && hashMap.containsRegex<String, String>(
                                   "transaction"
                               )
                           ) {
                               CustomInputField(
                                   annotatedLabel = buildAnnotatedString {
                                       append(it.label)
                                       withStyle(
                                           SpanStyle(color = Color.Red)
                                       ) {
                                           if (it.required) {
                                               append("*")
                                           }
                                       }
                                   },
                                   enabled = false,
                                   placeholder = it.label,
                                   state = state.TransactionId.toString(),
                                   keyboardOptions = KeyboardOptions(
                                       keyboardType = if (it.type == InputType.FIELD_NUMBER) KeyboardType.Number else KeyboardType.Text
                                   )
                               ) {
                               }
                           } else {
                               if (it.id == "900012621786") {
                                   if (hashMap.containsRegex<String, String>("transaction")) {
                                       CustomInputField(
                                           annotatedLabel = buildAnnotatedString {
                                               append(it.label)
                                               withStyle(
                                                   SpanStyle(color = Color.Red)
                                               ) {
                                                   if (it.required) {
                                                       append("*")
                                                   }
                                               }
                                           },
                                           placeholder = it.label,
                                           state = value.value,
                                           keyboardOptions = KeyboardOptions(
                                               keyboardType = if (it.type == InputType.FIELD_NUMBER) KeyboardType.Number else KeyboardType.Text
                                           )
                                       ) { text ->

                                           value.value = text
                                           hashMap.put(it.id.toString(), text)
                                       }
                                   } else if (state.TransactionId != null) {
                                       CustomInputField(
                                           annotatedLabel = buildAnnotatedString {
                                               append(it.label)
                                               withStyle(
                                                   SpanStyle(color = Color.Red)
                                               ) {
                                                   if (it.required) {
                                                       append("*")
                                                   }
                                               }
                                           },
                                           enabled = false,
                                           placeholder = it.label,
                                           state = state.TransactionId.toString(),
                                           keyboardOptions = KeyboardOptions(
                                               keyboardType = if (it.type == InputType.FIELD_NUMBER) KeyboardType.Number else KeyboardType.Text
                                           )
                                       ) {
                                       }
                                   }
                               } else {
                                   CustomInputField(
                                       annotatedLabel = buildAnnotatedString {
                                           append(it.label)
                                           withStyle(
                                               SpanStyle(color = Color.Red)
                                           ) {
                                               if (it.required) {
                                                   append("*")
                                               }
                                           }
                                       },
                                       placeholder = it.label,
                                       state = value.value,
                                       keyboardOptions = KeyboardOptions(
                                           keyboardType = if (it.type == InputType.FIELD_NUMBER) KeyboardType.Number else KeyboardType.Text
                                       )
                                   ) { text ->

                                       value.value = text
                                       hashMap.put(it.id.toString(), text)
                                   }
                               }

                           }

                       }
                   }
                    CustomAttachmentInputField(
                        label = buildAnnotatedString {
                            append(stringResource(R.string.attachment))

                        },
                        placeholder = "Upload Documents",
                        labelColor = ticketTextDarkColor,
                        isError = attachmentError.value,
                        errorMessage = attachmentErrorMessage.value,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(com.isu.common.R.drawable.upload),
                                "",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    ) {
                        if (state.attachment.size < 5) {
                            uploadFile.launch("*/*")
                        } else {
                            scope.launch {
                                attachmentError.value = true
                                attachmentErrorMessage.value =
                                    context.getString(R.string.maximum_5_files_can_be_uploaded)
                                delay(1000)
                                attachmentError.value = false
                                attachmentErrorMessage.value = ""
                            }
                        }
                    }
                    if (state.attachment.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            state.attachment.forEach {
                                UploadedDocumentDetail(it) {
                                    state.attachment.remove(it)

                                }
                            }
                        }
                    }
                }



                Spacer(modifier = Modifier.height(10.dp))
//                Column(Modifier.wrapContentHeight()) {
//                    CustomProfileDropDown(
//                        items = stringArrayResource(R.array.inquiry_types),
//                        labelRequired = true,
//                        labelComponent = {
//                            val label = buildAnnotatedString {
//                                append(context.getString(R.string.category))
//                                withStyle(SpanStyle(color = Color.Red, letterSpacing = 10.sp)) {
//                                    append(context.getString(R.string.astrisk))
//                                }
//                            }
//                            CustomText(
//                                text = label,
//                                fontFamily = LATO_FONT,
//                                fontWeight = FontWeight(400),
//                                modifier = Modifier.height(25.dp),
//                                fontSize = 13.sp.noFontScale(),
//                                color = Color.Black
//                            )
//                        },
//                        isError = state.categoryError,
//                        errorMessage = state.categoryErrorMessage.asString(),
//                        labelColor = Color.Black,
//                        placeholder = stringResource(R.string.please_select_a_category),
//                        state = state.category
//                    ) {
//                        onEvent(
//                            CommonScreenEvents.OnTextChanged(
//                                text = it,
//                                RaiseATicketTextInput.Category
//                            )
//                        )
//                    }
//
//                    CustomProfileDropDown(
//                        items = stringArrayResource(R.array.priority_levels),
//                        labelRequired = true,
//                        labelComponent = {
//                            val label = buildAnnotatedString {
//                                append(context.getString(R.string.priority))
//                                withStyle(SpanStyle(color = Color.Red, letterSpacing = 10.sp)) {
//                                    append(context.getString(R.string.astrisk))
//                                }
//                            }
//                            CustomText(
//                                text = label,
//                                fontFamily = LATO_FONT,
//                                fontWeight = FontWeight(400),
//                                modifier = Modifier.height(25.dp),
//                                fontSize = 13.sp.noFontScale(),
//                                color = Color.Black
//                            )
//                        },
//                        isError = state.priorityError,
//                        errorMessage = state.priorityErrorMessage.asString(),
//                        labelColor = Color.Black,
//                        state = state.priority,
//                        placeholder = stringResource(R.string.please_select_a_priority)
//                    ) {
//                        onEvent(
//                            CommonScreenEvents.OnTextChanged(
//                                text = it,
//                                RaiseATicketTextInput.Priority
//                            )
//                        )
//                    }
//
//                    CustomInputField(
//                        labelComponent = {
//                            val annotatedLabel = buildAnnotatedString {
//                                append(context.getString(R.string.title))
//                                withStyle(SpanStyle(color = Color.Red, letterSpacing = 10.sp)) {
//                                    append(context.getString(R.string.astrisk))
//                                }
//                            }
//                            CustomText(
//                                text = annotatedLabel,
//                                fontFamily = LATO_FONT,
//                                fontWeight = FontWeight(400),
//                                modifier = Modifier.height(25.dp),
//                                fontSize = 13.sp.noFontScale(),
//                                color = Color.Black
//                            )
//                        },
//                        errorMessage = state.titleErrorMessage.asString(),
//                        isError = state.titleError,
//                        placeholder = stringResource(R.string.enter_title_name),
//                        state = state.title
//                    ) {
//                        onEvent(CommonScreenEvents.OnTextChanged(it, RaiseATicketTextInput.Title))
//                    }
//
//                    CustomInputField(
//                        modifier = Modifier.height(125.dp),
//                        singleLine = false,
//                        state = state.description,
//                        errorMessage = state.descriptionErrorMessage.asString(),
//                        isError = state.descriptionError,
//                        onValueChange = {
//                            onEvent(
//                                CommonScreenEvents.OnTextChanged(
//                                    it,
//                                    RaiseATicketTextInput.Description
//                                )
//                            )
//                        },
//                        labelComponent = {
//                            val annotatedLabel = buildAnnotatedString {
//                                append(context.getString(R.string.description))
//                                withStyle(SpanStyle(color = Color.Red, letterSpacing = 10.sp)) {
//                                    append(context.getString(R.string.astrisk))
//                                }
//                            }
//                            CustomText(
//                                text = annotatedLabel,
//                                fontFamily = LATO_FONT,
//                                fontWeight = FontWeight(400),
//                                modifier = Modifier.height(25.dp),
//                                fontSize = 13.sp.noFontScale(),
//                                color = Color.Black
//                            )
//                        },
//                        placeholder = stringResource(R.string.any_additional_details)
//                    )
//
//                    CustomText(
//                        text = stringResource(R.string.describe_your_issue_within_300_characters),
//                        fontSize = 12.sp.noFontScale(),
//                        color = ticketTextLightColor,
//                        modifier = Modifier.graphicsLayer {
//                            translationY = -30f
//                        }
//                    )
//
//                    CustomAttachmentInputField(
//                        label = buildAnnotatedString {
//                            append(stringResource(R.string.attachment))
//
//                        },
//                        placeholder = stringResource(R.string.please_add_attachment),
//                        labelColor = ticketTextDarkColor,
//                        isError = attachmentError.value,
//                        errorMessage = attachmentErrorMessage.value
//                    ) {
//                        if (state.attachment.size < 5) {
//                            uploadFile.launch("*/*")
//                        } else {
//                            scope.launch {
//                                attachmentError.value = true
//                                attachmentErrorMessage.value =
//                                    context.getString(R.string.maximum_5_files_can_be_uploaded)
//                                delay(1000)
//                                attachmentError.value = false
//                                attachmentErrorMessage.value = ""
//                            }
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(10.dp))
//
//                    if (state.attachment.isNotEmpty()) {
//                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
//                            state.attachment.forEach {
//                                UploadedDocumentDetail(it) {
//                                    state.attachment.remove(it)
//
//                                }
//                            }
//                        }
//                    }
//                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomResizeButton(
                        onClick = {
                            onEvent(CommonScreenEvents.OnClick<Any>(RaiseATicketClickableTypes.Submit))
                        },
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth(),
                        innerComponent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CustomText(
                                    text = stringResource(R.string.submit),
                                    color = Color.White
                                )
                            }
                        }
                    )



                    CustomResizeButton(
                        onClick = {
                            onEvent(CommonScreenEvents.OnClick<Any>(RaiseATicketClickableTypes.Cancel))
                        },
                        color = Color.White,
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth(1f)
                            .border(
                                1.dp,
                                authTextColor,
                                shape = RoundedCornerShape(5.dp)
                            ),
                        innerComponent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CustomText(
                                    text = stringResource(R.string.cancel),
                                    color = authTextColor
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun <K, V> MutableMap<K, String>.containsRegex(v: String): Boolean {
    var bool = false
    this.values.forEach {
        if (it.lowercase().contains(v.lowercase().toRegex())) return true
    }
    return bool
}
