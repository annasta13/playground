package com.annas.playground.ui.components.zoomableimage

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View

fun interface OnTouchCoordinatesListener {
    fun onTouchCoordinate(view: View, event: MotionEvent, bitmapPoint: PointF)
}
