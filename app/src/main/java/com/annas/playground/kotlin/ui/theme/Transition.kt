package com.annas.playground.kotlin.ui.theme

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

const val POP_ENTER_TRANSITION_DELAY: Int = 900
const val EXIT_TRANSITION_DELAY: Int = 1200

val enterTransition = slideInHorizontally { EXIT_TRANSITION_DELAY }
val popEnterTransition = slideInHorizontally { -POP_ENTER_TRANSITION_DELAY }
val exitTransition = slideOutHorizontally { EXIT_TRANSITION_DELAY }
