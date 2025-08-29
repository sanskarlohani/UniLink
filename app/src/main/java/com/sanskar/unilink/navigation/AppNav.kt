package com.sanskar.unilink.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanskar.unilink.screens.HomeScreen
import com.sanskar.unilink.screens.ItemDetailsScreen
import com.sanskar.unilink.screens.LoginScreen
import com.sanskar.unilink.screens.ProfileScreen
import com.sanskar.unilink.screens.ReportItemScreen
import com.sanskar.unilink.screens.SignUpScreen
import com.sanskar.unilink.screens.SplashScreen
import com.sanskar.unilink.viewmodel.ViewModel

@Composable
fun AppNav() {
    val viewModel: ViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) { SplashScreen(navController, viewModel) }
        composable(Routes.SIGNUP) { SignUpScreen(navController, viewModel) }
        composable(Routes.LOGIN) { LoginScreen(navController, viewModel) }
        composable(Routes.HOME) { HomeScreen(navController, viewModel) }
        composable(Routes.REPORT) { ReportItemScreen(navController, viewModel) }
        composable(Routes.PROFILE) { ProfileScreen(navController, viewModel) }
        composable("${Routes.ITEM_DETAILS}/{itemId}/{type}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            val type = backStackEntry.arguments?.getString("type")
            itemId?.let {
                ItemDetailsScreen(navController, it, viewModel = viewModel, type = type ?: "")
            }
        }

        composable(Routes.LOST){
            LoginScreen(navController, viewModel)
        }
        composable(Routes.FOUND){
            LoginScreen(navController, viewModel)
        }
    }
}
