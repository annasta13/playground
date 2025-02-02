package com.annas.playground.ui.screen.objectdetector.tensorflow.binding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.annas.playground.databinding.FragmentCameraBinding
import com.annas.playground.helper.DetectedObject
import com.annas.playground.helper.TensorflowDetectorHelper
import com.annas.playground.ui.screen.objectdetector.tensorflow.compose.getRotation
import com.annas.playground.utils.ContextExtension.toast

@Suppress("unused", "DEPRECATION")
@Composable
fun BindingCameraView(modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val composeContext = LocalContext.current
    val rotation = remember { getRotation(composeContext) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val binding = FragmentCameraBinding.inflate(inflater).apply {
                    root.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val listener = object : TensorflowDetectorHelper.DetectorListener {
                        override fun onError(error: String) {
                            context.toast(error)
                        }

                        override fun onResults(results: List<DetectedObject>) {
                            overlay.clear()
                            overlay.setResults(results)
                            overlay.invalidate()
                        }
                    }
                    val objectDetector =
                        TensorflowDetectorHelper(
                            context = context,
                            listener = listener
                        )

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
                        preview.surfaceProvider = viewFinder.surfaceProvider
                    }, ContextCompat.getMainExecutor(context))
                }
                binding.root
            }
        )
    }
}
