package com.isu.cardissuanceonboarding.inapp.inapp.common

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DownloadBroadcastReceiver(
    private val onReceiveDownloadBroadcast: (downloadId: Long, receiver: BroadcastReceiver) -> Unit,
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            onReceiveDownloadBroadcast(downloadId ?: -1, this)
        }
    }

}
