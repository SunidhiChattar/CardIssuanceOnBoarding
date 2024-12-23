package com.isu.prepaidcard.presentation.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.ui.theme.CardRed
import com.isu.common.ui.theme.TextGreen
import com.isu.common.utils.UiText
import com.isu.common.utils.ZonedDateFormatter
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferenceData
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.common.utils.pdfutils.PDFData
import com.isu.common.utils.pdfutils.createPdfWithTableAndBorders
import com.isu.prepaidcard.data.TransactionDeatils
import com.isu.prepaidcard.data.request.DetailedStatementRequest
import com.isu.prepaidcard.data.request.MiniStatementRequest
import com.isu.prepaidcard.data.response.MiniStatementResponse
import com.isu.prepaidcard.domain.usecase.statement_usecases.DetailedStatementApiUseCase
import com.isu.prepaidcard.domain.usecase.statement_usecases.MiniStatementApiUseCase
import com.isu.prepaidcard.presentation.screens.dashboard.statement.convertDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This ViewModel class manages data related to card statements.
 *
 * @HiltViewModel indicates that this class is a ViewModel and leverages Hilt for dependency injection.
 */
sealed interface TransactionTypeTimeLine {
    object ONE_MONTH : TransactionTypeTimeLine
    object THREE_MONTH : TransactionTypeTimeLine
    object SIX_MONTH : TransactionTypeTimeLine
    object ONE_YEAR : TransactionTypeTimeLine
}

