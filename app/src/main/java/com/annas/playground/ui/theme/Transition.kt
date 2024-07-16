package com.annas.playground.ui.theme

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.annas.playground.constants.IntConstant.NINE_HUNDRED
import com.annas.playground.constants.IntConstant.ONE_THOUSAND_TWO_HUNDRED

val enterTransition = slideInHorizontally { ONE_THOUSAND_TWO_HUNDRED }
val popEnterTransition = slideInHorizontally { -NINE_HUNDRED }
val exitTransition = slideOutHorizontally { ONE_THOUSAND_TWO_HUNDRED }
