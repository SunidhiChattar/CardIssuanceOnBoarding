package com.isu.prepaidcard.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.common.R
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.utils.ManageFeatures
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.PreferenceData
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.handleFlow
import com.isu.prepaidcard.data.request.AuthTokenData
import com.isu.prepaidcard.data.request.ChangeCardStatusRequest
import com.isu.prepaidcard.data.request.ViewCardDataByCardRefNumberRequest
import com.isu.prepaidcard.domain.models.res.CARD_STATUS
import com.isu.prepaidcard.domain.usecase.carddata_api_usecase.ViewCardDataByRefIdUsecase
import com.isu.prepaidcard.domain.usecase.changecardstatus_api_usecase.ChangeCardStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardStatusViewModel @Inject constructor(
    private val viewCardDataByRefIDUseCase: ViewCardDataByRefIdUsecase,
    private val dataStoreManager: DataStoreManager,
    private val changeStateusUsecase: ChangeCardStatusUseCase,
) : ViewModel() {

    val requestedStatusChange: MutableState<String> = mutableStateOf("")
    val cardStatus: MutableState<Boolean> = mutableStateOf(false)
    val currentStatusToCheck: MutableState<String> = mutableStateOf("")
    val title: MutableState<Int> = mutableIntStateOf(0)
    val description: MutableState<Int> = mutableIntStateOf(0)
    val confirmTitle: MutableState<Int> = mutableIntStateOf(0)
    val image: MutableState<Int> = mutableIntStateOf(0)


    val cardStatusCheck: MutableList<CurrentStatus> = mutableStateListOf(

    )

    fun dialogStatusCheck(currentStatus: String) {
        when (currentStatus) {
            ManageFeatures.DE_ACTIVATE -> {
                title.value = R.string.deactivate_your_card
                image.value = R.drawable.mandatedialogimage
                description.value =
                    R.string.concerned_about_security_would_you_like_to_deactivate_your_card_to_temporarily_suspend_transactions_and_protect_your_account
                confirmTitle.value = R.string.deactivate
            }

            ManageFeatures.LOST -> {
                title.value = R.string.mark_card_as_lost
                description.value =
                    R.string.worried_about_unauthorized_charges_do_you_want_to_block_your_card_now_for_enhanced_security_and_financial_protection
                image.value = R.drawable.lost_or_stolen
                confirmTitle.value = R.string.mark_as_lost
            }

            ManageFeatures.STOLEN -> {
                title.value = R.string.mark_card_as_stolen
                description.value =
                    R.string.worried_about_unauthorized_charges_do_you_want_to_block_your_card_now_for_enhanced_security_and_financial_protection
                image.value = R.drawable.lost_or_stolen
                confirmTitle.value = R.string.mark_as_stolen
            }

            ManageFeatures.DAMAGE -> {
                title.value = R.string.mark_card_as_damaged
                description.value = R.string.do_you_want_to_mark_your_card_as_damaged
                image.value = R.drawable.damage
                confirmTitle.value = R.string.mark_as_damaged
            }

            ManageFeatures.TEMP_BLOCK -> {
                title.value = R.string.block_your_card
                description.value =
                    R.string.do_you_want_to_block_your_card_now_for_enhanced_security_and_financial_protection
                image.value = R.drawable.block_card_image
                confirmTitle.value = R.string.block
            }
            ManageFeatures.ACTIVE -> {

                title.value = if (currentStatusToCheck.value.lowercase()
                        .contains("inactive".toRegex()) == true
                ) com.isu.prepaidcard.R.string.activate_the_card else com.isu.prepaidcard.R.string.re_activate_the_card

                description.value = if (currentStatusToCheck.value.lowercase()
                        .contains("inactive".toRegex()) == true
                ) com.isu.prepaidcard.R.string.do_you_want_to_activate_your_card else com.isu.prepaidcard.R.string.do_you_want_to_re_activate_your_card



                image.value = R.drawable.activate
                confirmTitle.value = if (currentStatusToCheck.value.lowercase()
                        .contains("inactive".toRegex()) == true
                ) com.isu.prepaidcard.R.string.activate else com.isu.prepaidcard.R.string.re_activate

            }
        }
    }

    fun getStatus(onSuccess: () -> Unit = {}) {
        cardStatusCheck.clear()
        currentStatusToCheck.value = ""
        viewModelScope.launch {
            val cardRef = dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val latLong = LatLongFlowProvider.latLongFlow.value
            val request = ViewCardDataByCardRefNumberRequest(
                cardRefNo = cardRef.toString(),
                deviceId = deviceId.toString(),
                latLong = latLong

            )

            handleFlow(
                apiCall = {
                    viewCardDataByRefIDUseCase.invoke(
                        AuthTokenData(
                            request = request,
                            authorization = "",
                            tokenProperties = "",
                        )
                    )
                },
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
                onSuccesss = { resp ->
                    viewModelScope.launch {

                        if (resp != null) {


                            if (resp.data != null) {
                                if (resp.data.isDamage == true) {
                                    dataStoreManager.savePreferences(
                                        listOf(
                                            PreferenceData(
                                                key = PreferencesKeys.CARD_STATUS,
                                                CARD_STATUS.isDamaged
                                            )
                                        )
                                    )
                                    currentStatusToCheck.value = ManageFeatures.DAMAGE
                                } else if (resp.data.isLost == true) {
                                    dataStoreManager.savePreferences(
                                        listOf(
                                            PreferenceData(
                                                key = PreferencesKeys.CARD_STATUS,
                                                CARD_STATUS.isLost
                                            )
                                        )
                                    )
                                    currentStatusToCheck.value = ManageFeatures.LOST
                                } else if (resp.data.isStolen == true) {
                                    dataStoreManager.savePreferences(
                                        listOf(
                                            PreferenceData(
                                                key = PreferencesKeys.CARD_STATUS,
                                                CARD_STATUS.isStolen
                                            )
                                        )
                                    )
                                    currentStatusToCheck.value = ManageFeatures.STOLEN
                                } else if (resp.data.isHotlist == true) {
                                    dataStoreManager.savePreferences(
                                        listOf(
                                            PreferenceData(
                                                key = PreferencesKeys.CARD_STATUS,
                                                CARD_STATUS.isDamaged
                                            )
                                        )
                                    )
                                    currentStatusToCheck.value = ManageFeatures.DE_ACTIVATE
                                } else if (resp.data.isBlock == true) {
                                    dataStoreManager.savePreferences(
                                        listOf(
                                            PreferenceData(
                                                key = PreferencesKeys.CARD_STATUS,
                                                CARD_STATUS.isBlocked
                                            )
                                        )
                                    )
                                    currentStatusToCheck.value = ManageFeatures.TEMP_BLOCK
                                } else if (resp.data.isActive == true)
                                {
                                    dataStoreManager.savePreferences(
                                        listOf(
                                            PreferenceData(
                                                key = PreferencesKeys.CARD_STATUS,
                                                CARD_STATUS.isActive
                                            )
                                        )
                                    )
                                    currentStatusToCheck.value = ManageFeatures.ACTIVE
                                    addToCardSTatusCheck()


                                } else if (resp.data.isActive == false) {
                                    currentStatusToCheck.value = ManageFeatures.INACTIVE
                                    dataStoreManager.savePreferences(
                                        listOf(
                                            PreferenceData(
                                                key = PreferencesKeys.CARD_STATUS,
                                                CARD_STATUS.isInactive
                                            )
                                        )
                                    )
                                }


                                if (currentStatusToCheck.value == ManageFeatures.DAMAGE || currentStatusToCheck.value == ManageFeatures.DE_ACTIVATE || currentStatusToCheck.value == ManageFeatures.LOST || currentStatusToCheck.value == ManageFeatures.TEMP_BLOCK || currentStatusToCheck.value == ManageFeatures.ACTIVE || currentStatusToCheck.value == ManageFeatures.INACTIVE) {
                                    cardStatusCheck.clear()


                                    if (currentStatusToCheck.value == ManageFeatures.TEMP_BLOCK || currentStatusToCheck.value == ManageFeatures.INACTIVE) {
                                        cardStatusCheck.add(
                                            CurrentStatus(
                                                text = ManageFeatures.ACTIVE,
                                                status = cardStatus.value
                                            )
                                        )
                                    } else if (currentStatusToCheck.value == ManageFeatures.ACTIVE) {
                                        cardStatusCheck.add(
                                            CurrentStatus(
                                                text = ManageFeatures.DE_ACTIVATE,
                                                status = cardStatus.value
                                            )
                                        )
                                        cardStatusCheck.add(
                                            CurrentStatus(
                                                text = ManageFeatures.DAMAGE,
                                                status = cardStatus.value
                                            )
                                        )
                                        cardStatusCheck.add(
                                            CurrentStatus(
                                                text = ManageFeatures.LOST,
                                                status = cardStatus.value
                                            )
                                        )
                                        cardStatusCheck.add(
                                            CurrentStatus(
                                                text = ManageFeatures.STOLEN,
                                                status = cardStatus.value
                                            )
                                        )
                                        cardStatusCheck.add(
                                            CurrentStatus(
                                                text = ManageFeatures.TEMP_BLOCK,
                                                status = cardStatus.value
                                            )
                                        )
                                        cardStatusCheck.forEach {
                                            if (it.text == ManageFeatures.ACTIVE) {
                                                cardStatusCheck.remove(it)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
            )
        }
    }

    private fun addToCardSTatusCheck() {
        val listOfStatus = listOf(
            CurrentStatus(
                text = ManageFeatures.DE_ACTIVATE,
                status = cardStatus.value
            ),
            CurrentStatus(
                text = ManageFeatures.TEMP_BLOCK,
                status = cardStatus.value
            ),
            CurrentStatus(
                text = ManageFeatures.LOST,
                status = cardStatus.value
            ),
            CurrentStatus(
                text = ManageFeatures.DAMAGE,
                status = cardStatus.value
            ),
        )
        listOfStatus.forEach {
            if (!cardStatusCheck.contains(it)) {
                cardStatusCheck.add((it))
            }
        }

    }

    fun changeCardStatus(requestedStatus: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val cardREdfNumber =
                dataStoreManager.getPreferenceValue(PreferencesKeys.CARD_REF_ID)
            val deviceId = dataStoreManager.getPreferenceValue(PreferencesKeys.ANDROID_ID)
            val username =
                "CUST${dataStoreManager.getPreferenceValue(PreferencesKeys.USER_MOBILE_NUMBER)}"
            val latLong = LatLongFlowProvider.latLongFlow.first()
            val reques = ChangeCardStatusRequest(
                cardRefNumber = cardREdfNumber.toString(),
                channel = "ANDROID",
                deviceId = deviceId,
                latLong = latLong,
                requestedStatus = requestedStatus,
                shippingAddress1 = null,
                shippingAddress2 = null,
                shippingCity = null,
                shippingCountry = null,
                shippingPinCode = null,
                shippingState = null

            )
            handleFlow(
                apiCall = {
                    changeStateusUsecase.invoke(reques)
                }, scope = viewModelScope, dispatcher = Dispatchers.IO, onSuccesss = {
                    viewModelScope.launch {
                        if (it?.statusCode == 0) {

                            ShowSnackBarEvent.helper.emit(
                                ShowSnackBarEvent.show(
                                    SnackBarType.SuccessSnackBar,
                                    UiText.DynamicString(it.statusDesc ?: "Success")
                                )
                            )
                            getStatus()
                        } else {
                            LoadingErrorEvent.helper.emit(
                                LoadingErrorEvent.errorEncountered(
                                    UiText.DynamicString(
                                        it?.statusDesc ?: "Something went wrong"
                                    )
                                )
                            )
                        }
                    }
                }

            )
        }


    }

    fun handleFeatureClick(features: String, status: Boolean) {
        dialogStatusCheck(features)
        when (features) {
            ManageFeatures.ACTIVE -> {

                if (requestedStatusChange.value == ManageFeatures.ACTIVE)
                    cardStatus.value = status
            }

            ManageFeatures.DE_ACTIVATE -> {
                if (requestedStatusChange.value == ManageFeatures.DE_ACTIVATE)
                    cardStatus.value = status
            }

            ManageFeatures.TEMP_BLOCK -> {
                if (requestedStatusChange.value == ManageFeatures.TEMP_BLOCK)
                    cardStatus.value = status

            }

            ManageFeatures.LOST -> {
                if (requestedStatusChange.value == ManageFeatures.LOST)
                    cardStatus.value = status

            }


            ManageFeatures.STOLEN -> {
                if (requestedStatusChange.value == ManageFeatures.STOLEN)
                    cardStatus.value = status

            }

            ManageFeatures.DAMAGE -> {
                if (requestedStatusChange.value == ManageFeatures.DAMAGE)
                    cardStatus.value = status

            }
        }

    }

}

data class CurrentStatus(
    var text: String,
    var status: Boolean
)