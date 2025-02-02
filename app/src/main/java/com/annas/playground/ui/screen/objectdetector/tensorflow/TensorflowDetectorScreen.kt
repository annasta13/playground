package com.annas.playground.ui.screen.objectdetector.tensorflow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.annas.playground.helper.ObjectDetectorType
import com.annas.playground.ui.screen.objectdetector.DetectorContainer
import com.annas.playground.ui.screen.objectdetector.tensorflow.compose.TensorflowDetectorCameraView

//Source: https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection
/** Tensorflow framework detector
 * @param type is one of the following:
 * - [ObjectDetectorType.MODEL_MOBILENET_V1]
 * - [ObjectDetectorType.MODEL_EFFICIENTDET_V0]
 * - [ObjectDetectorType.MODEL_EFFICIENTDET_V1]
 * - [ObjectDetectorType.MODEL_EFFICIENTDET_V2]
 * */
@Composable
fun TensorflowDetectorScreen(
    navigateUp: () -> Unit,
    type: Int
) {
    DetectorContainer(
        onNavigateUp = navigateUp,
        cameraView = { TensorflowDetectorCameraView(type = type) },
        title = remember { ObjectDetectorType.getLabel(type) }
    )
}
