package com.isu.cardissuanceonboarding.inapp.presentation

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.cardissuanceonboarding.R
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.APPLICATION_ID
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.apkName
import com.isu.cardissuanceonboarding.inapp.inapp.common.Utils

import com.isu.cardissuanceonboarding.inapp.inapp.common.Utils.generateRandomNumber
import com.isu.cardissuanceonboarding.inapp.inapp.presentation.viewmodel.InAppViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun UpdateScreen(viewModel: InAppViewModel) {
    val context = LocalContext.current
    val bitmap: MutableState<Bitmap?> = remember {
        mutableStateOf(null)
    }
    val manager: DownloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    LaunchedEffect(key1 = true) {
        val sharedPreference =
            context.getSharedPreferences("ISERVEU_INAPP_DISTRIBUTION_SDK", Context.MODE_PRIVATE)
        val previousDownloadId = sharedPreference.getLong(APPLICATION_ID, 0)
        if (previousDownloadId == 0L) {
            apkName =
                "isu_apk_v" + viewModel.inAppApkVersionCode.value + "_" + generateRandomNumber() + ".apk"
            viewModel.buttonText.value = "DOWNLOAD NOW"
            viewModel.appUpdateText.value = context.getString(R.string.before_update_text)
            val downloadManagerState =
                context.packageManager.getApplicationEnabledSetting("com.android.providers.downloads")
            if (downloadManagerState == PackageManager.COMPONENT_ENABLED_STATE_DISABLED || downloadManagerState == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER || downloadManagerState == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
//            showEnableDownloadManagerDialog()
                Toast.makeText(context, "Not Enabled", Toast.LENGTH_SHORT).show()
            } else {
                val icon: Drawable =
                    context.packageManager.getApplicationIcon(APPLICATION_ID)
                bitmap.value = Utils.drawableToBitmap(icon)
            }
        } else {
            apkName = sharedPreference.getString(previousDownloadId.toString(), "").toString()
            viewModel.getDownloadStatus(previousDownloadId, context, manager)
        }

    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            (context as Activity).finish()
                        }
                        .padding(10.dp)
                        .size(30.dp),
                    imageVector = Icons.Default.Close, contentDescription = "",
                )
            }
        }
    ) { topBarPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(
                    top = topBarPadding.calculateTopPadding(),
                    start = 20.dp,
                    end = 20.dp
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                bitmap.value?.let {
                    Image(bitmap = it.asImageBitmap(), contentDescription = "")
                }
                Text(
                    text = viewModel.appUpdateText.value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            AnimatedVisibility(visible = viewModel.appUpdateText.value != context.getString(R.string.before_update_text)) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${viewModel.downloadProgress.value}% downloaded",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress =
                        viewModel.downloadProgress.value.toFloat() / 100
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.buttonText.value != "Downloading...",
                onClick = {
                    when (viewModel.buttonText.value) {
                        "DOWNLOAD NOW" -> {
                            viewModel.startDownloadApp(apkName, context, manager)
                        }

                        else -> {
                            viewModel.installNewApk(apkName, context, manager)
                        }
                    }
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = viewModel.buttonText.value)
            }
        }
    }
}