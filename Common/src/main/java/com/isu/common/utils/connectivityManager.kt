package com.isu.common.utils
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * @author karthik
 * Connectivity manager
 * -utility to manage internet connection states
 */
val Context.connectivityManager get(): ConnectivityManager {
    return getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

/**
 * Network Utility to observe availability or unavailability of Internet connection
 */
fun ConnectivityManager.observeConnectivityAsFlow() = callbackFlow {

    val callback = NetworkCallback { connectionState -> trySend(connectionState) }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    registerNetworkCallback(networkRequest, callback)

    awaitClose {
        unregisterNetworkCallback(callback)
    }
}.distinctUntilChanged()

/**
 * Network utility to get current state of internet connection
 */
fun ConnectivityManager.currentConnectivityState( networkState: (ConnectionState)->Unit){

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Check if the device is connected to the internet
                val networkCapabilities = getNetworkCapabilities(network)
                if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
                    // Device is connected to the internet
                     networkState.invoke(ConnectionState.Available)
                }else
                    networkState(ConnectionState.Unavailable)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                networkState(ConnectionState.Unavailable)
            }
        }
       registerDefaultNetworkCallback(networkCallback)


    }

@Suppress("FunctionName")
fun NetworkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectionState.Available)
        }

        override fun onLost(network: Network) {
            callback(ConnectionState.Unavailable)
        }

        override fun onUnavailable() {
            callback(ConnectionState.Unavailable)
        }
    }
}