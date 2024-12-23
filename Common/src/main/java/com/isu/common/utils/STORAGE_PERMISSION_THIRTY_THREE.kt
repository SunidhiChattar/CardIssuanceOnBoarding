package com.isu.common.utils

import android.os.Build
import androidx.annotation.RequiresApi
import android.Manifest

/**
     * Permission array for API level 33 (Android Tiramisu).
     *
     * This array contains permissions for Bluetooth scan and connect, read/write external storage,
     * camera, fine location access, and read phone state.
     */
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    val STORAGE_PERMISSION_THIRTY_THREE = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS
    )

    /**
     * Permission array for API level 12 (Android S).
     *
     * This array contains permissions for read/write external storage, camera, fine location access,
     * and read phone state.
     */
    val PERMISSION_FOR_TWELVE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS
    )