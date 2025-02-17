package com.annas.playground.kotlin.ui.screen.objectdetector.mlkit

import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

//https://github.com/crysxd/CameraX-Object-Tracking
@OptIn(ExperimentalGetImage::class)
@Composable
fun MLKitDetectorScreen(onNavigateBack: () -> Unit) {
    val objectDetector = remember {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE) // Live tracking mode
            .enableClassification() // Optional: categorize objects
            .build()
        ObjectDetection.getClient(options)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    var imageWidth by remember { mutableFloatStateOf(0f) }
    var imageHeight by remember { mutableFloatStateOf(0f) }
    var detectedObject by remember { mutableStateOf<ArObject?>(null) }
    val displayMatrix = LocalConfiguration.current
//    DetectorContainer(onNavigateUp = onNavigateBack, title = "MLKit Detector") {
    Box(modifier = Modifier.fillMaxSize()) {
        val scaleHelper =
            remember {
                ScaleHelper(
                    targetWidth = displayMatrix.screenWidthDp,
                    targetHeight = displayMatrix.screenHeightDp,
                    false
                )
            }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    val preview = Preview.Builder()
                        .setTargetAspectRatio(AspectRatio.RATIO_16_9)
//                        .setTargetResolution(Size(width, height))
                        .build()
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    val cameraProvider = cameraProviderFuture.get()
                    cameraProviderFuture.addListener({
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        val imageAnalyzer = ImageAnalysis.Builder()
                            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
//                            .setTargetResolution(Size(width, height))
//                            .pipe(PositionTranslator(width, height))
//                            .pipe(PathInterpolator())
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                                    val mediaImage = imageProxy.image
                                    val rotation = imageProxy.imageInfo.rotationDegrees
                                    if (mediaImage != null) {
                                        if (imageWidth == 0f && imageHeight == 0f) {
                                            imageWidth = imageProxy.width.toFloat()
                                            imageHeight = imageProxy.height.toFloat()
                                        }
                                        val image = InputImage.fromMediaImage(mediaImage, rotation)
                                        val imageSize = Size(image.width, image.height)
                                        objectDetector.process(image)
                                            .addOnSuccessListener { objects ->
                                                objects.forEach { item ->
                                                    detectedObject = scaleHelper.resize(
                                                        ArObject(
                                                            trackingId = item.trackingId ?: -1,
                                                            boundingBox = item.boundingBox.toRectF() /* The bounding box */,
                                                            sourceSize = imageSize,
                                                            sourceRotationDegrees = rotation,
                                                            labels = item.labels
                                                        )
                                                    )
                                                }
                                            }
                                            .addOnCompleteListener {
                                                imageProxy.close()
                                            }
                                    }
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
            onRelease = { objectDetector.close() }
        )

        MLKitDetectorOverlay(
            detectedObject = detectedObject,
        )
    }
}
