package com.annas.playground.ui.screen.objectdetector.tensorflow.compose

import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.annas.playground.helper.DetectedObject
import java.util.Locale
import kotlin.math.max


@Suppress("MagicNumber")
@Composable
fun CameraDetectorOverlay(
    detectedObjects: List<DetectedObject>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            detectedObjects.forEachIndexed { index, obj ->
                val scaleFactor = max(
                    canvasWidth * 1f / obj.tensorImage.width.toFloat(),
                    canvasHeight * 1f / obj.tensorImage.height.toFloat()
                )

                val boundingBox = obj.detection.boundingBox
                val top = boundingBox.top * scaleFactor
                val bottom = boundingBox.bottom * scaleFactor
                val left = boundingBox.left * scaleFactor
                val right = boundingBox.right * scaleFactor

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
}
