package com.foliaco.memocards.ui.login.ui

import android.content.Context
import android.content.res.Resources
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.foliaco.memocards.R
import com.foliaco.memocards.screens.Screens
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

enum class ProviderAuthType {
    BASIC,
    GOOGLE
}

class LoginViewModel(
    private val navController: NavController
) : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>(false)
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessFullLogin = MutableLiveData<Boolean>(false)
    val isSuccessFullLogin: LiveData<Boolean> = _isSuccessFullLogin

    private val _msgLogin = MutableLiveData<String>("")
    val msgLogin: LiveData<String> = _msgLogin

    fun onLoginChange(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidEmail(value: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(value).matches()

    private fun isValidPassword(value: String): Boolean = value.length > 6

    fun closeModal() {
        _isSuccessFullLogin.value = false;
    }

    fun onLogin() {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(_email.value.toString(), _password.value.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    _isSuccessFullLogin.value = false
                    navController.navigate(Screens.HomeScreen.route)
                } else {
                    _isSuccessFullLogin.value = true
                    _msgLogin.value =
                        "Error al iniciar sesión, la contraseña o el usuario no existen"
                    println(it)
                }
            }
    }

    fun sigInWithGoogle() {
        val res = Resources.getSystem()
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(res.getString(R.string.default_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()

    }
}
