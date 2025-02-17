package com.annas.playground.kotlin.ui.screen.objectdetector.imageobject

import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.annas.playground.kotlin.helper.tensorflow.DetectedObject
import java.util.Locale


@Suppress("MagicNumber")
@Composable
fun ImageObjectDetectorOverlayView(
    detectedObjects: List<DetectedObject>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        detectedObjects.forEachIndexed { index, obj ->
            val scaleHeight = canvasHeight * 1f / obj.tensorImageHeight.toFloat()
            val scaleWidth = canvasWidth * 1f / obj.tensorImageWidth.toFloat()
            println("check canvasHeight $canvasHeight canvasWidth $canvasWidth")
            val boundingBox = obj.detection.boundingBox
            val top = boundingBox.top * scaleHeight
            val bottom = boundingBox.bottom * scaleHeight
            val left = boundingBox.left * scaleWidth
            val right = boundingBox.right * scaleWidth

            val drawableRect = RectF(left, top, right, bottom)
            val size = Size(drawableRect.width(), drawableRect.height())
            drawRect(
                color = when (index) {
                    0 -> Color.Red
                    1 -> Color.Gray
                    2 -> Color.Green
                    3 -> Color.Blue
                    else -> Color.Yellow
                },
                topLeft = Offset(left, top),
                size = size,
                style = Stroke(width = 20f)
            )

            // Create text to display alongside detected objects
            val score =
                String.format(Locale.getDefault(), "%.2f", obj.detection.categories[0].score)
            val drawableText = "${obj.detection.categories[0].label} $score"
            val textPaint = Paint().apply {
                color = android.graphics.Color.GRAY
                textSize = 50f
                typeface = Typeface.DEFAULT_BOLD
            }
            drawContext.canvas.nativeCanvas.drawText(
                drawableText,
                left + 15f,
                top + 50f,
                textPaint,
            )
        }
    }
}
