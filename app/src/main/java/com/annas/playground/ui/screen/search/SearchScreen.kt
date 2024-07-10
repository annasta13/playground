package com.annas.playground.ui.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.annas.playground.R
import com.annas.playground.data.domain.model.mockCities
import com.annas.playground.ui.components.BodyText
import com.annas.playground.ui.components.BorderedTextField
import com.annas.playground.ui.components.ScreenContainer
import com.annas.playground.ui.components.ThemePreviewParameterProvider
import com.annas.playground.ui.theme.LargePadding
import com.annas.playground.ui.theme.MediumPadding
import com.annas.playground.ui.theme.PlaygroundTheme
import com.annas.playground.ui.theme.SmallPadding

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val list = viewModel.listShowing
    val searchKey by viewModel.searchKey
    LaunchedEffect(key1 = searchKey) {
        viewModel.searchCity()
    }
    SearchContentView(list = list, searchState = viewModel.searchKey, onNavigateUp = onNavigateUp)
}


@Composable
fun SearchContentView(
    list: SnapshotStateList<String>, searchState: MutableState<String>,
    onNavigateUp: () -> Unit
) {
    ScreenContainer(
        barTitle = stringResource(id = R.string.search_demo_title),
        onLeadingIconClicked = onNavigateUp
    ) {
        Column(modifier = Modifier.padding(LargePadding)) {
            BorderedTextField(
                modifier = Modifier.fillMaxWidth(),
                state = searchState,
                label = stringResource(id = R.string.search)
            )
            LazyColumn(
                contentPadding = PaddingValues(vertical = LargePadding),
                verticalArrangement = Arrangement.spacedBy(MediumPadding)
            ) {
                itemsIndexed(list) { i, item ->
                    SearchItemView(i + 1, item)
                }
            }
        }
    }
}


@Composable
private fun SearchItemView(number: Int, title: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding),
            horizontalArrangement = Arrangement.spacedBy(SmallPadding)
        ) {
            BodyText(text = number.toString().plus("."))
            BodyText(text = title)
        }
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun SearchContentPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    PlaygroundTheme(isDarkTheme) {
        val list = remember { mutableStateListOf<String>() }
        LaunchedEffect(key1 = Unit) {
            list.addAll(mockCities)
        }
        SearchContentView(
            list = list,
            searchState = remember { mutableStateOf("") },
            onNavigateUp = {})
    }
}
