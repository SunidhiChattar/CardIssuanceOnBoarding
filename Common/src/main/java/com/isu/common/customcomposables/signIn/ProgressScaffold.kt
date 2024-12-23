import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.isu.common.customcomposables.KeyBoardAwareScreen
import com.isu.common.customcomposables.signIn.AddressScreenState
import com.isu.common.customcomposables.signIn.CardInfoSCreenState
import com.isu.common.customcomposables.signIn.CardInfoScreen
import com.isu.common.customcomposables.signIn.PersonalInfoScreen
import com.isu.common.customcomposables.signIn.PersonalInfoScreenState
import com.isu.common.events.CommonScreenEvents
import com.isu.common.ui.theme.appMainColor

sealed interface SignInScreen {
    data class PersonalInfo(val name: String = "personal") : SignInScreen
    data class CardInfo(val name: String = "card") : SignInScreen
    data class AddressInfo(val name: String = "Address") : SignInScreen
}

@Composable
fun ProgressScaffold(
    modifier: Modifier = Modifier,
    cardInfoState: CardInfoSCreenState,
    personalInfoState: PersonalInfoScreenState,
    addressState: AddressScreenState,
    onEvent: (CommonScreenEvents) -> Unit,
) {

    Scaffold {
        it
        val screenStack = remember {
            mutableStateListOf<String>(SignInScreen.PersonalInfo().name)
        }
        val selectedScreen = remember {
            mutableStateOf(SignInScreen.PersonalInfo().name)
        }
        val context = LocalContext.current

        KeyBoardAwareScreen(backHandler = {
            if (screenStack.size > 1) {
                screenStack.removeLast()
                selectedScreen.value = screenStack.last()
                Toast.makeText(context, "${screenStack}", Toast.LENGTH_LONG).show()
            } else {

                Toast.makeText(context, "empty${screenStack}", Toast.LENGTH_LONG).show()
            }
        }, shouldScroll = false) {


            val listOfPersonalScreenDetail = listOf(
                personalInfoState.email,
                personalInfoState.lastName,
                personalInfoState.firstName,
                personalInfoState.dateOfBirth,
                personalInfoState.gender,
                personalInfoState.phoneNumber
            )
            val listOfCardInfoDetail = listOf(
                cardInfoState.cardNumber,
                cardInfoState.cardType
            )
            val screenOneWeight = remember(personalInfoState) {

                androidx.compose.animation.core.Animatable(
                    if (listOfPersonalScreenDetail.none { it.isNotEmpty() } && listOfPersonalScreenDetail.none { it.isNotEmpty() }) {
                        0f
                    } else {
                        listOfPersonalScreenDetail.filter { it.isNotEmpty() }.size.toFloat() / 6f
                    }

                )

            }
            val screenTwoWeight = remember(cardInfoState) {
                androidx.compose.animation.core.Animatable(
                    if (listOfCardInfoDetail.none { it.isNotEmpty() } && listOfPersonalScreenDetail.none { it.isNotEmpty() }) {
                        0f
                    } else {
                        listOfCardInfoDetail.filter { it.isNotEmpty() }.size.toFloat() / listOfCardInfoDetail.size
                    }

                )
            }
            val screen_three_weight = remember {
                mutableStateOf(
                    0f
                )
            }
            val screen_four_weight = remember {
                mutableStateOf(
                    0f
                )
            }
            Row(
                Modifier.height(70.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(Modifier.weight(1f).height(7.dp).background(
                    color = Color.LightGray.copy(0.5f),
                    RoundedCornerShape(50.dp)
                ).clickable {
                    val name = SignInScreen.PersonalInfo().name
                    selectedScreen.value = name
                    if (screenStack.last() != name) {
                        screenStack.add(name)
                    }
                }) {


                    Row(
                        Modifier.fillMaxWidth(screenOneWeight.value).height(7.dp).background(
                            color = appMainColor,
                            RoundedCornerShape(50.dp)
                        )
                    ) {

                    }
                }
                Row(Modifier.weight(1f).height(7.dp).background(
                    color = Color.LightGray.copy(0.5f),
                    RoundedCornerShape(50.dp)

                ).clickable {
                    val name = SignInScreen.CardInfo().name
                    selectedScreen.value = name
                    if (screenStack.last() != name) {
                        screenStack.add(name)
                    }
                }) {


                    Row(
                        Modifier.fillMaxWidth(screenTwoWeight.value).height(7.dp).background(
                            color = appMainColor,
                            RoundedCornerShape(50.dp)
                        )
                    ) {

                    }
                }
                Row(
                    Modifier.weight(1f).height(7.dp).background(
                        color = Color.LightGray.copy(0.5f),
                        RoundedCornerShape(50.dp)
                    )
                ) {

                    val startAnim = remember {
                        mutableStateOf(false)
                    }
                    val animateFloat =
                        animateFloatAsState(if (startAnim.value) 0f else screen_three_weight.value)
                    Row(
                        Modifier.fillMaxWidth(animateFloat.value).height(7.dp).background(
                            color = appMainColor,
                            RoundedCornerShape(50.dp)
                        )
                    ) {

                    }
                }
                Row(
                    Modifier.weight(1f).height(7.dp).background(
                        color = Color.LightGray.copy(0.5f),
                        RoundedCornerShape(50.dp)
                    )
                ) {

                    val startAnim = remember {
                        mutableStateOf(false)
                    }
                    val animateFloat =
                        animateFloatAsState(if (startAnim.value) 0f else screen_four_weight.value)
                    Row(
                        Modifier.fillMaxWidth(animateFloat.value).height(7.dp).background(
                            color = appMainColor,
                            RoundedCornerShape(50.dp)
                        )
                    ) {

                    }
                }


            }
            AnimatedVisibility(visible = selectedScreen.value == SignInScreen.PersonalInfo().name) {
                PersonalInfoScreen(personalInfoState, onEvent = onEvent) {
                    selectedScreen.value = SignInScreen.CardInfo().name

                    screenStack.add(SignInScreen.CardInfo().name)
                    Toast.makeText(context, "${screenStack}", Toast.LENGTH_LONG).show()
                }
            }
            AnimatedVisibility(visible = selectedScreen.value == SignInScreen.CardInfo().name) {
                CardInfoScreen(cardInfoState, onEvent = onEvent, onClick = {

                })
            }
        }
    }
}