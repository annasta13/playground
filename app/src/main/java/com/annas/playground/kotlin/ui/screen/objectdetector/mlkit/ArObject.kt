package com.annas.playground.kotlin.ui.screen.objectdetector.mlkit

import android.graphics.RectF
import android.util.Size
import com.google.mlkit.vision.objects.DetectedObject

data class ArObject(
    val trackingId: Int,
    val boundingBox: RectF,
    val sourceSize: Size,
    val sourceRotationDegrees: Int,
    val labels: List<DetectedObject.Label>
)
