package com.annas.playground.kotlin.ui.screen.objectdetector.imageobject

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
import com.annas.playground.kotlin.helper.tensorflow.DetectedObject
import com.annas.playground.kotlin.helper.tensorflow.ObjectDetectorType
import com.annas.playground.kotlin.helper.tensorflow.TensorflowDetectorHelper
import com.annas.playground.kotlin.utils.ContextExtension.toast


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
                    R.drawable.sample_detection
                )
            }
            LaunchedEffect(Unit) {
                objectDetector.detectBitmap(bitmapSource)
            }

            val (imageRef, overlayRef) = createRefs()

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

            ImageObjectDetectorOverlayView(modifier = Modifier
                .constrainAs(overlayRef) {
                    top.linkTo(imageRef.top)
                    start.linkTo(imageRef.start)
                    end.linkTo(imageRef.end)
                    bottom.linkTo(imageRef.bottom)
                }
                .border(8.dp, Color.Red)
                .fillMaxSize()
                ,

                detectedObjects = detectedObjects)
        }
    }
}
