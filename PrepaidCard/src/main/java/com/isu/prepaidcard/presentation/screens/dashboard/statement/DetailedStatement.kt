package com.isu.prepaidcard.presentation.screens.dashboard.statement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomDateField
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.CardBlue
import com.isu.common.utils.LocalNavController
import com.isu.prepaidcard.presentation.viewmodels.StatementViewModel
import com.isu.prepaidcard.presentation.viewmodels.TransactionTypeTimeLine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getCurrentDateFormatted(pattern: String = "yyyy-MM-dd", pastMonths: Int = 0): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault()) // Specify your desired format
    val currentDate = Date()
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, -pastMonths)
    dateFormat.format(calendar.time)
    return dateFormat.format(calendar.time)
}
@Composable
fun DetailedStatement(statementViewModel: StatementViewModel) {
    val cardType = "GPR"
    val config = LocalConfiguration.current
    val startDate = statementViewModel.startDate
    val endDate = statementViewModel.endDate
    val scope = rememberCoroutineScope()
    val currentDate = getCurrentDateFormatted()
    LaunchedEffect(Unit) {
        startDate.value = ""
        endDate.value = ""
        statementViewModel.transactionType.value = null
    }


    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Detailed Statement"
            )
        },
        containerColor = Color.White
    ) {
        LaunchedEffect(key1 = Unit){

            statementViewModel.getPreferenceData()


        }
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

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CustomInputField(
                        annotatedLabel = buildAnnotatedString {
                            append("Card Selected")
                        withStyle(SpanStyle(color = Color.Red)) {
                            append("*")
                        }
                        },
                        placeholder = "XXXX-XXXX-XXXX-${
                            statementViewModel.cardReferenceNumber.value.takeLast(4)
                        }",
                        enabled = false
                    ) { }

                    Column {


                        CustomText(text = "Transaction Type")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                RadioButton(
                                    selected = statementViewModel.transactionType.value == TransactionTypeTimeLine.ONE_MONTH,
                                    onClick = {
                                        statementViewModel.transactionType.value =
                                            TransactionTypeTimeLine.ONE_MONTH
                                        endDate.value = currentDate
                                        startDate.value = getCurrentDateFormatted(pastMonths = 1)
                                    })
                                CustomText(text = "Last 1 month")
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                RadioButton(
                                    selected = statementViewModel.transactionType.value == TransactionTypeTimeLine.THREE_MONTH,
                                    onClick = {
                                        statementViewModel.transactionType.value =
                                            TransactionTypeTimeLine.THREE_MONTH
                                        startDate.value = getCurrentDateFormatted(pastMonths = 3)
                                        endDate.value = currentDate
                                    })
                                CustomText(text = "Last 3 month")
                            }

                        }
                    }
                    Column {

                        Row(modifier = Modifier.fillMaxWidth()) {


                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                RadioButton(
                                    selected = statementViewModel.transactionType.value == TransactionTypeTimeLine.SIX_MONTH,
                                    onClick = {
                                        statementViewModel.transactionType.value =
                                            TransactionTypeTimeLine.SIX_MONTH
                                        endDate.value = currentDate
                                        startDate.value = getCurrentDateFormatted(pastMonths = 6)
                                    })
                                CustomText(text = "Last 6 month")
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                RadioButton(
                                    selected = statementViewModel.transactionType.value == TransactionTypeTimeLine.ONE_YEAR,
                                    onClick = {
                                        statementViewModel.transactionType.value =
                                            TransactionTypeTimeLine.ONE_YEAR
                                        startDate.value = getCurrentDateFormatted(pastMonths = 12)
                                        endDate.value = currentDate
                                    })
                                CustomText(text = "Last 1 Year")
                            }

                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CustomDateField(
                            modifier = Modifier.weight(1f),
                            pattern = "yyyy-MM-dd",
                            state = startDate.value,
                            annotatedLabel = buildAnnotatedString {
                                append("Start Date")
                                withStyle(SpanStyle(color = Color.Red)) {
                                    append("*")
                                }
                            },
                            placeholderComposable = {
                                CustomText(
                                    text = "yyyy-MM-dd",
                                    fontSize = 12.sp.noFontScale(),
                                    modifier = Modifier,
                                    textAlign = TextAlign.Start
                                )
                            },
                            placeholder = "yyyy-MM-dd"
                        ) {
                            statementViewModel.transactionType.value = null
                            startDate.value = it
                        }
                        CustomDateField(
                            modifier = Modifier.weight(1f),
                            pattern = "yyyy-MM-dd",
                            state = endDate.value,
                            annotatedLabel = buildAnnotatedString {
                                append("End Date")
                                withStyle(SpanStyle(color = Color.Red)) {
                                    append("*")
                                }
                            },
                            placeholderComposable = {
                                CustomText(
                                    text = "yyyy-MM-dd",
                                    fontSize = 12.sp.noFontScale(),
                                    modifier = Modifier,
                                    textAlign = TextAlign.Start
                                )
                            },
                            placeholder = "yyyy-MM-dd"
                        ) {
                            statementViewModel.transactionType.value = null
                            endDate.value = it
                        }
                    }

                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    val navController = LocalNavController.current
                    val context = LocalContext.current
                    CustomButton(
                        color = CardBlue, text = "Proceed",
                        modifier = Modifier.weight(
                            1f
                        ),
                        onClick = {
                            if (startDate.value.isNotEmpty() && endDate.value.isNotEmpty()) {
                                statementViewModel.detailedStatement(
                                    startDate.value,
                                    endDate.value
                                ) {
                                    scope.launch {
                                        NavigationEvent.helper.navigateTo(CardManagement.DetailedStatementList)
                                    }
                                }
                            }
                        })

                }
            }
        }
    }
}