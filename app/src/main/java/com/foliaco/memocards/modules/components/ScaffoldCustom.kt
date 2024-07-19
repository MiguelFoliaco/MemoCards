package com.foliaco.memocards.modules.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.foliaco.memocards.R
import com.foliaco.memocards.screens.ListScreensHome
import com.foliaco.memocards.utils.bottomBorder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldCustomHome(navController: NavController, content: @Composable () -> Unit) {
    val auth = Firebase.auth
    val user = auth.currentUser
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color(0xFFFFFFFF),
                ), title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Memo Cards")
                    }
                }, modifier = Modifier.shadow(3.dp, ambientColor = Color(0xFF000000))
            )
        },
        bottomBar = {
            if (user != null) {
                BottomBarHome(user, navController)
            }
        }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}

@Composable
fun BottomBarHome(user: FirebaseUser, navController: NavController) {
    BottomAppBar(
        modifier = Modifier.height(70.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        tonalElevation = BottomAppBarDefaults.ContainerElevation,
        contentPadding = BottomAppBarDefaults.ContentPadding,
        windowInsets = BottomAppBarDefaults.windowInsets,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val currentRoute = navController.currentDestination!!.route.orEmpty()
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(30.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.label),
                    contentDescription = "label button",
                    tint = if (currentRoute === ListScreensHome.target.route) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(60.dp)
                )
            }
            IconButton(
                onClick = {
                    navController.navigate(route = ListScreensHome.home.route)
                }, modifier = Modifier
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "label button",
                    tint = if (currentRoute === ListScreensHome.home.route) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(60.dp)
                )
            }

            IconButton(
                onClick = {
                    navController.navigate(route = ListScreensHome.add.route)
                }, modifier = Modifier
                    .size(30.dp)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "label add card",
                    tint = if (currentRoute === ListScreensHome.add.route) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(25.dp)
                )
            }
            IconButton(
                onClick = { /*TODO*/ }, modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "label button",
                    tint = if (currentRoute === ListScreensHome.settings.route) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(60.dp)
                )
            }
            IconButton(
                onClick = { /*TODO*/ }, modifier = Modifier.size(30.dp)
            ) {
                if (user.photoUrl != null) {
                    SubcomposeAsyncImage(
                        model = user.photoUrl,
                        contentDescription = null,
                        loading = {
                            CircularProgressIndicator()
                        },
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clip(CircleShape),
                    )

                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "label button",
                        tint = if (currentRoute === ListScreensHome.profile.route) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}