package com.annas.playground.ui.screen.games.choreographer.game.model

data class ScoreEffect(
    var value: Int,
    var x: Float,
    var y: Float,
    var alpha: Float = 1f,
    var lifetime: Float = 0f
)
