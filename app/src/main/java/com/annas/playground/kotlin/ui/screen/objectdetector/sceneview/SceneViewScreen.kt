package com.annas.playground.kotlin.ui.screen.objectdetector.sceneview

import android.view.MotionEvent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.sceneview.Scene
import io.github.sceneview.collision.HitResult
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.CylinderNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView

// https://github.com/SceneView/sceneview-android
@Composable
fun SceneViewScreen() {
    // An Engine instance main function is to keep track of all resources created by the user and manage
// the rendering thread as well as the hardware renderer.
// To use filament, an Engine instance must be created first.
    val engine = rememberEngine()
// Encompasses all the state needed for rendering a [Scene].
// [View] instances are heavy objects that internally cache a lot of data needed for
// rendering. It is not advised for an application to use many View objects.
// For example, in a game, a [View] could be used for the main scene and another one for the
// game's user interface. More [View] instances could be used for creating special
// effects (e.g. a [View] is akin to a rendering pass).
    val view = rememberView(engine)
// A [Renderer] instance represents an operating system's window.
// Typically, applications create a [Renderer] per window. The [Renderer] generates drawing
// commands for the render thread and manages frame latency.
    val renderer = rememberRenderer(engine)
// Provide your own instance if you want to share [Node]s' scene between multiple views.
    val scene = rememberScene(engine)
// Consumes a blob of glTF 2.0 content (either JSON or GLB) and produces a [Model] object, which is
// a bundle of Filament textures, vertex buffers, index buffers, etc.
// A [Model] is composed of 1 or more [ModelInstance] objects which contain entities and components.
    val modelLoader = rememberModelLoader(engine)
// A Filament Material defines the visual appearance of an object.
// Materials function as a templates from which [MaterialInstance]s can be spawned.
    val materialLoader = rememberMaterialLoader(engine)
// Utility for decoding an HDR file or consuming KTX1 files and producing Filament textures,
// IBLs, and sky boxes.
// KTX is a simple container format that makes it easy to bundle miplevels and cubemap faces
// into a single file.
    val environmentLoader = rememberEnvironmentLoader(engine)
// Physics system to handle collision between nodes, hit testing on a nodes,...
    val collisionSystem = rememberCollisionSystem(view)

    Scene(
        // The modifier to be applied to the layout.
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        view = view,
        renderer = renderer,
        scene = scene,
        modelLoader = modelLoader,
        materialLoader = materialLoader,
        environmentLoader = environmentLoader,
        collisionSystem = collisionSystem,
        // Controls whether the render target (SurfaceView) is opaque or not.
        isOpaque = true,
        // Always add a direct light source since it is required for shadowing.
        // We highly recommend adding an [IndirectLight] as well.
        mainLightNode = rememberMainLightNode(engine) {
            intensity = 100_000.0f
        },
        // Load the environement lighting and skybox from an .hdr asset file
        environment = rememberEnvironment(environmentLoader) {
            environmentLoader.createHDREnvironment(
                assetFileLocation = "sceneview/sky_2k.hdr"
            )!!
        },
        // Represents a virtual camera, which determines the perspective through which the scene is
        // viewed.
        // All other functionality in Node is supported. You can access the position and rotation of the
        // camera, assign a collision shape to it, or add children to it.
        cameraNode = rememberCameraNode(engine) {
            // Position the camera 4 units away from the object
            position = Position(z = 4.0f)
        },
        // Helper that enables camera interaction similar to sketchfab or Google Maps.
        // Needs to be a callable function because it can be reinitialized in case of viewport change
        // or camera node manual position changed.
        // The first onTouch event will make the first manipulator build. So you can change the camera
        // position before any user gesture.
        // Clients notify the camera manipulator of various mouse or touch events, then periodically
        // call its getLookAt() method so that they can adjust their camera(s). Three modes are
        // supported: ORBIT, MAP, and FREE_FLIGHT. To construct a manipulator instance, the desired mode
        // is passed into the create method.
        cameraManipulator = rememberCameraManipulator(),
        // Scene nodes
        childNodes = rememberNodes {
            // Add a glTF model
            add(
                ModelNode(
                    // Load it from a binary .glb in the asset files
                    modelInstance = modelLoader.createModelInstance(
                        assetFileLocation = "sceneview/damaged_helmet.glb"
                    ),
                    scaleToUnits = 1.0f
                )
            )
            // Add a Cylinder geometry
            add(
                CylinderNode(
                    engine = engine,
                    radius = 0.2f,
                    height = 2.0f,
                    // Choose the basic material appearance
                    materialInstance = materialLoader.createColorInstance(
                        color = Color.Blue,
                        metallic = 0.5f,
                        roughness = 0.2f,
                        reflectance = 0.4f
                    )
                ).apply {
                    // Position it on top of the model and rotate it
                    transform(
                        position = Position(y = 1.0f),
                        rotation = Rotation(x = 90.0f)
                    )
                })
            // ...See all available nodes in the nodes packagage
        },
        // The listener invoked for all the gesture detector callbacks.
        // Detects various gestures and events.
        // The gesture listener callback will notify users when a particular motion event has occurred.
        // Responds to Android touch events with listeners.
        onGestureListener = rememberOnGestureListener(
            onDoubleTapEvent = { event, tapedNode ->
                // Scale up the tap node (if any) on double tap
                tapedNode?.let { it.scale *= 2.0f }
            }),
        // Receive basics on touch event on the view
        onTouchEvent = { event: MotionEvent, hitResult: HitResult? ->
            hitResult?.let { println("World tapped : ${it.worldPosition}") }
            // The touch event is not consumed
            false
        },
        // Invoked when an frame is processed.
        // Registers a callback to be invoked when a valid Frame is processing.
        // The callback to be invoked once per frame **immediately before the scene is updated.
        // The callback will only be invoked if the Frame is considered as valid.
        onFrame = { frameTimeNanos ->

        }
    )
}
