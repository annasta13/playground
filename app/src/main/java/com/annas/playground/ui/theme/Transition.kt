package com.annas.playground.ui.theme

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

val transitionOffset = 1200
val enterTransition = slideInHorizontally { transitionOffset }
val popEnterTransition = slideInHorizontally { -900 }
val exitTransition = slideOutHorizontally { transitionOffset }
