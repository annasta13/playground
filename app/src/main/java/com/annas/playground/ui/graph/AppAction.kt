package com.annas.playground.ui.graph

import androidx.navigation.NavController
import timber.log.Timber

class AppAction(navController: NavController) {
    val navigate: (String) -> Unit = {
        Timber.d("check navigate $it")
        navController.navigate(it)
    }

    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }

}
