package com.isu.profile.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.isu.common.BuildConfig
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.WebViewScreen
import com.isu.profile.R

/**
 * Composable function to display the "About Us" screen.
 *
 * @param modifier A [Modifier] for applying additional layout modifications.
 */
@Composable
fun AboutUsScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { CustomProfileTopBar(text = stringResource(R.string.about_us_hd)) },
        containerColor = Color.White
    ) { innerPadding ->
        KeyBoardAwareScreen(modifier.padding(innerPadding)) {
            WebViewScreen(url = BuildConfig.WEB_VIEW_ABOUT_US)
        }
    }
}

/**
 * Composable function to display the "Terms and Conditions" screen.
 *
 * @param modifier A [Modifier] for applying additional layout modifications.
 */
@Composable
fun TermsAndConditionScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { CustomProfileTopBar(text = stringResource(R.string.terms_and_conditions_hd)) },
        containerColor = Color.White
    ) { innerPadding ->
        KeyBoardAwareScreen(modifier.padding(innerPadding)) {
            WebViewScreen(url = BuildConfig.WEB_VIEW_TERMS_AND_CONDITION)
        }
    }
}
