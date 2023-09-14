package com.my.zitos.binke.colorpicker.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.my.zitos.binke.colorpicker.screen.HomeScreen
import com.my.zitos.binke.colorpicker.screen.PickImageScreen
import com.my.zitos.binke.colorpicker.viewModel.HomeViewModel

@Composable
fun SetupNavGraph(windowSize: WindowSize, navController: NavHostController) {

    val homeViewModel : HomeViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {

            HomeScreen(windowSize = windowSize, navigationController = navController , homeViewModel = homeViewModel)

        }

        composable(route = Screen.PickedImageScn.route) {

            PickImageScreen(windowSize = windowSize,navigationController = navController, homeViewModel = homeViewModel)
        }


    }
}
