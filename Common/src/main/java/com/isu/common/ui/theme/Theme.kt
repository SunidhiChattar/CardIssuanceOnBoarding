package com.isu.common.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.isu.cardissuanceonboarding.presentation.ui.theme.Typography


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = appMainColor,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun CardIssuanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.White.toArgb()
            window.navigationBarColor=Color.White.toArgb()

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
//        val systemUiController = rememberSystemUiController()
//        val useDarkIcons = !isSystemInDarkTheme()
//        val statusBarLight = Color.LightGray
//        val statusBarDark = Color.Blue
//        val navigationBarLight = Color.LightGray
//        val navigationBarDark = Color.Blue
//        DisposableEffect(systemUiController, useDarkIcons) {
//            systemUiController.setNavigationBarColor(
//                color = if (useDarkIcons) {
//                    statusBarLight
//                } else {
//                    statusBarDark
//                },
//                darkIcons = true
//            )
//            systemUiController.setStatusBarColor(
//                color = if (useDarkIcons) {
//                    navigationBarLight
//                } else {
//                    navigationBarDark
//                },
//                darkIcons = true
//            )
//
//            onDispose { }
//        }
    }
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme)  dynamicLightColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme ->  LightColorScheme
        else -> LightColorScheme


    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}