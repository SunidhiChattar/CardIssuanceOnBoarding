package com.isu.cardissuanceonboarding.inapp.inapp.presentation.viewmodel

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.core.content.pm.PackageInfoCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isu.cardissuanceonboarding.R
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.APPLICATION_ID
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.CURRENT_APP_VERSION
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.ISU_IN_APP_SDK_FCM_PREFS_KEY
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.UPDATED_FLAG
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.UPDATE_VERSION
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.VERSION_CODE
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.isUserAppDetailsUpdated
import com.isu.cardissuanceonboarding.inapp.inapp.common.DownloadBroadcastReceiver
import com.isu.cardissuanceonboarding.inapp.inapp.common.UserDetails
import com.isu.cardissuanceonboarding.inapp.inapp.common.Utils
import com.isu.cardissuanceonboarding.inapp.inapp.common.handleFlow
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.req.InsertRequestModel
import com.isu.cardissuanceonboarding.inapp.inapp.data.models.req.UserAppDetails
import com.isu.cardissuanceonboarding.inapp.inapp.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@HiltViewModel
class InAppViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private var isLoading = mutableStateOf(false)
    private var downloadUrl = mutableStateOf("")
    val isInternetAvailable = mutableStateOf(true)
    var isUnsecure = mutableStateOf(false)

    lateinit var installResultLauncher: ActivityResultLauncher<Intent>

    var inAppApkVersionCode = mutableStateOf("")
        private set
    var showDialogue = mutableStateOf(false)
        private set
    var isUpdateGoingOn = mutableStateOf(false)
        private set
    var dialogueMessage = mutableStateOf("")
        private set
    var dialogueTitle = mutableStateOf("")
        private set
    var dialogueConfirmBtnText = mutableStateOf("")
        private set
    var downloadProgress = mutableStateOf(0L)
        private set
    var appUpdateText = mutableStateOf("")
        private set
    var buttonText = mutableStateOf("")
        private set
    var unInstallDialog = mutableStateOf(false)

    /**
     * Launcher for requesting runtime permissions.
     */
    var unKnownRequestPermission = false
    var updateDialoguePermission = false

    fun getLatestVersionApk(
        checkUnknownPermissionCallBack: () -> Unit,
    ) {
        val userAppDetails = UserAppDetails(
            appPackageName = APPLICATION_ID, isEnabled = true
        )
        handleFlow(
            apiCall = {
                useCases.getLatestVersionUseCase(
                    userAppDetails
                )
            }, scope = viewModelScope
        ) {
            onLoading {
                isLoading.value = it
            }
            onFailure { _, _, _ ->

            }
            onSuccess { response ->
                response?.let {
                    when (it.status) {
                        1 -> {
                            if (it.data?.size != 0) {
                                downloadUrl.value = it.data?.get(0)?.appUrl ?: ""
                                inAppApkVersionCode.value = it.data?.get(0)?.versioncode ?: ""
                                UPDATE_VERSION = it.data?.get(0)?.version ?: ""
                                val installedAppVersionCode = VERSION_CODE
                                try {
                                    if (inAppApkVersionCode.value.toInt() > installedAppVersionCode) {
                                        // Check Unknown Source Permission
                                        checkUnknownPermissionCallBack.invoke()
                                    }
                                } catch (e: NumberFormatException) {
                                    Utils.showLog(
                                        e.localizedMessage ?: "Invalid in app apk version code"
                                    )
                                }
                            }
                        }

                        -1 -> {
                            // Show Dialog to grant unknown source permission
                            dialogueMessage.value = "Please Uninstall the app..."
                            dialogueTitle.value = "Service unavailable !!!"
                            dialogueConfirmBtnText.value = "Uninstall"
                            showDialogue.value = true
                            unKnownRequestPermission = false
                            updateDialoguePermission = false
                            unInstallDialog.value = true
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun startDownloadApp(appName: String, context: Context, manager: DownloadManager) {
        val downloadManagerRequest = DownloadManager.Request(Uri.parse(downloadUrl.value))
        downloadManagerRequest.setTitle(appName)
        downloadManagerRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        downloadManagerRequest.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, appName
        )
        val downloadId = manager.enqueue(downloadManagerRequest)
        val sharedPreference =
            context.getSharedPreferences("ISERVEU_INAPP_DISTRIBUTION_SDK", Context.MODE_PRIVATE)
        sharedPreference.edit().apply {
            putLong(APPLICATION_ID, downloadId)
            putString(downloadId.toString(), appName)
            putInt(UPDATED_FLAG, VERSION_CODE)
        }.apply()

        val downloadBroadcastReceiver = DownloadBroadcastReceiver { it, receiver ->
            if (downloadId == it) {
                getDownloadStatus(it, context, manager)
                context.unregisterReceiver(receiver)
            }
        }
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                downloadBroadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            context.registerReceiver(
                downloadBroadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED
            )
        }


        Timer().schedule(object : TimerTask() {
            override fun run() {
                try {
                    val downloadManagerQuery = DownloadManager.Query()
                    downloadManagerQuery.setFilterById(downloadId)
                    val cursor: Cursor = manager.query(downloadManagerQuery)
                    cursor.moveToFirst()
                    val bytesDownloadedColumnIndex =
                        cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                    val bytesTotalColumnIndex =
                        cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                    if (bytesDownloadedColumnIndex != -1 && bytesTotalColumnIndex != -1) {
                        val bytesDownloaded = cursor.getInt(bytesDownloadedColumnIndex).toLong()
                        val bytesTotal = cursor.getInt(bytesTotalColumnIndex).toLong()
                        val dlProgress = (bytesDownloaded * 100 / bytesTotal)
                        if (dlProgress == 100L) {
                            cancel()
                            buttonText.value = "INSTALL"
                            appUpdateText.value = context.getString(R.string.after_update_text)

                            installNewApk(appName, context, manager)
                        } else {
                            buttonText.value = "Downloading..."
                            appUpdateText.value =
                                context.getString(R.string.downloading_app_update_text)
                        }
                        downloadProgress.value = dlProgress
                        //Use bytesDownloaded and bytesTotal
                    } else {
                        //Handle the error case where the column is not found
                    }
                    cursor.close()
                } catch (e: NullPointerException) {
                    Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                } catch (e: CursorIndexOutOfBoundsException) {
                    Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }, 0, 10)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun installNewApk(appName: String, context: Context, manager: DownloadManager) {
        viewModelScope.launch {
            try {
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    appName
                )
                if (file.exists()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    val downloaded_apk = FileProvider.getUriForFile(
                        context,
                        APPLICATION_ID + ".provider",
                        file
                    )
                    intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive")
                    val resInfoList: List<ResolveInfo> =
                        context.packageManager
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                    for (resolveInfo in resInfoList) {
                        context.grantUriPermission(
                            context.getPackageName() + ".provider",
                            downloaded_apk,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    delay(500)
                    installResultLauncher.launch(intent)
                    // context.startActivity(intent)

                } else {
                    startDownloadApp(appName, context, manager)
                }
            } catch (e: IllegalArgumentException) {
                Utils.showLog(
                    e.localizedMessage
                        ?: "Couldn't find meta-data for provider with authority ${APPLICATION_ID}.provider"
                )
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getDownloadStatus(
        previousDownloadId: Long,
        context: Context,
        manager: DownloadManager,
    ) {

        val cursor = manager.query(DownloadManager.Query().setFilterById(previousDownloadId))
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if (columnIndex != -1) {
                val status = cursor.getInt(columnIndex)

                when (status) {
                    DownloadManager.STATUS_FAILED, DownloadManager.STATUS_PAUSED, DownloadManager.STATUS_PENDING -> {
                        buttonText.value = "Download Now"
                        appUpdateText.value = context.getString(R.string.before_update_text)
                        downloadProgress.value = 0L
                    }

                    DownloadManager.STATUS_RUNNING -> {
                        updateDownloadProgress(previousDownloadId, context, manager)
                    }

                    DownloadManager.STATUS_SUCCESSFUL -> {
                        checkAppInLocalDirectory(context, manager)
                    }

                    else -> {
                        buttonText.value = "Download Now"
                        appUpdateText.value = context.getString(R.string.before_update_text)
                        downloadProgress.value = 0L
                    }
                }
            }
        } else {
            buttonText.value = "Download Now"
            appUpdateText.value = context.getString(R.string.before_update_text)
            downloadProgress.value = 0L
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAppInLocalDirectory(context: Context, manager: DownloadManager) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            Constants.apkName
        )
        if (file.exists()) {
            appUpdateText.value = context.getString(R.string.after_update_text)
            downloadProgress.value = 100L
            buttonText.value = "Install"
            installNewApk(appName = Constants.apkName, context, manager)
        } else {
            appUpdateText.value = context.getString(R.string.before_update_text)
            downloadProgress.value = 0L
            buttonText.value = "Download Now"
        }
    }

    private fun updateDownloadProgress(
        previousDownloadId: Long,
        context: Context,
        manager: DownloadManager,
    ) {
        Timer().schedule(object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun run() {
                try {
                    val downloadManagerQuery = DownloadManager.Query()
                    downloadManagerQuery.setFilterById(previousDownloadId)
                    val cursor: Cursor = manager.query(downloadManagerQuery)
                    cursor.moveToFirst()
                    val bytesDownloadedColumnIndex =
                        cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                    val bytesTotalColumnIndex =
                        cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                    if (bytesDownloadedColumnIndex != -1 && bytesTotalColumnIndex != -1) {
                        val bytesDownloaded = cursor.getInt(bytesDownloadedColumnIndex).toLong()
                        val bytesTotal = cursor.getInt(bytesTotalColumnIndex).toLong()
                        val dlProgress = (bytesDownloaded * 100 / bytesTotal)
                        downloadProgress.value = dlProgress
                        if (dlProgress == 100L) {
                            buttonText.value = "INSTALL"
                            appUpdateText.value = context.getString(R.string.after_update_text)
                        } else {
                            buttonText.value = "Downloading..."
                            appUpdateText.value =
                                context.getString(R.string.downloading_app_update_text)
                        }
                        //Use bytesDownloaded and bytesTotal
                    } else {
                        //Handle the error case where the column is not found
                    }
                    cursor.close()
                } catch (e: NullPointerException) {
                    manager.remove(previousDownloadId)
                    getDownloadStatus(previousDownloadId, context, manager)
                } catch (e: CursorIndexOutOfBoundsException) {
                    manager.remove(previousDownloadId)
                    getDownloadStatus(previousDownloadId, context, manager)
                }
            }
        }, 0, 10)
    }

    fun unInstallAPK(context: Context) {
        val packageURI = Uri.parse("package:$APPLICATION_ID")
        val uninstallIntent = Intent(Intent.ACTION_DELETE, packageURI)
        uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(uninstallIntent)
    }

    /**
     * This function is used to begin the application and set all the device details to the shared preference
     * **/
    fun begin(context: Context) {
        val sharedPreference =
            context.getSharedPreferences(ISU_IN_APP_SDK_FCM_PREFS_KEY, Context.MODE_PRIVATE)
        if (!isUserAppDetailsUpdated) {
            try {
                setUserAppDetails(context, sharedPreference)
                isUserAppDetailsUpdated = true
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                // LogUtils.printDebugLog("begin: UserAppDetails Updated before: ${e.localizedMessage}")
            }
        } else {
            // LogUtils.printDebugLog("begin: UserAppDetails Updated before")
        }
        insertAppDetailsForCountDownloads(context, sharedPreference)

    }

    /**
     * This function is used for set the android id and application details,ip address to the shared preference
     * **/
    @Throws(PackageManager.NameNotFoundException::class)
    fun setUserAppDetails(context: Context, sharedPreference: SharedPreferences) {
        getFCMTokenNew(context, sharedPreference)

        val androidId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("tag", "setUserAppDetails: $androidId")

        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            UserDetails.appVersionName = info.versionName
            UserDetails.appVersionCode = PackageInfoCompat.getLongVersionCode(info).toInt()
            UserDetails.appPackageName = info.packageName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            //LogUtils.printDebugLog("onCreate: getPackageName Crashed")
        }

        UserDetails.androidId = androidId
        getLocalIpAddress()?.let {
            UserDetails.userIpAddress = it
        }
    }

    /**
     * This function is used to get the local ip address of the device
     * **/
    private fun getLocalIpAddress(): String? {
        return try {
            NetworkInterface.getNetworkInterfaces().asSequence().forEach { intf ->
                intf.inetAddresses.asSequence().forEach { inetAddress ->
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.hostAddress
                    }
                }
            }
            null
        } catch (ex: SocketException) {
            ex.printStackTrace()
            null
        }
    }

    private fun getFCMTokenNew(context: Context, sharedPreference: SharedPreferences) {
        UserDetails.fcmToken = "firebase token"
        sharedPreference.edit().apply {
            this.putString(ISU_IN_APP_SDK_FCM_PREFS_KEY, "firebase token")
        }.apply()

//        FirebaseMessaging.getInstance().token
//            .addOnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                     Log.w("tag", "Fetching FCM registration token failed", task.exception)
//                    sharedPreference.edit().apply {
//                        this.putString(ISU_IN_APP_SDK_FCM_PREFS_KEY, "failed")
//                    }.apply()
//                    UserDetails.fcmToken = "failed"
//                    return@addOnCompleteListener
//                }
//
//                // Get new FCM registration token
//                val token = task.result
//                // Log and toast
//                Log.d("tag", "Your FCM TOKEN IS $token")
//                sharedPreference.edit().apply {
//                    this.putString(ISU_IN_APP_SDK_FCM_PREFS_KEY, "failed")
//                }.apply()
//                UserDetails.fcmToken = token
//            }
    }

    /**
     * This function is used to check if this is the first time the application is installed.
     * by checking the value present in the shared preference.
     **/
    private fun insertAppDetailsForCountDownloads(
        context: Context,
        sharedPreference: SharedPreferences,
    ) {
        val fcmToken = sharedPreference.getString(ISU_IN_APP_SDK_FCM_PREFS_KEY, "")

        if (fcmToken.equals("failed", ignoreCase = true) || fcmToken.equals(
                "init", ignoreCase = true
            )
        ) {
            getFCMTokenNew(context, sharedPreference)
        }
        if (fcmToken.equals("failed", ignoreCase = true) || fcmToken.equals(
                "init", ignoreCase = true
            )
        ) {
//            LogUtils.printDebugLog("insertAppDetails Failed: Failed to get FCMToken")
        } else {
            val currentVersion = sharedPreference.getString(CURRENT_APP_VERSION, "")
            if (currentVersion.equals(UserDetails.appVersionCode.toString(), ignoreCase = true)) {

//                LogUtils.printDebugLog(
//                    "insertAppDetails : data inserted before :: $currentVersion ${UserDetails.appVersionCode}"
//                )
            } else {
                insertAppDataToApiDatabase()
                sharedPreference.edit().also {
                    it.putString(CURRENT_APP_VERSION, UserDetails.appVersionCode.toString())
                }.apply()

//                LogUtils.printDebugLog(
//                    "insertAppDetails Success: details inserted :: $currentVersion ${UserDetails.appVersionCode}"
//                )
            }
        }

    }

    /**
     * This function is used for the insert the application details to the api database.
     * **/
    private fun insertAppDataToApiDatabase() {

        isLoading.value = true
        val insertRequestModel = InsertRequestModel(
            app_package_name = UserDetails.appPackageName,
            app_version = UserDetails.appVersionName,
            device_id = UserDetails.androidId,
            device_details = "${UserDetails.userDeviceModel} / IP - ${UserDetails.userIpAddress} / API LEVEL- ${UserDetails.osVersion}",
            fcm_token = UserDetails.fcmToken,
            user_name = UserDetails.USER_NAME
        )

        val response = handleFlow(
            apiCall = {
                useCases.sendAppDetailsToApiUseCase(insertRequestModel)
            }, scope = viewModelScope
        ) {

            onLoading = {
                isLoading.value = it
            }
            onFailure = { message, ResponseCode, ResponseBody ->
                isLoading.value = false
                Utils.showLog(message)
            }
            onSuccess = {
                isLoading.value = false
                if (it != null) {

//                        LogUtils.printDebugLog(
//                            " insertAppDataToApiDatabase :: onResponse: " + it.message + " " + it.status
//                        )
                } else {

//                        LogUtils.printDebugLog(
//                            "insertAppDataToApiDatabase :: onResponse: code::  Duplicate data found"
//                        )

                }
            }

        }
    }
}