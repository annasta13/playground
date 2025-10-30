package com.annas.playground.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.annas.playground.data.domain.model.Menu

@Composable
fun SebMenuSelectionScreen(
    topBarTitle: String,
    title: String,
    onNavigateUp: () -> Unit,
    list: List<Menu>,
    onNavigate: (String) -> Unit
) {
    ScreenContainer(
        barTitle = topBarTitle,
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
                    text = title,
                    modifier = Modifier.padding(bottom = 12.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                list.forEach {
                    SecondaryButton(
                        modifier = Modifier.fillMaxWidth(fraction = 0.7f),
                        text = it.title,
                        onClick = { onNavigate(it.route) }
                    )
                }
            }
        }
    }
}
