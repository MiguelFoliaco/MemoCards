package com.foliaco.memocards

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.foliaco.memocards.screens.AppNavigation
import com.foliaco.memocards.modules.theme.MemoCardsTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {

    private lateinit var mGoogleSigInClient: GoogleSignInClient
    private var auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSigInClient = GoogleSignIn.getClient(this, gso)
        setContent {
            MemoCardsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation() {
                        startSignIntWithGoogle()
                    }
                }
            }
        }
    }


    private fun startSignIntWithGoogle() {
        val googleSigIntent = mGoogleSigInClient.signInIntent
        googleSigInARL.launch(googleSigIntent)
    }

    private val googleSigInARL =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val data = it.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    AuthenticarGoogleAndFirebase(account.idToken)
                } catch (err: Exception) {
                    println("\nError al login ${err.message}")
                    Toast.makeText(
                        this,
                        "Ocurrio un error debido a ${err.message}",
                        Toast.LENGTH_LONG
                    )
                }
            } else {
                Toast.makeText(this, "Inicio de sesión cancelado", Toast.LENGTH_LONG)
            }
        }

    private fun AuthenticarGoogleAndFirebase(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                val intent = Intent(this, HomeActivity::class.java)
                Toast.makeText(
                    applicationContext,
                    "Bienvenido ${it.user!!.email ?: ""}!",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG)
                    .show()
            }
    }

}
