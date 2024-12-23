package com.isu.common.utils.pdfutils

import android.content.ContentValues
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.IOException
import kotlin.random.Random


fun savePdfWithMediaStore(
    context: Context,
    pdfDocument: PdfDocument,
    onSuccess: (Uri?) -> Unit = {}
): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "GeneratedPdf${Random.nextInt()}.pdf")
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
    }

    val uri =
        context.contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

    try {
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                pdfDocument.writeTo(outputStream)
                onSuccess(it)
                Toast.makeText(context, "PDF saved successfully", Toast.LENGTH_LONG).show()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
    return uri
}