package com.isu.prepaidcard.presentation.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.isu.common.utils.pdfutils.convertPdfToImage
import com.isu.prepaidcard.presentation.viewmodels.StatementViewModel


/**
 * @author Anandeswar Sahu
 * Composable function for displaying a PDF file using Jetpack Compose.
 *
 */
@Composable
fun PDFViewScreen(

    statementViewModel: StatementViewModel,

    ) {
    val context = LocalContext.current
    val filePath: Uri? = statementViewModel.uri.value

    // Convert PDF to Image Bitmap
    val imageBitmap = convertPdfToImage(filePath, context)
    Scaffold(
        topBar = {

        }
    ) {

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
                .padding(top = it.calculateTopPadding())
                .background(Color.White),
        ) {

            imageBitmap?.forEach { image ->
                var scale by remember { mutableStateOf(1f) }
                var offsetX by remember { mutableStateOf(0f) }
                var offsetY by remember { mutableStateOf(0f) }
                Box(
                    modifier = Modifier.fillMaxSize()
//                    .pointerInput(Unit) {
//                    detectTransformGestures { _, pan, zoom, _ ->
//                        scale = (scale * zoom).coerceIn(0.5f, 3f) // Limit zoom levels
//
//                    }
//                }.graphicsLayer(
//                    scaleX = if(scale>1f)scale else 1f,
//                    scaleY = if(scale>1f)scale else 1f,
//                    translationX = offsetX,
//                    translationY = offsetY
//                )
                ) {
                    Box(
                        modifier = Modifier.padding(5.dp).border(1.dp, color = Color.Black)
                            .fillMaxSize()

                    ) {

                        // Display the Image
                        image?.let {
                            Image(
                                bitmap = it.asImageBitmap(), contentDescription = "",
                                modifier = Modifier.fillMaxSize()
                            )
                        }


                    } ?: Text(text = "No image found")
                }

            }
        }

    }
}