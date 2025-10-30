package com.annas.playground.ui.screen.games.jumpinggame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.annas.playground.R
import com.annas.playground.ui.components.BodyText
import com.annas.playground.ui.components.HeadingText
import com.annas.playground.ui.components.PrimaryButton
import com.annas.playground.ui.screen.games.jumpinggame.JumpingGameConstants.GAME_OVER_BUTTON_FRACTION
import com.annas.playground.ui.theme.MediumPadding


@Composable
fun GameOverView(score: Int, highestScore: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeadingText(text = stringResource(R.string.game_over), color = Color.White)
                HeadingText(
                    text = stringResource(R.string.score, score),
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                )
                BodyText(
                    text = stringResource(R.string.highest_score, highestScore),
                    style = MaterialTheme.typography.bodyMedium.copy(Color.White)
                )
            }
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(GAME_OVER_BUTTON_FRACTION),
                text = stringResource(id = R.string.retry), onClick = onClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameOverPreview() {
    GameOverView(12, 100) {

    }
}
