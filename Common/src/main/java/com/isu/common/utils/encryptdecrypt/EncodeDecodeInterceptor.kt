package com.isu.common.utils.encryptdecrypt

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

class LogOutInterceptor(val callBack: () -> Unit = {}) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        Log.d("LOGOUT_INTERCEPT", "intercept:start ")
        try {

            if (response.code != 200) {

                val responseBody = response.body
                val responseBodyString = responseBody.string()


                val newResponse = response.newBuilder()
                    .body(responseBodyString.toResponseBody(responseBody.contentType()))
                    .code(response.code).headers(response.headers).request(response.request)
                    .protocol(response.protocol).message(response.message).build()

                val responseObj = JSONObject(responseBodyString)



                Log.d("LOGOUT_INTERCEPT", "intercept:$responseObj ")
                if (responseObj.has("statusDesc")) {
                    val statusDesc = responseObj.getString("statusDesc")
                    if ((statusDesc.lowercase()
                            .contains("expire".toRegex()) && statusDesc.lowercase()
                            .contains("token".toRegex()))
                        || (statusDesc.lowercase()
                            .contains("token".toRegex()) && statusDesc.lowercase()
                            .contains("missing".toRegex()))
                    ) {
                        callBack.invoke()
                        Log.d("LOGOUT_INTERCEPT", "intercept:call back called ")

                    }
                }
                return newResponse


            }

        } catch (e: Exception) {
            Log.d("LOGOUT_INTERCEPT", "intercept error:${e.localizedMessage} ")
        }
        return response
    }

}

