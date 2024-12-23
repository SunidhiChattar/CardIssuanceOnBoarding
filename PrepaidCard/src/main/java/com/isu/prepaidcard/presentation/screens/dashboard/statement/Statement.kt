package com.isu.prepaidcard.presentation.screens.dashboard.statement

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomCancelButton
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.CustomerSupportScreen
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.CardBlue
import com.isu.common.utils.LocalNavController
import com.isu.common.utils.navigateTo
import com.isu.prepaidcard.data.DetailOrderStatementCard
import com.isu.prepaidcard.data.OrderStatementCard
import com.isu.prepaidcard.presentation.viewmodels.StatementViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

/**
 * This composable function represents the card statement screen of the application.
 *
 * @param statementViewModel ViewModel containing the statement related data and logic.
 *
 * @Composable This annotation indicates that the function is composable and can be used within a Jetpack Compose composable hierarchy.
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Statement(statementViewModel: StatementViewModel) {

//    val txnType = remember { mutableStateOf(false) }
//    val fromDatePicker = remember { mutableStateOf(false) }
//    val toDatePicker = remember { mutableStateOf(false) }

    val fromDateState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()

        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun isSelectableYear(year: Int): Boolean {
            return year <= LocalDate.now().year

        }

    })
    val toDateState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()

        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun isSelectableYear(year: Int): Boolean {
            return year <= LocalDate.now().year

        }

    })

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit){
        statementViewModel.miniStatement{

        }
    }
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Card Statement"
            )
        },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding()).background(Color.White)
        ) {

           /* AnimatedVisibility(visible = txnType.value){
                Card(modifier = Modifier.padding(6.dp),
                    colors = CardDefaults.cardColors(Color.White)) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CustomText(text = "Debit")
                        HorizontalDivider()
                        CustomText(text = "Credit")
                    }
                }
            }
            AnimatedVisibility(visible = fromDatePicker.value){
                DatePickerDialog(
                    onDismissRequest = { fromDatePicker.value = false },
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
                            fromDatePicker.value = false
                        }, colors = ButtonDefaults.buttonColors(containerColor = appMainColor)) {
                            Text("OK")
                        }
                    }) {
                    DatePicker(
                        state = fromDateState,
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
                                text = "Select From Date",
                                color = appMainColor,
                                fontSize = 19.sp.noFontScale(),
                                modifier = Modifier.padding(start = 10.dp),
                                fontWeight = FontWeight(600),
                            )
                        }
                    )
                }
            }
            AnimatedVisibility(visible = toDatePicker.value){
                DatePickerDialog(
                    onDismissRequest = { toDatePicker.value = false },
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
                            toDatePicker.value = false
                            statementViewModel.miniStatement()
                        }, colors = ButtonDefaults.buttonColors(containerColor = appMainColor)) {
                            Text("OK")
                        }
                    }) {
                    DatePicker(
                        state = toDateState,
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
                                text = "Select To Date",
                                color = appMainColor,
                                fontSize = 19.sp.noFontScale(),
                                modifier = Modifier.padding(start = 10.dp),
                                fontWeight = FontWeight(600),
                            )
                        }
                    )
                }
            }*/
            LazyColumn(
                verticalArrangement =
                if (statementViewModel.statementData.isEmpty())
                    Arrangement.Center
                else
                    Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(20.dp)
            ) {

                if (statementViewModel.statementData.isEmpty()) {
                    item {
                        Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment
                            .CenterHorizontally
                        ) {
                        Image(
                            painter = painterResource(com.isu.common.R.drawable.no_data),
                            "",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    }
                }
                else{
                    items(statementViewModel.statementData) {
                        OrderStatementCard(
                            selectedTransaction = it
                        )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEachStatement(statementViewModel: StatementViewModel) {
    val raisedDisputeClicked = remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

//    val txnType = remember { mutableStateOf(false) }
//    val fromDatePicker = remember { mutableStateOf(false) }
//    val toDatePicker = remember { mutableStateOf(false) }

    val fromDateState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()

        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun isSelectableYear(year: Int): Boolean {
            return year <= LocalDate.now().year

        }

    })
    val toDateState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()

        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun isSelectableYear(year: Int): Boolean {
            return year <= LocalDate.now().year

        }

    })


    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Card Statement"
            )
        },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding()).background(Color.White)
        ) {
            if (raisedDisputeClicked.value) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    val navController = LocalNavController.current
                    val context = LocalContext.current
                    CustomCancelButton(text = "Detailed Statement",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            raisedDisputeClicked.value = false
                        })
                    CustomButton(color = CardBlue, text = " Raise Dispute",
                        modifier = Modifier.weight(
                            1f
                        ),
                        onClick = {
                            raisedDisputeClicked.value = true
                        })

                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    val navController = LocalNavController.current
                    val context = LocalContext.current
                    CustomButton(color = CardBlue, text = "Detailed Statement",
                        modifier = Modifier.weight(
                            1f
                        ),
                        onClick = {
                            raisedDisputeClicked.value = false
                        })
                    CustomCancelButton(text = "Raise Dispute",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            raisedDisputeClicked.value = true
                        })
                }
            }


            /* AnimatedVisibility(visible = txnType.value){
                 Card(modifier = Modifier.padding(6.dp),
                     colors = CardDefaults.cardColors(Color.White)) {
                     Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                         CustomText(text = "Debit")
                         HorizontalDivider()
                         CustomText(text = "Credit")
                     }
                 }
             }
             AnimatedVisibility(visible = fromDatePicker.value){
                 DatePickerDialog(
                     onDismissRequest = { fromDatePicker.value = false },
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
                             fromDatePicker.value = false
                         }, colors = ButtonDefaults.buttonColors(containerColor = appMainColor)) {
                             Text("OK")
                         }
                     }) {
                     DatePicker(
                         state = fromDateState,
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
                                 text = "Select From Date",
                                 color = appMainColor,
                                 fontSize = 19.sp.noFontScale(),
                                 modifier = Modifier.padding(start = 10.dp),
                                 fontWeight = FontWeight(600),
                             )
                         }
                     )
                 }
             }
             AnimatedVisibility(visible = toDatePicker.value){
                 DatePickerDialog(
                     onDismissRequest = { toDatePicker.value = false },
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
                             toDatePicker.value = false
                             statementViewModel.miniStatement()
                         }, colors = ButtonDefaults.buttonColors(containerColor = appMainColor)) {
                             Text("OK")
                         }
                     }) {
                     DatePicker(
                         state = toDateState,
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
                                 text = "Select To Date",
                                 color = appMainColor,
                                 fontSize = 19.sp.noFontScale(),
                                 modifier = Modifier.padding(start = 10.dp),
                                 fontWeight = FontWeight(600),
                             )
                         }
                     )
                 }
             }*/
            LazyColumn(
                verticalArrangement =
                if (statementViewModel.detailStatementData.isEmpty())
                    Arrangement.Center
                else
                    Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
                    .padding(20.dp)
            ) {

                if (statementViewModel.detailStatementData.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment
                                .CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(com.isu.common.R.drawable.no_data),
                                "",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                } else {
                    items(statementViewModel.detailStatementData) {
                        DetailOrderStatementCard(
                            showRadioButton = raisedDisputeClicked.value,
                            selectedTransaction = it,
                            radioValue = statementViewModel.selectedDetailedTrasnaction.value,
                            onRadioClick = { item ->
                                statementViewModel.selectedDetailedTrasnaction.value = item
                            }
                        )
                    }
                }


            }
            if (raisedDisputeClicked.value) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    val navController = LocalNavController.current
                    val context = LocalContext.current
                    CustomButton(color = CardBlue, text = "Raise a dispute",
                        modifier = Modifier.weight(
                            1f
                        ),
                        onClick = {
                            if (statementViewModel.selectedDetailedTrasnaction.value != null) {
                                scope.launch {
                                    statementViewModel.saveTransactionDataInDataStore()
                                    NavigationEvent.helper.navigateTo(CustomerSupportScreen.RaiseTicketScreen)
                                }
                            }


                        })
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    val navController = LocalNavController.current
                    val context = LocalContext.current
                    CustomButton(color = CardBlue, text = "View PDF",
                        modifier = Modifier.weight(
                            1f
                        ),
                        onClick = {
                            statementViewModel.createPdf(context){
                              scope.launch {

                                    navController.navigateTo(CardManagement.VIEW_PDF_SCREEN)

                                }
                            }



                        })
                    CustomCancelButton(text = "Send to Email",
                        modifier = Modifier.weight(1f),
                        onClick = {

                        })
                }
            }

        }
    }
}

fun convertDate(
    dateString: String,
    inputFormat: String = "dd MMM, yyyy hh.mm a",
    outputDateFormat: String = "dd/MM/yyyy"
): String? {
    // Define the input and output date formats
    return try {
        val inputFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputDateFormat, Locale.ENGLISH)

        // Parse the date using the input format
        val date = inputFormat.parse(dateString)

        // Format the date to the desired output format
        return date?.let { outputFormat.format(it) }
    } catch (e: Exception) {
        null
    }

}