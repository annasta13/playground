package com.annas.playground.kotlin.ui.screen.loadinganimation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.annas.playground.R
import com.annas.playground.kotlin.ui.components.HeadingText
import com.annas.playground.kotlin.ui.components.ScreenContainer
import com.annas.playground.kotlin.ui.components.animateHorizontalAlignmentAsState
import com.annas.playground.kotlin.ui.screen.loadinganimation.LoadingAnimationState.DEFAULT_HORIZONTAL_BIAS
import com.annas.playground.kotlin.ui.screen.loadinganimation.LoadingAnimationState.HIDING_HORIZONTAL_INTERVAL
import com.annas.playground.kotlin.ui.screen.loadinganimation.LoadingAnimationState.HORIZONTAL_INTERVAL
import com.annas.playground.kotlin.ui.screen.loadinganimation.LoadingAnimationState.HORIZONTAL_MOTION
import com.annas.playground.kotlin.ui.screen.loadinganimation.LoadingAnimationState.TARGET_HORIZONTAL_BIAS
import com.annas.playground.kotlin.ui.theme.LargePadding
import com.annas.playground.kotlin.ui.theme.SmallPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoadingAnimationScreen() {
    ScreenContainer(barTitle = stringResource(id = R.string.loading_animation)) {
        var horizontalBias by remember { mutableFloatStateOf(-DEFAULT_HORIZONTAL_BIAS) }
        var showing by remember { mutableStateOf(true) }
        val alignment by animateHorizontalAlignmentAsState(horizontalBias)
        val scope = rememberCoroutineScope()
        LaunchedEffect(key1 = Unit) {
            scope.launch {
                while (true) {
                    if (horizontalBias < DEFAULT_HORIZONTAL_BIAS) {
                        delay(HORIZONTAL_INTERVAL)
                        horizontalBias += HORIZONTAL_MOTION
                    } else {
                        showing = false
                        horizontalBias = TARGET_HORIZONTAL_BIAS
                        delay(HIDING_HORIZONTAL_INTERVAL)
                        showing = true
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(LargePadding),
            contentAlignment = Alignment.Center
        ) {
            Column {
                HeadingText(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.loading_animation_title)
                )
                Spacer(modifier = Modifier.size(LargePadding))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = alignment,
                    ) {
                        if (showing) Image(
                            painter = painterResource(id = R.drawable.ic_truck),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(SmallPadding))
                    Image(
                        painter = painterResource(id = R.drawable.ic_factory),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .size(40.dp)
                    )
                }

                HorizontalDivider(thickness = 2.dp)
            }
        }
    }
}

object LoadingAnimationState {
    const val DEFAULT_HORIZONTAL_BIAS = 1f
    const val TARGET_HORIZONTAL_BIAS = -2f
    const val HORIZONTAL_INTERVAL = 30L
    const val HORIZONTAL_MOTION = 0.05f
    const val HIDING_HORIZONTAL_INTERVAL = 100L
}

@Preview
@Composable
private fun LoadingAnimationScreenPreview() {
    LoadingAnimationScreen()
}
