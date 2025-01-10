package com.annas.playground.ui.screen.jumpinggame

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.annas.playground.R
import com.annas.playground.ui.components.BodyText
import com.annas.playground.ui.components.HeadingText
import com.annas.playground.ui.components.ScreenContainer
import com.annas.playground.ui.components.animateHorizontalAlignmentAsState
import com.annas.playground.ui.components.animateVerticalAlignmentAsState
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.CONSTRAINT_MOTION_DELAY
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.CONSTRAINT_SPEED_DELAY
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.DEFAULT_CONSTRAINT_POSITION
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.DEFAULT_VERTICAL_BIAS
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.FALL_DELAY
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.HIDING_DELAY
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.HORIZONTAL_BIAS_INTERVAL
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.JUMP_DELAY
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.MAX_SPEED_DELAY
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.MIN_SPEED_DELAY
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.TARGET_VERTICAL_BIAS
import com.annas.playground.ui.screen.jumpinggame.JumpingGameConstants.VERTICAL_BIAS_INTERVAL
import com.annas.playground.ui.theme.LargePadding
import com.annas.playground.ui.theme.SmallPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun JumpingGameScreen(
    viewModel: JumpingGameViewModel = hiltViewModel()
) {
    val highestScore by remember { viewModel.highestScore }

    JumpingGameContentView(highestScore = highestScore, onFinish = viewModel::saveScore)
}

@Composable
private fun JumpingGameContentView(highestScore: Int, onFinish: (Int) -> Unit) {
    var verticalBias by remember { mutableFloatStateOf(DEFAULT_VERTICAL_BIAS) }
    val playerAlignment by animateVerticalAlignmentAsState(verticalBias)
    var isGameOver by remember { mutableStateOf(false) }
    var isGameStarted by remember { mutableStateOf(false) }
    var playerBounds by remember { mutableStateOf<Rect?>(null) }
    var currentScore by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    val onRetry: () -> Unit = {
        verticalBias = DEFAULT_VERTICAL_BIAS
        isGameOver = false
        isGameStarted = false
        currentScore = 0
    }

    val startGame: () -> Unit = {
        isGameStarted = true
    }

    val finishGame: () -> Unit = {
        onFinish(currentScore)
        isGameOver = true
    }

    val movePlayer: () -> Unit = {
        if (verticalBias == DEFAULT_VERTICAL_BIAS) scope.launch {
            var isUp = true
            var isDown = false
            while (isUp) {
                if (verticalBias > TARGET_VERTICAL_BIAS) {
                    verticalBias -= VERTICAL_BIAS_INTERVAL
                    delay(JUMP_DELAY)
                } else {
                    isUp = false
                    isDown = true
                }
            }
            while (isDown) {
                if (verticalBias < DEFAULT_VERTICAL_BIAS) {
                    verticalBias += VERTICAL_BIAS_INTERVAL
                } else {
                    verticalBias = DEFAULT_VERTICAL_BIAS
                    isDown = false
                }
                delay(FALL_DELAY)
            }
        }
    }

    ScreenContainer(barTitle = "Jumping Game") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .clickable(
                    onClick = if (!isGameStarted) startGame else movePlayer,
                    enabled = !isGameOver,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            if (!isGameOver && highestScore > 0) BodyText(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(LargePadding),
                text = stringResource(id = R.string.highest_score, highestScore),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
            if (isGameOver) GameOverView(
                score = currentScore,
                highestScore = highestScore,
                onClick = onRetry
            )
            else Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isGameStarted) HeadingText(
                    text = stringResource(id = R.string.tap_to_start),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                else HeadingText(
                    text = stringResource(R.string.score, currentScore),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Green
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            GameConstraintView(
                                onGameOver = finishGame,
                                playerBounds = playerBounds,
                                onPassed = { currentScore++ },
                                isGameStarted = isGameStarted,
                                imageRes = R.drawable.ic_truck
                            )

                            GameConstraintView(
                                onGameOver = finishGame,
                                playerBounds = playerBounds,
                                onPassed = { currentScore++ },
                                isGameStarted = isGameStarted,
                                imageRes = R.drawable.ic_bus
                            )
                        }

                        Spacer(modifier = Modifier.size(SmallPadding))
                        Image(
                            painter = painterResource(id = R.drawable.ic_horse),
                            contentDescription = null,
                            modifier = Modifier
                                .onGloballyPositioned {
                                    playerBounds = it.boundsInRoot()
                                }
                                .size(40.dp)
                                .align(playerAlignment)
                        )
                        Spacer(modifier = Modifier.size(LargePadding))
                    }
                    HorizontalDivider(thickness = 2.dp)
                }
            }
        }
    }
}

@Composable
fun GameConstraintView(
    modifier: Modifier = Modifier,
    isGameStarted: Boolean,
    onPassed: () -> Unit,
    onGameOver: () -> Unit,
    @DrawableRes imageRes: Int,
    playerBounds: Rect?
) {
    var showing by remember { mutableStateOf(true) }
    var horizontalBias by remember { mutableFloatStateOf(-DEFAULT_CONSTRAINT_POSITION) }
    val constraintAlignment = animateHorizontalAlignmentAsState(horizontalBias)
    var constraintDelay by remember { mutableLongStateOf(CONSTRAINT_SPEED_DELAY) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = isGameStarted) {
        scope.launch {
            if (imageRes == R.drawable.ic_truck) delay(CONSTRAINT_MOTION_DELAY)
            while (isGameStarted) {
                if (horizontalBias < DEFAULT_CONSTRAINT_POSITION) {
                    delay(constraintDelay)
                    horizontalBias += HORIZONTAL_BIAS_INTERVAL
                } else kotlin.runCatching {
                    showing = false
                    horizontalBias = -DEFAULT_CONSTRAINT_POSITION
                    delay(HIDING_DELAY)
                    showing = true
                    onPassed()
                    constraintDelay =
                        Random.nextLong(from = MIN_SPEED_DELAY, until = MAX_SPEED_DELAY)
                }
            }
        }
    }
    if (showing) Column(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = modifier
                .onGloballyPositioned {
                    //player bounds in roots Rect.fromLTRB(912.0, 1341.0, 1080.0, 1479.0)
                    if (playerBounds != null) {
                        val bounds = it.boundsInRoot()
                        if (playerBounds.overlaps(bounds)) {
                            horizontalBias = -DEFAULT_CONSTRAINT_POSITION
                            showing = true
                            onGameOver()
                        }
                    }
                }
                .size(40.dp)
                .align(constraintAlignment.value)
        )
    }
}

@Preview
@Composable
private fun JumpingGameScreenPreview() {
    JumpingGameContentView(10) { _ -> }
}

@Suppress("unused")
private class JumpingGamePreviewProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}


object JumpingGameConstants {
    const val DEFAULT_CONSTRAINT_POSITION = 2f
    const val JUMP_DELAY = 15L
    const val FALL_DELAY = 15L
    const val HIDING_DELAY = 100L
    const val CONSTRAINT_SPEED_DELAY = 20L
    const val MIN_SPEED_DELAY = 30L
    const val MAX_SPEED_DELAY = 10L
    const val HORIZONTAL_BIAS_INTERVAL = 0.05f
    const val VERTICAL_BIAS_INTERVAL = 0.20f
    const val TARGET_VERTICAL_BIAS = -2.5f
    const val DEFAULT_VERTICAL_BIAS = 1f
    const val GAME_OVER_BUTTON_FRACTION = 0.4f
    const val CONSTRAINT_MOTION_DELAY = 1000L
}
