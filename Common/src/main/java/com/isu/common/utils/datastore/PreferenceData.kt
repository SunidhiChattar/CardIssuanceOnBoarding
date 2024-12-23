package com.isu.common.utils.datastore

import androidx.datastore.preferences.core.Preferences


data class PreferenceData<T>(
    val key: Preferences.Key<T>,
    val value: T
)