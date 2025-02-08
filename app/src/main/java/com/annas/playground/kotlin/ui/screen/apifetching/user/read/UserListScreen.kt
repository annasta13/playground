package com.annas.playground.kotlin.ui.screen.apifetching.user.read

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.annas.playground.R
import com.annas.playground.kotlin.data.domain.model.User
import com.annas.playground.kotlin.ui.components.BodyText
import com.annas.playground.kotlin.ui.components.CaptionText
import com.annas.playground.kotlin.ui.components.FloatingIcon
import com.annas.playground.kotlin.ui.components.PagingHolder
import com.annas.playground.kotlin.ui.components.ScreenContainer
import com.annas.playground.kotlin.ui.components.ThemePreviewParameterProvider
import com.annas.playground.kotlin.ui.graph.Destination
import com.annas.playground.kotlin.ui.graph.onRefresh
import com.annas.playground.kotlin.ui.theme.LargePadding
import com.annas.playground.kotlin.ui.theme.MediumPadding
import com.annas.playground.kotlin.ui.theme.PlaygroundTheme
import com.annas.playground.kotlin.ui.theme.SmallPadding
import kotlinx.coroutines.flow.flowOf

@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel(),
    navController: NavController,
    onNavigate: (String) -> Unit,
    onNavigateUp: () -> Unit
) {
    val list = viewModel.list.collectAsLazyPagingItems()
    navController.onRefresh(function = viewModel::fetchUsers)

    UserListContent(
        list = list,
        onRefresh = list::refresh,
        onNavigate = onNavigate,
        onNavigateUp = onNavigateUp
    )
}

@Composable
private fun UserListContent(
    list: LazyPagingItems<User>,
    onRefresh: () -> Unit,
    onNavigate: (String) -> Unit,
    onNavigateUp: () -> Unit
) {
    ScreenContainer(
        barTitle = stringResource(id = R.string.users),
        onRefresh = onRefresh,
        onLeadingIconClicked = onNavigateUp,
        floatingActionButton = {
            FloatingIcon { onNavigate(Destination.CREATE_USER) }
        }
    ) {
        PagingHolder(
            list = list,
            contentPadding = PaddingValues(LargePadding),
            verticalArrangement = Arrangement.spacedBy(MediumPadding),
            onRetry = onRefresh
        ) {
            items(list.itemCount) {
                runCatching { list[it] }.getOrNull()?.let { item ->
                    UserItemView(item)
                }
            }
        }
    }
}

@Composable
private fun UserItemView(user: User) {
    user.apply {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SmallPadding),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.widthIn(max = 250.dp),
                    horizontalArrangement = Arrangement.spacedBy(SmallPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp),
                        tint = Color.Gray
                    )
                    Column {
                        BodyText(text = name, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        CaptionText(text = email, maxLines = 1)
                    }
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    BodyText(text = status)
                    CaptionText(text = gender)
                }
            }
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
private fun UserScreenContentPreview(@PreviewParameter(ThemePreviewParameterProvider::class) isDarkTheme: Boolean) {
    val users = mutableListOf<User>()
    repeat(10) {
        val count = it + 1
        users.add(
            User(
                email = "sample$count@gmail.com",
                gender = "Female",
                id = 847353 + count,
                name = "User $count",
                status = "active"
            )
        )
    }

    val list = flowOf(PagingData.from(users)).collectAsLazyPagingItems()
    PlaygroundTheme(isDarkTheme) {
        ScreenContainer(barTitle = stringResource(id = R.string.users), onLeadingIconClicked = {}) {
            LazyColumn(contentPadding = PaddingValues(LargePadding)) {
                items(list.itemCount) {
                    kotlin.runCatching { list[it] }.getOrNull()?.let { item ->
                        UserItemView(item)
                    }
                }
            }
        }
    }
}
