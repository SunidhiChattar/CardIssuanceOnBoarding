package com.isu.common.customcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isu.common.navigation.NavigationEvent
import kotlinx.coroutines.launch

@Composable
fun CustomClosingTopBar(
    modifier: Modifier = Modifier,
    onBackPress: suspend () -> Unit = {
        NavigationEvent.helper.navigateBack()
    },
    onCrossClick: suspend () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    Row(
        Modifier.height(60.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            scope.launch {
                onBackPress()
            }
        }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "")
        }
        IconButton(onClick = {
            scope.launch {
                onCrossClick()
            }
        }) {
            Icon(Icons.Default.Close, "")
        }
    }
}