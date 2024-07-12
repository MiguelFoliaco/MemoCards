package com.foliaco.memocards.screens

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foliaco.memocards.HomeActivity
import com.foliaco.memocards.ui.home.ui.HomeScreen
import com.foliaco.memocards.ui.login.ui.LoginScreen
import com.foliaco.memocards.ui.login.ui.LoginViewModel
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
            if(user!=null){
                val intent=Intent(navController.context, HomeActivity::class.java)
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