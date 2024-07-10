/*
 * Copyright (c) Habil Education 2023. All rights reserved.
 */

package com.annas.playground.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.annas.playground.ui.theme.PlaygroundTheme

/**
 * Created by Annas Surdyanto on 09/07/24*/
@Composable
fun defaultElevation() = ButtonDefaults.elevatedButtonElevation(0.dp, 0.dp, 0.dp, 0.dp)

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        enabled = enabled,
        elevation = defaultElevation(),
        shape = ButtonDefaults.shape
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        enabled = enabled,
        elevation = defaultElevation(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PrimaryButtonPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    PlaygroundTheme(isDarkTheme) {
        Column {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Primary", onClick = {}
            )
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Secondary", onClick = { }
            )
        }
    }
}
