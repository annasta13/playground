package com.annas.playground.ui.screen.games.choreographer.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LifeIndicatorView(modifier: Modifier = Modifier, life: Int) {
    var lastLife by remember { mutableIntStateOf(life) }
    var blinkTrigger by remember { mutableStateOf(false) }
    LaunchedEffect(life) {
        if (life != lastLife) {
            blinkTrigger = true
            lastLife = life
            delay(timeMillis = 600)
            blinkTrigger = false
        }
    }
    val alpha by animateFloatAsState(
        targetValue = if (blinkTrigger) 0.3f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lifeBlink"
    )

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(times = life.coerceAtLeast(0)) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "lives",
                modifier = Modifier
                    .size(22.dp)
                    .alpha(alpha),
                tint = Color.Red
            )
        }
    }
}

@Preview
@Composable
private fun LifeIndicatorViewPreview() {
    LifeIndicatorView(life = 5)
}
