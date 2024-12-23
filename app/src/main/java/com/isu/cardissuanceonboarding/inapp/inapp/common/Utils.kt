package com.isu.cardissuanceonboarding.inapp.inapp.common

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.isu.cardissuanceonboarding.BuildConfig
import java.util.Random

object Utils {
    fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d("APP_EXCEPTION", message)
        }
    }
    fun canRequestPackageInstalls(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.canRequestPackageInstalls()
        } else {
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.INSTALL_NON_MARKET_APPS,
                0
            ) == 1
        }
    }
    /**
     * showInstallFromUnknownSourceDialog().
     * This method will check if the user all permission to install app from unknown store (From Non Play Store).
     * If user wouldn't allow the permission then it will show dialog to allow the permission
     * method otherwise it won't call insertAppDataToApiDatabase() the method
     */
     fun goToAppPermissionSettings(
        intentActivityResultLauncher: ActivityResultLauncher<Intent>
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intentActivityResultLauncher.launch(
                Intent(
                    Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                ).setFlags(0)
            )
        } else {
            intentActivityResultLauncher.launch(
               Intent(Settings.ACTION_SECURITY_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun generateRandomNumber(): Int {
        val r = Random(System.currentTimeMillis())
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000))
    }
}