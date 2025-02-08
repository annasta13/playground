package com.annas.playground.kotlin.ui.screen.generator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.annas.playground.R
import com.annas.playground.kotlin.ui.components.BodyText
import com.annas.playground.kotlin.ui.components.ClickableImage
import com.annas.playground.kotlin.ui.components.ScreenContainer
import com.annas.playground.kotlin.ui.components.Space
import com.annas.playground.kotlin.ui.theme.LargePadding
import com.annas.playground.kotlin.ui.theme.MediumPadding

@Composable
fun GeneratorResultScreen(onNavigateUp: () -> Unit) {
    ScreenContainer(
        onLeadingIconClicked = onNavigateUp,
        barTitle = stringResource(id = R.string.csv_based_string_resource)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(LargePadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MediumPadding)
        ) {
            BodyText(text = stringResource(R.string.csv_file_imported))
            ClickableImage(
                resource = R.drawable.sample_generator_csv,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                title = stringResource(R.string.csv_file_imported)
            )

            Space()

            BodyText(text = stringResource(R.string.string_resource_generated))
            ClickableImage(
                resource = R.drawable.sample_generator_xml,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                title = stringResource(R.string.string_resource_generated)
            )
        }
    }
}
