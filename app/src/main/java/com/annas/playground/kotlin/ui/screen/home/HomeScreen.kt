package com.annas.playground.kotlin.ui.screen.home

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.annas.playground.R
import com.annas.playground.kotlin.ui.components.BodyText
import com.annas.playground.kotlin.ui.components.ScreenContainer
import com.annas.playground.kotlin.ui.components.ThemePreviewParameterProvider
import com.annas.playground.kotlin.ui.graph.Destination
import com.annas.playground.kotlin.ui.graph.Section
import com.annas.playground.kotlin.ui.screen.arcore.ArCoreActivity
import com.annas.playground.kotlin.ui.theme.LargePadding
import com.annas.playground.kotlin.ui.theme.MediumPadding
import com.annas.playground.kotlin.ui.theme.PlaygroundTheme
import com.annas.playground.kotlin.ui.theme.SmallPadding

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val list = remember { Section.sectionList }
    ScreenContainer(barTitle = stringResource(id = R.string.app_name)) {
        Column(modifier = Modifier.padding(LargePadding)) {
            BodyText(text = stringResource(id = R.string.topics), fontWeight = FontWeight.SemiBold)
            LazyColumn(
                contentPadding = PaddingValues(vertical = LargePadding),
                verticalArrangement = Arrangement.spacedBy(MediumPadding)
            ) {
                items(list) {
                    HomeItemView(it, onNavigate)
                }
            }
        }
    }
}

@Composable
private fun HomeItemView(item: Section, onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    Card(modifier = Modifier.clickable {
        when (item.route) {
            Destination.ARCORE -> {
                Intent(context, ArCoreActivity::class.java).apply {
                    context.startActivity(this)
                }
            }

            else -> onNavigate(item.route)
        }
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding),
            horizontalArrangement = Arrangement.spacedBy(SmallPadding)
        ) {
            BodyText(text = item.id.toString().plus("."))
            BodyText(text = item.title)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    PlaygroundTheme(isDarkTheme) {
        HomeScreen() {}
    }
}
