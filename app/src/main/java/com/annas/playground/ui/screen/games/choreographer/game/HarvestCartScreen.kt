package com.annas.playground.ui.screen.games.choreographer.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleStartEffect
import com.annas.playground.R
import com.annas.playground.helper.rememberPreferenceHelper
import com.annas.playground.ui.components.BodyText
import com.annas.playground.ui.components.ScreenContainer
import com.annas.playground.ui.components.SecondaryButton
import com.annas.playground.ui.components.TitleText
import com.annas.playground.utils.onCondition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HarvestCartScreen(
    onNavigateUp: () -> Unit,
) {
    val scoreKey = remember { "harvest_tap_highest_score" }
    val scoreState = remember { mutableIntStateOf(0) }
    val preferenceState = rememberPreferenceHelper()


    val game = remember { mutableStateOf<HarvestTapView?>(null) }
    val highestScore = remember { mutableIntStateOf(preferenceState.getInt(scoreKey)) }
    var loading by remember { mutableStateOf(true) }
    val lives = remember { mutableIntStateOf(value = 5) }

    val scope = rememberCoroutineScope()
    val setLoading: () -> Unit = {
        scope.launch {
            game.value?.pauseGame()
            loading = true
            delay(timeMillis = 1000)
            loading = false
        }
    }
    LaunchedEffect(Unit) { setLoading() }
    val saveHighScore: () -> Unit = {
        if (scoreState.intValue > highestScore.intValue) {
            highestScore.intValue = scoreState.intValue
            preferenceState.saveInt(scoreKey, highestScore.intValue)
        }
    }
    LifecycleStartEffect(Unit) {
        onStopOrDispose {
            game.value?.pauseGame()
            saveHighScore()
        }
    }
    GameContent(scoreState, highestScore, onNavigateUp, lives, game)
    LaunchedEffect(lives.intValue) {
        if (lives.intValue == 0) saveHighScore()
    }
}

@Composable
private fun GameContent(
    scoreState: MutableIntState,
    highestScore: MutableIntState,
    onNavigateUp: () -> Unit,
    lives: MutableIntState,
    game: MutableState<HarvestTapView?>
) {

    ScreenContainer(
        barTitle = stringResource(R.string.harvest_cart),
        onLeadingIconClicked = onNavigateUp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TitleText(text = stringResource(R.string.catch_palm_oil_fruit))
                LifeIndicatorView(life = lives.intValue)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BodyText(text = stringResource(R.string.score_label))
                    TitleText(text = scoreState.intValue.toString())
                }
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BodyText(text = stringResource(R.string.highest_score_label))
                    TitleText(text = highestScore.intValue.toString())
                }
            }
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.ic_palm_oil_trees),
                    modifier = Modifier.fillMaxWidth().alpha(0.05f).align(Alignment.BottomCenter),
                    contentDescription = "background"
                )
                AndroidView(
                    modifier = Modifier.onCondition(lives.intValue == 0) { alpha(alpha = 0.5f) },
                    factory = { context ->
                        HarvestTapView(context).apply {
                            setState(score = scoreState, liveState = lives)
                            game.value = this
                        }
                    }
                )

                if (lives.intValue == 0) Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    BodyText(
                        text = stringResource(R.string.game_over),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 3
                    )
                    SecondaryButton(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(R.string.retry),
                        onClick = {
                            game.value?.resetGame()
                            lives.intValue = 5
                            game.value?.resumeGame()
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HarvestTapScreenPreview() {
    GameContent(
        scoreState = remember { mutableIntStateOf(0) },
        highestScore = remember { mutableIntStateOf(0) },
        lives = remember { mutableIntStateOf(0) },
        game = remember { mutableStateOf(null) },
        onNavigateUp = {}
    )
}
