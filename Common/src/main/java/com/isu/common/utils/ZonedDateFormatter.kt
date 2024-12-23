package com.isu.common.utils

import android.icu.text.SimpleDateFormat
import android.os.Build
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object ZonedDateFormatter {
    fun format(inputDate: String, pattern: String = "dd MMM, yyyy hh.mm a"): String? {
        val inputDateTime = inputDate


        // Parse the input string into a ZonedDateTime object
        val zonedDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime.parse(inputDateTime)
        } else {
           return null
        }

        // Define the desired output format
        val outputFormatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)

        // Format the ZonedDateTime object into the desired string format
        val formattedDateTime = zonedDateTime.format(outputFormatter)

        // Print the result
        return formattedDateTime
    }
    fun formatMillisecondsToDate(milliseconds: Long, format: String = "dd-MM-yyyy"): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(milliseconds)
        return sdf.format(date)
    }
}