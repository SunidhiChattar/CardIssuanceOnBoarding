package com.isu.prepaidcard.presentation.screens.dashboard.cardstatus

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.customcomposables.CardCustomText
import com.isu.common.customcomposables.CardStatusDialog
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.StatusColorComponent
import com.isu.common.customcomposables.noFontScale
import com.isu.common.ui.theme.ActiveTextBackground
import com.isu.common.ui.theme.TextGreen
import com.isu.common.ui.theme.authTextColor
import com.isu.common.utils.CardStatus
import com.isu.common.utils.ManageFeatures
import com.isu.prepaidcard.presentation.viewmodels.CardStatusViewModel

/**
 * This composable function represents the card status screen.
 *
 * @param cardStatusViewModel ViewModel containing card status information and logic.
 *
 * The screen displays the current card status and allows the user to update it
 * if the card is stolen, lost, or inaccessible. Users can't edit the current status
 * directly but can choose from a list of predefined statuses. Selecting a new status
 * will trigger a confirmation dialog explaining the chosen status and its implications.
 * Upon confirmation, the new status is saved, and the screen reflects the update.
 *
 * The screen consists of the following elements:
 *  - CustomProfileTopBar with the title "Card Status".
 *  - A column containing the main content:
 *      - Text explaining the purpose of the screen.
 *      - A disabled text field displaying the current card status.
 *      - A list of selectable text fields representing available card statuses
 *        (excluding the current one). Selecting a new status triggers the confirmation dialog.
 *      - A confirmation dialog (conditionally displayed) explaining the chosen status
 *        and prompting for confirmation.
 *  - Two buttons at the bottom:
 *      - "Save" button: Saves the new card status and navigates back.
 *      - "Cancel" button: Navigates back without saving any changes.
 */
@Composable
fun CardStatus(cardStatusViewModel: CardStatusViewModel) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        cardStatusViewModel.getStatus()
    }

    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Card Status"
            )
        },
        containerColor = Color.White
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        if (cardStatusViewModel.cardStatus.value) {
            CardStatusDialog(
                cardTitle = cardStatusViewModel.title.value,
                cardDescription = cardStatusViewModel.description.value,
                titleImage = cardStatusViewModel.image.value,
                confirmTitle = cardStatusViewModel.confirmTitle.value,
                onConfirm = {

                    cardStatusViewModel.cardStatus.value = false
                    cardStatusViewModel.changeCardStatus(
                        requestedStatus = if (cardStatusViewModel.requestedStatusChange.value == ManageFeatures.DE_ACTIVATE) CardStatus.DEACTIVATE.value
                        else if (cardStatusViewModel.requestedStatusChange.value == ManageFeatures.TEMP_BLOCK) CardStatus.TEMP_BLOCK.value
                        else if (cardStatusViewModel.requestedStatusChange.value == ManageFeatures.LOST) CardStatus.LOST.value
                        else if (cardStatusViewModel.requestedStatusChange.value == ManageFeatures.STOLEN) CardStatus.STOLEN.value
                        else if (cardStatusViewModel.requestedStatusChange.value == ManageFeatures.DAMAGE) CardStatus.DAMAGED.value
                        else CardStatus.ACTIVATE.value,
                        onSuccess = {}
                    )

                },
                onDismiss = { cardStatusViewModel.cardStatus.value = false })
        }
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding()).background(Color.White)
                .heightIn(screenHeight.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                CardCustomText(
                    text = "Update the status of the card in case the card is stolen, lost or inaccessible",
                    modifier = Modifier,
                    color = authTextColor, fontSize = 14.sp.noFontScale()
                )
                if (cardStatusViewModel.cardStatusCheck.size != 0) {
                    CustomInputField(
                        modifier = Modifier.height(64.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {},
                        enabled = false,
                        placeholder = cardStatusViewModel.currentStatusToCheck.value,
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = {
                            Box(
                                modifier = Modifier.padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                StatusColorComponent(
                                    status = "current status",
                                    color = ActiveTextBackground,
                                    textColor = TextGreen,
                                    withCircle = false
                                )
                            }

                        },
                        labelRequired = false,
                    )
                }

                cardStatusViewModel.cardStatusCheck.filter { !it.equals(cardStatusViewModel.requestedStatusChange.value) }
                    .forEach {
                        CustomInputField(modifier = Modifier.height(64.dp).clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource()}) {
                            cardStatusViewModel.requestedStatusChange.value = it.text
                            it.status = true
                            cardStatusViewModel.handleFeatureClick(it.text, status = it.status)
                            cardStatusViewModel.dialogStatusCheck(cardStatusViewModel.requestedStatusChange.value)
                        },
                            state = it.text,
                            enabled = false,
                            shape = RoundedCornerShape(10.dp),
                            placeholder = "",
                            labelRequired = false
                        )


                    }
            }


            /*Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomResizeButton(modifier = Modifier.weight(1f).height(40.dp),
                    color = appMainColor,
                    text = "Save",
                    onClick = {
                        scope.launch { NavigationEvent.helper.emit(NavigationEvent.NavigateBack) }
                    })
                CustomResizeButton(modifier = Modifier.weight(1f).height(40.dp).border(
                    0.5.dp, authTextColor,
                    RoundedCornerShape(5.dp)
                ), color = Color.White, innerComponent = {
                    CustomText(text = "Cancel", modifier = Modifier, color = authTextColor)
                },
                    onClick = {
                        scope.launch { NavigationEvent.helper.emit(NavigationEvent.NavigateBack) }
                    })
            }*/
        }
    }
}