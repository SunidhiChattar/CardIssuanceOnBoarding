package com.isu.profile.presentation.screens.customersupport.presentation.screens.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.ShimmerComposable
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.events.LoadingErrorEvent
import com.isu.common.events.LogOutEvent
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.errorColor
import com.isu.common.utils.FontProvider.INTER
import com.isu.profile.R
import com.isu.profile.presentation.screens.customersupport.presentation.screens.createTicket.RaiseATicketAPIType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author-karthik
 * Composable function to display text within the Profile screen.
 *
 * @param modifier Modifier to apply to this composable.
 * @param text The text content to display.
 * @param fontSize The size of the font for the text.
 * @param fontFamily The font family to use for the text.
 * @param textAlign The alignment of the text.
 * @param color The color of the text.
 * @param lineHeight The line height of the text.
 * @param fontWeight The weight of the font for the text.
 */
@Composable
fun ProfileText(
    modifier: Modifier = Modifier,
    text: String = "",
    fontSize: TextUnit = 14.sp.noFontScale(),
    fontFamily: String = INTER,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = authTextColor,
    lineHeight: TextUnit = 1.0.em,
    fontWeight: FontWeight = FontWeight.Medium,
) {
    CustomText(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        fontFamily = fontFamily,
        textAlign = textAlign,
        color = color,
        lineHeight = lineHeight,
        fontWeight = fontWeight
    )
}

/**
 * Composable function for displaying the Profile screen.
 *
 * @param modifier Modifier to apply to this composable.
 * @param state The state containing the profile data.
 * @param event Function to handle screen events.
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileState,
    event: (CommonScreenEvents) -> Unit,
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (state.requireProfileFetching) {
            event(CommonScreenEvents.CallApi<Any>(apiType = RaiseATicketAPIType.FetchProfileData) {})
            event(CommonScreenEvents.CallApi<Any>(apiType = RaiseATicketAPIType.FetchFormData){})
        }
    }

    Scaffold(containerColor = Color.White) {
        KeyBoardAwareScreen(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 22.dp,
                bottom = it.calculateBottomPadding()
            ),
            shouldScroll = false
        ) {
            ProfileText(
                text = stringResource(R.string.my_account),
                color = Color.Black,
                fontSize = 20.sp.noFontScale(),
                fontWeight = FontWeight(550)
            )
            Spacer(Modifier.height(5.dp))
            ProfileImageSection(state = state.usernae)

            Column {
                GeneralInfoComponent(event = { event(CommonScreenEvents.OnClick<Any>(it)) })
                SupportInfoComponent(event = { event(CommonScreenEvents.OnClick<Any>(it)) })
                CompanyInfoComponent(event = { event(CommonScreenEvents.OnClick<Any>(it)) })
            }

            ProfileText(
                text = stringResource(R.string.logout),
                color = errorColor,
                fontSize = 16.sp.noFontScale(),
                modifier = Modifier.clickable {
                    scope.launch {
                        LogOutEvent.logOut()
                    }
                }
            )
        }
    }
}

/**
 * Composable function to display the profile image section.
 *
 * @param edit Indicates whether the profile image can be edited.
 */
