package com.isu.common.customcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isu.common.navigation.NavigationEvent
import kotlinx.coroutines.launch

@Composable
fun CustomTopBar(
    text: String = "",
    onLeadingIconClick: suspend () -> Unit = {
        NavigationEvent.helper.emit(NavigationEvent.NavigateBack)
    },
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onTrailingIconClick: suspend () -> Unit = {

    },
) {
    val scope= rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Row(modifier = Modifier.weight(1f).padding(start = 20.dp)) {
         if(leadingIcon!=null){
          leadingIcon()
         }else{

         }
        }
        Row(
            modifier = Modifier.wrapContentHeight().fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ProfileText(
                text = text,
                color = Color.Black,
                fontSize = 17.sp.noFontScale(),
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier.weight(1f).fillMaxHeight().padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            if(trailingIcon!=null){
            IconButton(
                onClick = {scope.launch {
                   onTrailingIconClick()
                }},
                modifier = Modifier.size(22.dp)
            ) {
                trailingIcon()
                }

            }
        }
    }
}
