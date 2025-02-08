package com.annas.playground.kotlin.helper.tensorflow

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.hardware.display.DisplayManager
import android.media.Image
import android.view.Surface
import androidx.camera.core.ImageProxy
import com.annas.playground.kotlin.helper.tensorflow.ObjectDetectorType.Companion.MODEL_EFFICIENTDET_V0
import com.annas.playground.kotlin.helper.tensorflow.ObjectDetectorType.Companion.MODEL_EFFICIENTDET_V1
import com.annas.playground.kotlin.helper.tensorflow.ObjectDetectorType.Companion.MODEL_EFFICIENTDET_V2
import com.annas.playground.kotlin.helper.tensorflow.ObjectDetectorType.Companion.MODEL_MOBILENET_V1
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder


data class DetectedObject(
    val detection: Detection,
    val inferenceTime: Long,
    val tensorImageWidth: Int,
    val tensorImageHeight: Int,
)

class TensorflowDetectorHelper(
    private val threshold: Float = 0.5f,
    private val numThreads: Int = 2,
    private val maxResults: Int = 1,
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
    private fun setupObjectDetector() {
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
                MODEL_MOBILENET_V1 -> "tensorflow/mobilenetv1.tflite"
                MODEL_EFFICIENTDET_V0 -> "tensorflow/efficientdet-lite0.tflite"
                MODEL_EFFICIENTDET_V1 -> "tensorflow/efficientdet-lite1.tflite"
                MODEL_EFFICIENTDET_V2 -> "tensorflow/efficientdet-lite2.tflite"
                else -> "tensorflow/mobilenetv1.tflite"
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

    fun detect(imageProxy: ImageProxy, previousInferenceTime: Long? = null) {
        if (objectDetector == null) {
            setupObjectDetector()
        }

        val currentTimestamp = System.currentTimeMillis()
        val difference = currentTimestamp - (previousInferenceTime ?: 0L)
        val shouldDetect = difference > 1000 || previousInferenceTime == 0L

        println("check currentTimestamp: $currentTimestamp previousInferenceTime: $previousInferenceTime shouldDetect: $shouldDetect")

        if (shouldDetect.not()) {
//            runBlocking {
//                delay(1000)
            imageProxy.close()
//            }
            return
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
        results?.firstOrNull()?.let {
            println("check tensorImage width ${tensorImage.width} height ${tensorImage.height}")
            println("check result box ${it.boundingBox}")
        }
        listener?.onResults(
            results?.map {
                DetectedObject(
                    detection = it,
                    inferenceTime = System.currentTimeMillis(),
                    tensorImageWidth = tensorImage.width,
                    tensorImageHeight = tensorImage.height
                )
            }.orEmpty()
        )
        imageProxy.close()
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
            results?.map {
                DetectedObject(
                    detection = it,
                    inferenceTime = System.currentTimeMillis(),
                    tensorImageWidth = tensorImage.width,
                    tensorImageHeight = tensorImage.height
                )
            }.orEmpty()
        )
    }

    private val imageProcessor =
        ImageProcessor.Builder()
            .build()

    fun detectImage(image: Image, imageRotation: Int) {
        if (objectDetector == null) {
            setupObjectDetector()
        }

        val matrix = Matrix()
//        val modelInputSize = 320
        val bitmap = imageToBitmap(image)
        matrix.postRotate(imageRotation.toFloat())
        val bitmapResult =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        val mat = Matrix()
        val rotation = getRotation(context) // Get rotation from Display
        mat.postRotate(-rotation.toFloat())
        val imageProcessor =
            ImageProcessor.Builder()
//                .add(Rot90Op(-rotation / ROTATION))
                .build()

        val imageTensor = TensorImage.fromBitmap(bitmapResult)
        val tensorImage = imageProcessor.process(imageTensor)
        val results = objectDetector?.detect(tensorImage)


        //CameraX
        //check result box RectF(17.0, 419.0, 279.0, 626.0)
        //check currentTimestamp: 1739359915931 previousInferenceTime: null shouldDetect: true
        //check tensorImage width 480 height 640
        //check result box RectF(1.0, 423.0, 297.0, 632.0)
        results?.firstOrNull()?.let {
            println("check tensorImage width ${tensorImage.width} height ${tensorImage.height}")
            println("check result box ${it.boundingBox}")
        }

        listener?.onResults(
            results?.map {
                DetectedObject(
                    detection = it,
                    inferenceTime = System.currentTimeMillis(),
                    tensorImageWidth = tensorImage.width,
                    tensorImageHeight = tensorImage.height
                )
            }.orEmpty()
        )
        bitmap.recycle()
        bitmapResult.recycle()
    }

    fun convertToBitmapByGemini(image: Image): Bitmap {
        image.apply {
            val yBuffer = planes[0].buffer // Y
            val uBuffer = planes[1].buffer // U
            val vBuffer = planes[2].buffer // V

            val ySize = yBuffer.remaining()
            val uSize = uBuffer.remaining()
            val vSize = vBuffer.remaining()

            val nv21 = ByteArray(ySize + uSize + vSize)
            yBuffer.get(nv21, 0, ySize)
            vBuffer.get(nv21, ySize, vSize)
            uBuffer.get(nv21, ySize + vSize, uSize)

            val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
            val out = ByteArrayOutputStream()
            val rect = Rect(0, 0, width, height) // Create a Rect with the image dimensions
            yuvImage.compressToJpeg(rect, 100, out) // Quality 100
            val imageBytes = out.toByteArray()
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }
    }

    fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer =
            ByteBuffer.allocateDirect(4 * bitmap.width * bitmap.height * 3) // 4 bytes per float, 3 channels (RGB)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        for (i in 0 until bitmap.width) {
            for (j in 0 until bitmap.height) {
                val `val` = pixels[pixel++]
                byteBuffer.putFloat(((`val` shr 16 and 0xFF) - 127) / 128f)
                byteBuffer.putFloat(((`val` shr 8 and 0xFF) - 127) / 128f)
                byteBuffer.putFloat(((`val` and 0xFF) - 127) / 128f)
            }
        }
        byteBuffer.rewind()
        return byteBuffer
    }

    private fun toBitmap(image: Image): Bitmap {
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * image.width
        val bitmap = Bitmap.createBitmap(
            image.width + rowPadding / pixelStride,
            image.height, Bitmap.Config.ARGB_8888
        )
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }

