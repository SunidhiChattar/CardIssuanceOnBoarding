package com.isu.cardissuanceonboarding.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.iserveu.permission.multiplepermission.MultiPlePermission
import com.iserveu.permission.multiplepermission.MyActivityResultCallback
import com.iserveu.permission.multiplepermission.OnUserAction
import com.isu.authentication.presentation.screens.newflow.newRegistration.RegistrationViewModel
import com.isu.cardissuanceonboarding.R
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.APPLICATION_ID
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.UPDATED_FLAG
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.VERSION_CODE
import com.isu.cardissuanceonboarding.inapp.inapp.common.Constants.appVersionName
import com.isu.cardissuanceonboarding.inapp.inapp.common.Utils
import com.isu.cardissuanceonboarding.inapp.inapp.presentation.viewmodel.InAppViewModel
import com.isu.cardissuanceonboarding.presentation.navigation.SetUpNavGraph
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.ErrorCard
import com.isu.common.ui.theme.CardIssuanceTheme
import com.isu.common.utils.datastore.PreferenceData
import com.isu.common.utils.datastore.PreferencesKeys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private lateinit var scope: CoroutineScope

    private val inAppViewModel: InAppViewModel by viewModels()
    private val intentActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (inAppViewModel.unKnownRequestPermission) {
                if (Utils.canRequestPackageInstalls(this)) {
                    inAppViewModel.unKnownRequestPermission = false
                    inAppViewModel.dialogueMessage.value =
                        getString(R.string.new_update_message)
                    inAppViewModel.dialogueTitle.value =
                        getString(R.string.new_update_title)
                    inAppViewModel.dialogueConfirmBtnText.value = getString(R.string.update)
                    inAppViewModel.updateDialoguePermission = true
                    inAppViewModel.showDialogue.value = true
                } else {
                    inAppViewModel.showDialogue.value = true
                }
            } else {
                MyActivityResultCallback.onActivityResult(result)
            }
        }
    private val mMultiplePermissionRequestLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            MyActivityResultCallback.onActivityResult(permissions)
        }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
//        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        val intentDataFromApp=intent.extras
        val requiredData=intentDataFromApp?.getString("data")
        Log.d("SDK_DATA", "onCreate:${intentDataFromApp} ")
        val requiredDataObj=try{
            if (requiredData != null) {
                JSONObject(requiredData)
            }else{
                null
            }
        }catch (e:Exception){
            e.printStackTrace()
            null

        }

        val splashScreen = installSplashScreen()
        val manager: PackageManager = this.packageManager
        val info: PackageInfo = manager.getPackageInfo(this.packageName, 0)
        appVersionName = info.versionName
        VERSION_CODE = PackageInfoCompat.getLongVersionCode(info).toInt()
        APPLICATION_ID = info.packageName
        // Initialize installation result launcher
        val installResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
        }
        inAppViewModel.installResultLauncher = installResultLauncher
        // Initialize and check for previous app update version
        val sp = this@MainActivity.getSharedPreferences(
            "ISERVEU_INAPP_DISTRIBUTION_SDK",
            Context.MODE_PRIVATE
        )

        setContent {

        val allPermissionGranted = remember {
                mutableStateOf(false)
            }
            val showUpdateScreen = remember {
                mutableStateOf(false)
            }
            val showInvalidDataDialog= remember {
                mutableStateOf(false)
            }
            val registrationViewModel= hiltViewModel<RegistrationViewModel>()
            LaunchedEffect(Unit) {
                try{
                    Log.d("SDK_DATA", "SetUpNavGraph: ${requiredDataObj}")
                    val phoneNumber=requiredDataObj?.getString("userMobileNumber")
                    val clientId=requiredDataObj?.getString("clientID")
                    val clientSecret=requiredDataObj?.getString("clientSecret")
                    if(phoneNumber.isNullOrEmpty()||clientId.isNullOrEmpty()||clientSecret.isNullOrEmpty()){
                        showInvalidDataDialog.value=true
                    }else{

                        registrationViewModel.dataStoreInstance.savePreferences(listOf(
                            PreferenceData(PreferencesKeys.USER_MOBILE_NUMBER,phoneNumber),
                            PreferenceData(PreferencesKeys.CLIENT_ID,clientId),
                            PreferenceData(PreferencesKeys.CLIENT_SECRET,clientSecret),))
                    }
                }catch (e:Exception){
                    showInvalidDataDialog.value=true
                }


            }


            LaunchedEffect(key1 = true) {
                inAppViewModel.begin(this@MainActivity)
                val previousVersion = sp.getInt(UPDATED_FLAG, 0)
                if (previousVersion != 0 && previousVersion < VERSION_CODE) {
                    Toast.makeText(
                        this@MainActivity,
                        "App is updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    sp.edit().putInt(UPDATED_FLAG, VERSION_CODE).apply()
                }
            }
            // Initialize navigation controller
            val navController = rememberNavController()
            // Request runtime permissions for older Android versions
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                MultiPlePermission
                    .Builder()
                    .context(this)
                    .permissionList(
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ).onCompletePermissionGranted(object : OnUserAction {
                        override fun onAllPermissionGranted() {
                            allPermissionGranted.value = true
                        }

                        override fun onDeniedToGrantPermission() {
                            allPermissionGranted.value = false
                        }
                    })
                    .multiplePermissionLauncher(mMultiplePermissionRequestLauncher)
                    .intentResultLauncher(intentActivityResultLauncher)
                    .build()
            } else {
                // All permissions granted for Android R and above
                allPermissionGranted.value = true
            }
            if(showInvalidDataDialog.value){
                ErrorCard(text = "Required data could not be fetched.Try opening with whitelabel app", btnComposable = {
                    CustomButton(color = Red, modifier = Modifier
                        .padding(22.dp)
                        .fillMaxWidth(), onClick = {
                        this.finish()
                    }, text = "Close")
                })
            }


            // Display dialogs based on ViewModel states


                if (!inAppViewModel.showDialogue.value&& !showInvalidDataDialog.value) {
                    scope = rememberCoroutineScope()

                    CardIssuanceTheme(darkTheme = false) {


                        SetUpNavGraph(requiredDataObj)
                    }

                }




        }
    }

    override fun onResume() {
        super.onResume()

    }

}
























