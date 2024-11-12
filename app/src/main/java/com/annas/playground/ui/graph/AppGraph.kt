package com.annas.playground.ui.graph

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.annas.playground.ui.screen.apifetching.user.create.CreateUserScreen
import com.annas.playground.ui.screen.apifetching.user.read.UserListScreen
import com.annas.playground.ui.screen.dataprocessing.createtransaction.CreateTransactionScreen
import com.annas.playground.ui.screen.dataprocessing.productdetail.ProductDetailScreen
import com.annas.playground.ui.screen.dataprocessing.productlist.ProductListScreen
import com.annas.playground.ui.screen.generator.GeneratorResultScreen
import com.annas.playground.ui.screen.heartbeat.AnimationScreen
import com.annas.playground.ui.screen.home.HomeScreen
import com.annas.playground.ui.screen.loadinganimation.LoadingAnimationScreen
import com.annas.playground.ui.screen.search.SearchScreen
import com.annas.playground.ui.theme.enterTransition
import com.annas.playground.ui.theme.exitTransition
import com.annas.playground.ui.theme.popEnterTransition

@Composable
fun NavGraph() {
    val navController: NavHostController = rememberNavController()
    val action = remember(navController) { AppAction(navController) }

    NavHost(
        navController = navController, startDestination = Destination.HOME_SCREEN
    ) {
        registerScreen(route = Destination.HOME_SCREEN) {
            HomeScreen(onNavigate = action.navigate)
        }

        registerScreen(route = Destination.SEARCH_SCREEN) {
            SearchScreen(onNavigateUp = action.navigateUp)
        }

        registerScreen(route = Destination.PRODUCT_LIST) {
            ProductListScreen(onNavigateUp = action.navigateUp, onNavigate = { productId ->
                action.navigate(
                    Destination.PRODUCT_DETAIL.replace(
                        oldValue = "{${RouteParam.PRODUCT_ID}}", newValue = productId.toString()
                    )
                )
            })
        }

        registerScreen(
            route = Destination.PRODUCT_DETAIL,
            arguments = listOf(navArgument(RouteParam.PRODUCT_ID) { type = NavType.IntType })
        ) {
            ProductDetailScreen(
                onNavigate = action.navigate,
                navController = navController,
                onNavigateUp = action.navigateUp
            )
        }

        registerScreen(route = Destination.ANIMATION) {
            AnimationScreen(onNavigateUp = action.navigateUp)
        }

        registerScreen(route = Destination.GENERATOR_RESULT) {
            GeneratorResultScreen(onNavigateUp = action.navigateUp)
        }

        registerScreen(route = Destination.USER_LIST) {
            UserListScreen(
                onNavigate = action.navigate,
                navController = navController,
                onNavigateUp = action.navigateUp
            )
        }

        registerScreen(route = Destination.CREATE_USER) {
            CreateUserScreen(
                onSuccess = {
                    navController.refreshPreviousPage()
                    action.navigateUp()
                },
                onNavigateUp = action.navigateUp
            )
        }

        registerScreen(
            route = Destination.CREATE_TRANSACTION,
            arguments = listOf(
                navArgument(RouteParam.PRODUCT_ID) { type = NavType.IntType },
                navArgument(RouteParam.STOCK) { type = NavType.IntType },
                navArgument(RouteParam.MIN_DATE) { type = NavType.LongType }
            )
        ) {
            CreateTransactionScreen(onNavigateUp = action.navigateUp, onSuccess = {
                navController.refreshPreviousPage()
                action.navigateUp()
            })
        }

        registerScreen(route = Destination.LOADING_ANIMATION) {
            LoadingAnimationScreen()
        }
    }
}

fun NavGraphBuilder.registerScreen(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = { enterTransition },
        popEnterTransition = { popEnterTransition },
        popExitTransition = { exitTransition },
        content = content
    )
}

fun NavController.refreshPreviousPage(key: String = RouteParam.REFRESH) {
    previousBackStackEntry?.savedStateHandle?.set(key, true)
}


fun NavController.onRefresh(key: String = RouteParam.REFRESH, function: () -> Unit) {
    val shouldRefresh =
        this.currentBackStackEntry?.savedStateHandle?.get<Boolean>(key) ?: false
    println("check should refresh $shouldRefresh")
    if (shouldRefresh) function.invoke()
        .also { currentBackStackEntry?.savedStateHandle?.remove<Boolean>(key) }
}
