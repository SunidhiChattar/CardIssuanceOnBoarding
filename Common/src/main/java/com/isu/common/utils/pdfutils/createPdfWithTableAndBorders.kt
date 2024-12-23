package com.isu.common.utils.pdfutils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.annotation.ColorInt
import com.isu.common.R
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.min

fun createPdfWithTableAndBorders(
    ind: Int = 0,
    context: Context,
    list: List<List<String>>,
    data: PDFData,
    onSuccess: (Uri?) -> Unit,

) {

    val headers = listOf(
        "ID",
        "       Date       ",
        "              URN Number    ",
        " Transaction Description ",
        "Transaction Amount",
        "Credit    ",
        "Available Balance"
    )
    val rows =
        listOf(
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            listOf("1", "12/04/24", "16792422750", "ECOM/PAYU/9375099", "295.00", "295.00"),
            // Add as many rows as needed for testing pagination
        )

    val pdfDocument = PdfDocument()
    val pageWidth = 595
    val pageHeight = 942
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

    var page = pdfDocument.startPage(pageInfo)
    var canvas = page.canvas

    val bannerBorder = 0xFFF2F0F0
    val paint = Paint().apply { color = Color.BLACK; textSize = 12f; typeface = Typeface.DEFAULT }
    val headerPaint = Paint().apply { typeface = Typeface.DEFAULT_BOLD; color = Color.WHITE }
    val borderPaint =
        Paint().apply { color = bannerBorder.toInt(); strokeWidth = 2f; style = Paint.Style.STROKE }
    val demoborder =
        Paint().apply { color = Color.RED.toInt(); strokeWidth = 2f; style = Paint.Style.STROKE }
    val demogreenborder =
        Paint().apply { color = Color.GREEN.toInt(); strokeWidth = 2f; style = Paint.Style.STROKE }
    val headerTablePaint = Paint().apply { color = Color.LTGRAY; style = Paint.Style.FILL }

    val margin = 10f
    val baseRowHeight = 40f
    var yPosition = 100f
    var totalWidth = 0f
    val columnWidths = headers.indices.map { index ->
        if ((headers[index] == " Transaction Description ")) {
            70f
        } else if(headers[index] == "Transaction Amount" || headers[index] == " Available Balance"){
            100f
        }else if (headers[index] == "Credit    ") {
            50f
        } else {
            min(paint.measureText(headers[index]), paint.measureText(headers[index])) + 20
        }

    }
    headers.forEachIndexed { index, header ->

        totalWidth += margin + columnWidths[index]
    }
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.hero_football)
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, false)
    val taBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.talogo
        ), 180, 60, false
    )
    val odishFCBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.odishafclogo
        ), 150, 80, false
    )
    val bannerBackground = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.banner_bg
        ), pageWidth-40, 200, false
    )

    @ColorInt
    val textColor = 0xFF546881
    val bannerColor = 0xFF1F1E4A



    canvas.drawBitmap(taBitmap, margin, 10f, null)
    canvas.drawBitmap(odishFCBitmap, pageWidth - 130f, 10f, null)
    drawWrappedText(
        canvas,
        "Transaction Analysts OdishaFC Prepaid Card E-statement",
        margin,
        120f,
        paint,
        pageWidth.toFloat(),
        paint.textSize * 1.5f
    )
    canvas.drawBitmap(bannerBackground, margin+10f, yPosition + 40f, null)

    canvas.drawBitmap(scaledBitmap, 350f, 150f, null)
        canvas.drawText(
            "CardNumber: ${data.cardNumber}",
            margin + 30f,
            margin+130f +30f,
            Paint().apply { color = Color.WHITE })
    canvas.drawText(
            "Registered Email Address: ${data.email}",
            margin + 30f,
            margin+130f +60f,
            Paint().apply { color = Color.WHITE })
    canvas.drawText(
            "Customer Name: ${data.username}",
            margin + 30f,
            margin+130f +90f,
            Paint().apply { color = Color.WHITE })
    canvas.drawText(
            "Customer Address:${data.address}",
            margin + 30f,
            margin+130f +120f,
            Paint().apply { color = Color.WHITE })
    canvas.drawText(
            "Registered Mobile Number: ${data.mobileNumber}",
            margin + 30f,
            margin+130f +150f,
            Paint().apply { color = Color.WHITE })
    yPosition += 300f

    canvas.drawRoundRect(
        margin+20f,
        yPosition -50f,
        margin + totalWidth / 4 - 40f,
        yPosition -10f,
        0f,
        0f,
        Paint().apply {
            color =
                bannerColor.toInt()
        })
    canvas.drawText("Statement Period",
        margin + 30f,
        375f,
        Paint().apply
     { color = Color.WHITE })
    canvas.drawRoundRect(
        margin+20f+2f,
        yPosition -10f,
        totalWidth / 4 - 32f,
        yPosition + 30f,
        0f,
        0f,
        borderPaint
    )

    drawWrappedText(
        canvas = canvas,
        data.stateMentPeriod.split(" to ".toRegex())[0],
        margin + 35f,
        405f,
        maxWidth = 100f,
        lineHeight = 5f,
        paint = Paint().apply
        { color = Color.BLACK })

    drawWrappedText(
        canvas = canvas,
        data.stateMentPeriod.split(" to ".toRegex())[1],
        margin + 35f,
        420f,
        maxWidth = 100f,
        lineHeight = 5f,
        paint = Paint().apply
        { color = Color.BLACK })


    canvas.drawRoundRect(
        margin + totalWidth/4 - 20f,
        yPosition - 50f,
        margin + totalWidth/4+ totalWidth / 4 - 76f,
        yPosition -10f,
        0f,
        0f,
        Paint().apply {
            color =
                bannerColor.toInt()
        })
    drawWrappedText(
        canvas = canvas,
        "Card Status",
        margin + 170f,
        375f,
        maxWidth = 170f,
        lineHeight = 20f,

        paint = Paint().apply
        { color = Color.WHITE })
    canvas.drawRoundRect(
        margin + totalWidth/4 - 20f,
        yPosition -10f,
        margin + totalWidth/4 + totalWidth / 4 - 78f,
        yPosition + 30f,
        0f,
        0f,
        borderPaint
    )
    canvas.drawText(
        data.cardStatus,
        margin + 180f,
        415f,
        Paint().apply
        { color = Color.BLACK })

    canvas.drawRoundRect(
        margin + 2*(totalWidth/4) - 60f,
        yPosition - 50f,
        margin+2*(totalWidth/4)+totalWidth / 4 - 100f,
        yPosition -10f,
        0f,
        0f,
        Paint().apply {
            color =
                bannerColor.toInt()
        })
    canvas.drawText("Card Expiry",
        margin + 310f,
        375f,
        Paint().apply
        { color = Color.WHITE })
    canvas.drawRoundRect(
        margin +  2*(totalWidth/4) - 60f,
        yPosition - 10,
        margin +  2*(totalWidth/4)+ totalWidth / 4 - 98f,
        yPosition + 30f,
        0f,
        0f,
        borderPaint
    )
    canvas.drawText(
        data.cardExpiry,
        margin + 320f,
        415f,
        Paint().apply
        { color = Color.BLACK })

    canvas.drawRoundRect(
        margin + 3*(totalWidth/4) - 80f,
        yPosition - 50f,
        margin + 3*(totalWidth/4)+totalWidth / 4 - 140f,
        yPosition - 10f,
        0f,
        0f,
        Paint().apply {
            color =
                bannerColor.toInt()
        })
    canvas.drawText("Co-Brand Partner",
        margin + 430f,
        375f,
        Paint().apply
        { color = Color.WHITE })
    canvas.drawRoundRect(
        margin +3*(totalWidth/4) - 80f,
        yPosition -10f,
        margin +3*(totalWidth/4)+ totalWidth / 4 - 138f,
        yPosition + 30f,
        0f,
        0f,
       borderPaint
    )
    yPosition += 100

    canvas.drawText("Odisha FC",
        margin + 440f,
        415f,
        Paint().apply
        { color = Color.BLACK })

    canvas.drawText(
        "Transaction Details from ${data.stateMentPeriod} ",
        margin + 20f,
        470f,
        Paint().apply
        { color = textColor.toInt() })

    fun drawHeaderRow() {
        var xPosition = margin


        canvas.drawRoundRect(
            xPosition,
            yPosition,
            totalWidth - 50f,
            yPosition + baseRowHeight + 30f,
            0f,
            0f,
            headerTablePaint
        )
        headers.forEachIndexed { index, header ->
        //            canvas.drawRoundRect(xPosition, yPosition, xPosition + columnWidths[index], yPosition + baseRowHeight+30f,10f,10f, headerTablePaint)
            drawWrappedText(
                canvas,
                header,
                xPosition + 10,
                y = if (headers[index] == " Transaction Description " || headers[index] == "Credit    ") {
                    yPosition + 5f
                }else{
                    yPosition +25f
                },
                headerPaint,
                columnWidths[index] - 20f,
                20f
            )
            xPosition += columnWidths[index]
        }
        yPosition += baseRowHeight + 30f
    }

    fun addNewPage(ind: Int, subList: List<String>, size: Int) {


        val newpage = pdfDocument.startPage(pageInfo)
        canvas = newpage.canvas
        yPosition = 10f
        drawHeaderRow() // Draw the header on each new page
    }

    drawHeaderRow()

    rows.forEachIndexed { ind, row ->
        if (yPosition + baseRowHeight > pageHeight - (margin + 200)) {

        } else {
            val rowcolumnWidths = headers.indices.map { index ->

                if ((headers[index] == " Transaction Description ")) {
                    70f
                } else if (headers[index] == "Transaction Amount" || headers[index] == "Available Balance") {
                    100f
                } else if (headers[index] == "Credit    ") {
                    50f
                } else {
                    min(paint.measureText(headers[index]), paint.measureText(headers[index])) + 20
                }
            }

            var xPosition = margin
            val rowHeight =
                row.map { getTextHeight(it, paint, columnWidths[it.length % rowcolumnWidths.size]) }
                    .maxOrNull() ?: baseRowHeight

            row.forEachIndexed { index, cell ->
                canvas.drawRect(xPosition,
                    yPosition,
                    xPosition + rowcolumnWidths[index],
                    yPosition + rowHeight,
                    borderPaint.apply {
                        color = Color.LTGRAY
                    }
                )
                drawWrappedText(
                    canvas,
                    cell,
                    xPosition + 10,
                    yPosition + 20,
                    paint,
                    rowcolumnWidths[index] - 20f,
                    paint.textSize * 1.5f
                )
                xPosition += rowcolumnWidths[index]
            }
            yPosition += rowHeight
        }

    }
    drawWrappedText(
        canvas,
        "Important Message",
        margin,
        942f - 100f,
        maxWidth = pageWidth.toFloat(),
        paint = paint.apply {
            color = Color.RED
        },
        lineHeight = 30f
    )
    canvas.drawLine(margin, 942f - 85f, pageWidth.toFloat(), 942f - 85f, paint.apply {
        color = Color.RED
    }
    )
    drawWrappedText(
        canvas,
        "1. Safe Banking Tips – Do not transact if you have any suspicious device attached to the ATM/POS machine. Do not take help from strangers in an ATM/POS transactions.",
        margin,
        942f - 65f,
        maxWidth = pageWidth.toFloat(),
        paint = paint.apply {
            color = Color.BLACK

        },
        lineHeight =15f
    )
    drawWrappedText(
        canvas,
        "2. For any clarification or more information you may contact us through “Help and Support”.",
        margin,
        942f - 30f,
        maxWidth = pageWidth.toFloat(),
        paint = paint.apply {
            color = Color.BLACK

        },
        lineHeight = 15f
    )

    pdfDocument.finishPage(page)
    savePdfWithMediaStore(context, pdfDocument) {
        onSuccess(it)
    }
    pdfDocument.close()
}

