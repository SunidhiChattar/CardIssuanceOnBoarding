package com.isu.profile.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomCancelButton
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.UiText
import com.isu.profile.R
import kotlinx.coroutines.launch

/**
 * @author-karthik
 *
 * Composable function to display the Notification Settings screen.
 *
 * @param modifier A [Modifier] for applying additional layout modifications.
 */
@Composable
fun NotificationSettings(modifier: Modifier = Modifier) {
    val config = androidx.compose.ui.platform.LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val mobileNotification = remember { mutableStateOf(true) }
    val emailNotification = remember { mutableStateOf(false) }
    val allNotification = remember { mutableStateOf(false) }
    val userSpecific = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { CustomProfileTopBar(text = stringResource(R.string.notification_settings)) },
        containerColor = Color.White
    ) { innerPadding ->
        KeyBoardAwareScreen(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .fillMaxHeight(),
            shouldScroll = false
        ) {
            Column(
                modifier = Modifier.heightIn(min = screenHeight.dp - 160.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    CustomText(
                        text = stringResource(R.string.get_notified_about_important_stuff_via),
                        fontSize = 15.sp.noFontScale()
                    )
                    NotificationTypeComponent(mobileNotification, emailNotification)
                    NotificationMediumComponent(allNotification, userSpecific)
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomText(
                        text = stringResource(R.string.you_can_adjust_these_settings_later),
                        fontSize = 13.sp.noFontScale()
                    )
                    CustomButton(
                        text = stringResource(R.string.update),
                        shape = RoundedCornerShape(5.dp),
                        onClick = {

                            if (mobileNotification.value || emailNotification.value || allNotification.value || userSpecific.value) {
                                scope.launch {
                                    ShowSnackBarEvent.helper.emit(
                                        ShowSnackBarEvent.show(
                                            SnackBarType.SuccessSnackBar,
                                            UiText.StringResource(R.string.notification_settings_updated_successfully)
                                        )
                                    )
                                    NavigationEvent.helper.emit(NavigationEvent.NavigateBack)
                                }

                            } else {
                                scope.launch {

                                }

                            }

                        })
                    CustomCancelButton(text = stringResource(R.string.cancel), onClick = {
                        scope.launch {
                            if (mobileNotification.value || emailNotification.value || allNotification.value || userSpecific.value) {

                                NavigationEvent.helper.emit(NavigationEvent.NavigateBack)
                            } else {

                            }

                        }
                    })
                }
            }
        }
    }
}

/**
 * Composable function to display the notification type options.
 */
@Composable
fun NotificationTypeComponent(
    mobileNotification: MutableState<Boolean>,
    emailNotification: MutableState<Boolean>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(5.dp)
    ) {
        CustomText(
            text = stringResource(R.string.we_ll_notify_you_via),
            fontWeight = FontWeight(520),
            color = Color.Black,
            fontSize = 14.sp.noFontScale()
        )
        CustomNotificationCheckBox(state = mobileNotification)
        CustomNotificationCheckBox(text = stringResource(R.string.email_notify), emailNotification)
    }
}

/**
 * Composable function to display the notification medium options.
 */
@Composable
fun NotificationMediumComponent(
    allNotification: MutableState<Boolean>,
    userSpecific: MutableState<Boolean>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(5.dp)
    ) {
        CustomText(
            text = stringResource(R.string.we_ll_notify_you_about),
            fontWeight = FontWeight(520),
            color = Color.Black,
            fontSize = 14.sp.noFontScale()
        )
        CustomNotificationCheckBox(
            text = stringResource(R.string.all_notifications),
            allNotification
        )
        CustomNotificationCheckBox(text = stringResource(R.string.user_specific), userSpecific)
    }
}

/**
 * Composable function to display a custom checkbox with text.
 *
 * @param text The label text for the checkbox.
 */
@Composable
fun CustomNotificationCheckBox(
    text: String = stringResource(R.string.mobile_notification),
    state: MutableState<Boolean>,
) {

    OutlinedTextField(
        value = text,
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { state.value = !state.value },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledBorderColor = Color.LightGray.copy(0.4f)

        ),
        enabled = false,
        trailingIcon = {
            Checkbox(
                checked = state.value,
                modifier = Modifier
                    .border(
                        if (state.value) 1.3.dp else 0.5.dp,
                        authTextColor,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .size(20.dp),
                onCheckedChange = { state.value = it },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = authTextColor,
                    checkedColor = Color.White,
                    uncheckedColor = Color.Transparent
                )
            )
        },
        textStyle = TextStyle(
            fontSize = 14.sp.noFontScale(),
            fontWeight = FontWeight.Medium
        )
    )
}
