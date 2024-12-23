package com.isu.common.customcomposables

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.Modifier
import com.iserveu.permission.location.LocationAgent
import com.iserveu.permission.location.LocationUpdateListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


fun getLatLong(
    modifier: Modifier = Modifier,
    context: Context,
    multiplePermissioin: ActivityResultLauncher<Array<String>>,
    intentLauncher: ActivityResultLauncher<Intent>,
    mlatLong: MutableStateFlow<String>,
    scope: CoroutineScope,

    ) {


    LocationAgent(
        mContext = context,
        mMultiplePermissionLauncher = multiplePermissioin,
        mIntentLauncher = intentLauncher,
        mLocationUpdateListener = object :
            LocationUpdateListener {
            override fun onDeniedToGrantPermission() {
//                    TODO("Not yet implemented")
            }

            override fun onDeniedToTurnOnLocation() {
//                    TODO("Not yet implemented")
            }

            override fun onLocationUpdate(latLong: String?) {
                scope.launch {
                    mlatLong.emit(latLong ?: "")
                }

//                Toast.makeText(context,mlatLong.value+"kil"+latLong.toString(),Toast.LENGTH_LONG).show()

            }

            override fun proceedWithUi() {
//                    TODO("Not yet implemented")
            }

        }

    ).getLocation()

}