/*
 * Copyright (c) Habil Education 2023. All rights reserved.
 */

@file:Suppress("DEPRECATION")

package com.annas.playground.ui.screen.objectdetector.imageobject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.annas.playground.constants.IntConstant.BITMAP_QUALITY
import com.annas.playground.utils.LongExtension.toDateString
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


@SuppressLint("ViewConstructor")
class ScreenshotView(
    context: Context,
    view: @Composable () -> Unit,
    onBitmapCreated: (Bitmap) -> Unit
) : FrameLayout(context) {
    init {
        val display = (context as Activity).windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val heightPixels = outMetrics.heightPixels.toFloat()

        val width = widthPixels.toInt()
        val height = heightPixels.toInt()

        val contentView = ComposeView(context)
        contentView.visibility = View.GONE
        contentView.layoutParams = LayoutParams(width, height)
        this.addView(contentView)
        contentView.setContent {
            view()
        }
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val bitmap =
                    createBitmapFromView(
                        view = contentView,
                        width = width,
                        height = height
                    )
                val dir = context.cacheDir
                val file = File(dir, System.currentTimeMillis().toDateString() + ".png")
                val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
                bitmap.compress(Bitmap.CompressFormat.PNG, BITMAP_QUALITY, os)
                os.close()
                onBitmapCreated(bitmap)
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    fun createBitmapFromView(view: View, width: Int, height: Int): Bitmap {
        view.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        view.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )

        view.layout(0, 0, width, height)

        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        canvas.setBitmap(bitmap)
        view.draw(canvas)

        return bitmap
    }
}

