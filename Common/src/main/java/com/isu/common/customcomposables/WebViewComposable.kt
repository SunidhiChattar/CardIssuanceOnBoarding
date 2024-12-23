package com.isu.common.customcomposables

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(url:String="https://iserveu.in/privacy-policy/") {
    val isLoading= remember {
        mutableStateOf(true)
    }

    val fontSize=10.sp.noFontScale().value.toInt()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        if(isLoading.value){
            CustomLoader()
        }else{

        }

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()

                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.defaultFontSize=fontSize
                    settings.textZoom=100

                    settings.setSupportZoom(true)
                    webViewClient=object :WebViewClient(){
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading.value=true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading.value=false
                        }
                    }

                }
            },
            update = { webView ->
                webView.loadUrl(url)

            }
        )
    }

}