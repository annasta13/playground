package com.annas.playground.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by Annas Surdyanto on 09/07/24*/
@Composable
fun HeadingText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    paddingTop: Dp = 8.dp,
    paddingBottom: Dp = 8.dp
) {
    Text(
        text = text,
        modifier = modifier.padding(bottom = paddingTop, top = paddingBottom),
        style = style
    )
}

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.titleMedium
) {
    Text(
        text = text,
        modifier = modifier,
        style = style
    )
}

@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    maxLines: Int = 1000,
    overflow: TextOverflow = TextOverflow.Clip,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        style = style,
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow,
        fontWeight = fontWeight
    )
}

@Composable
fun CaptionTextBlack(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
) {
    Text(text = text, style = style, modifier = modifier)
}

@Composable
fun CaptionText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = 1000,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        text = text,
        style = style,
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun CaptionTextSecondary(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary),
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(text = text, style = style, modifier = modifier, textAlign = textAlign)
}

@Composable
fun BodyTextSecondary(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary),
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        text = text,
        modifier = modifier,
        style = style,
        textAlign = textAlign
    )
}
