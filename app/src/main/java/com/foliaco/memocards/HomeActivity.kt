package com.foliaco.memocards

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.foliaco.memocards.modules.economia.model.CoinManager
import com.foliaco.memocards.modules.home.ui.HomeScreenViewModel
import com.foliaco.memocards.modules.theme.MemoCardsTheme
import com.foliaco.memocards.screens.AppNavigationHome
import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val firebaseUser = FirebaseAuth.getInstance()
    private val viewModel: HomeScreenViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        validateSession()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@HomeActivity) {}
        }
        setContent {
            MemoCardsTheme {
                AppNavigationHome(viewModel) {
                    clearActivity()
                }
            }
        }
    }

    private fun validateSession() {
        val user = firebaseUser.currentUser
        if (user == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun clearActivity() {
        finish()
    }
}