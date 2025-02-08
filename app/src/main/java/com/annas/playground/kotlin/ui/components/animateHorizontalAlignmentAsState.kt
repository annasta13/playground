package com.annas.playground.kotlin.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.BiasAlignment

@Composable
fun animateHorizontalAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment.Horizontal> {
    val bias by animateFloatAsState(targetBiasValue, label = "bias")
    val state = remember { derivedStateOf { BiasAlignment.Horizontal(bias) } }
    return state
}

@Composable
fun animateVerticalAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment.Vertical> {
    val bias by animateFloatAsState(targetBiasValue, label = "vertical-bias")
    val state = remember { derivedStateOf { BiasAlignment.Vertical(bias) } }
    return state
}
