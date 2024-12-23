package com.isu.prepaidcard.presentation.viewmodels

import CardCvvDetails
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.R
import com.isu.common.events.CommingSoonEvent
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.utils.CircularBoxLayout
import com.isu.common.utils.ManageFeatures
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferenceData
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.data.request.AuthTokenData
import com.isu.prepaidcard.data.request.ChangeMccRequestBody
import com.isu.prepaidcard.data.request.RestrictMccWrapperRequestsItem
import com.isu.prepaidcard.data.request.SetPrimaryRequest
import com.isu.prepaidcard.data.request.ViewCardCvvRequest
import com.isu.prepaidcard.data.request.ViewCardDataByCardRefNumberRequest
import com.isu.prepaidcard.data.request.ViewCardDataByMobileNumber
import com.isu.prepaidcard.data.response.CardBalanceState

import com.isu.prepaidcard.data.response.CardDataItem
import com.isu.prepaidcard.data.response.ChangeMccResponseBody
import com.isu.prepaidcard.data.response.ViewCardBalanceResponse
import com.isu.prepaidcard.data.response.ViewCardCvvResponse
import com.isu.prepaidcard.data.response.ViewCardDataByMobileNumberResponse
import com.isu.prepaidcard.data.response.ViewCardDataByRefIdResponse
import com.isu.prepaidcard.domain.models.res.CARD_STATUS
import com.isu.prepaidcard.domain.usecase.SetPrimaryUseCase
import com.isu.prepaidcard.domain.usecase.balance_api_usecase.ViewCardBalanceUseCase
import com.isu.prepaidcard.domain.usecase.carddata_api_usecase.ViewCardDataByMobNoApiUseCase
import com.isu.prepaidcard.domain.usecase.carddata_api_usecase.ViewCardDataByRefIdUsecase
import com.isu.prepaidcard.domain.usecase.mccstatus_apiusecase.MccStatusApiUseCase
import com.isu.prepaidcard.domain.usecase.viewcvv_api_usecase.ViewCardCvvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

