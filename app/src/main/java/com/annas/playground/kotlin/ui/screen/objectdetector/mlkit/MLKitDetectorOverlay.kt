package com.annas.playground.kotlin.ui.screen.objectdetector.mlkit

import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.annas.playground.kotlin.ui.screen.objectdetector.tensorflow.compose.VideoOverlay
import java.util.Locale


@Suppress("MagicNumber")
@Composable
fun MLKitDetectorOverlay(
    detectedObject: ArObject?,
    modifier: Modifier = Modifier
) {

//    var drawableRect by remember { mutableStateOf<RectF?>(null) }
    var currentObjectId by remember { mutableIntStateOf(detectedObject?.trackingId ?: 0) }

    val xPos = remember { Animatable(0f) }
    val yPos = remember { Animatable(0f) }

    LaunchedEffect(detectedObject) {
        if (currentObjectId != detectedObject?.trackingId || currentObjectId == 0) {
            xPos.animateTo(detectedObject?.boundingBox?.left ?: 0f)
            yPos.animateTo(detectedObject?.boundingBox?.top ?: 0f)
            currentObjectId = detectedObject?.trackingId ?: 0
        }
    }
    if (detectedObject != null) Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width//.toInt()
            val canvasHeight = size.height//.toInt()

            val scaleWidth = canvasWidth * 1f / detectedObject.sourceSize.width
            val scaleHeight = canvasHeight * 1f / detectedObject.sourceSize.height

            val boundingBox = detectedObject.boundingBox//.toComposeRect()
            val top = boundingBox.top * scaleHeight
            val bottom = boundingBox.bottom * scaleHeight
            val left = boundingBox.left * scaleWidth
            val right = boundingBox.right * scaleWidth
            val rect = RectF(left, top, right, bottom)
//            drawableRect = RectF(boundingBox.left, top, right, bottom)
            val size = Size(rect.width(), rect.height())
            drawRect(
                color = Color.Red,
                topLeft = Offset(left, top),
                size = size,
                style = Stroke(width = 20f)
            )

            // Create text to display alongside detected objects
            detectedObject.labels.firstOrNull()?.let { label ->
                val score = String.format(Locale.getDefault(), "%.2f", label.confidence)
                val drawableText = "${label.text} $score"
                val textPaint = Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 50f
                    typeface = Typeface.DEFAULT_BOLD
                }
                drawContext.canvas.nativeCanvas.drawText(
                    drawableText,
                    left,
                    top,
                    textPaint,
                )
            }

        }

        VideoOverlay(
            modifier = Modifier,
            xPos, yPos
        )
    }
}
