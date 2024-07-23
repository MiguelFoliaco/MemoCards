package com.foliaco.memocards.screens

import com.foliaco.memocards.R

sealed class ListScreensHome(val name: String, val icon: Int?, val route: String) {
    data object target : ListScreensHome("target", R.drawable.label, "targets_screen")
    data object home : ListScreensHome("home", R.drawable.home, "home_screen")
    data object add : ListScreensHome("add", R.drawable.plus, "add_new_card")
    data object update : ListScreensHome("update", R.drawable.plus, "update_card")
    data object settings : ListScreensHome("settings", R.drawable.settings, "setting_screen")
    data object profile : ListScreensHome("profile", null, "profile_screen")

}
