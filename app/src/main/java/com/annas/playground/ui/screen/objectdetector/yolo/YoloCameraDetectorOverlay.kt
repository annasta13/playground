package com.annas.playground.ui.screen.objectdetector.yolo

import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.annas.playground.helper.YoloBoundingBox
import java.util.Locale


@Suppress("MagicNumber")
@Composable
fun YoloCameraDetectorOverlay(
    detectedObjects: List<YoloBoundingBox>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier
            .aspectRatio(3f / 4f)
            .fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            detectedObjects.forEachIndexed { index, obj ->
                val left = obj.x1 * canvasWidth
                val top = obj.y1 * canvasHeight
                val right = obj.x2 * canvasWidth
                val bottom = obj.y2 * canvasHeight

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
                    String.format(Locale.getDefault(), "%.2f", obj.cnf)
                val drawableText = "${obj.clsName} $score"
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
