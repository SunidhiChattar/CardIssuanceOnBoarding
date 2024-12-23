package com.isu.authentication.presentation.screens.newflow.taregistration



import android.graphics.Bitmap
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationViewModel
import com.isu.common.navigation.AuthenticationScreens
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.ProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreenForTA(url:String="https://iserveu.in/privacy-policy/", viewModel: RegistrationViewModel, backHanlder:(WebView)->Unit={}) {
    val isLoading= remember {
        mutableStateOf(true)
    }
    val webView:MutableState<WebView?> = remember { mutableStateOf(null) }
    BackHandler {
        if (webView.value!=null){
            backHanlder.invoke(webView.value!!)
        }


    }

    val fontSize=10.sp.value.toInt()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        if(isLoading.value){
            BasicAlertDialog(onDismissRequest = {

            }) {
                Card(
                    Modifier.wrapContentWidth().height(70.dp), colors = CardDefaults.cardColors(
                        Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.width(10.dp))
                        Text("Loading...", color = Color.Black)
                    }

                }

            }
        }else{

        }


        fun WebView.injectJavaScriptForVisibilityMonitoring() {
            val jsCode = """
          (function() {
                const targetElement =  document.querySelector(".glassmpyes");
                const targetElementNo =  document.querySelector(".glassmpno");
                const targetElement1 =  document.querySelector("#form1");
           
                
                if(targetElementNo){
                targetElementNo.onClick=null
                const observer = new IntersectionObserver((entries) => {
                        entries.forEach(entry => {
                            // Send visibility status to Android when it changes
                           // window.AndroidInterface.onVisibilityChanged(entry.isIntersecting);
                           if(entry.isIntersecting){
                         targetElement1.onsubmit = function(event) {
                        event.preventDefault(); // Prevent the default form submission
                        const formData = new FormData(form);
                        const data = {};
                        formData.forEach((value, key) => {
                            data[key] = value;
                        });
                        // Pass the form data to Android
                        window.AndroidInterface.onFormSubmit();
                    };
                         }
                         targetElementNo.addEventListener("click",(e)=>{
                         event.stopPropagation(); // Prevents any other click events
                        event.preventDefault(); 
                         window.AndroidInterface.onErrorOccured();
                         
                         })
                
               
                        });
                        observer.observe(targetElementNo);
                    }, { threshold: 0.1 });
                    observer.observe(targetElementNo);
                }
             
           
                if (targetElement) {
                 targetElement.onClick=null
                    const observer = new IntersectionObserver((entries) => {
                        entries.forEach(entry => {
                            // Send visibility status to Android when it changes
                           // window.AndroidInterface.onVisibilityChanged(entry.isIntersecting);
                           if(entry.isIntersecting){
                         targetElement1.onsubmit = function(event) {
                        event.preventDefault(); // Prevent the default form submission
                        const formData = new FormData(form);
                        const data = {};
                        formData.forEach((value, key) => {
                            data[key] = value;
                        });
                        // Pass the form data to Android
                        window.AndroidInterface.onFormSubmit();
                    };
                         }
                         targetElement.addEventListener("click",(e)=>{
                         event.stopPropagation(); // Prevents any other click events
                        event.preventDefault(); 
                         window.AndroidInterface.onVisibilityChanged(entry.isIntersecting);
                         
                         })
                
               
                        });
                    }, { threshold: 0.1 });
                    observer.observe(targetElement);
                }
            })();
        """

            // Evaluate JavaScript after the page has loaded
            this.evaluateJavascript(jsCode, object : ValueCallback<String> {
                override fun onReceiveValue(value: String?) {
                    Toast.makeText(context,value,Toast.LENGTH_LONG).show()
                }

            })}
        val config= LocalConfiguration.current
        val screenHeightDP=config.screenHeightDp
        val screenWidthDp=config.smallestScreenWidthDp

        AndroidView(
            factory = { context ->

                WebView(context).apply {
                    // Define the JavaScript interface
                    webView.value=this
                    class WebAppInterface {
                        @android.webkit.JavascriptInterface
                        fun onVisibilityChanged(visible: Boolean) {
                            // Called from JavaScript with the visibility status
                            Toast.makeText(context,"Called",Toast.LENGTH_LONG).show()
                           /* viewModel.callStatusCheckApi {
                                val dataToVerify=it?.data?.getOrNull(0)?.cardDetails?.getOrNull(0)?.card?.getOrNull(0)
                                if(dataToVerify!=null&&(dataToVerify.statusCode==0&&dataToVerify.status=="SUCCESS")){
                                    viewModel.viewModelScope.launch {
                                        NavigationEvent.helper.navigateTo(ProfileScreen.DashBoardScreen)
                                    }
                                }else{
                                    viewModel.viewModelScope.launch {
                                        NavigationEvent.helper.navigateTo(AuthenticationScreens.EnterMobileNumberDeviceBindingScreen)
                                    }
                                }
                            }*/
                        }
                        @android.webkit.JavascriptInterface
                        fun onFormSubmit(visible: Boolean) {
                            // Called from JavaScript with the visibility status
                            Toast.makeText(context,"FormSubmitCalled",Toast.LENGTH_LONG).show()

                        }
                        @android.webkit.JavascriptInterface
                        fun onErrorOccured() {
                            // Called from JavaScript with the visibility status
                            Toast.makeText(context,"FormSubmitCalled",Toast.LENGTH_LONG).show()
                            viewModel.viewModelScope.launch {
                                NavigationEvent.helper.navigateTo(AuthenticationScreens.EnterMobileNumberDeviceBindingScreen)
                            }

                        }
                    }
                    addJavascriptInterface(WebAppInterface(), "AndroidInterface")
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()

//                    settings.loadWithOverviewMode = true
//                    settings.useWideViewPort = true
                    settings.defaultFontSize=fontSize
                    settings.textZoom=100

                    settings.setSupportZoom(true)
                    webViewClient=object : WebViewClient(){
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading.value=true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading.value=false
                            injectJavaScriptForVisibilityMonitoring()
                        }

                        // Custom WebViewClient to intercept URL loading


                        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                            Log.d("WebViewActivity", "Intercepted URL: ${request.url.toString()} ")
                            // Example: Check for a specific URL pattern to interceptt
                            if (request.url.toString().lowercase().contains("login".toRegex())) {
                                // Navigate back to Android on specific URL
                                // or navigate to another screen
//                                    onComplete()
                                return true // Prevent WebView from loading this URL
                            }
                            return false }// Allow WebView to handle other URLs normally


                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest
                        ): WebResourceResponse? {
                            val url = request.url.toString()
                            if(url.contains("TAWalletWeb/LoginPage".toRegex())){
//                                y7
                            }
                            return super.shouldInterceptRequest(view, request)
                        }
                    }


                    // Custom WebChromeClient to handle JavaScript pop-ups
                    webChromeClient = object : WebChromeClient() {
                        override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                            Log.d("ALERT", "onJsAlert:called ")
                            // Customize JavaScript alert handling

                            return super.onJsAlert(view, url, message, result) // Default handling
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