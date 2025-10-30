package com.annas.playground.ui.screen.games.choreographer.game.model

import android.graphics.Bitmap

data class HarvestObject(
    var x: Float,
    var y: Float,
    val size: Float,
    var speed: Float,
    var bitmap: Bitmap,
    var type: Type,
    var hitTimer: Float = 0f
) {
    enum class Type { FFB, CONSTRAINTS, POW }
}
