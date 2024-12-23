package com.isu.common.utils.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val CLIENT_SECRET: Preferences.Key<String> = stringPreferencesKey("clientSecret")
    val CLIENT_ID: Preferences.Key<String>  = stringPreferencesKey("clientId")
    val IS_CHILD_CUSTOMER: Preferences.Key<Boolean> = booleanPreferencesKey("isChildCustomer")
    val CARD_EXPIRY: Preferences.Key<String> = stringPreferencesKey("cardExpiry")
    val CARD_STATUS: Preferences.Key<String> = stringPreferencesKey("cardStatus")
    val ACTIVE_CARDSTATUS: Preferences.Key<Boolean> = booleanPreferencesKey("active_status")
    val CARDNUMBER: Preferences.Key<String> = stringPreferencesKey("cardnumber")
    val USERNAME: Preferences.Key<String> = stringPreferencesKey("username")
    val BLOCK_CARDSTATUS: Preferences.Key<Boolean> = booleanPreferencesKey("block_cardstatus")
    val USER_CREDENTIAL = stringPreferencesKey("user_credential")
    val AUTH_TOKEN = stringPreferencesKey("authToken")
    val USER_MOBILE_NUMBER= stringPreferencesKey("mobile_number")
    val HAS_PASSED_SPLASH_SCREENS= booleanPreferencesKey("has_passed_splash_screens")
    val CARD_REF_ID = intPreferencesKey("card_ref_id")
    val DATABINDING_DONE = booleanPreferencesKey("DATABINDING_DONE")
    val ANDROID_ID = stringPreferencesKey("ANDROID_ID")
    val DISPUTED_TRANSACTION_ID = stringPreferencesKey("DISPUTED_TRANSACTION_ID")
}