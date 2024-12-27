package com.isu.common.utils

import android.util.Log

object Logger {

    private const val DEFAULT_TAG = "OnBoardingLogger"
    var isDebugEnabled: Boolean = true

    // Log methods
    fun d( message: String,tag: String = DEFAULT_TAG,) {
        if (isDebugEnabled) {
            Log.d(tag, message)
        }
    }

    fun e(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        if (isDebugEnabled) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    fun i(tag: String = DEFAULT_TAG, message: String) {
        if (isDebugEnabled) {
            Log.i(tag, message)
        }
    }

    fun w(tag: String = DEFAULT_TAG, message: String) {
        if (isDebugEnabled) {
            Log.w(tag, message)
        }
    }

    fun v(tag: String = DEFAULT_TAG, message: String) {
        if (isDebugEnabled) {
            Log.v(tag, message)
        }
    }
}
