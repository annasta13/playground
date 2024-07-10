package com.annas.playground.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.annas.playground.R
import com.annas.playground.ui.theme.MediumPadding
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    var holdLoading by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = Unit) {
        delay(300)
        holdLoading = false
    }
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            verticalArrangement = Arrangement.spacedBy(MediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BodyText(text = stringResource(R.string.loading_data))
            if (!holdLoading) LoadingIconScreen()
        }
    }
}

@Composable
fun LoadingIconScreen(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier, color = MaterialTheme.colorScheme.primary)
}


@Composable
@Preview
fun LoadingScreenPreview() {
    LoadingScreen()
}
