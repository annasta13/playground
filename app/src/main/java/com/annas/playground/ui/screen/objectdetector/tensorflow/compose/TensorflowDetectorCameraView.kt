package com.annas.playground.ui.screen.objectdetector.tensorflow.compose

import android.content.Context
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
import com.annas.playground.helper.DetectedObject
import com.annas.playground.helper.ObjectDetectorType
import com.annas.playground.helper.TensorflowDetectorHelper
import com.annas.playground.utils.ContextExtension.toast

@Suppress("DEPRECATION")
/** Tensorflow framework detector camera
 * @param type is one of the following:
 * - [ObjectDetectorType.MODEL_MOBILENET_V1]
 * - [ObjectDetectorType.MODEL_EFFICIENTDET_V0]
 * - [ObjectDetectorType.MODEL_EFFICIENTDET_V1]
 * - [ObjectDetectorType.MODEL_EFFICIENTDET_V2]
 * */
@Composable
fun TensorflowDetectorCameraView(type: Int) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val detectedObjects = remember { mutableStateListOf<DetectedObject>() }

    val listener = remember {
        object : TensorflowDetectorHelper.DetectorListener {
            override fun onError(error: String) {
                context.toast(error)
            }

            override fun onResults(results: List<DetectedObject>) {
                detectedObjects.clear()
                detectedObjects.addAll(results)
            }
        }
    }

    val rotation = remember { getRotation(context) }
    val objectDetector = remember {
        TensorflowDetectorHelper(
            currentModel = type,
            context = context,
            listener = listener
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_START

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
                                it.setAnalyzer(ContextCompat.getMainExecutor(context)) { image ->
                                    objectDetector.detect(image)
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
            onRelease = { objectDetector.clearObjectDetector() }
        )

        CameraDetectorOverlay(detectedObjects)
    }
}

fun getRotation(context: Context): Int {
    val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    val display = displayManager.getDisplay(android.view.Display.DEFAULT_DISPLAY)
    return display?.rotation ?: Surface.ROTATION_0
}
