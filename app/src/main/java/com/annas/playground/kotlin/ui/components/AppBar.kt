package com.annas.playground.kotlin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.annas.playground.kotlin.ui.theme.MediumPadding
import com.annas.playground.kotlin.ui.theme.PlaygroundTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    appBarBackground: Color = MaterialTheme.colorScheme.primary,
    appBarContentColor: Color = Color.White,
    barTitle: String,
    onLeadingIconClicked: (() -> Unit)? = null,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors()
            .copy(containerColor = appBarBackground, titleContentColor = appBarContentColor),
        modifier = modifier
            .background(color = appBarBackground)
            .padding(horizontal = MediumPadding),
        title = {
            Text(
                modifier = Modifier,
                text = barTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = appBarContentColor,
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        navigationIcon = {
            onLeadingIconClicked?.let {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable { onLeadingIconClicked() },
                    tint = appBarContentColor
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AppBarPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    PlaygroundTheme(isDarkTheme) {
        AppBar(barTitle = "Preview", onLeadingIconClicked = {})
    }
}
