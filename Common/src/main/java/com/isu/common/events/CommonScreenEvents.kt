package com.isu.common.events

import com.isu.common.navigation.Screen
import com.isu.common.utils.datastore.PreferenceData

/**
 * Common screen events
 *  common screen events that a user can perform
 * @constructor Create empty Common screen events
 */
sealed interface CommonScreenEvents {
    data class OnTextChanged(val text: String, val type: CommonTextField) : CommonScreenEvents
    data class OnClick<T>(val type: Clickables,val additionData:T?=null,val onComplete:()->Unit={}) : CommonScreenEvents
    data class OnCheckChanged(val isChecked:Boolean,val type: CommonCheckBoxType): CommonScreenEvents
    data object ClearFields: CommonScreenEvents
    data object ClearStack : CommonScreenEvents
    data class CallApi<T>(val apiType: APIType,val additionalInfo: T?=null,val onSuccess:()->Unit,) : CommonScreenEvents
    data class NavigateTo(val screen: Screen) : CommonScreenEvents
    data class SaveToDataStore<T>(val preferenceData: PreferenceData<T>) : CommonScreenEvents
    data class GetDataStoreData<T>(val preferenceData: androidx.datastore.preferences.core.Preferences.Key<T>) :
        CommonScreenEvents


}
interface CommonTextField
interface Clickables
interface CommonCheckBoxType
interface APIType