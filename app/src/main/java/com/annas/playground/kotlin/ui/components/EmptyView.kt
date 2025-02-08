/*
 * Copyright (c) Habil Education 2023. All rights reserved.
 */

package com.annas.playground.kotlin.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.annas.playground.R
import com.annas.playground.kotlin.ui.theme.LargePadding

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    message: String = stringResource(id = R.string.data_not_found),
    buttonText: String = stringResource(id = R.string.retry),
    @DrawableRes emptyIcon: Int = R.drawable.ic_empty_data_icon,
    showIcon: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(LargePadding)
        ) {
            if (showIcon) Image(
                painter = painterResource(id = emptyIcon),
                contentDescription = stringResource(R.string.data_not_found),
                modifier = Modifier.size(100.dp)
            )
            BodyText(text = message, textAlign = TextAlign.Center)
            onClick?.let {
                PrimaryButton(text = buttonText, onClick = it)
            }
        }
    }
}

