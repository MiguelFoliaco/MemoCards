package com.foliaco.memocards.screens

import android.content.Intent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foliaco.memocards.HomeActivity
import com.foliaco.memocards.modules.cards.ui.CrateCardScreen
import com.foliaco.memocards.modules.cards.ui.CreateCardForm
import com.foliaco.memocards.modules.components.ScaffoldCustomHome
import com.foliaco.memocards.modules.home.ui.HomeScreen
import com.foliaco.memocards.modules.home.ui.HomeScreenViewModel
import com.foliaco.memocards.modules.login.ui.LoginScreen
import com.foliaco.memocards.modules.login.ui.LoginViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(loginGoogle: () -> Unit) {
    val navController = rememberNavController()
    val auth = Firebase.auth
    val user = auth.currentUser

    NavHost(navController = navController, startDestination = Screens.LoginScreen.route) {
        composable(Screens.LoginScreen.route) {
            val viewModel = LoginViewModel(navController, loginGoogle)
            if (user != null) {
                val intent = Intent(navController.context, HomeActivity::class.java)
                navController.context.startActivity(intent)
            }
            println("Usuario $user")
            LoginScreen(viewModel = viewModel)
        }
//        composable(Screens.HomeScreen.route) {
//            if(user==null){
//                navController.navigate(Screens.LoginScreen.route)
//            }
//            HomeScreen()
//        }
    }
}


@Composable
fun AppNavigationHome(viewModel: HomeScreenViewModel, clearActivity: () -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ListScreensHome.home.route) {
        composable(route = ListScreensHome.home.route) {
            ScaffoldCustomHome(navController, clearActivity) {
                HomeScreen(viewModel = viewModel)
            }
        }
        composable(route = ListScreensHome.add.route) {
            ScaffoldCustomHome(navController, clearActivity) {
                CrateCardScreen(viewModel)
            }
        }
    }
}


