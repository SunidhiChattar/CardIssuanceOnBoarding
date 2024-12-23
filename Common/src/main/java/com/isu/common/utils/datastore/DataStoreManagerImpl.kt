package com.isu.common.utils.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.isu.common.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Data store manager impl
 *
 * @property context
 * @constructor Create empty Data store manager impl
 */
class DataStoreManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) :
    DataStoreManager {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")


    private suspend fun <T> savePreference(key: Preferences.Key<T>, value: Any?) {
        context.dataStore.edit { preferences ->
            when (value) {
                is String -> preferences[key as Preferences.Key<String>] = value
                is Int -> preferences[key as Preferences.Key<Int>] = value
                is Boolean -> preferences[key as Preferences.Key<Boolean>] = value
                is Float -> preferences[key as Preferences.Key<Float>] = value
                is Long -> preferences[key as Preferences.Key<Long>] = value
                else -> throw IllegalArgumentException(context.getString(R.string.unsupported_type))
            }
        }
    }

    override suspend fun savePreferences(preferencesList: List<PreferenceData<*>>) {
        preferencesList.forEach { (key, value) ->
            savePreference(key, value)
        }
    }

    override suspend fun <T> getPreferenceValue(preferenceKey: Preferences.Key<T>): T?{
        return context.dataStore.data.first()[preferenceKey]
    }

    override suspend fun clearDataStore() {
        context.dataStore.edit {
            it.clear()
        }
    }
    override suspend fun <T> clearDataStoreSpecificData(preferenceKey: Preferences.Key<T>) {
        context.dataStore.edit {
            it.remove(preferenceKey)
        }
    }
}




