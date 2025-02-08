package com.annas.playground.kotlin.ui.screen.arcore

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.annas.playground.R
import com.annas.playground.kotlin.ui.screen.objectdetector.DetectorContainer


@Composable
fun ArcoreScreen(onNavigateUp: () -> Unit) {

    DetectorContainer(onNavigateUp = onNavigateUp, title = stringResource(R.string.arcore)) {
        ObjectScanningView()
    }
}

