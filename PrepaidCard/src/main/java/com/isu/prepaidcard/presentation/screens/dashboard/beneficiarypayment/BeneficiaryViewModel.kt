package com.isu.prepaidcard.presentation.screens.dashboard.beneficiarypayment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.isu.common.utils.BeneDetails
import com.isu.common.utils.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class BeneficiaryViewModel @Inject constructor(private val dataStore: DataStoreManager) :
    ViewModel() {
    val beneList = mutableStateListOf<BeneDetails>(
        BeneDetails(),
        BeneDetails(),
        BeneDetails(),
        BeneDetails(),
        BeneDetails(),
        BeneDetails(),
        BeneDetails(),
        BeneDetails(),
        BeneDetails(),
        BeneDetails(),
    )

    val selectedBene: MutableState<BeneDetails?> = mutableStateOf(null)
}