/**
 * This ViewModel class manages data and logic for the dashboard screen.
 *
 * @HiltViewModel indicates that this class is a ViewModel and leverages Hilt for dependency injection.
 */

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dataStore: DataStoreManager,
    private val viewCardDataByMobNoApiUseCase: ViewCardDataByMobNoApiUseCase,
    private val viewCardDataByRefIdUseCase: ViewCardDataByRefIdUsecase,
    private val viewCardBalanceUseCase: ViewCardBalanceUseCase,
    private val viewCardCvvUseCase: ViewCardCvvUseCase,
    private val setPrimaryUseCase: SetPrimaryUseCase,
    private val changeMccStatusUseCase: MccStatusApiUseCase
) : ViewModel() {
    val mutableHashSet: SnapshotStateList<String> =
        mutableStateListOf()

    val selectedMccCategory: MutableState<String> = mutableStateOf("")
    val userName: MutableState<String> = mutableStateOf("")

    val mccCategoryCodeMap =
        mutableMapOf<String, List<ViewCardDataByRefIdResponse.Data.MccDetail?>>()
    val mccNameCodeMap =
        mutableMapOf<String, String>()
    val mccCodes = hashSetOf<String>()
    val mcclist = mutableStateMapOf<String, Boolean>()
    val mcclistRequest = mutableStateMapOf<String, Boolean>()
    var cardRefid: MutableState<Int?> = mutableStateOf(null)

    // UI State variables
    val showCardAccess = mutableStateOf(true)
    val isChildCustomer = mutableStateOf(false)
    val showTransaction = mutableStateOf(false)
    val showAdditionalOption = mutableStateOf(false)
    val showMccDialog = mutableStateOf(false)
    val deActivate = mutableStateOf(false)
    val manageNFC = mutableStateOf(false)
    val currentPage = mutableStateOf(0)


    // StateFlow for card details and balance with initial values
//    private val _cardDashBoardDetails = MutableStateFlow(CardDashBoardDetails())
//    val cardDashBoardDetails: StateFlow<CardDashBoardDetails> = _cardDashBoardDetails
    // StateFlow for card details and balance with initial values


    private val _cardCvvDetails = MutableStateFlow(CardCvvDetails())
    val cardCvvDetails: StateFlow<CardCvvDetails> = _cardCvvDetails

    private val _cardBalanceState = MutableStateFlow(CardBalanceState())
    val cardBalanceState: StateFlow<CardBalanceState> = _cardBalanceState

    val currentcardData: MutableState<ViewCardDataByRefIdResponse.Data?> = mutableStateOf(null)
    val cardStatus = mutableStateOf("")
    val newCardRefId = mutableStateOf("")

    // List of card data items (mutable)
    private val _cardList = mutableStateListOf<CardDataItem?>()
    val cardList
        get() = _cardList as List<CardDataItem?>
    val listOfCardReferenceId = mutableStateListOf<BigInteger>()

    // Pre-defined lists for card access and transaction options
    val cardAccess = listOf(
        CircularBoxLayout(
            icon = R.drawable.replacement,
            iconSize = 20.dp, text = ManageFeatures.RE_ISSUANCE
        ),
        CircularBoxLayout(
            icon = R.drawable.statement,
            iconSize = 20.dp,
            text = ManageFeatures.KIT_TO_KIT
        ),
        CircularBoxLayout(
            icon = R.drawable.order_physical,
            iconSize = 20.dp,
            text = ManageFeatures.CARD_STATUS
        ),
        CircularBoxLayout(
            icon = R.drawable.modify_pin,
            iconSize = 20.dp,
            text = ManageFeatures.MODIFY_PIN
        )

    )
    val cardAccessII = listOf(


        CircularBoxLayout(
            icon = R.drawable.mcc,
            iconSize = 20.dp,
            text = ManageFeatures.MCC
        ),
        CircularBoxLayout(
            icon = R.drawable.transaction_history,
            iconSize = 20.dp,
            text = ManageFeatures.TRANSACTION_SETTINGS
        )
    )
    val cardAccessIII = listOf(
        CircularBoxLayout(
            icon = R.drawable.order_physical,
            iconSize = 20.dp,
            text = ManageFeatures.ORDER_PHYSICAL
        )
    )

    val transaction = listOf(
        CircularBoxLayout(
            icon = R.drawable.load_card,
            iconSize = 20.dp,
            text = ManageFeatures.LOAD_CARD
        ),
        CircularBoxLayout(
            icon = R.drawable.statement,
            iconSize = 20.dp,
            text = ManageFeatures.MINI_STATEMENT
        ),
        CircularBoxLayout(
            icon = R.drawable.statement,
            iconSize = 20.dp,
            text = ManageFeatures.DETAILED_STATEMENT
        ),
//        CircularBoxLayout(
//            icon = R.drawable.analysis,
//            iconSize =20.dp,
//            text = ManageFeatures.SPEND_ANALYTICS
//        ),
    )
    val transactionII: List<CircularBoxLayout> = emptyList()
//
//        CircularBoxLayout(
//            icon = R.drawable.beneficiary,
//            iconSize = 30.dp,
//            text = ManageFeatures.BENEFICIARY
//        ),
//        CircularBoxLayout(
//            icon = R.drawable.mandate,
//            iconSize = 30.dp,
//            text = ManageFeatures.MANDATES
//        ),
//
//        CircularBoxLayout(
//            icon = R.drawable.tokens,
//            iconSize = 30.dp,
//            text = ManageFeatures.TOKENS
//        )



    // Function to handle clicks on feature options
    fun handleFeatureClick(features: String) {
        // Handle navigation and actions based on the selected feature
        when (features) {
            "Complete your KYC" -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.CompleteKYC)
                }
            }

            ManageFeatures.PAY -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.PaymentModeSelectionScreen)
                }
            }

            ManageFeatures.OFFERS -> {
                viewModelScope.launch {
                    CommingSoonEvent.showCommingSoon()
                }
            }

            ManageFeatures.LINK_CARD -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.LinkCard)
                }
            }

            ManageFeatures.ORDER_PHYSICAL -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.OrderPhyicalCardSelectScreen)
                }
            }

            ManageFeatures.REQUEST_CARD -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.OrderPhyicalCardSelectScreen)
                }
            }

            ManageFeatures.RE_ISSUANCE -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.CardReissuance)
                }
            }

            ManageFeatures.CARD_STATUS -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(
                        CardManagement.CardStatus
                    )
                }
            }

            ManageFeatures.MODIFY_PIN -> {
//                manageNFC.value = true
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.ModifyPinScreen)
                }
            }

            ManageFeatures.MCC -> {
                viewModelScope.launch {

                    NavigationEvent.helper.navigateTo(CardManagement.MCCCategoryScreen)
                }
            }

            ManageFeatures.ORDER_HISTORY -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.OrderHistory)
                }
            }

            ManageFeatures.LOAD_CARD -> {
                if (cardRefid.value != null) {
                    viewModelScope.launch {
                        dataStore.savePreferences(
                            listOf(
                                PreferenceData(
                                    key = PreferencesKeys.CARD_REF_ID,
                                    cardRefid.value!!
                                )
                            )
                        )
                    }

                }

                viewModelScope.launch { NavigationEvent.helper.navigateTo(CardManagement.LoadCardScreen) }
            }

            ManageFeatures.MANDATES -> {
                viewModelScope.launch { NavigationEvent.helper.navigateTo(CardManagement.Mandate) }
            }

            ManageFeatures.TRANSACTION_SETTINGS -> {
                viewModelScope.launch { NavigationEvent.helper.navigateTo(CardManagement.TransactionSettings) }
            }

