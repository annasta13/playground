package com.annas.playground.ui.screen.dataprocessing.productlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.annas.playground.R
import com.annas.playground.data.domain.model.Product
import com.annas.playground.ui.components.BodyText
import com.annas.playground.ui.components.ScreenContainer
import com.annas.playground.ui.components.ThemePreviewParameterProvider
import com.annas.playground.ui.theme.LargePadding
import com.annas.playground.ui.theme.MediumPadding
import com.annas.playground.ui.theme.PlaygroundTheme
import com.annas.playground.ui.theme.SmallPadding

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel(),
    onNavigate: (Int) -> Unit,
    onNavigateUp: () -> Unit
) {
    val list = viewModel.list
    ProductListContent(list = list, onNavigate = onNavigate, onNavigateUp = onNavigateUp)
}

@Composable
private fun ProductListContent(
    list: SnapshotStateList<Product>, onNavigate: (Int) -> Unit,
    onNavigateUp: () -> Unit
) {
    ScreenContainer(
        barTitle = stringResource(id = R.string.product_list),
        onLeadingIconClicked = onNavigateUp
    ) {
        LazyColumn(
            contentPadding = PaddingValues(LargePadding),
            verticalArrangement = Arrangement.spacedBy(MediumPadding)
        ) {
            items(list) {
                Card {
                    Row(
                        modifier = Modifier
                            .clickable { onNavigate(it.id) }
                            .fillMaxWidth()
                            .padding(SmallPadding),
                        horizontalArrangement = Arrangement.spacedBy(MediumPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(it.imageUrl),
                            contentDescription = it.name,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        BodyText(text = it.name, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListContentPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    val list = remember { mutableStateListOf<Product>() }
    repeat(10) {
        val item = Product(
            id = it + 1,
            name = "Product - $it",
            imageUrl = "http://dummyimage.com/111x100.png/ff$it$it$it$it/ffffff"
        )
        list.add(item)
    }
    PlaygroundTheme(isDarkTheme) {
        ProductListContent(list = list, onNavigate = {}, onNavigateUp = {})
    }
}
