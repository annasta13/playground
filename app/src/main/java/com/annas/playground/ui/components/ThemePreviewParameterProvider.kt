package com.annas.playground.ui.components

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ThemePreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        true, false
    )
}
