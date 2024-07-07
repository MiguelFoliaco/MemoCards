package com.foliaco.memocards.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foliaco.memocards.ui.home.ui.HomeScreen
import com.foliaco.memocards.ui.login.ui.LoginScreen
import com.foliaco.memocards.ui.login.ui.LoginViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.LoginScreen.route){
        composable(Screens.LoginScreen.route){
            val viewModel=LoginViewModel(navController)
            LoginScreen(viewModel = viewModel,)
        }
        composable(Screens.HomeScreen.route){
            HomeScreen()
        }
    }
}