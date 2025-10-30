package com.annas.playground.ui.graph

import com.annas.playground.ui.graph.RouteParam.MIN_DATE
import com.annas.playground.ui.graph.RouteParam.MODEL
import com.annas.playground.ui.graph.RouteParam.PRODUCT_ID
import com.annas.playground.ui.graph.RouteParam.STOCK

object Destination {
    const val HOME_SCREEN = "home-screen"
    const val SEARCH_SCREEN = "search-screen"
    const val PRODUCT_LIST = "product-list-screen"
    const val PRODUCT_DETAIL = "product-detail-screen/{$PRODUCT_ID}"
    const val ANIMATION = "animation-screen"
    const val USER_LIST = "user-list-screen"
    const val CREATE_USER = "create-user-screen"
    const val CREATE_TRANSACTION = "create-transaction-screen/{$PRODUCT_ID}/{$MIN_DATE}/{$STOCK}"
    const val GENERATOR_RESULT = "generator-result-screen"
    const val LOADING_ANIMATION = "loading-animation-screen"
    const val JUMPING_GAME = "jumping-game-screen"
    const val FRUIT_CART_GAME = "fruit-cart-game-screen"
    const val SCAN_IMAGE_TEXT = "scan-image-text-screen"
    const val OBJECT_DETECTOR = "object-detector-screen"
    const val GAMES = "games-screen"
    const val TENSORFLOW_DETECTOR = "tensorflow-detector-screen/{$MODEL}"
    const val YOLO_DETECTOR = "yolo-detector-screen"
}

data class Section(
    val id: Int, val title: String, val route: String
) {
    companion object {
        val sectionList = listOf(
            Section(1, "Search Indonesian City", route = Destination.SEARCH_SCREEN),
            Section(2, "Data Processing", route = Destination.PRODUCT_LIST),
            Section(3, "Heart Beat Animation", route = Destination.ANIMATION),
            Section(4, "Api Fetching", route = Destination.USER_LIST),
            Section(5, "CSV-Based String Resources", route = Destination.GENERATOR_RESULT),
            Section(6, "Loading Animation", route = Destination.LOADING_ANIMATION),
            Section(7, "Games", route = Destination.GAMES),
            Section(8, "Scan Image Text", route = Destination.SCAN_IMAGE_TEXT),
            Section(9, "Object Detector", route = Destination.OBJECT_DETECTOR)
        )
    }
}