// Helper functions remain the same as in your original code (getTextHeight and drawWrappedText)

fun drawLimitedText(
    canvas: Canvas,
    text: String,
    x: Float,
    y: Float,
    paint: Paint,
    maxWidth: Float
) {
    var displayText = text
    val ellipsis = "..."
    var textWidth = paint.measureText(displayText)

    // Check if text exceeds max width
    if (textWidth > maxWidth) {
        // Truncate text to fit within max width
        while (textWidth > maxWidth - paint.measureText(ellipsis) && displayText.isNotEmpty()) {
            displayText = displayText.dropLast(1)
            textWidth = paint.measureText(displayText + ellipsis)
        }
        // Append ellipsis to indicate truncation
        displayText += ellipsis
    }

    // Draw the text within the max width
    canvas.drawText(displayText, x, y, paint)
}

fun getTextHeight(text: String, paint: Paint, columnWidth: Float): Float {
    val words = text.split(" ")
    var lines = 1
    var currentLineWidth = 0f
    words.forEach { word ->
        val wordWidth = paint.measureText(word)
        if (currentLineWidth + wordWidth > columnWidth) {
            lines++
            currentLineWidth = wordWidth
        } else {
            currentLineWidth += wordWidth + paint.measureText(" ")
        }
    }
    return lines * paint.textSize * 1.5f // Adjust line height factor as needed
}

