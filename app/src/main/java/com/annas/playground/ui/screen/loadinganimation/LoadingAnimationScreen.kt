package com.annas.playground.ui.screen.loadinganimation

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.annas.playground.R
import com.annas.playground.ui.components.HeadingText
import com.annas.playground.ui.components.ScreenContainer
import com.annas.playground.ui.theme.LargePadding
import com.annas.playground.ui.theme.SmallPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoadingAnimationScreen() {
    ScreenContainer(barTitle = stringResource(id = R.string.loading_animation)) {
        var horizontalBias by remember { mutableFloatStateOf(-1f) }
        var showing by remember { mutableStateOf(true) }
        val alignment by animateHorizontalAlignmentAsState(horizontalBias)
        val scope = rememberCoroutineScope()
        LaunchedEffect(key1 = Unit) {
            scope.launch {
                while (true) {
                    if (horizontalBias < 1f) {
                        delay(30)
                        horizontalBias += 0.05f
                    } else {
                        showing = false
                        horizontalBias = -2f
                        delay(80)
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

@Composable
private fun animateHorizontalAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment.Horizontal> {
    val bias by animateFloatAsState(targetBiasValue, label = "bias")
    val state = remember { derivedStateOf { BiasAlignment.Horizontal(bias) } }
    return state
}


@Preview
@Composable
private fun LoadingAnimationScreenPreview() {
    LoadingAnimationScreen()
}