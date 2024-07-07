package com.foliaco.memocards.screens

sealed class Screens(val route:String) {
    object LoginScreen: Screens("login_screen")
    object HomeScreen: Screens("home_screen")
}