package com.foliaco.memocards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.foliaco.memocards.modules.home.ui.HomeScreen
import com.foliaco.memocards.modules.home.ui.HomeScreenViewModel
import com.foliaco.memocards.modules.theme.MemoCardsTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val auth = Firebase.auth
    private val user = auth.currentUser!!
    private val viewModel: HomeScreenViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoCardsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = Color(0xFFFFFFFF),
                            ),
                            title = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 10.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(text = "Memo Cards")
                                }
                            },
                            modifier = Modifier.shadow(3.dp, ambientColor = Color(0xFF000000))
                        )
                    },
                    bottomBar = {
                        BottomAppBar(
                            modifier = Modifier.height(70.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            tonalElevation = BottomAppBarDefaults.ContainerElevation,
                            contentPadding = BottomAppBarDefaults.ContentPadding,
                            windowInsets = BottomAppBarDefaults.windowInsets,
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.size(30.dp).fillMaxHeight().align(Alignment.CenterVertically)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.label),
                                        contentDescription = "label button",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(60.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.home),
                                        contentDescription = "label button",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(60.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.settings),
                                        contentDescription = "label button",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(60.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.size(30.dp)
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
                                            painter = painterResource(id = R.drawable.label),
                                            contentDescription = "label button",
                                            tint = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                ) {
                    Column(modifier = Modifier.padding(it)) {
                        HomeScreen(viewModel=viewModel)
                    }
                }
            }
        }
    }
}

