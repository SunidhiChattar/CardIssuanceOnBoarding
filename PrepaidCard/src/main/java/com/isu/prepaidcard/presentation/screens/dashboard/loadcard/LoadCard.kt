
package com.isu.common.customcomposables.loadcard

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomCheckBoxField
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.UpiExpandable
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.CardManagement
import com.isu.common.ui.theme.appMainColor
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.prepaidcard.presentation.screens.dashboard.loadcard.LoadCardButtons
import com.isu.prepaidcard.presentation.screens.dashboard.loadcard.LoadCardState
import com.isu.prepaidcard.presentation.screens.dashboard.loadcard.LoadCardTextFields
import com.isu.prepaidcard.presentation.screens.dashboard.loadcard.LoadCardViewModel
import com.isu.prepaidcard.presentation.screens.dashboard.loadcard.UpiPaymentStatus
import kotlinx.coroutines.launch

@Preview
@Composable
fun LoadCard(
    modifier: Modifier = Modifier,
    state: LoadCardState = LoadCardState(),
    onEvent: (CommonScreenEvents) -> Unit = {},
    viewModel: LoadCardViewModel,
) {
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        onEvent(CommonScreenEvents.GetDataStoreData(PreferencesKeys.CARD_REF_ID))
    }
    val upiMode = remember {
        mutableStateOf(false)
    }
    DisposableEffect(lifecycleOwner.lifecycle.currentState) {
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {}
                Lifecycle.Event.ON_START -> {}
                Lifecycle.Event.ON_RESUME -> {
                    upiMode.value = false
                    onEvent(CommonScreenEvents.GetDataStoreData(PreferencesKeys.CARD_REF_ID))
                }

                Lifecycle.Event.ON_PAUSE -> {}
                Lifecycle.Event.ON_STOP -> {}
                Lifecycle.Event.ON_DESTROY -> {}
                Lifecycle.Event.ON_ANY -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.intentUri.value = ""
    }
    val ammountToLoad = state.amountToLoad
    val ammountToLoadError = state.amountToLoadError
    val ammountToLoadErrorMessage = state.amountToLoadErrorMessage

    val vpaMode = remember {
        mutableStateOf(false)
    }
    val qrMode = remember {
        mutableStateOf(false)
    }
    val resolveInfo: MutableState<ResolveInfo?> = remember {
        mutableStateOf(null)
    }
    val config = LocalConfiguration.current
    val intentToSelectedUpiApp: MutableState<Intent?> = remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val rememberUpiPayResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            scope.launch {
                ShowSnackBarEvent.helper.emit(
                    ShowSnackBarEvent.show(
                        SnackBarType.SuccessSnackBar,
                        UiText.DynamicString("Payment Successful")
                    )
                )
            }

            if (it.data == null) {
                viewModel.updateFailurePaymenData()
            } else {
                Log.d("PaymentResult", "UpiExpandable:${it} : ")
                it.data?.extras.let { bundle ->
                    bundle?.keySet()?.forEach {
                        if (bundle.getString("Status") == "SUCCESS") {
                            Toast.makeText(context, "FAILED", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "FAILED", Toast.LENGTH_LONG).show()
                            viewModel.updateFailurePaymenData()
                        }

                        Log.d("PaymentResult", "UpiExpandable:${it} : ${bundle[it]}")
                    }
                }

            }
            onEvent(CommonScreenEvents.NavigateTo(CardManagement.LoadCardResultScreen))

        }
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Load Card"
            )
        },
        containerColor = Color.White
    ) {
        it
        val screenHeight = LocalConfiguration.current.screenHeightDp
        KeyBoardAwareScreen(
            shouldScroll = false,
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = 22.dp,
                bottom = 0.dp,
                end = 22.dp
            )
        ) {
            val scope = rememberCoroutineScope()
            Column(
                modifier = Modifier.heightIn(config.screenHeightDp.dp - (it.calculateTopPadding() + 90.dp)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    CustomInputField(
                        enabled = false,
                        state = "XXXX XXXX XXXX ${state.cardRefId.takeLast(4)}",
                        label = "Selected Card"
                    )
                    CustomInputField(
                        enabled = true,
                        state = ammountToLoad,
                        onValueChange = {
                            if (it.isDigitsOnly()) {
                                onEvent(
                                    CommonScreenEvents.OnTextChanged(
                                        type = LoadCardTextFields.LoadCardAmount,
                                        text = it
                                    )
                                )
                            }
                        },
                        isError = ammountToLoadError,
                        errorMessage = ammountToLoadErrorMessage.asString(),
                        placeholder = "Enter the amount",
                        label = "Amount to Load",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    CustomText(text = "")
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CustomText(text = "Select a mode to Pay")
                        CustomCheckBoxField(
                            text = "UPI App",
                            expand = upiMode,
                            onSelected = {
                                upiMode.value = true
                                qrMode.value = false
                                vpaMode.value = false
                            }
                        ) {
                            UpiExpandable(onUpiAppClick = { info, onsuces ->
                                onsuces()
                                resolveInfo.value = info
                                onEvent(CommonScreenEvents.OnClick<Any>(type = LoadCardButtons.PayApp))
                            })
                        }


                    }
                }
                Column(modifier = Modifier.heightIn(20.dp)) {}
                Column {

                    CustomButton(text = "Continue", color = appMainColor, onClick = {
                        if (ammountToLoad.isEmpty()) {
                            scope.launch {
                                ShowSnackBarEvent.helper.emit(
                                    ShowSnackBarEvent.show(

                                        SnackBarType.ErrorSnackBar,
                                        UiText.DynamicString("Plase eneter a ammount to load")
                                    )
                                )
                            }
                        } else {
                            onEvent(
                                CommonScreenEvents.OnClick<Any>(
                                    type = LoadCardButtons.LoadCard,
                                    onComplete = {

                                        Log.d("INTENT", "LoadCard:not ONCOMPLETE $state")
                                        if (viewModel.intentUri.value != null) {
                                            Log.d("INTENT", "LoadCard:not nulll ")
                                            val upiIntent = Intent(Intent.ACTION_VIEW).apply {
                                                data = Uri.parse(viewModel.intentUri.value)
                                                setPackage(resolveInfo.value?.activityInfo?.packageName)  // Set the package to the selected UPI app
                                            }
                                            Log.d("INTENT", "LoadCard:not $upiIntent ")
                                            rememberUpiPayResultLauncher.launch(
                                                upiIntent
                                            )
                                        } else {
                                            Log.d("INTENT", "LoadCard:not ONCOMPLETE ")
                                            scope.launch {
                                                ShowSnackBarEvent.helper.emit(
                                                    ShowSnackBarEvent.show(
                                                        SnackBarType.ErrorSnackBar,
                                                        UiText.DynamicString("Plase select an upi app")
                                                    )
                                                )
                                            }
                                        }

                                        focusManager.clearFocus()
                                    })
                            )
                        }


                    })
                }
            }


        }
    }

}