package com.annas.playground.kotlin.ui.screen.documentScanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.annas.playground.R
import com.annas.playground.kotlin.ui.components.PrimaryButton

/**
 * Created by Annas Surdyanto on 02/03/2023
 * @param onTakePicture is the unit triggered when the user click the button to take picture.
 */
@Composable
fun StandbyView(modifier: Modifier = Modifier, onTakePicture: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        PrimaryButton(text = stringResource(R.string.take_picture), onClick = onTakePicture)
    }
}
