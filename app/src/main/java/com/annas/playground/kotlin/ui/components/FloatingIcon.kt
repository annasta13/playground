package com.annas.playground.kotlin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.annas.playground.R
import com.annas.playground.kotlin.ui.theme.PlaygroundTheme

@Composable
fun FloatingIcon(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Default.AddCircle,
        contentDescription = stringResource(R.string.add_user),
        modifier = modifier
            .size(45.dp)
            .clickable(onClick = onClick),
        tint = MaterialTheme.colorScheme.tertiary
    )
}

@Preview
@Composable
private fun FloatingIconPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    PlaygroundTheme(isDarkTheme) {
        ScreenContainer(
            barTitle = "Floating Icon",
            floatingActionButton = { FloatingIcon(onClick = {}) }
        ) {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}
