package com.isu.prepaidcard.presentation.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.isu.common.utils.MerchantData
import com.isu.common.utils.merchants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * This ViewModel class manages data related to mandates.
 *
 * @HiltViewModel indicates that this class is a ViewModel and leverages Hilt for dependency injection.
 */
@HiltViewModel
class MandateViewModel @Inject constructor(): ViewModel(){

    // List of active mandates
    val list: SnapshotStateList<MerchantData> = merchants

    // List of canceled mandates
    val canceledList: SnapshotStateList<MerchantData> = mutableStateListOf()
}