package com.sanskar.unilink

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
import com.sanskar.unilink.screens.signUpScreen
import com.sanskar.unilink.viewmodel.ViewModel

@Composable
fun AppNav() {
    val viewModel: ViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.SIGNUP) { signUpScreen(navController,viewModel) }
        composable(Routes.LOGIN) { LoginScreen(navController,viewModel) }
        composable(Routes.HOME) { HomeScreen(navController, viewModel) }
        composable(Routes.REPORT) { ReportItemScreen(navController,viewModel) }
        composable(Routes.PROFILE) { ProfileScreen(navController) }
        composable("${Routes.ITEM_DETAILS}/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            itemId?.let { ItemDetailsScreen(navController, it) }
        }

        composable(Routes.LOST){
            LoginScreen(navController, viewModel)
        }
    }
}
