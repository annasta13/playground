package com.annas.playground.kotlin.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Space(modifier: Modifier = Modifier, size: Dp = 12.dp) {
    Spacer(modifier = modifier.size(size))
}
