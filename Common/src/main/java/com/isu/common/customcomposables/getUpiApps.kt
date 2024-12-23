package com.isu.common.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.widget.Toast
import kotlin.random.Random

fun getUpiApps(context: Context): List<ResolveInfo> {
    val packageManager: PackageManager = context.packageManager

    // Intent for UPI payment
    val upiIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("upi://pay")
    }

    // Get a list of apps that can handle this UPI intent
    val resolveInfoList: List<ResolveInfo> =
        packageManager.queryIntentActivities(upiIntent, PackageManager.MATCH_DEFAULT_ONLY)
    resolveInfoList[0]
    return resolveInfoList
}


fun payUsingSelectedUpiApp(
    context: Context,
    resolveInfo: ResolveInfo,
    upiId: String,
    amount: String,
    transactionNote: String,
    callResultLauncher: (Intent) -> Unit = {},
) {
    // UPI payment URI format
    val uri = Uri.parse("upi://pay").buildUpon()
        .appendQueryParameter("pa", upiId)  // Payee UPI ID
        .appendQueryParameter("pn", "Karthik")  // Payee Name (optional)
        .appendQueryParameter("mc", "")  // Merchant code (optional)
        .appendQueryParameter("tid", "${Random(1000).nextInt()}")  // Transaction ID (optional)
        .appendQueryParameter(
            "tr",
            "${Random(1000).nextInt()}"
        )  // Transaction reference ID (optional)
        .appendQueryParameter("tn", transactionNote)  // Transaction note
        .appendQueryParameter("am", amount)  // Amount
        .appendQueryParameter("cu", "INR")  // Currency
        .build()

    // Create the intent to launch the UPI app
    val upiIntent = Intent(Intent.ACTION_VIEW).apply {
        data = uri
        setPackage(resolveInfo.activityInfo.packageName)  // Set the package to the selected UPI app
    }



    try {
        // Start UPI activity with the intent
        callResultLauncher(upiIntent)
    } catch (e: Exception) {
        // Handle case where no app is available
        Toast.makeText(context, "No UPI app available", Toast.LENGTH_SHORT).show()
    }
}