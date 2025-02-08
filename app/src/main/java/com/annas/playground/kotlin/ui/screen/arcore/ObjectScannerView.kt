package com.annas.playground.kotlin.ui.screen.arcore

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.view.MotionEvent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.annas.playground.kotlin.ui.screen.objectdetector.tensorflow.compose.getRotation
import com.annas.playground.kotlin.utils.ContextExtension.toast
import com.google.ar.core.Anchor
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Camera
import com.google.ar.core.CameraIntrinsics
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer
import com.google.ar.sceneform.ux.TransformableNode
import com.google.ar.sceneform.ux.TransformationSystem


@Composable
fun ObjectScanningView() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var detectedObject by remember { mutableStateOf<DetectedObject?>(null) }
    var arSceneView by remember { mutableStateOf<ArSceneView?>(null) }
    var anchors by remember { mutableStateOf(mutableListOf<Anchor>()) }
    var modelRenderable by remember { mutableStateOf<ModelRenderable?>(null) }

    LaunchedEffect(Unit) {
        MaterialFactory.makeOpaqueWithColor(
            context,
            com.google.ar.sceneform.rendering.Color(Color.GREEN)
        )
            .thenAccept { material ->
                val cylinder = ShapeFactory.makeCylinder(
                    0.05f,            // Radius
                    0.3f,             // Height
                    Vector3.zero(),   // Center position
                    material          // Material color
                )
                modelRenderable = cylinder
            }
    }

    var installRequested by remember { mutableStateOf(false) }
    var session by remember { mutableStateOf<Session?>(null) }

    var frame by remember { mutableStateOf<Frame?>(null) } // Get the current ARCore frame
    var arCoreCamera by remember { mutableStateOf<Camera?>(null) }
    var cameraIntrinsics by remember { mutableStateOf<CameraIntrinsics?>(null) }

    val updateFramer: (Session) -> Unit = {
        frame = it.update()
        arCoreCamera = frame?.camera
        cameraIntrinsics = arCoreCamera?.getImageIntrinsics()

    }

//    LaunchedEffect(session) {
//        if (session != null) {
//            if (frame == null) frame = session!!.update()
//            if (arCoreCamera == null) arCoreCamera = frame?.camera
//            if (cameraIntrinsics == null) cameraIntrinsics = arCoreCamera?.getImageIntrinsics()
//        }
//    }
    val createSession: () -> Unit = {
        tryCreateSession(
            context = context,
            installRequested = installRequested,
            onInstallRequestedState = { installRequested = it },
            onSession = {
                session = it.apply { configureSession(this) }
//                updateFramer(it)
            }
        )
    }

    /* LifecycleStartEffect(Unit) {
         if (session == null) createSession()
         onStopOrDispose {
             session?.close()
         }
     }

     LifecycleResumeEffect(Unit) {
         session?.resume()
         onPauseOrDispose {
             session?.pause()
         }
     }
 */

    val listener = remember {
        object : DetectorHelper.DetectorListener {
            override fun onError(error: String) {
                context.toast(error)
            }

            override fun onResults(results: List<DetectedObject>) {
                detectedObject = results.firstOrNull()
                println("check on result ${detectedObject?.detection?.categories}")
                val obj = results.firstOrNull() ?: return
                arSceneView?.let { scene ->


                    val hitResult = scene.arFrame?.hitTest(
                        obj.detection.boundingBox.centerX(),
                        obj.detection.boundingBox.centerY()
                    )

                    println("check hit result $hitResult")
                    if (!hitResult.isNullOrEmpty()) {
                        val anchor = hitResult[0].createAnchor()
                        val anchorNode = AnchorNode(anchor)
                        val matrix = context.resources.displayMetrics
                        val selectionVisualizer = FootprintSelectionVisualizer()
                        val transformationSystem =
                            TransformationSystem(matrix, selectionVisualizer)
                        val transformableNode = TransformableNode(transformationSystem)
                        transformableNode.renderable = modelRenderable
                        transformableNode.setParent(anchorNode)
                        scene.scene.addChild(anchorNode)
                    }

                }
            }
        }
    }

    /*LaunchedEffect(detectedObject) {
        if (detectedObject != null) {
            val pose = estimateObjectPose(
                obj = detectedObject!!.detection.boundingBox,
                cameraIntrinsics = cameraIntrinsics!!
            )
            session?.createAnchor(pose)?.let { anchor ->
                if (!anchors.contains(anchor)) { // Avoid adding the same anchor multiple times
                    anchors.add(anchor)

                    // Attach the 3D model to the anchor (run on UI thread)
                    context.run {
                        val anchorNode = AnchorNode(anchor)
                        val transformableNode = TransformableNode(transformationSystem)
                        transformableNode.setParent(anchorNode)

                        val modelNode = Node()
                        modelNode.renderable = modelRenderable
                        modelNode.setParent(transformableNode)

                        transformableNode.localScale = Vector3(
                            0.1f,
                            0.1f,
                            0.1f
                        ) // Example scaling

                        arSceneView?.scene?.addChild(anchorNode)

                    }
                }
            }
        }

    }*/

    val rotation = remember { getRotation(context) }
    val objectDetector = remember {
        DetectorHelper(
            context = context,
            maxResults = 1,
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
                                    val previousInferenceTime = detectedObject?.inferenceTime ?: 0L
                                    val current = System.currentTimeMillis()
                                    val shouldDetect = current - previousInferenceTime >= 1000
                                    if (shouldDetect) objectDetector.detect(image)
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
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val view = ArSceneView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
                arSceneView = view
                view
            },
            update = {view->
                arSceneView = view
                setupArSceneView(view, context, modelRenderable)
                view.scene.addOnUpdateListener { frameTime ->
                    val planes = arSceneView?.session?.getAllTrackables(Plane::class.java)
                    val hasTrackingPlane = planes?.any { it.trackingState == TrackingState.TRACKING } == true
                    println("check hasTrackingPlane $hasTrackingPlane")
                }
                println("check scene on update")
//                arSceneView = it
//                val matrix = it.context.resources.displayMetrics
//                val selectionVisualizer = FootprintSelectionVisualizer()
//                transformationSystem =
//                    TransformationSystem(matrix, selectionVisualizer) // Initialize here
//                if (session == null) createSession()
            }
        )
