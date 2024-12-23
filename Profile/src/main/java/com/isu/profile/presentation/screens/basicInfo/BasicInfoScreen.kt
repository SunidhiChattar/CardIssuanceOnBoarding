package com.isu.profile.presentation.screens.basicInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.events.CommonScreenEvents
import com.isu.common.ui.theme.authTextColor
import com.isu.profile.R
import com.isu.profile.presentation.screens.customersupport.presentation.screens.profile.ProfileImageSection
import com.isu.profile.presentation.screens.customersupport.presentation.screens.profile.ProfileText

/**
 * @author-karthik
 * Represents an item in the Basic Info screen displaying a key-value pair.
 *
 * @property key The label of the item.
 * @property value The value associated with the key.
 * @property textFieldsType The type of text field associated with the item, if any.
 */
data class BasicInfoItem(
    val key: String = "",
    val value: String = "",
    val textFieldsType: BasicInfoTextField? = null
)

/**
 * Composable function representing the Basic Info screen.
 *
 * @param state The state containing the data to be displayed on the screen.
 * @param event A lambda function to handle screen events.
 */
@Composable
fun BasicInfoScreen(state: BasicInfoState, event: (CommonScreenEvents) -> Unit) {
    val config = androidx.compose.ui.platform.LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val context = LocalContext.current

    val demoList = listOf(
        BasicInfoItem(
            key = stringResource(R.string._key),
            value = state.id,
            textFieldsType = BasicInfoTextField.ID
        ),
        BasicInfoItem(
            key = stringResource(R.string.name),
            value = state.name,
            textFieldsType = BasicInfoTextField.Name
        ),
        BasicInfoItem(
            key = stringResource(R.string.email),
            value = state.email,
            textFieldsType = BasicInfoTextField.Email
        ),
        BasicInfoItem(
            key = stringResource(R.string.mobile_number),
            value = state.mobileNumber,
            textFieldsType = BasicInfoTextField.MobileNumber
        ),
        BasicInfoItem(
            key = stringResource(R.string.kyc),
            value = state.kycStatus,
            textFieldsType = BasicInfoTextField.KycStatus
        ),
        BasicInfoItem(
            key = stringResource(R.string.invoicing_address),
            value = "${state.invoicingAddress}, ${state.city}, ${state.state}, ${state.country}",
            textFieldsType = BasicInfoTextField.InvoicingAddress)
    )

    /**
     * Composable function representing the details section of the Basic Info screen.
     */
    @Composable
    fun BasicInfoDetailsSection() {
        Column {
            demoList.forEach {
                BasicInfoDetailItem(text = it.key, value = it.value)
            }
        }
    }

    Scaffold(
        topBar = { CustomProfileTopBar() },
        containerColor = Color.White
    ) {
        KeyBoardAwareScreen(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                )
        ) {
            Column(
                modifier = Modifier
                    .heightIn(min = screenHeight.dp - 170.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    ProfileImageSection(true, state.name)
                    BasicInfoDetailsSection()
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomButton(
                        innerComponent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                CustomText(
                                    text = stringResource(R.string.edit),
                                    color = Color.White,
                                    fontSize = 14.sp.noFontScale()
                                )
                            }
                        },
                        onClick = { event(CommonScreenEvents.OnClick<Any>(BasicInfoClickable.Edit)) },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * Composable function representing a single detail item in the Basic Info screen.
 *
 * @param text The label of the item.
 * @param value The value associated with the label.
 */
@Composable
fun BasicInfoDetailItem(text: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        ProfileText(
            text = text,
            fontWeight = FontWeight(400),
            fontSize = 13.sp.noFontScale(),
            color = if (text == stringResource(R.string.id)) Color.Black else authTextColor
        )
        ProfileText(
            text = value,
            fontWeight = FontWeight(450),
            fontSize = 14.sp.noFontScale(),
            color = Color.Black,
            lineHeight = 24.sp
        )
    }
}
