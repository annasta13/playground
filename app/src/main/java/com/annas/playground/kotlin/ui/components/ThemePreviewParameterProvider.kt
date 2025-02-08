package com.annas.playground.kotlin.ui.components

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ThemePreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        true, false
    )
}