fun drawWrappedText(
    canvas: Canvas,
    text: String,
    x: Float,
    y: Float,
    paint: Paint,
    maxWidth: Float,
    lineHeight: Float
) {
    val words = text.split(" ")
    var currentLine = ""
    var yOffset = y

    for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        val testLineWidth = paint.measureText(testLine)

        if (testLineWidth <= maxWidth) {
            // Current line still fits within max width
            currentLine = testLine
        } else {
            // Draw the current line and move to the next
            canvas.drawText(currentLine, x, yOffset, paint)
            yOffset += lineHeight
            currentLine = word
        }
    }
    // Draw the last line if any text remains
    if (currentLine.isNotEmpty()) {
        canvas.drawText(currentLine, x, yOffset, paint)
    }
}

fun convertPdfToImage(filePath: Uri?, context: Context): List<Bitmap?>? {
    val bitMapList = mutableListOf<Bitmap?>()
    if (filePath == null) {
        return null
    }
    try {
        val file = getFileFromMediaStoreUri(context, filePath)
        // Open the PDF file using ParcelFileDescriptor
        val parcelFileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        parcelFileDescriptor.let {
            // Create PdfRenderer to render PDF pages
            val pdfRenderer = PdfRenderer(parcelFileDescriptor)
            if (pdfRenderer.pageCount <= 0) {
                // No pages found in the PDF
                return null
            }
            // Open the first page of the PDF
            for (i in 0..pdfRenderer.pageCount) {
                val currentPage = pdfRenderer.openPage(0)
                // Render the PDF page onto the Bitmap
                val bitmap = Bitmap.createBitmap(
                    currentPage.width + 400,
                    2 * currentPage.height,
                    Bitmap.Config.ARGB_8888
                )

                currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                currentPage.close()
                bitMapList.add(bitmap)
            }

            // Return the rendered Bitmap
            return bitMapList.toList()
        }
    } catch (e: FileNotFoundException) {
        // Handle any exceptions that may occur during the process
        Toast.makeText(context, e.localizedMessage ?: "", Toast.LENGTH_LONG).show()
        return null
    } catch (e: IOException) {
        Toast.makeText(context, e.localizedMessage ?: "", Toast.LENGTH_LONG).show()
        return null
    } catch (e: IllegalStateException) {
        Toast.makeText(context, e.localizedMessage ?: "", Toast.LENGTH_LONG).show()
        return null
    } catch (e: SecurityException) {
        Toast.makeText(context, e.localizedMessage ?: "", Toast.LENGTH_LONG).show()
        return null
    }
}