//            ManageFeatures.TOKENS -> {
//                viewModelScope.launch {
//                    NavigationEvent.helper.navigateTo(CardManagement.Tokens)
//                }
//            }

            ManageFeatures.SPEND_ANALYTICS -> {
                viewModelScope.launch {
                    CommingSoonEvent.showCommingSoon()
                }
            }

            ManageFeatures.BENEFICIARY -> {
                viewModelScope.launch {
                    CommingSoonEvent.showCommingSoon()
                }
            }

            ManageFeatures.MINI_STATEMENT -> {
                viewModelScope.launch { NavigationEvent.helper.navigateTo(CardManagement.Statement) }
            }

            ManageFeatures.DETAILED_STATEMENT -> {
                viewModelScope.launch { NavigationEvent.helper.navigateTo(CardManagement.DetailedStatement) }
            }

            ManageFeatures.KIT_TO_KIT -> {
                viewModelScope.launch {
                    NavigationEvent.helper.navigateTo(CardManagement.KitToKitTransfer)
                }
            }
        }
    }

    // Suspend function to fetch card data by mobile number with loading and error handling
    suspend fun getCardDataByMobileNo(
        onSuccess: (ViewCardDataByMobileNumberResponse) -> Unit,
    ) {

        val authToken = dataStore.getPreferenceValue(PreferencesKeys.AUTH_TOKEN)
        val userName = dataStore.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
        viewModelScope.launch {
            val deviceId = dataStore.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val mobileNumber = dataStore.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)
            val latLong = LatLongFlowProvider.latLongFlow.first()
            isChildCustomer.value =
                dataStore.getPreferenceValue(PreferencesKeys.IS_CHILD_CUSTOMER) == true
            handleFlow(
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                apiCall = {
                    val request = ViewCardDataByMobileNumber(
                        deviceId = deviceId, latLong = latLong,

                    )
                    viewCardDataByMobNoApiUseCase.invoke(
                        request = request
                    )
                }
            ) {
                onLoading {
                    viewModelScope.launch {
                        LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                    }
                }
                onFailure { msg, _, _, resp ->
                    viewModelScope.launch {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.ErrorSnackBar,
                                UiText.DynamicString(resp?.statusDesc ?: "Something went wrong")
                            )
                        )
                    }
                }
                onSuccess { resp ->
                    viewModelScope.launch {
                        if (resp?.statusCode == 0) {
                            onSuccess.invoke(resp)
                            if (resp.data != null) {
                                Log.d("response", "getCardDataByMobileNo:${resp} ")

                                if (cardList.isNotEmpty()) {
                                    _cardList.clear()
                                }
                                resp.data.data.let {
                                    Log.d("RESPONSE", "getCardDataByMobileNo:$it ")
                                    it?.map {
                                        Log.d("RESPONSE", "getCardDataByMobileNo:$it ")
                                        CardDataItem(
                                            expiryDate = it.expiryDate,
                                            decrypted = it.decrypted,
                                            addOnStatus = it.addOnStatus,
                                            nameOnCard = it.nameOnCard,
                                            childCard = it.childCard,
                                            cardRefId = it.cardRefId,
                                            isActive = it.isActive,
                                            isPrimary = it.isPrimary,
                                            isReissued = it.isReissued,
                                            productCategory = it.productCategory,

                                            isNew = listOfCardReferenceId.isNotEmpty() && !listOfCardReferenceId.contains(
                                                it.cardRefId?.toBigInteger() ?: 0
                                            )

                                        )
                                    }
                                        ?.let { it1 -> _cardList.addAll(it1.filter { it.isPrimary == true }) }
                                    it?.map {
                                        if (listOfCardReferenceId.isNotEmpty() && !listOfCardReferenceId.contains(
                                                (it.cardRefId ?: 0).toBigInteger()
                                            )
                                        ) {
                                            newCardRefId.value = it.cardRefId.toString()
                                        }
                                        Log.d("RESPONSE", "getCardDataByMobileNo:$it ")
                                        CardDataItem(
                                            expiryDate = it.expiryDate,
                                            decrypted = it.decrypted,
                                            addOnStatus = it.addOnStatus,
                                            nameOnCard = it.nameOnCard,
                                            childCard = it.childCard,
                                            cardRefId = it.cardRefId,
                                            isActive = it.isActive,
                                            isPrimary = it.isPrimary,
                                            isReissued = it.isReissued,
                                            productCategory = it.productCategory,
                                            isNew = listOfCardReferenceId.isNotEmpty() && !listOfCardReferenceId.contains(
                                                (it.cardRefId ?: 0).toBigInteger()
                                            )

                                        )

                                    }
                                        ?.let { it1 -> _cardList.addAll(it1.filter { it.isPrimary != true }) }

                                }
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.SuccessSnackBar,
                                        UiText.DynamicString("Card Data fetched successfully")
                                    )
                                )
                                listOfCardReferenceId.clear()
                                val newList =
                                    resp.data.data?.map { (it.cardRefId ?: 0).toBigInteger() }
                                listOfCardReferenceId.addAll(newList ?: emptyList())
                            }
//                        _cardDashBoardDetails.update {
//                            it.copy(requireCardDataFetching = false)
//                        }
                        } else {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.ErrorSnackBar,
                                    UiText.DynamicString(resp?.statusDesc.toString())
                                )
                            )
                        }
                    }
                }
            }
        }

    }

    // Suspend function which fetches card data by reference ID
    fun getCardDataByRefId(
        cardReferenceId: Int? = null,
        onSuccess: (ViewCardDataByRefIdResponse.Data?) -> Unit,
    ) {

        try {
            viewModelScope.launch {
                val cardRefId =
                    cardRefid.value ?: dataStore.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
                val deviceId = dataStore.getPreferenceValue(PreferencesKeys.ANDROID_ID)
                val latLong = LatLongFlowProvider.latLongFlow.first()
                val channel = "ANDROID"

                handleFlow(
                    scope = viewModelScope,
                    dispatcher = Dispatchers.IO,
                    apiCall = {
                        viewCardDataByRefIdUseCase.invoke(
                            request = AuthTokenData(
                                authorization = "",
                                tokenProperties = "",
                                request = ViewCardDataByCardRefNumberRequest(
                                    cardRefNo = cardRefId.toString(),
                                    deviceId = deviceId,
                                    latLong = latLong
                                )
                            )
                        )
                    }
                ) {
                    onLoading {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                        }
                    }
                    onSuccess { resp ->

                        mccCategoryCodeMap.clear()
                        mcclist.clear()
                        mcclistRequest.clear()
                        viewModelScope.launch {
                            dataStore.savePreferences(
                                listOf(
                                    PreferenceData(
                                        key = PreferencesKeys.CARD_EXPIRY,
                                        resp?.data?.expiryDate.toString()
                                    )
                                )
                            )

                            if (resp?.statusCode == 0) {

                                val newResp = resp.data?.copy(
                                    isNew = listOfCardReferenceId.isNotEmpty() && !listOfCardReferenceId.contains(
                                        (resp.data.cardRefId ?: 0).toBigInteger()
                                    )
                                )
                                Log.d("MCC", "getCardDataByRefId:${newResp?.mccDetails} ")
                                newResp?.mccDetails?.forEach {
                                    mccNameCodeMap.put(
                                        it?.mccCode.toString(),
                                        it?.mccName.toString()
                                    )
                                    val list =
                                        mutableListOf<ViewCardDataByRefIdResponse.Data.MccDetail?>()
                                    if (mccCategoryCodeMap.contains(it?.mccCategory)) {

                                        list.addAll(
                                            mccCategoryCodeMap.get(it?.mccCategory) ?: emptyList()
                                        )
                                        list.add(it)
                                        mccCategoryCodeMap.put(it?.mccCategory ?: "", list)
                                    } else {

                                        list.add(it)
                                        mccCategoryCodeMap.put(it?.mccCategory ?: "", list)
                                    }
                                    mcclist.put(it?.mccCode.toString(), it?.applicable == true)

                                }
                                Log.d("MCC", "getCardDataByRefId:${mccCategoryCodeMap} ")
                                onSuccess.invoke(newResp)
                                currentcardData.value = newResp

                                if (resp.data != null) {
                                    if (resp.data.isDamage == true) {
                                        dataStore.savePreferences(
                                            listOf(
                                                PreferenceData(
                                                    key = PreferencesKeys.CARD_STATUS,
                                                    CARD_STATUS.isDamaged
                                                )
                                            )
                                        )
                                        cardStatus.value = CARD_STATUS.isDamaged
                                    } else if (resp.data.isLost == true) {
                                        dataStore.savePreferences(
                                            listOf(
                                                PreferenceData(
                                                    key = PreferencesKeys.CARD_STATUS,
                                                    CARD_STATUS.isLost
                                                )
                                            )
                                        )
                                        cardStatus.value = CARD_STATUS.isLost
                                    } else if (resp.data.isBlock == true) {
                                        dataStore.savePreferences(
                                            listOf(
                                                PreferenceData(
                                                    key = PreferencesKeys.CARD_STATUS,
                                                    CARD_STATUS.isBlocked
                                                )
                                            )
                                        )
                                        cardStatus.value = CARD_STATUS.isBlocked
                                    } else if (resp.data.isActive == true) {
                                        dataStore.savePreferences(
                                            listOf(
                                                PreferenceData(
                                                    key = PreferencesKeys.CARD_STATUS,
                                                    CARD_STATUS.isActive
                                                )
                                            )
                                        )
                                        cardStatus.value = CARD_STATUS.isActive
                                    }
                                }

                                if (resp.data != null) {
                                    ShowSnackBarEvent.helper.emit(
                                        ShowSnackBarEvent.show(
                                            SnackBarType.SuccessSnackBar,
                                            UiText.DynamicString("Card services fetched successfully")
                                        )
                                    )
                                }


                            } else {
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.ErrorSnackBar,
                                        UiText.DynamicString(resp?.statusDesc.toString())
                                    )
                                )
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.stackTrace
        }
    }

    suspend fun viewCardCvv(
        cardReferenceId: Int? = null,
        onSuccess: (ViewCardCvvResponse) -> Unit,
    ) {
        val devicId = dataStore.getPreferenceValue(PreferencesKeys.ANDROID_ID)
        val latLong = LatLongFlowProvider.latLongFlow.first()
        val userName = dataStore.getPreferenceValue(PreferencesKeys.USER_CREDENTIAL)
        val cardRefId = cardReferenceId ?: dataStore.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
        val request = ViewCardCvvRequest(
            cardRefNo = cardReferenceId.toString(),
            deviceId = devicId,
            latLong = latLong

        )
        handleFlow(
            scope = viewModelScope,
            apiCall = {
                viewCardCvvUseCase.invoke(request = request)
            },
            dispatcher = Dispatchers.IO

        ) {
            onSuccess { resp ->
                viewModelScope.launch {
                    if (resp?.statusCode == 0) {
                        onSuccess.invoke(resp)
                        if (resp.data != null) {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.DynamicString("Card CVV Fetched Successfully")
                                )
                            )
                        }
                        _cardCvvDetails.update {
                            it.copy(requireCvvFetching = false)
                        }
                    } else {
                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.ErrorSnackBar,
                                UiText.DynamicString(resp?.statusDesc.toString())
                            )
                        )
                    }
                }
            }
            onLoading {
                viewModelScope.launch {
                    LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                }
            }
            onFailure { msg, i, s, viewCardCvvResponse ->

                viewModelScope.launch {
                    ShowSnackBarEvent.helper.emit(
                        ShowSnackBarEvent.show(
                            SnackBarType.ErrorSnackBar,
                            UiText.DynamicString(msg)
                        )
                    )
                }
            }
        }
    }

    fun changeMccStatus(
        cardRefId: Int? = null,
        enableAll: Boolean? = null,
        diableAll: Boolean? = null,
        onSuccess: (ChangeMccResponseBody) -> Unit = {},
        onError: () -> Unit = {},
    ) {
        Log.d("MCC", "changeMccStatus: ${mcclist}")
        viewModelScope.launch {
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val deviceId = dataStore.getPreferenceValue(PreferencesKeys.ANDROID_ID)

        val request = ChangeMccRequestBody(
            cardRefNumber = cardRefid.value,
            deviceId = deviceId,
            latLong = latLong,
            restrictMccWrapperRequests = if (enableAll == true) {
                mccCategoryCodeMap.get(selectedMccCategory.value)
                    ?.filter { it?.applicable == false }?.map {
                    RestrictMccWrapperRequestsItem(
                        mccCode = it?.mccCode.toString(), isActive = true
                    )
                }

            } else if (diableAll == true) {
                mccCategoryCodeMap.get(selectedMccCategory.value)?.filter { it?.applicable == true }
                    ?.map {
                    RestrictMccWrapperRequestsItem(
                        mccCode = it?.mccCode.toString(), isActive = false

                    )
                }
            } else {
                mcclistRequest.map {
                    if (mcclist.get(it.key) != it.value) {
                        RestrictMccWrapperRequestsItem(
                            mccCode = it.key, isActive = it.value
                        )
                    } else {
                        null
                    }

                }.filter { it != null }
            }

        )
        handleFlow(
            scope = viewModelScope,
            dispatcher = Dispatchers.IO,
            apiCall = {
                changeMccStatusUseCase.invoke(
                    request = request
                )
            }
        ) {
            onLoading {
                viewModelScope.launch {
                    LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                }
            }
            onFailure { msg, _, _, resp ->
                viewModelScope.launch {
                    LoadingErrorEvent.helper.emit(
                        LoadingErrorEvent.errorEncountered(
                            UiText.DynamicString(
                                msg
                            )
                        )
                    )
                }
                onError()
            }
            onSuccess { resp ->
                viewModelScope.launch {
                    if (resp?.statusCode == 0) {
                        onSuccess.invoke(resp)
                        mcclistRequest.clear()

                        ShowSnackBarEvent.helper.emit(
                            ShowSnackBarEvent.show(
                                SnackBarType.SuccessSnackBar,
                                UiText.DynamicString("Mcc Status updated successfully")
                            )
                        )
                    } else {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        resp?.statusDesc ?: "Something went wrong"
                                    )
                                )
                            )
                        }
                        onError()
                    }
                }
            }
        }
        }
    }

            // Suspend function to fetch card balance with loading and error handling
            suspend fun getCardBalance(
                cardReferenceId: Int? = null, onSuccess: (ViewCardBalanceResponse) -> Unit,
            ) {
                val deviceId = dataStore.getPreferenceValue(PreferencesKeys.ANDROID_ID)
                val latLong = LatLongFlowProvider.latLongFlow.first()

                val request = ViewCardCvvRequest(
                    cardRefNo = cardReferenceId.toString(), deviceId = deviceId, latLong = latLong

                )
                handleFlow(
                    scope = viewModelScope,
                    dispatcher = Dispatchers.IO,
                    apiCall = {
                        viewCardBalanceUseCase.invoke(
                            request = request
                        )
                    }
                ) {
                    onLoading {
                        viewModelScope.launch {
                            LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
                        }
                    }
                    onFailure { msg, _, _, resp ->
                        viewModelScope.launch {
                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.ErrorSnackBar,
                                    UiText.DynamicString(
                                        resp?.statusDesc ?: "Somenthing Went Wrong"
                                    )
                                )
                            )
                        }
                    }
                    onSuccess { resp ->
                        viewModelScope.launch {
                            if (resp?.statusCode == 0) {
                                onSuccess.invoke(resp)
                                if (resp.data != null) {
                                    ShowSnackBarEvent.helper.emit(
                                        ShowSnackBarEvent.show(
                                            SnackBarType.SuccessSnackBar,
                                            UiText.DynamicString("Balance fetched successfully")
                                        )
                                    )
                                }
                                _cardBalanceState.update {
                                    it.copy(requireCardBalanceFetching = false)
                                }
                            } else {
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(
                                        SnackBarType.ErrorSnackBar,
                                        UiText.DynamicString(resp?.statusDesc.toString())
                                    )
                                )
                            }
                        }
                    }
                }
            }

            fun onCardClick(cardRefId: Int?) {
                viewModelScope.launch {
                    dataStore.savePreferences(
                        listOf(
                            PreferenceData(
                                key = PreferencesKeys.CARD_REF_ID,
                                value = cardRefId ?: 0
                            )
                        )
                    )
                }
            }

    fun saveToPreference(data: ViewCardDataByRefIdResponse.Data?) {
                viewModelScope.launch {
                    dataStore.savePreferences(
                        preferencesList = listOf(
                            PreferenceData(
                                PreferencesKeys.USERNAME, data?.nameOnCard ?: ""
                            ),
                            PreferenceData(
                                PreferencesKeys.CARD_REF_ID, cardRefid.value ?: 0
                            ),
                            PreferenceData(
                                PreferencesKeys.CARDNUMBER, data?.encryptedCard ?: ""
                            ),
                            PreferenceData(
                                PreferencesKeys.ACTIVE_CARDSTATUS, data?.isActive ?: false
                            ),
                            PreferenceData(
                                PreferencesKeys.BLOCK_CARDSTATUS, data?.isActive ?: false
                            )
                        )
                    )
                }
            }

    fun setPrimary(cardRefId: String, onSuccess: () -> Unit) {
                viewModelScope.launch {
                    val deviceId = dataStore.getPreferenceValue(PreferencesKeys.ANDROID_ID)
                    val latLng = LatLongFlowProvider.latLongFlow.first()
                    val username =
                        "CUST${dataStore.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)}"
                    val request = SetPrimaryRequest(
                        cardRefNo = cardRefId,
                        deviceId = deviceId,
                        latLong = latLng,

                    )
                    handleFlow(
                        apiCall = {
                            setPrimaryUseCase.invoke(request)
                        },
                        scope = viewModelScope,
                        dispatcher = Dispatchers.IO,
                        onSuccesss = {
                            if (it?.statusCode == 0) {
                                viewModelScope.launch {
                                    ShowSnackBarEvent.helper.emit(
                                        ShowSnackBarEvent.show(
                                            SnackBarType.SuccessSnackBar,
                                            UiText.DynamicString(
                                                it.statusDesc ?: "Primary Card Set Successfully"
                                            )
                                        )
                                    )
                                    getCardDataByMobileNo {
                                        onSuccess()
                                    }

                                }


                            } else {
                                viewModelScope.launch {
                                    ShowSnackBarEvent.helper.emit(
                                        ShowSnackBarEvent.show(
                                            SnackBarType.ErrorSnackBar,
                                            UiText.DynamicString(it?.statusDesc.toString())
                                        )
                                    )
                                }
                            }
                        }

                    )
                }

            }

    fun getSavedData() {
        viewModelScope.launch {
            userName.value = dataStore.getPreferenceValue(PreferencesKeys.USERNAME).toString()
        }
    }
}
