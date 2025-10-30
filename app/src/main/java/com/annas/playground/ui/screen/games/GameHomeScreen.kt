package com.annas.playground.ui.screen.games

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.annas.playground.R
import com.annas.playground.data.domain.model.Menu
import com.annas.playground.ui.components.SebMenuSelectionScreen
import com.annas.playground.ui.graph.Destination

@Composable
fun GameHomeScreen(
    onNavigate:(String)->Unit,
    onNavigateUp:()->Unit
) {
    val list = remember {
        listOf(
            Menu(title = "Fruit Cart (Surface Choreographer)", route = Destination.FRUIT_CART_GAME),
            Menu(title = "Jumping Game (Jetpack Compose)", route = Destination.JUMPING_GAME)
        )
    }
    SebMenuSelectionScreen(
        topBarTitle = stringResource(R.string.object_detector),
        title = stringResource(id = R.string.select_model),
        onNavigateUp = onNavigateUp,
        list = list,
        onNavigate = onNavigate
    )
}
