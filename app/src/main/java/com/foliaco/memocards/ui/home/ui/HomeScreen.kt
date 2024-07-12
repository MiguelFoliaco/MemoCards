package com.foliaco.memocards.ui.home.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen() {
    val auth = Firebase.auth
    val user = auth.currentUser

    if(user==null){
        return CircularProgressIndicator()
    }
    Column(modifier = Modifier.padding(20.dp)) {

        Text(text = "Hola ${user!!.displayName ?: user!!.email}")
    }
}