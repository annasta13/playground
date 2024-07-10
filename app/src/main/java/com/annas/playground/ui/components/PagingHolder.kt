package com.annas.playground.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.compose.LazyPagingItems
import com.annas.playground.R
import com.annas.playground.helper.EmptyDataException
import com.annas.playground.ui.theme.LargePadding
import com.annas.playground.ui.theme.SmallPadding
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
fun <T : Any> PagingHolder(
    modifier: Modifier = Modifier,
    list: LazyPagingItems<T>,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    header: @Composable() (() -> Unit)? = null,
    onRetry: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    list.apply {
        when (loadState.refresh) {
            is Loading -> LoadingScreen()
            is Error -> {
                val e = loadState.refresh as Error
                if (e.error is EmptyDataException) {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                    ) {
                        header?.invoke()
                        EmptyView(
                            message = e.error.message.toString(),
                            buttonText = stringResource(id = R.string.retry),
                            onClick = onRetry
                        )
                    }
                } else {
                    EmptyView(
                        modifier = Modifier.fillMaxSize(),
                        message = e.error.message.orEmpty(),
                        onClick = list::retry
                    )
                }
            }

            else -> {
                if (list.itemCount > 0) LazyColumn(
                    modifier = modifier,
                    contentPadding = contentPadding,
                    verticalArrangement = verticalArrangement
                ) {
                    header?.let { item { it.invoke() } }
                    content()
                    if (loadState.append is Loading) item {
                        ListLoadingView()
                    }
                    if (loadState.append is Error && loadState.append !is Loading) {
                        val e = loadState.append as Error
                        item {
                            CardErrorView(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                message = e.error.message.orEmpty(),
                                onClick = list::retry
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListLoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = LargePadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SwipeRefreshIndicator(SwipeRefreshState(true), 50.dp)
    }
}

@Composable
fun CardErrorView(
    modifier: Modifier = Modifier,
    message: String,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .heightIn(min = 40.dp)
            .background(Color.DarkGray)
            .padding(horizontal = LargePadding)
    ) {
        val (textRef, buttonRef) = createRefs()
        CaptionText(
            text = message, modifier = Modifier.constrainAs(textRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(buttonRef.start, margin = 4.dp)
                width = Dimension.fillToConstraints
            }
        )

        CaptionText(
            text = stringResource(id = R.string.retry),
            modifier = Modifier
                .constrainAs(buttonRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .padding(SmallPadding)
                .clickable(onClick = onClick)
        )
    }
}

data class EmptyDataAction(
    val message: String,
    val emptyDataButtonText: String? = null,
    val onClick: (() -> Unit)? = null
)
