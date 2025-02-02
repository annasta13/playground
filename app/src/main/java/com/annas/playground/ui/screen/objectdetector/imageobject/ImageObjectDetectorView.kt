package com.annas.playground.ui.screen.objectdetector.imageobject

import android.graphics.BitmapFactory
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.annas.playground.R
import com.annas.playground.helper.DetectedObject
import com.annas.playground.helper.ObjectDetectorType
import com.annas.playground.helper.TensorflowDetectorHelper
import com.annas.playground.utils.ContextExtension.toast


@Composable
fun ImageObjectDetectorView() {
    val context = LocalContext.current
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

    val objectDetector = remember {
        TensorflowDetectorHelper(
            currentModel = ObjectDetectorType.MODEL_EFFICIENTDET_V2,
            context = context, listener = listener
        )
    }

    Scaffold { padding ->
        ConstraintLayout(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val bitmapSource = remember {
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.img_vehicles
                )
            }
            LaunchedEffect(Unit) {
                objectDetector.detectBitmap(bitmapSource)
            }

            val (imageRef, overlayRef) = createRefs()

//            val view: @Composable (Bitmap) -> Unit = { bitmap ->
//                Box(
//                    modifier = Modifier
//                        .constrainAs(imageRef) {
//                            top.linkTo(parent.top)
//                            start.linkTo(parent.start)
//                            end.linkTo(parent.end)
//                            bottom.linkTo(parent.bottom)
//                        }//.border(6.dp, Color.Red)
//                ) {
//                    AsyncImage(
//                        model = bitmap,
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxWidth()
//                        //    .align(Alignment.TopStart)
//                        ,
//                        contentScale = ContentScale.Fit,
//                    )
//                }
//            }

//            view(bitmapSource)
//

            AsyncImage(
                model = bitmapSource,
                contentDescription = null,
                modifier = Modifier.constrainAs(imageRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },//.border(8.dp, Color.Blue),//.fillMaxWidth(),
                contentScale = ContentScale.Fit,
            )
//            AndroidView(
//                modifier = Modifier.fillMaxSize(),
//                factory = { context ->
//                    val screenshotView =
//                        ScreenshotView(context = context, view = { view(bitmapSource) }) { result ->
//                            println("check bitmap ${result.height} ${result.width}")
//                            bitmapScreenshot = result
//                            objectDetector.detectBitmap(bitmapSource)
//                        }
//                    screenshotView
//                }
//            )

//            detectedObjects.firstOrNull()?.let { view(it.tensorImage.bitmap) }

            ImageObjectDetectorOverlayView(modifier = Modifier
                .constrainAs(overlayRef) {
                    top.linkTo(imageRef.top)
                    start.linkTo(imageRef.start)
                    end.linkTo(imageRef.end)
                    bottom.linkTo(imageRef.bottom)
                }
                .border(8.dp, Color.Red)
//                .fillMaxSize()
                ,

                detectedObjects = detectedObjects)
        }
    }
}
