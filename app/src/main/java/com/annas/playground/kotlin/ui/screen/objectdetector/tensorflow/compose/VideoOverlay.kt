package com.annas.playground.kotlin.ui.screen.objectdetector.tensorflow.compose

import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoOverlay(
    modifier: Modifier = Modifier,
    xPos: Animatable<Float, *>,
    yPos: Animatable<Float, *> ,
) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    val uri = remember { Uri.parse("android.resource://${context.packageName}/raw/sample_video") }
    LaunchedEffect(Unit) {
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.setMediaItem(mediaItem)
    }


    var playerView by remember { mutableStateOf<PlayerView?>(null) }

    LifecycleResumeEffect(Unit) {
        playerView?.onResume()
        onPauseOrDispose { playerView?.onPause() }
    }
//    Box(modifier = modifier.fillMaxSize()) {
       /*  val engine = rememberEngine()
            val view = rememberView(engine)
            val renderer = rememberRenderer(engine)
            val scene = rememberScene(engine)
            val modelLoader = rememberModelLoader(engine)
            val materialLoader = rememberMaterialLoader(engine)
            val environmentLoader = rememberEnvironmentLoader(engine)
            val collisionSystem = rememberCollisionSystem(view)
            Scene(
                modifier = Modifier.fillMaxSize(),
                engine = engine,
                view = view,
                renderer = renderer,
                scene = scene,
                modelLoader = modelLoader,
                materialLoader = materialLoader,
                environmentLoader = environmentLoader,
                collisionSystem = collisionSystem,
                isOpaque = true,
                cameraNode = rememberCameraNode(engine) {
                    position = Position(z = 4.0f)
                },
                cameraManipulator = rememberCameraManipulator(),
                // Scene nodes
                childNodes = rememberNodes {
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
                                position = Position(y = yPos.value),
                                rotation = Rotation(x = xPos.value)
                            )
                        })
                    // ...See all available nodes in the nodes packagage
                },
                onFrame = { frameTimeNanos ->

                }
            )*/
            AndroidView(
                factory = { context ->
                    val view = PlayerView(context).apply {
                        useController = false
                        player = exoPlayer
                        exoPlayer.prepare()
                        exoPlayer.play()
                    }
                    playerView = view
                    view
                },
                modifier = modifier
                    .offset { IntOffset(xPos.value.toInt(), yPos.value.toInt()) }
                    .size(200.dp, 140.dp),
                onRelease = {
                    exoPlayer.stop()
                    exoPlayer.release()
                    playerView?.player = null
                    playerView = null
                },
            )
        }
//    }

