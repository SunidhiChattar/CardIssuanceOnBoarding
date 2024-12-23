package com.isu.common.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService


fun fetchSimDetails(context: Context) {
    val telephonyManager =
        getSystemService(context, TelephonyManager::class.java) as TelephonyManager
    Log.d(
        "SIM_INFO", "fetchSimDetails: ${
            (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
            ))
        } " + "\n" + " ${
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_NUMBERS
            )
        }" + "\n" + "${
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            )
        }"
    )
    // Fetch SIM card phone number (this may return null depending on carrier restrictions)
    val phoneNumber = if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    ) {

        val phoneNumber = telephonyManager.line1Number


//
//                // Fetch other SIM-related information
//                val simSerialNumber = telephonyManager.simSerialNumber
//                val networkOperator = telephonyManager.networkOperatorName
//                val simCountry = telephonyManager.simCountryIso

        println("Phone Number: $phoneNumber")
//                println("SIM Serial Number: $simSerialNumber")
//                println("Network Operator: $networkOperator")
//                println("SIM Country: $simCountry")
        Log.d("SIM_INFO", "fetchSimDetails: $phoneNumber")
//                Log.d("SIM_INFO", "fetchSimDetails: $simSerialNumber")
//                Log.d("SIM_INFO", "fetchSimDetails: $networkOperator")
//                Log.d("SIM_INFO", "fetchSimDetails: $simCountry")
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    } else {
        Log.d("SIM_INFO", "fetchSimDetails: $")


        return
    }

}