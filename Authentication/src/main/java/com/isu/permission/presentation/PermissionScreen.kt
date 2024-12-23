package com.isu.permission.presentation
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iserveu.permission.multiplepermission.MultiPlePermission
import com.iserveu.permission.multiplepermission.MyActivityResultCallback
import com.iserveu.permission.multiplepermission.OnUserAction
import com.isu.common.R
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomRowItems
import com.isu.common.customcomposables.getLatLong
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.LatLongFlowProvider
import com.isu.common.utils.PERMISSION_FOR_TWELVE
import com.isu.common.utils.STORAGE_PERMISSION_THIRTY_THREE


/**
 * @author Abhilash Champatiray
 * date : 2024-05-01
 *
 * Composable function for displaying the permission screen.
 */
@Composable
fun PermissionScreen(onEvent: (CommonScreenEvents) -> Unit) {
    val context = LocalContext.current

    val isButtonClicked = remember {
        mutableStateOf(true)
    }
    val multiplePermissioin =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            MyActivityResultCallback.onActivityResult(permissions)
        }
    val intentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            MyActivityResultCallback.onActivityResult(result)
        }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        getLatLong(
            context = context,
            scope = scope,
            mlatLong = LatLongFlowProvider.latLongFlow,
            multiplePermissioin = multiplePermissioin,
            intentLauncher = intentLauncher
        )
    }

    Column(
        modifier = Modifier
            .widthIn(200.dp, 500.dp)
            .padding(top = 10.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column (modifier = Modifier.weight(1f)){
            PermissionHeader()
            PermissionItems()
        }
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            PermissionButton(onPermissionGranted = {

                onEvent(CommonScreenEvents.OnClick<Any>(PermissionButtonType.PermissionGranted))
            }, isButtonClicked, context)

        }

    }
}
/**
 * Composable function for displaying the header of the permission screen.
 */
@Composable
fun PermissionHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = stringResource(R.string.permission_required),
            fontWeight = FontWeight.Bold,
            fontSize = 19.5.sp.noFontScale(),
            modifier = Modifier.padding(top = 22.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = stringResource(R.string.for_the_app_to_function_properly_we_might_need_the_following_permission),
            fontSize = 15.60.sp.noFontScale(),
            modifier = Modifier.padding(
                top = 18.dp,
                start = 19.50.dp,
                end = 9.10.dp,
                bottom = 20.dp
            )
        )
    }
}
/**
 * Composable function for displaying individual permission items.
 */
@Composable
fun PermissionItems() {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        CustomRowItems(
            image = R.drawable.baseline_contacts_24,
            headerText = stringResource(R.string.get_accounts_contacts),
            subHeader = stringResource(R.string.to_create_an_account_using_the_google_accounts_available_in_device),
            testTag = "contactsIcon"
        )
        CustomRowItems(
            image = R.drawable.ic_baseline_phone_iphone_24,
            headerText = stringResource(R.string.device_info),
            subHeader = stringResource(R.string.to_allow_read_only_access_to_phone_state_including_the_phone_number_of_the_device_and_current_cellular_network_information),
            testTag = "phoneIcon"
        )
        CustomRowItems(
            image = R.drawable.ic_baseline_perm_media_24,
            headerText = stringResource(R.string.media_and_storage_files),
            subHeader = stringResource(R.string.to_access_the_media_and_files_to_save_in_internal_storage),
            testTag = "fileIcon"
        )
        CustomRowItems(
            image = R.drawable.ic_baseline_location_on_24,
            headerText = stringResource(R.string.location),
            subHeader = stringResource(R.string.to_verify_your_address_for_application),
            testTag = "locationIcon"
        )
    }
}
/**
 * Composable function for displaying the permission button.
 * @param navController NavController to navigate between destinations.
 * @param isButtonClicked MutableState for tracking button click state.
 * @param context Context for accessing application context.
 */
@Composable
fun PermissionButton(
    onPermissionGranted:()->Unit,
    isButtonClicked: MutableState<Boolean>,
    context: Context
) {

    val mMultiplePermissionRequestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        MyActivityResultCallback.onActivityResult(result)
    }
    val intentActivityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        MyActivityResultCallback.onActivityResult(result)
    }
    CustomButton(onClick = {
        isButtonClicked.value = false
        MultiPlePermission
            .Builder()
            .context(context)
            .permissionList(
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    PERMISSION_FOR_TWELVE
                } else {
                    STORAGE_PERMISSION_THIRTY_THREE
                }
            )
            .onCompletePermissionGranted(object : OnUserAction {
                override fun onAllPermissionGranted() {
                    onPermissionGranted()
                }
                override fun onDeniedToGrantPermission() {
                    isButtonClicked.value = true
                    Toast.makeText(
                        context,
                        context.getString(R.string.permission_denied), Toast.LENGTH_SHORT
                    ).show()
                }
            })
            .multiplePermissionLauncher(mMultiplePermissionRequestLauncher)
            .intentResultLauncher(intentActivityResultLauncher)
            .build()
    }, enabled = isButtonClicked.value, text ="Continue" )


}



