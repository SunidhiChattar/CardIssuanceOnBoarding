package com.isu.common.customcomposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.isu.common.R
import com.isu.common.ui.theme.authTextColor

@Composable
fun ErrorCard(
    modifier: Modifier = Modifier,
    text: String = "",
    onOkClick: () -> Unit = {},
    onRetry: () -> Unit = {},
) {

    val wantToReadMore = remember {
        mutableStateOf(false)
    }
    val isOverflown = remember {
        mutableStateOf(false)
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            onClick = {}, modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (!wantToReadMore.value) {
                        Modifier.height(350.dp)
                    } else {
                        Modifier.height(screenHeight.dp - 100.dp)
                    }
                ), colors = CardDefaults.cardColors(Color.White)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.error_bg),
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp)
                        .scale(1.9f)
                        .rotate(15f)

                        .align(
                            Alignment.CenterEnd
                        ),
                    alpha = 0.9f
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color.Red.copy(alpha = 0.7f))
                        .padding(30.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Oops!",
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight(600)
                    )
                }
            }
            Column(
                modifier = Modifier.wrapContentHeight().padding(10.dp).verticalScroll(
                    rememberScrollState()
                ), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(
                        text = text,
                        softWrap = true,
                        color = Color.Black,
                        modifier = Modifier.then(
                            if (!wantToReadMore.value) {
                                Modifier.height(120.dp)
                            } else {
                                Modifier
                                    .heightIn(screenHeight.dp - 200.dp)

                            }
                        ),
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = { textLayoutResult: TextLayoutResult ->
                            // Check if the text is overflowing
                            isOverflown.value = textLayoutResult.hasVisualOverflow
                        }
                    )


                }
                if (isOverflown.value || wantToReadMore.value) {
                    Text(
                        text = if (wantToReadMore.value) "Read less" else "Read more",
                        color = Color.Red,
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color.Red, RoundedCornerShape(50.dp)
                            )
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .clickable { wantToReadMore.value = !wantToReadMore.value },
                    )
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(20.dp), verticalAlignment = Alignment.CenterVertically
                )
                {
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onOkClick() },
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                Color.Red
                            )
                        ) {
                            Text(text = "Ok", color = Color.White)
                        }
                        OutlinedButton(
                            onClick = { onRetry() },
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White
                            ),
                            border = BorderStroke(2.dp, Color.Red)
                        ) {
                            Text(text = "Retry", color = Color.Red)
                        }
                    }
                }
            }


        }
    }

}

@Composable
fun ErrorCardWithCustomButtons(
    modifier: Modifier = Modifier,
    text: String = "",
    onOkClick: @Composable () -> Unit = {},
    onRetry: @Composable () -> Unit = {},
) {

    val wantToReadMore = remember {
        mutableStateOf(false)
    }
    val isOverflown = remember {
        mutableStateOf(false)
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            onClick = {}, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), colors = CardDefaults.cardColors(Color.White)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.error_bg),
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp)
                        .scale(1.9f)
                        .rotate(15f)

                        .align(
                            Alignment.CenterEnd
                        ),
                    alpha = 0.9f
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color.Red.copy(alpha = 0.7f))
                        .padding(30.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Oops!",
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight(600),

                    )
                }
            }
            Column(
                modifier = Modifier.wrapContentHeight().padding(10.dp).verticalScroll(
                    rememberScrollState()
                ), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(
                        text = text,
                        softWrap = true,
                        color = Color.Black,
                        modifier = Modifier.wrapContentHeight(),
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = { textLayoutResult: TextLayoutResult ->
                            // Check if the text is overflowing
                            isOverflown.value = textLayoutResult.hasVisualOverflow
                        }
                    )


                }
                if (isOverflown.value || wantToReadMore.value) {
                    Text(
                        text = if (wantToReadMore.value) "Read less" else "Read more",
                        color = Color.Red,
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color.Red, RoundedCornerShape(50.dp)
                            )
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .clickable { wantToReadMore.value = !wantToReadMore.value },
                    )
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(20.dp), verticalAlignment = Alignment.CenterVertically
                )
                {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        onOkClick()
                        onRetry()
                    }
                }
            }


        }
    }

}

@Composable
fun ErrorCard(
    modifier: Modifier = Modifier,
    text: String = "",
    btnComposable: @Composable () -> Unit = {},
) {

    val wantToReadMore = remember {
        mutableStateOf(false)
    }
    val isOverflown = remember {
        mutableStateOf(false)
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            onClick = {}, modifier = Modifier
                .width(280.dp)
                .then(
                    if (!wantToReadMore.value) {
                        Modifier.heightIn(200.dp)
                    } else {
                        Modifier.height(screenHeight.dp - 100.dp)
                    }
                ), colors = CardDefaults.cardColors(Color.White)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.failure_dialog),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()


                        .align(
                            Alignment.Center
                        ).aspectRatio(1f),
                    contentScale = ContentScale.FillWidth,
                    alpha = 0.9f
                )

            }
            Column(
                modifier = Modifier.wrapContentHeight().verticalScroll(
                    rememberScrollState()
                ).padding(top = 40.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = text,
                        fontSize = 15.sp.noFontScale(),
                        fontWeight = FontWeight(650),
                        softWrap = true,
                        textAlign = TextAlign.Center,
                        color = authTextColor,
                        modifier = Modifier.then(
                            if (!wantToReadMore.value) {
                                Modifier.heightIn(10.dp)
                            } else {
                                Modifier
                                    .heightIn(screenHeight.dp - 200.dp)

                            }
                        ),
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = { textLayoutResult: TextLayoutResult ->
                            // Check if the text is overflowing
                            isOverflown.value = textLayoutResult.hasVisualOverflow
                        }
                    )


                }
                if (isOverflown.value || wantToReadMore.value) {
                    Text(
                        text = if (wantToReadMore.value) "Read less" else "Read more",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .border(
                                1.dp,
                                Color.Red, RoundedCornerShape(50.dp)
                            )
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .clickable { wantToReadMore.value = !wantToReadMore.value },
                    )
                }
                Spacer(Modifier.height(50.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                )
                {

                Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        btnComposable()
                    }
                }
            }


        }
    }

}