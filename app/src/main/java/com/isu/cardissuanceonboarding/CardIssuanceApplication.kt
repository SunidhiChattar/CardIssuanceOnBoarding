package com.isu.cardissuanceonboarding

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.isu.cardissuanceonboarding.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CardIssuanceApplication : Application(), ExceptionListener {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val intent = Intent(this, CrashHandlingActivity::class.java)
        intent.putExtra(
            getString(R.string.unknown_exception), throwable.message +
                    "_______________________________________________________________________\n" +
                    throwable.localizedMessage +
                    "_________________________________________________________\n" +
                    throwable.cause.toString() +
                    "---------------------------------------------------------------\n" +
                    throwable.stackTrace.toString()
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun setupExceptionHandler() {
        Handler(Looper.getMainLooper()).post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    uncaughtException(Looper.getMainLooper().thread, e)
                }
            }
        }
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            uncaughtException(t, e)
        }
    }
}

interface ExceptionListener {
    fun uncaughtException(thread: Thread, throwable: Throwable)
}