sealed interface TransactionType {
    object CREDIT : TransactionType
    object DEBIT : TransactionType

}
@HiltViewModel
class StatementViewModel @Inject constructor(
    private val miniStatementApiUseCase: MiniStatementApiUseCase,
    private val detailedStatementApiUseCase: DetailedStatementApiUseCase,
    private val dataStoreManager: DataStoreManager
): ViewModel(){


    val startDate =
        mutableStateOf("")

    val endDate =
        mutableStateOf("")

    val uri: MutableState<Uri?> = mutableStateOf(null)
    val cardReferenceNumber = mutableStateOf("")
    fun getPreferenceData() {
        viewModelScope.launch {
            cardReferenceNumber.value =
                dataStoreManager.getPreferenceValue(PreferencesKeys.CARDNUMBER).toString()
        }

    }

    // List of statement data items
    val selectedDetailedTrasnaction =
        mutableStateOf<TransactionDeatils?>(null)
    val transactionType = mutableStateOf<TransactionTypeTimeLine?>(null)

    val statementData:SnapshotStateList<TransactionDeatils> = mutableStateListOf()
    val detailStatementData: SnapshotStateList<TransactionDeatils> = mutableStateListOf()
    var cardReferenceid: MutableState<Int?> = mutableStateOf(null)

    fun miniStatement(onSuccess:(MiniStatementResponse)-> Unit) {
        viewModelScope.launch {
            val cardRefId =
                cardReferenceid.value ?: dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val request = MiniStatementRequest(
                cardRefNumber = cardRefId.toString(),
                latLong = latLong,
                channel = "ANDROID",
                deviceId = deviceId
            )

            handleFlow(
                apiCall = {miniStatementApiUseCase.invoke(request = request)},
                dispatcher = Dispatchers.IO,
                scope = viewModelScope,
                onSuccesss = {resp->
                    statementData.clear()
                    if (resp?.statusCode == 0) {
                        onSuccess.invoke(resp)
                        if(resp.data!= null){

                            statementData.addAll(resp.data.statements?.map {
                                TransactionDeatils(

                                    transactionType = if (it?.transactionType.toString().lowercase()
                                            .contains("credit")
                                    ) {
                                        TransactionType.CREDIT
                                    } else {
                                        TransactionType.DEBIT
                                    },
                                    fontColor = if (it?.isCredit == true) TextGreen else CardRed,
                                    amount = it?.amount.toString(),
                                    date = it?.date.toString(),
                                    balance = resp.data.balance.toString()
                                )
                            } ?: emptyList())
                            viewModelScope.launch {
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.SuccessSnackBar,
                                        UiText.DynamicString(resp.statusDesc.toString())
                                    )
                                )
                            }
                        }else{
                            viewModelScope.launch {
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.ErrorSnackBar,
                                        UiText.DynamicString(resp.statusDesc.toString())
                                    )
                                )
                            }
                        }
                    } else {
                        viewModelScope.launch {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.ErrorSnackBar,
                                    UiText.DynamicString(resp?.statusDesc.toString())
                                )
                            )
                        }
                    }
                }
            )
        }
    }

    fun detailedStatement(startDate: String, endDate: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val userNam =
                "CUST" + dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val cardReferenceNumbr =
                dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val request = DetailedStatementRequest(
                start_date = startDate,
                end_date = endDate,

                cardReference = cardReferenceNumbr.toString(),
                username = userNam,

            )
            detailStatementData.clear()
            handleFlow(
                apiCall = {
                    detailedStatementApiUseCase.invoke(request = request)
                },
                dispatcher = Dispatchers.IO,
                scope = viewModelScope,
                onSuccesss = {

                    if (it?.status == 1) {
                        viewModelScope.launch {

                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.DynamicString(
                                        it.message ?: ""
                                    )
                                )
                            )
                            detailStatementData.addAll(
                                it.results?.map {
                                    TransactionDeatils(
                                        id = it?.transactionId.toString(),
                                        transactionType = if (it?.type.toString().lowercase()
                                                .contains("credit")
                                        ) {
                                            TransactionType.CREDIT
                                        } else {
                                            TransactionType.DEBIT
                                        },
                                        fontColor = if (it?.type.toString().lowercase()
                                                .contains("credit")
                                        ) {
                                            TextGreen
                                        } else {
                                            CardRed
                                        },
                                        amount = it?.amount.toString(),
                                        date = ZonedDateFormatter.format(it?.createdDate?.value.toString())
                                            .toString(),
                                        balance = it?.closingbalance.toString(),
                                        transactionMode = it?.transactionMode.toString(),
                                        rrn = it?.rrn.toString()
                                    )
                                } ?: emptyList()
                            )
                            onSuccess()
                        }
                    } else {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        it?.message ?:"Something went wrong"
                                    )
                                )
                            )
                        }
                    }
                }
            )
        }
    }

    fun saveTransactionDataInDataStore() {
        viewModelScope.launch {
            if (selectedDetailedTrasnaction.value != null) {
                dataStoreManager.savePreferences(
                    listOf(
                        PreferenceData(
                            PreferencesKeys.DISPUTED_TRANSACTION_ID,
                            selectedDetailedTrasnaction.value!!.id
                        )
                    )
                )
            }

        }
    }

    fun createPdf(context:Context,onSuccess: () -> Unit) {
        viewModelScope.launch {
            val stateMentList = detailStatementData.mapIndexed { ind, it ->
                val list = mutableListOf<String>()
                list.add("${ind + 1}")
                list.add(convertDate(it.date.toString()).toString())
                list.add(it.rrn)
                list.add(it.transactionMode)
                list.add(it.amount)
                list.add("Cr")
                list.add(it.balance)

                return@mapIndexed list
            }

            createPdfWithTableAndBorders(
                context = context,
                list = stateMentList,
                data = PDFData(
                    cardNumber = dataStoreManager.getPreferenceValue(PreferencesKeys.CARDNUMBER)
                        .toString(),
                    username = dataStoreManager.getPreferenceValue(PreferencesKeys.USERNAME)
                        .toString(),
                    stateMentPeriod = startDate.value + " to " + endDate.value,
                    cardStatus = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_STATUS)
                        .toString(),
                    cardExpiry = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_EXPIRY)
                        .toString(),
                    mobileNumber = dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
                        .toString(),
                )
            ) {
                uri.value = it
                onSuccess()


            }
        }
    }


}




