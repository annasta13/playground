//package com.annas.playground.kotlin.ui.screen.arcore
//
//import android.Manifest
//import android.graphics.PointF
//import android.net.Uri
//import android.os.Bundle
//import android.view.ViewGroup
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
//import androidx.appcompat.app.AppCompatActivity
//import androidx.camera.core.AspectRatio
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageAnalysis
//import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.content.ContextCompat
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.lifecycle.lifecycleScope
//import androidx.media3.common.MediaItem
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.ui.PlayerView
//import com.annas.playground.R
//import com.annas.playground.databinding.ActivityArCoreBinding
//import com.annas.playground.java.helpers.arcore.CameraPermissionHelper
//import com.annas.playground.kotlin.helper.tensorflow.DetectedObject
//import com.annas.playground.kotlin.helper.tensorflow.ObjectDetectorType
//import com.annas.playground.kotlin.helper.tensorflow.TensorflowDetectorHelper
//import com.annas.playground.kotlin.utils.ContextExtension.toast
//import com.google.ar.core.Anchor
//import com.google.ar.core.Pose
//import com.google.ar.core.TrackingState
//import com.google.ar.sceneform.AnchorNode
//import com.google.ar.sceneform.ArSceneView
//import com.google.ar.sceneform.FrameTime
//import com.google.ar.sceneform.math.Vector3
//import com.google.ar.sceneform.rendering.Color
//import com.google.ar.sceneform.rendering.MaterialFactory
//import com.google.ar.sceneform.rendering.ShapeFactory
//import com.google.ar.sceneform.rendering.ViewRenderable
//import com.google.ar.sceneform.ux.TransformableNode
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlin.math.atan
//
//
//@Suppress("TooManyFunctions", "unused", "ReturnCount", "MagicNumber")
//class ArCoreActivity : AppCompatActivity(), MyArFragment.OnCompleteListener {
//    private lateinit var binding: ActivityArCoreBinding
//    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
//    private var detectedObject: DetectedObject? = null
//    private var placedAnchorNode: AnchorNode? = null
//    private var missingTime = 0L
//    private lateinit var arFragment: MyArFragment
//    private lateinit var objectDetector: TensorflowDetectorHelper
//    private var lastDetectionTime = 0L
//    private var isWaiting = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityArCoreBinding.inflate(layoutInflater)
////        arFragment = MyArFragment
//        setupDetector()
//        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as MyArFragment
//        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        cameraPermissionLauncher = registerForActivityResult(RequestPermission()) { isGranted ->
//            if (isGranted) openCamera()
//        }
//        openCamera()
//
//    }
//
//    private fun observeObject() {
//        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
//            val currentTime = System.currentTimeMillis()
//
//            /* if (currentTime - lastDetectionTime > MISS_TOLERANCE) {
//                 detectedObject = null
//                 lastDetectionTime = currentTime + WARM_UP_TOLERANCE // Set future time to pause detection
//                 return@addOnUpdateListener
//             }*/
//
//            // If in warm-up period, don't detect yet
//            if (currentTime < lastDetectionTime) return@addOnUpdateListener
//
//            // Run detection only if 1 second has passed since last detection
//            if (currentTime - lastDetectionTime < WARM_UP_TOLERANCE) return@addOnUpdateListener
//
//            if (detectedObject == null) detectObject(currentTime, frameTime)
//        }
//    }
//
//
//    private fun detectObject(currentTime: Long, frameTime: FrameTime) {
//        lifecycleScope.launch(Dispatchers.IO) {
//            val frame = arFragment.arSceneView.arFrame ?: return@launch
//            if (currentTime - (arFragment.arSceneView.arFrame?.timestamp ?: 0L) < 100_000_000) {
//                arFragment.onUpdate(frameTime)
//            }
//            lastDetectionTime = currentTime
//            kotlin.runCatching {
//                val image = frame.acquireCameraImage()
//                val imageRotation = when (frame.camera.trackingState) {
//                    TrackingState.TRACKING -> 90   // Most devices need +90° correction
//                    else -> 0
//                }
//                objectDetector.detectImage(image, imageRotation, arFragment.arSceneView)
//                image.close()
//                lastDetectionTime = System.currentTimeMillis() // Update last detection time
//            }.onFailure {
//                println("check addOnUpdateListener error ${it.message}")
//            }
//        }
//    }
//
//    private fun setupDetector() {
//        val listener = object : TensorflowDetectorHelper.DetectorListener {
//            override fun onError(error: String) {
//                this@ArCoreActivity.toast(error)
//            }
//
//            override fun onResults(results: List<DetectedObject>) {
//                if (results.isEmpty() && detectedObject == null) return
//                println("check results ${results.firstOrNull()?.detection?.categories?.firstOrNull()}")
//                binding.overlay.clear()
//                detectedObject = results.firstOrNull()
//                if (detectedObject != null) {
//                    placeARObjectFromDetection(
//                        detectedObject!!.detection.boundingBox.centerX(),
//                        detectedObject!!.detection.boundingBox.centerY()
//                    )
//                }
//                binding.overlay.setResults(results)
//                binding.overlay.invalidate()
//            }
//        }
//        objectDetector = TensorflowDetectorHelper(
//            context = this@ArCoreActivity,
//            listener = listener,
//            maxResults = 1
//        )
//    }
//
//    fun convertImageToScreen(detectedX: Float, detectedY: Float, obj: DetectedObject): PointF {
//        val imageWidth = obj.tensorImageWidth // Get width from detection model
//        val imageHeight = obj.tensorImageHeight // Get height from detection model
//        val displayMetrics = resources.displayMetrics
//        val screenWidth = displayMetrics.widthPixels
//        val screenHeight = displayMetrics.heightPixels
//
//        val scaleX = screenWidth.toFloat() / imageWidth
//        val scaleY = screenHeight.toFloat() / imageHeight
//
//        val screenX = detectedX * scaleX
//        val screenY = detectedY * scaleY
//
//        return PointF(screenX, screenY)
//    }
//
//    private fun openCamera() {
//        if (!CameraPermissionHelper.hasCameraPermission(this)) {
//            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
//        } //else binding.viewFinder.post { setupCamera() }
//    }
//
//    private fun isNodeOnScreen(anchorNode: AnchorNode): Boolean {
//        val sceneView = arFragment.arSceneView ?: return false
//        val camera = sceneView.scene.camera ?: return false
//        val anchor = anchorNode.anchor ?: return false
//
//        // Convert node world position to screen coordinates
//        val worldPosition = anchor.pose
//        val screenPosition = camera.worldToScreenPoint(
//            Vector3(worldPosition.tx(), worldPosition.ty(), worldPosition.tz())
//        )
//
//        // Get screen size
//        val screenWidth = sceneView.width
//        val screenHeight = sceneView.height
//
//        // Check if the point is within the screen bounds
//        return screenPosition.x in 0f..screenWidth.toFloat() && screenPosition.y in 0f..screenHeight.toFloat()
//    }
//
//
//    fun placeARObjectFromDetection(detectedX: Float, detectedY: Float) {
//        val frame = arFragment.arSceneView.arFrame ?: return
//        val camera = frame.camera
//        val point = convertImageToScreen(detectedX, detectedY, obj = detectedObject!!)
//        val hitResult = frame.hitTest(detectedX, detectedY).firstOrNull()
//        if (hitResult != null) {
//            detectedObject?.detection?.apply {
//                val anchor = hitResult.createAnchor()
//                placeSphere(anchor)
//                createVideoNode(anchor)
//            }
//        } else {
//            println("check No AR surface, placing object in front of camera")
//            val obj = detectedObject ?: return
//            val boundingBox = obj.detection.boundingBox
//            val scaleHeight = binding.root.height * 1f / obj.tensorImageHeight.toFloat()
//            val scaleWidth = binding.root.width * 1f / obj.tensorImageWidth.toFloat()
////            println("check height $height width $width")
//            val top = boundingBox.top * scaleHeight
//            val bottom = boundingBox.bottom * scaleHeight
//            val left = boundingBox.left * scaleWidth
//            val right = boundingBox.right * scaleWidth
//            val position = camera.pose.compose(Pose.makeTranslation(0f, 0f, -1f)) // 1m in front
//            val anchor = arFragment.arSceneView.session?.createAnchor(position)
//            anchor?.let {
//                placeSphere(it)
//                createVideoNode(anchor)
//            }
//        }
//    }
//
//    fun createVideoNode(anchor: Anchor) {
//        removePlacedObject()
//        val uri = Uri.parse("android.resource://${packageName}/raw/sample_video")
//        val player = ExoPlayer.Builder(applicationContext).build()
//        val mediaItem = MediaItem.fromUri(uri)
//        player.setMediaItem(mediaItem)
//        val playerView = PlayerView(this)
//        playerView.apply {
//            layoutParams = ViewGroup.LayoutParams(
//                320,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        }
//        playerView.rotation = -90f
//        playerView.player = player
//        player.prepare()
//        player.play()
//        //https://stackoverflow.com/questions/51495481/viewrenderable-not-working-arcore
//        ViewRenderable.builder().setView(this, playerView)
//            .build()
//            .thenAccept { viewRenderable: ViewRenderable ->
//                val anchorNode = AnchorNode(anchor)
//                anchorNode.setParent(arFragment.arSceneView.scene)
//                anchorNode.renderable = viewRenderable
//                placedAnchorNode = anchorNode
//            }
//    }
//
//    private fun placeSphere(anchor: Anchor) {
//        runOnUiThread {
//            removePlacedObject()
//            MaterialFactory.makeOpaqueWithColor(
//                this@ArCoreActivity,
//                Color(android.graphics.Color.GREEN)
//            )
//                .thenAccept { material ->
//                    val arrowhead = ShapeFactory.makeSphere(
//                        0.05f,
//                        Vector3.zero(),   // Arrowhead height
//                        MaterialFactory.makeOpaqueWithColor(
//                            this@ArCoreActivity, Color(android.graphics.Color.RED)
//                        ).get()
//                    )
//                    val anchorNode = AnchorNode(anchor)
//                    anchorNode.setParent(arFragment.arSceneView.scene)
//
//                    val transformableNode = TransformableNode(arFragment.transformationSystem)
//                    transformableNode.setParent(anchorNode)
//                    transformableNode.renderable = arrowhead
//                    transformableNode.select()
//                    placedAnchorNode = anchorNode
//                }
//        }
//    }
//
//    private fun placeCube(anchor: Anchor) {
//        MaterialFactory.makeOpaqueWithColor(
//            this@ArCoreActivity,
//            Color(android.graphics.Color.BLUE)
//        )
//            .thenAccept { material ->
//                val cubeRenderable = ShapeFactory.makeCube(
//                    Vector3(
//                        0.1f,
//                        0.1f,
//                        0.1f
//                    ), // Width, height, and depth
//                    Vector3(0f, 0f, 0f), // Center of the cube
//                    material
//                )
//
//                val anchorNode = AnchorNode(anchor)
//                anchorNode.setParent(arFragment.arSceneView.scene)
//
//                val transformableNode = TransformableNode(arFragment.transformationSystem)
//                transformableNode.setParent(anchorNode)
//                transformableNode.renderable = cubeRenderable
//                transformableNode.select()
//                placedAnchorNode = anchorNode
//            }
//    }
//
//    private fun computeFov(imageSize: Int, focalLength: Float): Float {
//        val x = imageSize / (2.0 * focalLength)
//        val value = Math.toDegrees(2 * atan(x))
//        return value.toFloat()
//    }
//
//    private fun removePlacedObject() {
//        placedAnchorNode?.let {
//            arFragment.arSceneView.scene.removeChild(it)
//            it.anchor?.detach()
//            it.setParent(null)
//            placedAnchorNode = null
//        }
//    }
//
//    private fun getRealWorld(arSceneView: ArSceneView): Pair<Float, Float>? {
//        val camera = arSceneView.arFrame?.camera ?: return null
//        val cameraIntrinsics = camera.textureIntrinsics
//        val focalLengthX = cameraIntrinsics.focalLength[0]
//
//        val focalLengthY = cameraIntrinsics.focalLength[1]
//        val imageWidth = cameraIntrinsics.imageDimensions[0]
//        val imageHeight = cameraIntrinsics.imageDimensions[1]
//        val horizontalFov = computeFov(imageWidth, focalLengthX)
//        val verticalFov = computeFov(imageHeight, focalLengthY)
//        return Pair(horizontalFov, verticalFov)
//    }
//
//    override fun onComplete() {
//        arFragment.arSceneView.planeRenderer.isVisible = false
//        arFragment.planeDiscoveryController.hide()
//        arFragment.planeDiscoveryController.setInstructionView(null)
//        observeObject()
//    }
//
//    companion object {
//        const val MISS_TOLERANCE = 5000L
//        const val WARM_UP_TOLERANCE = 2000L
//    }
//
//
//    private fun setupCamera() {
//        binding.apply {
//            val listener = object : TensorflowDetectorHelper.DetectorListener {
//                override fun onError(error: String) {
//                    this@ArCoreActivity.toast(error)
//                }
//
//                override fun onResults(results: List<DetectedObject>) {
//                    overlay.clear()
//                    detectedObject = results.firstOrNull()
//                    overlay.setResults(results)
//                    overlay.invalidate()
//                }
//            }
//            val objectDetector = TensorflowDetectorHelper(
//                context = this@ArCoreActivity,
//                listener = listener,
//                maxResults = 1,
//                currentModel = ObjectDetectorType.MODEL_EFFICIENTDET_V2
//            )
//
//            val preview = Preview.Builder()
//                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
//                .setTargetRotation(viewFinder.display.rotation)
//                .build()
//            val cameraProviderFuture = ProcessCameraProvider.getInstance(this@ArCoreActivity)
//            val cameraProvider = cameraProviderFuture.get()
//            cameraProviderFuture.addListener({
//                val cameraSelector = CameraSelector.Builder()
//                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                    .build()
//
//                val imageAnalyzer = ImageAnalysis.Builder()
//                    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
//                    .setTargetRotation(viewFinder.display.rotation)
//                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                    .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
//                    .build()
//                    .also {
//                        it.setAnalyzer(ContextCompat.getMainExecutor(this@ArCoreActivity)) { image ->
//                            objectDetector.detect(
//                                imageProxy = image,
//                                previousInferenceTime = detectedObject?.inferenceTime
//                            )
//                        }
//                    }
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    this@ArCoreActivity,
//                    cameraSelector,
//                    preview,
//                    imageAnalyzer
//                )
//                preview.surfaceProvider = viewFinder.surfaceProvider
//            }, ContextCompat.getMainExecutor(this@ArCoreActivity))
//        }
//    }
//
//}
