package com.isu.common.utils.datastore


import androidx.datastore.preferences.core.Preferences

/**
 * @author-anand
 * @author-karthik
 * Data store manager
 *
 * @constructor Create empty Data store manager
 */
interface DataStoreManager {
    suspend fun savePreferences(preferencesList: List<PreferenceData<*>>)
    suspend fun  <T> getPreferenceValue(preferenceKey: Preferences.Key<T>): T?
    suspend fun clearDataStore()
    suspend fun <T> clearDataStoreSpecificData(preferenceKey: Preferences.Key<T>)

}

