package com.isu.common.utils

import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * @author-karthik
 * Remember ime state
 * utility to get notified about the keyboard state
 *
 * @return
 */
@Composable
fun rememberImeState(): MutableState<Boolean> {
    val imeState=remember{
        mutableStateOf(false)
    }
    val view= LocalView.current

    DisposableEffect(key1 = view){
        val listener=ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen=ViewCompat.getRootWindowInsets(view)?.isVisible(WindowInsetsCompat.Type.ime())?:true
            imeState.value=isKeyboardOpen
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return imeState
}