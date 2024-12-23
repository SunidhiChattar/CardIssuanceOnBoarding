package com.isu.cardissuanceonboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.cardissuanceonboarding.R
import com.isu.cardissuanceonboarding.presentation.MainActivity
import com.isu.cardissuanceonboarding.ui.theme.CardIssuanceTheme
import com.isu.common.customcomposables.CustomButton
import com.isu.common.customcomposables.CustomText
import com.isu.common.customcomposables.ErrorCard
import com.isu.common.customcomposables.noFontScale

class CrashHandlingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val crashInfo = intent.getStringExtra(getString(R.string.unknown_exception))


        setContent {
            val showDialog = remember {
                mutableStateOf(false)
            }
            val rememberCounter = remember {
                mutableStateOf(0)
            }
            if (showDialog.value) {
                ErrorCard(
                    text = crashInfo.toString(),
                    btnComposable = {
                        CustomButton(text = "Ok", color = Color.Red.copy(0.7f), onClick = {
                            rememberCounter.value = 0
                            showDialog.value = false
                        })
                    }
                )
            }
            CardIssuanceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(22.dp)) {
                        Image(
                            painter = painterResource(id = com.isu.common.R.drawable.maintenance_color),
                            contentDescription = "",
                            modifier = Modifier.clickable(interactionSource = remember {
                                MutableInteractionSource()
                            }, indication = null) {
                                rememberCounter.value++
                                if (rememberCounter.value == 7) {
                                    showDialog.value = true
                                }
                            })
                        Spacer(modifier = Modifier.height(20.dp))
                        CustomText(
                            text = "This feature is under maintenance.Please restart the app",
                            fontSize = 20.sp.noFontScale(),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        CustomButton(
                            text = "Restart",
                            modifier = Modifier.width(300.dp),
                            onClick = {

                                startActivity(
                                    Intent(
                                        this@CrashHandlingActivity,
                                        MainActivity::class.java
                                    )
                                )
                                finish()
                            })
                    }
                }
            }
        }
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CardIssuanceTheme {
        Greeting("Android")
    }
}