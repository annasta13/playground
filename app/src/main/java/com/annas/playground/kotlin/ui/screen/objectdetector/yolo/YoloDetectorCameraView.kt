package com.annas.playground.kotlin.ui.screen.objectdetector.yolo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.display.DisplayManager
import android.view.Surface
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.annas.playground.kotlin.helper.tensorflow.YoloBoundingBox
import com.annas.playground.kotlin.helper.tensorflow.YoloDetectorHelper

//Source: https://github.com/surendramaran/YOLO/blob/main/YOLOv9-Object-Detector-Android-Tflite/app/src/main/java/com/surendramaran/yolov9tflite/MainActivity.kt
@Suppress("DEPRECATION")
@Composable
fun YoloDetectorCameraView() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val detectedObjects = remember { mutableStateListOf<YoloBoundingBox>() }

    val listener = remember {
        object : YoloDetectorHelper.DetectorListener {
            override fun onEmptyDetect() {
                detectedObjects.clear()
            }

            override fun onDetect(results: List<YoloBoundingBox>, inferenceTime: Long) {
                detectedObjects.clear()
                detectedObjects.addAll(results)
            }
        }
    }

    val rotation = remember { getRotation(context) }
    val objectYoloDetectorHelper = remember {
        YoloDetectorHelper(context = context, detectorListener = listener)
    }

    val isFrontCamera = remember { false }
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
//                        scaleType = PreviewView.ScaleType.FILL_START

                    val preview = Preview.Builder()
                        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                        .setTargetRotation(rotation)
                        .build()
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    val cameraProvider = cameraProviderFuture.get()
                    cameraProviderFuture.addListener({
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        val imageAnalyzer = ImageAnalysis.Builder()
                            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                            .setTargetRotation(rotation)
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
                            .build()
                            .also {
                                it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                                    val bitmapBuffer =
                                        Bitmap.createBitmap(
                                            imageProxy.width,
                                            imageProxy.height,
                                            Bitmap.Config.ARGB_8888
                                        )
                                    imageProxy.use {
                                        bitmapBuffer.copyPixelsFromBuffer(
                                            imageProxy.planes[0].buffer
                                        )
                                    }
                                    imageProxy.close()

                                    val matrix = Matrix().apply {
                                        postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())

                                        if (isFrontCamera) {
                                            postScale(
                                                -1f,
                                                1f,
                                                imageProxy.width.toFloat(),
                                                imageProxy.height.toFloat()
                                            )
                                        }
                                    }

                                    val rotatedBitmap = Bitmap.createBitmap(
                                        bitmapBuffer,
                                        0,
                                        0,
                                        bitmapBuffer.width,
                                        bitmapBuffer.height,
                                        matrix,
                                        true
                                    )

                                    objectYoloDetectorHelper.detect(rotatedBitmap)
                                    imageProxy.close()
                                }
                            }
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalyzer
                        )
                        preview.surfaceProvider = surfaceProvider
                    }, ContextCompat.getMainExecutor(context))
                }
                previewView
            },
            onRelease = { objectYoloDetectorHelper.close() }
        )

        YoloCameraDetectorOverlay(detectedObjects)
    }
}

fun getRotation(context: Context): Int {
    val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    val display = displayManager.getDisplay(android.view.Display.DEFAULT_DISPLAY)
    return display?.rotation ?: Surface.ROTATION_0
}
