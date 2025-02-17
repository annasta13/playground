package com.annas.playground.kotlin.ui.screen.objectdetector.mlkit

import android.graphics.RectF
import android.util.Size
import timber.log.Timber

class ScaleHelper(
    private val targetWidth: Int,
    private val targetHeight: Int,
    private val frontFacing: Boolean = false
) {

    fun resize(arObject: ArObject): ArObject {
        Timber.i("boundingBoxStart = ${arObject.boundingBox}")

        // Rotate Size
        val rotatedSize = when (arObject.sourceRotationDegrees) {
            90, 270 -> Size(arObject.sourceSize.height, arObject.sourceSize.width)
            0, 180 -> arObject.sourceSize
            else -> throw IllegalArgumentException("Unsupported rotation. Must be 0, 90, 180 or 270")
        }
        Timber.d("Mapping from source ${rotatedSize.width}x${rotatedSize.height} to ${targetWidth}x$targetHeight")


        // Calculate scale
        val scaleX = targetWidth / rotatedSize.width.toDouble()
        val scaleY = targetHeight / rotatedSize.height.toDouble()
        val scale = Math.max(scaleX, scaleY)
        val scaleF = scale.toFloat()
        val scaledSize = Size(
            Math.ceil(rotatedSize.width * scale).toInt(),
            Math.ceil(rotatedSize.height * scale).toInt()
        )
        Timber.d("Use scale=$scale, scaledSize: ${scaledSize.width}x${scaledSize.height}")


        // Calculate offset (we need to center the overlay on the target)
        val offsetX = (targetWidth - scaledSize.width) / 2
        val offsetY = (targetHeight - scaledSize.height) / 2
        Timber.d("Use offsetX=$offsetX, offsetY=$offsetY")

        // Map bounding box
        val mappedBoundingBox = RectF().apply {
            left = arObject.boundingBox.right * scaleF + offsetX
            top = arObject.boundingBox.top * scaleF + offsetY
            right = arObject.boundingBox.left * scaleF + offsetX
            bottom = arObject.boundingBox.bottom * scaleF + offsetY
        }

        // The front facing image is flipped, so we need to mirrow the positions on the vertical axis (centerX)
        if (frontFacing) {
            val centerX = targetWidth / 2
            mappedBoundingBox.left = centerX + (centerX - mappedBoundingBox.left)
            mappedBoundingBox.right = centerX - (mappedBoundingBox.right - centerX)
        }

        Timber.d("Mapped bounding box=$mappedBoundingBox")

        return arObject.copy(
            boundingBox = mappedBoundingBox,
            sourceSize = Size(targetWidth, targetHeight)
        )
    }
}
