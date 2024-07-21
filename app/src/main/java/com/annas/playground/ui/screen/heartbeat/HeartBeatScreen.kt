package com.annas.playground.ui.screen.heartbeat

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.annas.playground.R
import com.annas.playground.constants.FloatConstant
import com.annas.playground.constants.IntConstant
import com.annas.playground.constants.IntConstant.SECONDS_OF_MINUTE
import com.annas.playground.ui.components.BodyText
import com.annas.playground.ui.components.ScreenContainer
import com.annas.playground.ui.components.ThemePreviewParameterProvider
import com.annas.playground.ui.theme.LargePadding
import com.annas.playground.ui.theme.MediumPadding
import com.annas.playground.ui.theme.PlaygroundTheme
import com.annas.playground.ui.theme.SmallPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationScreen(onNavigateUp: () -> Unit) {
    ScreenContainer(
        barTitle = stringResource(id = R.string.heart_beat_animation),
        onLeadingIconClicked = onNavigateUp
    ) {

        val scaleFactor = remember { Animatable(FloatConstant.DEFAULT_HEART_BEAT_SCALE) }
        var count by remember { mutableIntStateOf(1) }
        var heartBitPerMinute by remember { mutableIntStateOf(IntConstant.FIFTY_BEAT_PER_SECONDS) }

        LaunchedEffect(key1 = Unit) {
            while (true) {
                val duration = IntConstant.ONE_SECOND / heartBitPerMinute * SECONDS_OF_MINUTE
                scaleFactor.animateTo(
                    FloatConstant.HEART_BEATING_SCALE,
                    tween(
                        easing = FastOutSlowInEasing,
                        durationMillis = duration.div(count)
                    )
                )
                count++
                scaleFactor.animateTo(
                    FloatConstant.DEFAULT_HEART_BEAT_SCALE,
                    tween(
                        easing = FastOutSlowInEasing,
                        durationMillis = duration.div(count)
                    )
                )
                count--
            }
        }

        var showSheet by remember { mutableStateOf(false) }

        if (showSheet) HeartBeatSheet(
            onDismiss = { showSheet = false },
            onSelected = { heartBitPerMinute = it }
        )

        Column(
            modifier = Modifier.padding(LargePadding)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSheet = true },
                colors = CardDefaults.cardColors()
                    .copy(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.elevatedCardElevation()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MediumPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(SmallPadding)) {
                        BodyText(text = stringResource(id = R.string.heart_beat))
                        BodyText(
                            text = heartBitPerMinute.toString(),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                propagateMinConstraints = true
            ) {
                Image(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(id = R.string.content_description_heart_beat),
                    colorFilter = ColorFilter.tint(Color.Red),
                    modifier = Modifier
                        .size(72.dp)
                        .scale(scaleFactor.value)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AnimationScreenPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    PlaygroundTheme(isDarkTheme) {
        AnimationScreen() {}
    }
}