@Composable
fun ProfileImageSection(edit: Boolean = false, state: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val isLoading = remember { mutableStateOf(false) }
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            LoadingErrorEvent.helper.loadingErrorEvent.collectLatest { type ->
                when (type) {
                    is LoadingErrorEvent.errorEncountered -> { /* Handle error */ }
                    is LoadingErrorEvent.isLoading -> {
                        isLoading.value = type.isLoading
                    }
                }
            }
        }

        val cameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                if (it) { /* Handle picture taken */ }
            }

        Box(
            Modifier
                .padding(top = 15.dp)
                .size(70.dp)
                .background(color = Color.Red, shape = CircleShape)
        ) {
            if (isLoading.value) {
                ShimmerComposable {
                    Box(modifier = Modifier.size(200.dp).background(brush = it, CircleShape))
                }
            }

            Image(
                painter = painterResource(com.isu.common.R.drawable.profile_img),
                contentDescription = "",
                Modifier
                    .background(color = Color.Transparent, shape = CircleShape)
                    .clip(CircleShape)
            )

            if (edit) {
                IconButton(
                    onClick = {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            cameraLauncher.launch(Uri.EMPTY)
                        }

                    },
                    Modifier
                        .shadow(5.dp, CircleShape)
                        .background(Color.White, shape = CircleShape)
                        .align(Alignment.BottomEnd)
                        .size(25.dp)
                        .padding(5.dp)
                ) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "")
                }
            }
        }

        if (isLoading.value) {
            ShimmerComposable {
                ProfileText(
                    fontFamily = INTER,
                    text = "",
                    color = Color.Black,
                    fontSize = 20.sp.noFontScale(),
                    fontWeight = FontWeight(500),
                    modifier = Modifier.background(brush = it)
                )
            }
        }

        ProfileText(
            fontFamily = INTER,
            text = state,
            color = Color.Black,
            fontSize = 20.sp.noFontScale(),
            fontWeight = FontWeight(500)
        )
    }
}

/**
 * Composable function to display the General Information section.
 *
 * @param event Function to handle click events.
 */
@Composable
fun GeneralInfoComponent(event: (ProfileClickables) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        ProfileText(
            text = stringResource(R.string.general),
            fontSize = 12.sp.noFontScale(),
            color = authTextColor.copy(0.7f)
        )
        Spacer(modifier = Modifier.height(5.dp))
        ProfileDetailItem(
            text = stringResource(R.string.basic_info),
            onClick = { event(ProfileClickables.BasicInfo) })
        ProfileDetailItem(
            text = stringResource(R.string.notification_setting),
            onClick = { event(ProfileClickables.Notification) }
        )
        ProfileDetailItem(
            text = "Order History",
            onClick = { event(ProfileClickables.OrderHistory) }
        )
    }
}

/**
 * Composable function to display the Support Information section.
 *
 * @param event Function to handle click events.
 */
@Composable
fun SupportInfoComponent(event: (ProfileClickables) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        ProfileText(
            text = stringResource(R.string.support),
            fontSize = 12.sp.noFontScale(),
            color = authTextColor.copy(0.7f)
        )
        Spacer(modifier = Modifier.height(5.dp))
        ProfileDetailItem(
            text = stringResource(R.string.customer_support),
            onClick = { event(ProfileClickables.CustomerSupport) }
        )
        ProfileDetailItem(text = stringResource(R.string.complete_full_kyc))
    }
}

/**
 * Composable function to display the Company Information section.
 *
 * @param event Function to handle click events.
 */
@Composable
fun CompanyInfoComponent(event: (ProfileClickables) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
    ) {
        ProfileText(
            text = stringResource(R.string.company),
            fontSize = 12.sp.noFontScale(),
            color = authTextColor.copy(0.7f)
        )
        Spacer(modifier = Modifier.height(5.dp))
        ProfileDetailItem(
            text = stringResource(R.string.about_us),
            onClick = { event(ProfileClickables.AboutUs) }
        )
        ProfileDetailItem(
            text = stringResource(R.string.privacy_policy),
            onClick = { event(ProfileClickables.PrivacyPolicy) }
        )
        ProfileDetailItem(
            text = stringResource(R.string.terms_conditions),
            onClick = { event(ProfileClickables.TermsAndCondition) }
        )
        ProfileDetailItem(
            text = stringResource(R.string.get_in_touch_profile),
            onClick = { event(ProfileClickables.GetInTouch) }
        )
    }
}

/**
 * Composable function to display an item within the profile details section.
 *
 * @param text The text content to display for the item.
 * @param onClick Function to handle the click event for the item.
 */
@Composable
fun ProfileDetailItem(text: String, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProfileText(
            color = Color.Black,
            fontSize = 14.sp.noFontScale(),
            fontWeight = FontWeight(400),
            text = text
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "",
            tint = Color.DarkGray
        )
    }
}
