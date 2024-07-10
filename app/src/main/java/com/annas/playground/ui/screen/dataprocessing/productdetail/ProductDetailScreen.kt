package com.annas.playground.ui.screen.dataprocessing.productdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.annas.playground.R
import com.annas.playground.constants.StringConstant
import com.annas.playground.constants.StringConstant.SELLING
import com.annas.playground.data.domain.model.ProductDetail
import com.annas.playground.data.domain.model.Transaction
import com.annas.playground.data.local.model.TransactionEntity
import com.annas.playground.data.local.model.TransactionEntity.Companion.IN
import com.annas.playground.ui.components.BodyText
import com.annas.playground.ui.components.FloatingIcon
import com.annas.playground.ui.components.ProductHeaderView
import com.annas.playground.ui.components.ScreenContainer
import com.annas.playground.ui.components.ThemePreviewParameterProvider
import com.annas.playground.ui.graph.Destination
import com.annas.playground.ui.graph.RouteParam.MIN_DATE
import com.annas.playground.ui.graph.RouteParam.PRODUCT_ID
import com.annas.playground.ui.graph.RouteParam.STOCK
import com.annas.playground.ui.graph.onRefresh
import com.annas.playground.ui.theme.LargePadding
import com.annas.playground.ui.theme.MediumPadding
import com.annas.playground.ui.theme.PlaygroundTheme
import com.annas.playground.ui.theme.Purple40
import com.annas.playground.ui.theme.Purple80
import com.annas.playground.ui.theme.PurpleGrey40
import com.annas.playground.ui.theme.SmallPadding
import com.annas.playground.utils.LongExtension.toDateString
import com.annas.playground.utils.StringExtension.toMilliseconds

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    navController: NavController,
    onNavigate: (String) -> Unit,
    onNavigateUp: () -> Unit
) {
    val product by viewModel.product
    navController.onRefresh(function = viewModel::getProductDetail)
    ProductDetailContent(product = product, onNavigate = onNavigate, onNavigateUp = onNavigateUp)
}

@Composable
private fun ProductDetailContent(
    product: ProductDetail?, onNavigate: (String) -> Unit,
    onNavigateUp: () -> Unit
) {
    ScreenContainer(
        barTitle = stringResource(id = R.string.product_detail),
        onLeadingIconClicked = onNavigateUp,
        floatingActionButton = {
            product?.let {
                FloatingIcon {
                    val minTransactionDate = it.transactionList.first().date.toMilliseconds()
                    onNavigate(
                        Destination.CREATE_TRANSACTION
                            .replace("{$PRODUCT_ID}", it.id.toString())
                            .replace("{$MIN_DATE}", minTransactionDate.toString())
                            .replace("{$STOCK}", product.stock.toString())
                    )
                }
            }
        }
    ) {
        product?.run {
            LazyColumn(
                contentPadding = PaddingValues(LargePadding),
                verticalArrangement = Arrangement.spacedBy(MediumPadding)
            ) {
                item {
                    ProductHeaderView(
                        name = name,
                        stock = stock,
                        image = imageUrl
                    )
                }
                items(transactionList) {
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(SmallPadding),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val typeLabel = if (it.type == IN) StringConstant.STOCKING else SELLING
                            val rotation = if (it.type == IN) 180f else 0f
                            val color = if (it.type == IN) MaterialTheme.colorScheme.outline
                            else Color.Magenta
                            Column {
                                BodyText(text = it.date)
                                BodyText(
                                    text = stringResource(
                                        R.string.label_amount,
                                        typeLabel,
                                        it.amount
                                    )
                                )
                            }
                            Image(
                                painter = painterResource(id = R.drawable.ic_in_out),
                                contentDescription = "Transaction ${it.type}",
                                modifier = Modifier
                                    .size(20.dp)
                                    .rotate(rotation)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(color)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductDetailContentPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    val product by remember {
        val transactions = listOf(
            Transaction(1, IN, System.currentTimeMillis().toDateString(), 10),
            Transaction(1, TransactionEntity.OUT, System.currentTimeMillis().toDateString(), 5),
            Transaction(1, TransactionEntity.OUT, System.currentTimeMillis().toDateString(), 33),
        )
        val product = ProductDetail(
            1,
            "Pickerel - Fillets",
            "http://dummyimage.com/111x100.png/ff4444/ffffff",
            stock = 50,
            transactions
        )
        mutableStateOf(product)
    }
    PlaygroundTheme(isDarkTheme) {
        ProductDetailContent(product = product, onNavigateUp = {}, onNavigate = {})
    }
}
