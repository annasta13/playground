package com.annas.playground.kotlin.helper.tensorflow

import com.annas.playground.kotlin.ui.graph.Destination
import com.annas.playground.kotlin.ui.graph.RouteParam.MODEL

data class ObjectDetectorType(
    val id: Int,
    val name: String,
    val route: String
) {
    companion object {
        const val MODEL_MOBILENET_V1 = 0
        const val MODEL_EFFICIENTDET_V0 = 1
        const val MODEL_EFFICIENTDET_V1 = 2
        const val MODEL_EFFICIENTDET_V2 = 3
        const val YOLO_V9 = 4

        fun getLabel(type: Int) = list.find { it.id == type }?.name ?: ""

        val list = listOf(
            ObjectDetectorType(
                id = 0,
                name = "SSD Mobile Net V1",
                route = Destination.TENSORFLOW_DETECTOR.replace("{$MODEL}", "$MODEL_MOBILENET_V1")
            ),
            ObjectDetectorType(
                id = 1,
                name = "Efficient Det V0",
                route = Destination.TENSORFLOW_DETECTOR.replace(
                    "{$MODEL}",
                    "$MODEL_EFFICIENTDET_V0"
                )
            ),
            ObjectDetectorType(
                id = 2,
                name = "Efficient Det V1",
                route = Destination.TENSORFLOW_DETECTOR.replace(
                    "{$MODEL}",
                    "$MODEL_EFFICIENTDET_V1"
                )
            ),
            ObjectDetectorType(
                id = 3,
                name = "Efficient Det V2",
                route = Destination.TENSORFLOW_DETECTOR.replace(
                    "{$MODEL}",
                    "$MODEL_EFFICIENTDET_V2"
                )
            ),
            ObjectDetectorType(
                id = 4,
                name = "Yolo V9",
                route = Destination.YOLO_DETECTOR
            )

        )
    }
}
