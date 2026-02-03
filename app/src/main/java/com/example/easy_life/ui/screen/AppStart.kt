package com.example.easy_life.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.easy_life.data.local.Theme
import com.example.easy_life.data.local.isFirstLaunch
import com.example.easy_life.data.local.setNotFirstLaunch
import com.example.easy_life.ui.screen.home.HomeScreen
import com.example.easy_life.ui.screen.home.SetupScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AppStart(
    theme: Theme = Theme.LIGHTTHEME,
    navController: NavHostController) {
    val context = LocalContext.current
    val isFirstLaunch by isFirstLaunch(context)
        .collectAsState(initial = null)

    when (isFirstLaunch) {
        true -> {
            SetupScreen(
                theme = theme,
                onFinish = {
                    CoroutineScope(Dispatchers.Main).launch {
                        setNotFirstLaunch(context)
                        navController.navigate("home") {
                            popUpTo("setup") { inclusive = false }
                        }
                    }
                }
            )
        }
        false -> {
            HomeScreen(
                theme = theme,
                navController = navController)
        }
        null -> {}
    }
}
