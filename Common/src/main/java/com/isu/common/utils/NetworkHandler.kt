package com.isu.common.utils

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.isu.common.events.LoadingErrorEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * Author: Anandeswar Sahu
 * Description: Utility functions and sealed class for handling network responses, error messages, and flow processing.
 */

typealias ResponseCode = Int
typealias ResponseBody = String

sealed class NetworkResource<T>(
    val data: T? = null,
    val message: String? = null,
    val responseCode: Int? = null,
    val errorObj: String? = null
) {

    class Success<T>(data: T?) : NetworkResource<T>(data)

    class Error<T>(
        message: String,
        data: T? = null,
        responseCode: ResponseCode? = null,
        errorObj: ResponseBody? = null
    ) : NetworkResource<T>(data, message, responseCode, errorObj)

    class Loading<T>(val isLoading: Boolean) : NetworkResource<T>(null)

}

/**
 * Description: Function to handle API responses and convert DTO response to domain response.
 *
 * @param call: Suspending function representing the API call.
 * @param mapFun: Lambda function to map DTO response to domain response.
 * @return Flow<NetworkResource<O>>: Flow emitting NetworkResource representing success, error, or loading states.
 */
fun <T, O> handleFlowResponse(
    call: suspend () -> Response<T>,
    mapFun: suspend (it: T) -> O?,
): Flow<NetworkResource<O>> {
    return flow {

        // Emit the loading state true
        emit(NetworkResource.Loading(true))
        try {
            // Call the api
            val response = call.invoke()
            val unusedBody = response.body()
            Log.d("KAPIRESP", "handleFlowResponse:$response ")
            if (response.isSuccessful) {
                // Emit the success response
                val data = response.body()?.let { mapFun(it) }
                Log.d("KAPIRESP", "handleFlowResponse:${data} ")
                emit(NetworkResource.Success(data))
            } else {
                //Emit the error response
                val errorBody = response.errorBody()?.string() ?: ""
                val errorMsg = extractErrorMessage(errorBody)
                val responseCode = response.code()
                Log.d("KAPIRESP", "handleFlowResponse:${errorBody} ")
                emit(
                    NetworkResource.Error(
                        message = errorMsg,
                        data = response.body()?.let { mapFun(it) },
                        responseCode = responseCode,
                        errorObj = errorBody
                    )
                )
            }
        } catch (e: IOException) {
            val errorMessage = handleIOException(e)
            emit(NetworkResource.Error(errorMessage))
        } catch (e: HttpException) {
            e.response()?.errorBody()?.string()?.let {
                emit(NetworkResource.Error(it))
            } ?: e.message?.let {
                emit(NetworkResource.Error(it))
            } ?: emit(NetworkResource.Error("Something Went Wrong"))
        } catch (e: IllegalStateException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: SocketException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        } catch (e: NullPointerException) {
            e.message?.let { emit(NetworkResource.Error(it)) }
        }
        emit(NetworkResource.Loading(false))
    }
}

/**
 * Handles IOException and returns a corresponding error message based on the type of IOException.
 *
 * @param ioException The IOException to handle.
 * @return The error message corresponding to the type of IOException.
 */
private fun handleIOException(ioException: IOException) = when (ioException) {
    is ConnectException -> {
        "Could not connect to the server."
    }

    is SocketTimeoutException -> {
        "Connection timed out."
    }

    is UnknownHostException -> {
        "Unknown host. Please check your internet connection."
    }

    else -> {
        // Check exception message for additional clues
        val exceptionMessage: String = ioException.message ?: "Something went wrong"
        if (exceptionMessage.contains("timeout")) {
            "Gateway timeout."
        } else if (exceptionMessage.contains("unreachable")) {
            "Host unreachable."
        } else {
            exceptionMessage
        }
    }
}

/**
 * Description: Function to extract error message from JSON.
 *
 * @param errorBody: Error response body as a String.
 * @return String: Extracted error message.
 */
private fun extractErrorMessage(errorBody: String): String {
    return try {
        val errorMap = errorBody.toMap()
        when {
            "apiComment" in errorMap -> errorMap["apiComment"].toString()
            "message" in errorMap -> errorMap["message"].toString()
            "data" in errorMap -> extractDataErrorMessage(errorMap)
            "fault" in errorMap -> extractFaultErrorMessage(errorMap)
            "transactionStatus" in errorMap -> errorMap["transactionStatus"].toString()
            "statusDesc" in errorMap -> errorMap["statusDesc"].toString()
            else -> errorBody.ifEmpty { "Something went wrong" }
        }
    } catch (e: JsonSyntaxException) {
        errorBody.ifEmpty { "No Response From Server" }
    }
}

/**
 * Description: Function to extract error message from "data" JSON.
 *
 * @param errorMap: Map representing the error response.
 * @return String: Extracted error message from "data" JSON.
 */
private fun extractDataErrorMessage(errorMap: Map<String, Any?>): String {
    return try {
        val dataObj = errorMap["data"] as Map<*, *>
        dataObj["statusDesc"].toString()
    } catch (e: ClassCastException) {
        errorMap["data"].toString()
    }
}

/**
 * Description: Function to extract error message from "fault" JSON.
 *
 * @param errorMap: Map representing the error response.
 * @return String: Extracted error message from "fault" JSON.
 */
