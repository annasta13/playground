package com.annas.playground.kotlin.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isTransparentLoading: Boolean = false,
    errorMessage: String? = null,
    isDataEmpty: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    appBarBackground: Color = MaterialTheme.colorScheme.primary,
    appBarContentColor: Color = Color.White,
    barTitle: String? = null,
    floatingActionButton: @Composable () -> Unit = {},
    onLeadingIconClicked: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            barTitle?.let {
                AppBar(
                    barTitle = it,
                    appBarBackground = appBarBackground,
                    appBarContentColor = appBarContentColor,
                    onLeadingIconClicked = onLeadingIconClicked
                )
            }
        },
        floatingActionButton = floatingActionButton,
        content = { padding ->
            val initialLoading = isDataEmpty && isLoading
            SwipeRefresh(
                state = SwipeRefreshState(isLoading && !isDataEmpty),
                onRefresh = { onRefresh?.invoke() },
                modifier = modifier.padding(padding),
                swipeEnabled = onRefresh != null
            ) {
                when {
                    initialLoading -> LoadingScreen()
                    isDataEmpty -> EmptyView()
                    errorMessage.isNullOrEmpty().not() -> EmptyView(
                        message = errorMessage.orEmpty(),
                        onClick = onRefresh
                    )

                    else -> {
                        content()
                        if (isTransparentLoading) TransparentLoading()
                    }
                }
            }
        }
    )
}