//        CameraDetectorOverlay(detectedObjects)
    }
}


fun setupArSceneView(arSceneView: ArSceneView, context: Context, modelRenderable: ModelRenderable?) {
    val session = Session(arSceneView.context)
    val config = Config(session).apply {
        planeFindingMode = Config.PlaneFindingMode.HORIZONTAL // Detect horizontal surfaces
    }
    session.configure(config)
    arSceneView.setupSession(session)
//
//    // Enable touch interaction
    arSceneView.scene.setOnTouchListener { hitTestResult, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            println("check arSceneView.arFrame ${arSceneView.arFrame}")
            val hitResult = arSceneView.arFrame?.hitTest(motionEvent)?.firstOrNull()
            println("check click hitResult $hitResult")
            hitResult?.let {
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                val matrix = context.resources.displayMetrics
                val selectionVisualizer = FootprintSelectionVisualizer()
                val transformationSystem =
                    TransformationSystem(matrix, selectionVisualizer)
                val transformableNode = TransformableNode(transformationSystem)
                transformableNode.renderable = modelRenderable
                transformableNode.setParent(anchorNode)
                arSceneView.scene.addChild(anchorNode)
            }
        }
        true
    }
}

/**
 * Estimating the 3D pose of a detected object from its 2D bounding box is a complex problem.  There's no single perfect solution, and the best approach depends on your specific use case and the information you have available.  Here are a few methods, ranging from simple approximations to more advanced techniques:
 * 1. Approximation using Camera Intrinsics (Simplest, but Least Accurate):
 * This method is the simplest, but it makes significant assumptions and is likely to be inaccurate, especially for objects that are not close to the center of the image or if the camera is tilted.  It's a good starting point for experimentation, but don't rely on it for accurate pose estimation.
 * */
private fun estimateObjectPose(obj: RectF, cameraIntrinsics: CameraIntrinsics): Pose {
    // Get the center of the bounding box
    val centerX = obj.centerX()
    val centerY = obj.centerY()

    // Get the focal lengths and principal point from camera intrinsics
    val fx = cameraIntrinsics.getFocalLength()[0] // x-axis focal length
    val fy = cameraIntrinsics.getFocalLength()[1] // y-axis focal length
    val cx = cameraIntrinsics.getPrincipalPoint()[0] // x-axis principal point
    val cy = cameraIntrinsics.getPrincipalPoint()[1] // y-axis principal point

    // Calculate the 3D ray in camera coordinates
    val x = (centerX - cx) / fx
    val y = (centerY - cy) / fy
    val z = 1.0f // Assume a distance of 1 meter (or any other reasonable distance)
    val translation = floatArrayOf(x, y, z) // Translation vector
    val rotation = floatArrayOf(0f, 0f, 0f, 1f)
    // Create a pose from the ray (assuming the object lies on the ray at distance z)
    return Pose(translation, rotation) // This pose is in camera coordinates
}


private fun tryCreateSession(
    context: Context, installRequested: Boolean,
    onInstallRequestedState: (Boolean) -> Unit,
    onSession: (Session) -> Unit
) {
    runCatching {
        when (ArCoreApk.getInstance().requestInstall(context as Activity, !installRequested)) {
            ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                onInstallRequestedState(true)
            }

            ArCoreApk.InstallStatus.INSTALLED -> {
                // Left empty nothing needs to be done.
            }
        }

        // Create a session if Google Play Services for AR is installed and up to date.
        onSession(Session(context))
    }.onFailure { e ->
        e.message?.let { context.toast(it) }
    }.getOrNull()
}

private fun configureSession(session: Session) {
    session.configure(
        session.config.apply {
            lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR

            // Depth API is used if it is configured in Hello AR's settings.
            depthMode =
                if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    Config.DepthMode.AUTOMATIC
                } else {
                    Config.DepthMode.DISABLED
                }

            // Instant Placement is used if it is configured in Hello AR's settings.
//            instantPlacementMode =
//                if (instantPlacementSettings.isInstantPlacementEnabled) {
//                    InstantPlacementMode.LOCAL_Y_UP
//                } else {
//                    InstantPlacementMode.DISABLED
//                }
        }
    )
}
