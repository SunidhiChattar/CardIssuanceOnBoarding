package com.isu.prepaidcard.presentation.screens.dashboard.addoncard

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomInputField
import com.isu.common.customcomposables.CustomProfileTopBar
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.noFontScale
import com.isu.common.navigation.CardManagement
import com.isu.common.navigation.NavigationEvent
import com.isu.common.ui.theme.CardBlue
import com.isu.common.ui.theme.authTextColor
import com.isu.common.ui.theme.plaeholderColor
import com.isu.common.utils.FontProvider.LATO_FONT
import com.isu.prepaidcard.R
import kotlinx.coroutines.launch

@Composable
fun AddOnCard(addOnCardViewModel: AddOnCardViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val beneNumber = addOnCardViewModel.beneNumber
    val beneNumberError = addOnCardViewModel.beneNumberError
    val beneNumberErrorMessage = addOnCardViewModel.beneNumberErrorMessage
    val config = LocalConfiguration.current
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "https://drive.google.com/file/d/1GSssLFmke1YZPZyVGkgXXr_OSjr_vMRH/view?usp=sharing"
        ) // Replace with your link
        type = "text/plain"
    }
    val shareInstallLink =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            scope.launch {

                NavigationEvent.helper.emit(
                    NavigationEvent.NavigateToNextScreen(
                        CardManagement.AddonCardForSomeoneElseSuccess
                    )
                )
            }
        }
    Scaffold(
        topBar = {
            CustomProfileTopBar(
                text = "Add Card"
            )
        },
        bottomBar = {

        },
        containerColor = Color.White
    ) {
        KeyBoardAwareScreen(
            shouldScroll = false,
            modifier = Modifier.padding(top = it.calculateTopPadding())
        ) {
            Column(modifier = Modifier, verticalArrangement = Arrangement.SpaceBetween) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box(Modifier.widthIn(400.dp, 450.dp).height(230.dp).padding(0.dp)) {
                            Image(
                                painterResource(com.isu.common.R.drawable.card_bg),
                                modifier = Modifier.widthIn(400.dp, 450.dp).height(250.dp)
                                    .aspectRatio(1f),
                                contentScale = ContentScale.FillWidth,
                                contentDescription = null
                            )
                            Column(
                                Modifier.fillMaxSize().padding(22.dp),
                                verticalArrangement = Arrangement.Center
                            ) {

                                Column {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = com.isu.common.R.drawable.chip),
                                            "",
                                            modifier = Modifier.size(50.dp)
                                        )
                                        Image(
                                            painter = painterResource(id = com.isu.common.R.drawable.network),
                                            "",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    CustomText(
                                        text = "XXXX XXXX XXXX XXXX",
                                        fontSize = 20.sp.noFontScale(),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                        Column(
                            modifier = Modifier.border(
                                BorderStroke(
                                    1.dp,
                                    color = authTextColor.copy(0.5f)
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ).padding(5.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                          Row(modifier = Modifier.fillMaxWidth().padding(5.dp),
                              horizontalArrangement = Arrangement.spacedBy(10.dp),
                              verticalAlignment = Alignment.Top) {
                              Box(
                                  modifier = Modifier.padding(top = 5.dp).size(5.dp)
                                      .background(shape = CircleShape, color = authTextColor)
                              )
                              CustomText(
                                  text = "You can only create card for someone below 18 years. Since the invite has to be minor, you will have following controls of the Card.",
                                  fontWeight = FontWeight.Bold
                              )
                          }
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 5.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Image(
                                            painter = painterResource(com.isu.common.R.drawable.visible_icon),
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp)
                                        )
                                        CustomText(
                                            text = "Visibility",
                                            color = CardBlue
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.padding(vertical = 5.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Image(
                                            painter = painterResource(com.isu.common.R.drawable.control),
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp)
                                        )
                                        CustomText(
                                            text = "Manage",
                                            color = CardBlue
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.padding(vertical = 5.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Image(
                                            painter = painterResource(com.isu.common.R.drawable.security),
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp)
                                        )
                                        CustomText(
                                            text = "Security",
                                            color = CardBlue
                                        )
                                    }
                                }
                        }
                    }

                   Row(modifier = Modifier.fillMaxWidth().padding(5.dp),
                       horizontalArrangement = Arrangement.spacedBy(10.dp),
                       verticalAlignment = Alignment.Top) {
                       Box(
                           modifier = Modifier.padding(5.dp).size(5.dp)
                               .background(shape = CircleShape, color = authTextColor)
                       )
                       CustomText(
                           text = "You can only create card for someone below 18 years. Since the invite has to be minor, you will have following controls of the Card.",
                           fontWeight = FontWeight.Bold
                       )
                   }
                    Column(
                        modifier = Modifier.padding(
                            vertical = 10.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CustomInputField(
                            labelRequired = true,
                            labelComponent = {
                                val label = buildAnnotatedString {
                                    append(context.getString(R.string.bene_phone_number))
                                    withStyle(SpanStyle(color = Color.Red, letterSpacing = 10.sp)) {
                                        append(context.getString(R.string.astrisk))
                                    }
                                }
                                CustomText(
                                    text = label,
                                    fontFamily = LATO_FONT,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.height(25.dp).padding(horizontal = 5.dp),
                                    fontSize = 13.sp.noFontScale(),
                                    color = authTextColor
                                )
                            },
                            isError = beneNumberError.value,
                            errorMessage = beneNumberErrorMessage.value,
                            placeholder = "Enter Phone Number",
                            state = beneNumber.value,
                            onValueChange = {
                                if (it.isDigitsOnly() && it.length <= 10) {
                                    if (it.length == 10) {
                                        beneNumber.value = it
                                        beneNumberError.value = false
                                        beneNumberErrorMessage.value = ""

                                    } else {
                                        beneNumber.value = it
                                        beneNumberError.value = true
                                        beneNumberErrorMessage.value =
                                            context.getString(com.isu.common.R.string.mobile_number_should_be_10_digits)
                                    }


                                }

                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                }
                CustomButton(
                    color = if (beneNumber.value.isEmpty()) plaeholderColor else CardBlue,
                    text = "Send Invite Code",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    onClick = {
                        if (beneNumber.value.isNotEmpty()) {
                            addOnCardViewModel.addOnCard(
                                nameOnCard = "",
                                addCardForSelf = false,
                                onSuccess = {
                                    shareInstallLink.launch(shareIntent)

                                }

                            )

                        }
                    }
                )
            }
        }
    }
}