package com.annas.playground.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.annas.playground.ui.graph.NavGraph
import com.annas.playground.ui.theme.PlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var splashScreen: SplashScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        viewModel.prepareProduct()
        splashScreen.setKeepOnScreenCondition { false }
        enableEdgeToEdge()
        setContent {
            PlaygroundTheme {
                NavGraph()
            }
        }
    }
}
