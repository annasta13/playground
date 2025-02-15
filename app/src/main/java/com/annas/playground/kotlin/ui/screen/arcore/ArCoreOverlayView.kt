/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.annas.playground.kotlin.ui.screen.arcore

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.annas.playground.kotlin.helper.tensorflow.DetectedObject
import java.util.LinkedList
import java.util.Locale
import kotlin.math.max

@Suppress("MagicNumber")
class ArCoreOverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var results: List<DetectedObject> = LinkedList<DetectedObject>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var scaleFactor: Float = 1f

    private var bounds = Rect()

    init {
        initPaints()
    }

    fun clear() {
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = Color.YELLOW//ContextCompat.getColor(context!!, Color.YELLOW)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        //Image Object
        //check scaleHeight 0.318555 scaleWidth 0.33333334
        //check scaleHeight 0.318555 scaleWidth 0.31851852
        ////check canvasHeight 2328.0 canvasWidth 1080.0
        //check canvasHeight 2328.0 canvasWidth 1032.0
        //check tensorImage width 480 height 640
        //check result box RectF(68.0, 333.0, 290.0, 570.0)


        //ArCore
        //check scaleHeight 3.80625 scaleWidth 2.25
        //check height 2436 width 1080
        //check tensorImage width 480 height 640
        for (item in results) {
            val result = item.detection
            val boundingBox = result.boundingBox
            val scaleHeight = height * 1f / item.tensorImageHeight.toFloat()
            val scaleWidth = width * 1f / item.tensorImageWidth.toFloat()
            println("check height $height width $width")
            val top = boundingBox.top * scaleHeight
            val bottom = boundingBox.bottom * scaleHeight
            val left = boundingBox.left * scaleWidth
            val right = boundingBox.right * scaleWidth

            // Draw bounding box around detected objects
            val drawableRect = RectF(left, top, right, bottom)
            canvas.drawRect(drawableRect, boxPaint)

            // Create text to display alongside detected objects
            val drawableText = result.categories[0].label + " " + String.format(
                Locale.getDefault(),
                "%.2f",
                result.categories[0].score
            )

            // Draw rect behind display text
            textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()
            canvas.drawRect(
                left,
                top,
                left + textWidth + BOUNDING_RECT_TEXT_PADDING,
                top + textHeight + BOUNDING_RECT_TEXT_PADDING,
                textBackgroundPaint
            )

            // Draw text for detected object
            canvas.drawText(drawableText, left, top + bounds.height(), textPaint)
        }
    }

    fun setResults(detectionResults: List<DetectedObject>) {
        results = detectionResults
        val imageWidth = results.firstOrNull()?.tensorImageWidth ?: 1080
        val imageHeight = results.firstOrNull()?.tensorImageHeight ?: 1920

        // PreviewView is in FILL_START mode. So we need to scale up the bounding box to match with
        // the size that the captured images will be displayed.
        scaleFactor = max((width / imageWidth).toFloat(), (height / imageHeight).toFloat())
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}
