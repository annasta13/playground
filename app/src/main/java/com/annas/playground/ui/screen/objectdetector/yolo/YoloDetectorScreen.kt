package com.annas.playground.ui.screen.objectdetector.yolo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.annas.playground.ui.screen.objectdetector.DetectorContainer
import com.annas.playground.helper.ObjectDetectorType
import com.annas.playground.helper.ObjectDetectorType.Companion.YOLO_V9

//Source: https://github.com/surendramaran/YOLO/blob/main/YOLOv9-Object-Detector-Android-Tflite
@Composable
fun YoloDetectorScreen(navigateUp: () -> Unit) {
    DetectorContainer(
        onNavigateUp = navigateUp,
        title = remember { ObjectDetectorType.getLabel(YOLO_V9) },
        cameraView = { YoloDetectorCameraView() }
    )
}