private fun extractFaultErrorMessage(errorMap: Map<String, Any?>): String {
    return try {
        val faultError = errorMap["fault"] as Map<*, *>
        faultError["faultstring"].toString()
    } catch (e: ClassCastException) {
        errorMap["fault"].toString()
    }
}


/**
 * Handles the flow of network resource states and provides callbacks for different states:
 * loading, failure, and success.
 *
 * @param T The type of the data in the success state.
 * @param apiCall A function that returns a Flow of NetworkResource<T> representing the network call.
 * @param scope The CoroutineScope in which to launch the coroutine.
 * @param dispatcher The CoroutineDispatcher to be used for the network call, defaults to Dispatchers.IO.
 * @param builder A lambda with receiver on FlowHandlerBuilder<T> to set the handlers for different states.
 */
fun <T> handleFlow(
    apiCall: () -> Flow<NetworkResource<T>>,
    scope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
    builder: FlowHandlerBuilder<T>.() -> Unit
) {
    val handler = FlowHandlerBuilder<T>().apply(builder)

    scope.launch {
        val response = withContext(dispatcher) {
            apiCall.invoke()
        }
        response.collectLatest {
            when (it) {
                is NetworkResource.Error -> {
                    handler.onFailure(
                        it.message ?: "Something went wrong.",
                        it.responseCode,
                        it.errorObj,
                        it.data
                    )
                }

                is NetworkResource.Loading -> {
                    handler.onLoading(it.isLoading)
                }

                is NetworkResource.Success -> {
                    handler.onSuccess(it.data)
                }
            }
        }
    }
}

fun <T> handleFlow(
    apiCall: () -> Flow<NetworkResource<T>>,
    scope: CoroutineScope,
    onSuccesss: (T?) -> Unit,
    onFailure: (err: T?, message: String, errBody: ResponseBody?) -> Unit = { resp, message, errJsonString ->
        var errMsg = message


        scope.launch {
            try {
                val jsonOBj = JSONObject(errJsonString)
                if (message.isEmpty()) {
                    if (jsonOBj.has("statusDesc")) {
                        errMsg = jsonOBj.getString("statusDesc")
                    } else if (jsonOBj.has("message")) {
                        errMsg = jsonOBj.getString("message")
                    }
                    LoadingErrorEvent.helper.emit(
                        LoadingErrorEvent.errorEncountered(
                            UiText.DynamicString(
                                errMsg
                            )
                        )
                    )
                } else {
                    LoadingErrorEvent.helper.emit(
                        LoadingErrorEvent.errorEncountered(
                            UiText.DynamicString(
                                message
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                LoadingErrorEvent.helper.emit(
                    LoadingErrorEvent.errorEncountered(
                        UiText.DynamicString(
                            message
                        )
                    )
                )
            }


        }
    },
    dispatcher: CoroutineDispatcher,

    ) {

    val handler = FlowHandlerBuilder<T>().apply {
        onSuccess {
            onSuccesss(it)
        }
        onFailure { errorMessage, responseCode, errorJsonString, resp ->
            onFailure(resp, errorMessage, errorJsonString)
        }
        onLoading {
            scope.launch {
                LoadingErrorEvent.helper.emit(LoadingErrorEvent.isLoading(it))
            }

        }
    }

    scope.launch {
        val response = withContext(dispatcher) {
            apiCall.invoke()
        }

        response.collectLatest {
            when (it) {
                is NetworkResource.Error -> {
                    Log.d(
                        "CHECK_RESP",
                        "genrateBindingOtp:${it.message}+${it.errorObj}+${it.responseCode}"
                    )
                    handler.onFailure(
                        it.message ?: "karthik went wrong.",
                        it.responseCode,
                        it.errorObj,
                        it.data,

                    )
                }

                is NetworkResource.Loading -> {
                    handler.onLoading(it.isLoading)
                }

                is NetworkResource.Success -> {
                    handler.onSuccess(it.data)
                }
            }
        }
    }
}

/**
 * A builder class for handling different states of a network resource flow.
 * This class allows setting handlers for loading, failure, and success states.
 *
 * @param <T> The type of the data in the success state.
 */
class FlowHandlerBuilder<T> {
    /**
     * A lambda function to handle the loading state.
     * It takes a Boolean parameter indicating whether loading is in progress.
     */
    var onLoading: (Boolean) -> Unit = {}

    /**
     * A lambda function to handle the failure state.
     * It takes a String parameter containing the error message.
     */
    var onFailure: (String, ResponseCode?, ResponseBody?, T?) -> Unit = { _, _, _, _ ->

    }

    /**
     * A lambda function to handle the success state.
     * It takes a nullable parameter of type T containing the data.
     */
    var onSuccess: (T?) -> Unit = {}

    /**
     * Sets the handler for the loading state.
     *
     * @param handler A lambda function that takes a Boolean parameter indicating loading state.
     */
    fun onLoading(handler: (Boolean) -> Unit) {
        onLoading = handler
    }

    /**
     * Sets the handler for the failure state.
     *
     * @param handler A lambda function that takes a String parameter containing the error message.
     */
    fun onFailure(handler: (String, ResponseCode?, ResponseBody?, T?) -> Unit) {

        onFailure = handler
    }

    /**
     * Sets the handler for the success state.
     *
     * @param handler A lambda function that takes a nullable parameter of type T containing the data.
     */
    fun onSuccess(handler: (T?) -> Unit) {
        onSuccess = handler
    }
}