//    private fun imageToBitmap(image: Image, modelInputSize: Int): Bitmap {
//        val yBuffer = image.planes[0].buffer
//        val uBuffer = image.planes[1].buffer
//        val vBuffer = image.planes[2].buffer
//
//        val ySize = yBuffer.remaining()
//        val uSize = uBuffer.remaining()
//        val vSize = vBuffer.remaining()
//
//        val nv21 = ByteArray(ySize + uSize + vSize)
//        yBuffer.get(nv21, 0, ySize)
//        vBuffer.get(nv21, ySize, vSize)
//        uBuffer.get(nv21, ySize + vSize, uSize)
//
//        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
//        val out = ByteArrayOutputStream()
//        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, out)
//
//        val jpegBytes = out.toByteArray()
//        val bitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)
//
//        return Bitmap.createScaledBitmap(bitmap, modelInputSize, modelInputSize, true)
//    }
    fun imageToBitmap(image: Image): Bitmap {
        val yBuffer = image.planes[0].buffer
        val uvBuffer = image.planes[1].buffer
        val ySize = yBuffer.remaining()
        val uvSize = uvBuffer.remaining()

        val nv21 = ByteArray(ySize + uvSize)
        yBuffer.get(nv21, 0, ySize)
        uvBuffer.get(nv21, ySize, uvSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val outputStream = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, outputStream)

        val jpegArray = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.size)
    }

    fun getRotation(context: Context): Int {
        val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val display = displayManager.getDisplay(android.view.Display.DEFAULT_DISPLAY)
        return display?.rotation ?: Surface.ROTATION_0
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

