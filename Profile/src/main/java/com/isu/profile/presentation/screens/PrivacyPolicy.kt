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
 *@author-karthik
 * Privacy policy screen
 * web view for privacy policy of company
 * @param modifier
 */
@Composable
fun PrivacyPolicyScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { CustomProfileTopBar(text = stringResource(R.string.privacy_policy_hd)) },

        containerColor = Color.White
    )
    {
        KeyBoardAwareScreen(Modifier.padding(it)) {
            WebViewScreen(BuildConfig.WEB_VIEW_PRIVACY_POLICY)
        }
    }
}
