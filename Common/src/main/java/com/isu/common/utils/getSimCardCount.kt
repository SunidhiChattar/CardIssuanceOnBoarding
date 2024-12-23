package com.isu.common.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

// Function to get the number of SIM cards present in the device
fun getSimCardDetails(context: Context): Int {
    val telephonyManager =
        getSystemService(context, TelephonyManager::class.java) as TelephonyManager

    // For API level 22 and above (Dual SIM support introduced in Lollipop MR1)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        val subscriptionManager = getSystemService(context, SubscriptionManager::class.java)

        val activeSubscriptionInfoList = if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            subscriptionManager?.activeSubscriptionInfoList
        } else {
            null
        }
        activeSubscriptionInfoList?.forEach {

            SubscriptionManager.getSubscriptionId(it.simSlotIndex)
            Log.d("SIM_INFO", "getSimCardDetails: ${SubscriptionManager.getSubscriptionId(1)}")
        }
        val simCount = activeSubscriptionInfoList?.size ?: 0

        Toast.makeText(context, "Number of SIM cards: $simCount ", Toast.LENGTH_LONG).show()
        println("Number of SIM cards: $simCount")
        return simCount
    } else {
        // For devices below API level 22
        val simCount = telephonyManager.phoneCount
        Toast.makeText(context, "Number of SIM cards: $simCount", Toast.LENGTH_LONG).show()
        println("Number of SIM cards: $simCount")
        return simCount
    }

}