package com.annas.playground.kotlin.ui.screen.objectdetector

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.annas.playground.R
import com.annas.playground.kotlin.ui.components.HeadingText
import com.annas.playground.kotlin.ui.components.ScreenContainer
import com.annas.playground.kotlin.ui.components.SecondaryButton
import com.annas.playground.kotlin.helper.tensorflow.ObjectDetectorType

@Composable
fun ObjectDetectorScreen(
    onNavigate: (String) -> Unit,
    onNavigateUp: () -> Unit
) {
    val list = remember { ObjectDetectorType.list }
    ScreenContainer(
        barTitle = stringResource(R.string.object_detector),
        onLeadingIconClicked = onNavigateUp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeadingText(
                    text = stringResource(R.string.select_model),
                    modifier = Modifier.padding(bottom = 12.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                list.forEach {
                    SecondaryButton(
                        modifier = Modifier.fillMaxWidth(fraction = 0.7f),
                        text = it.name,
                        onClick = { onNavigate(it.route) }
                    )
                }
            }
        }
    }
}
