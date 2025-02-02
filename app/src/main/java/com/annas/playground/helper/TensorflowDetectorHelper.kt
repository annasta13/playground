package com.annas.playground.helper

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.annas.playground.helper.ObjectDetectorType.Companion.MODEL_EFFICIENTDET_V0
import com.annas.playground.helper.ObjectDetectorType.Companion.MODEL_EFFICIENTDET_V1
import com.annas.playground.helper.ObjectDetectorType.Companion.MODEL_EFFICIENTDET_V2
import com.annas.playground.helper.ObjectDetectorType.Companion.MODEL_MOBILENET_V1
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector

data class DetectedObject(
    val detection: Detection,
    val imageProxy: Bitmap,
    val tensorImage: TensorImage,
)

class TensorflowDetectorHelper(
    private val threshold: Float = 0.5f,
    private val numThreads: Int = 2,
    private val maxResults: Int = 5,
    private val currentDelegate: Int = 0,
    private val currentModel: Int = 0,
    val context: Context,
    val listener: DetectorListener?
) {

    private lateinit var bitmapBuffer: Bitmap

    // For this example this needs to be a var so it can be reset on changes. If the ObjectDetector
    // will not change, a lazy val would be preferable.
    private var objectDetector: ObjectDetector? = null

    init {
        setupObjectDetector()
    }

    @Suppress("unused")
    fun clearObjectDetector() {
        objectDetector = null
    }

    // Initialize the object detector using current settings on the
    // thread that is using it. CPU and NNAPI delegates can be used with detectors
    // that are created on the main thread and used on a background thread, but
    // the GPU delegate needs to be used on the thread that initialized the detector
    fun setupObjectDetector() {
        // Create the base options for the detector using specifies max results and score threshold
        val optionsBuilder =
            ObjectDetector.ObjectDetectorOptions.builder()
                .setScoreThreshold(threshold)
                .setMaxResults(maxResults)

        // Set general detection options, including number of used threads
        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(numThreads)

        // Use the specified hardware for running the model. Default to CPU
        when (currentDelegate) {
            DELEGATE_CPU -> {
                // Default
            }

            DELEGATE_GPU -> {
                if (CompatibilityList().isDelegateSupportedOnThisDevice) {
                    baseOptionsBuilder.useGpu()
                } else {
                    listener?.onError("GPU is not supported on this device")
                }
            }

            DELEGATE_NNAPI -> {
                baseOptionsBuilder.useNnapi()
            }
        }

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        val modelName =
            when (currentModel) {
                MODEL_MOBILENET_V1 -> "mobilenetv1.tflite"
                MODEL_EFFICIENTDET_V0 -> "efficientdet-lite0.tflite"
                MODEL_EFFICIENTDET_V1 -> "efficientdet-lite1.tflite"
                MODEL_EFFICIENTDET_V2 -> "efficientdet-lite2.tflite"
                else -> "mobilenetv1.tflite"
            }

        runCatching {
            objectDetector =
                ObjectDetector.createFromFileAndOptions(context, modelName, optionsBuilder.build())
        }.onFailure {
            listener?.onError(
                "Object detector failed to initialize. See error logs for details"
            )
        }
    }

    fun detect(imageProxy: ImageProxy) {
        if (objectDetector == null) {
            setupObjectDetector()
        }

        if (!::bitmapBuffer.isInitialized) {
            bitmapBuffer = Bitmap.createBitmap(
                imageProxy.width,
                imageProxy.height,
                Bitmap.Config.ARGB_8888
            )
        }

        imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
        val imageRotation = imageProxy.imageInfo.rotationDegrees

        // Create preprocessor for the image.
        // See https://www.tensorflow.org/lite/inference_with_metadata/
        //            lite_support#imageprocessor_architecture
        val imageProcessor =
            ImageProcessor.Builder()
                .add(Rot90Op(-imageRotation / ROTATION))
                .build()

        // Preprocess the image and convert it into a TensorImage for detection.
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmapBuffer))
        val results = objectDetector?.detect(tensorImage)
        listener?.onResults(
            results?.map { DetectedObject(it, bitmapBuffer, tensorImage) }.orEmpty()
        )
    }

    fun detectBitmap(bitmap: Bitmap) {
        if (objectDetector == null) {
            setupObjectDetector()
        }

        val imageProcessor =
            ImageProcessor.Builder()
                .build()

        // Preprocess the image and convert it into a TensorImage for detection.
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
        val results = objectDetector?.detect(tensorImage)
        listener?.onResults(
            results?.map { DetectedObject(it, bitmap, tensorImage) }.orEmpty()
        )
    }

    interface DetectorListener {
        fun onError(error: String)
        fun onResults(results: List<DetectedObject>)
    }

    companion object {
        const val DELEGATE_CPU = 0
        const val DELEGATE_GPU = 1
        const val DELEGATE_NNAPI = 2
        const val ROTATION = 90
    }
}
