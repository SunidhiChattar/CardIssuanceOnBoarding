package com.isu.common.utils

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.isu.common.R

/**
 * Font provider
 * utility function which provides with font-family via string
 * @constructor Create empty Font provider
 */
object FontProvider {
    const val LATO_FONT = "Lato"
    const val INTER="Inter"
    const val QUICK_SAND = "Quicksand"

    fun getFont(font: String, weight: FontWeight = FontWeight(600)): FontFamily {
        val provider = GoogleFont.Provider(
            providerAuthority = "com.google.android.gms.fonts",
            providerPackage = "com.google.android.gms",
            certificates = R.array.com_google_android_gms_fonts_certs
        )
        val fontName = GoogleFont(font)
        val fontFamily = FontFamily(
            Font(googleFont = fontName, fontProvider = provider, weight)
        )
        return fontFamily
    }

}