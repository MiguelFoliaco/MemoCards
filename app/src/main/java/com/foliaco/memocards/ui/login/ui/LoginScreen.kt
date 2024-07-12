package com.foliaco.memocards.ui.login.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.foliaco.memocards.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(viewModel: LoginViewModel) {

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center), viewModel)
    }
}


@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel) {
    val email: String by viewModel.email.observeAsState("")
    val password: String by viewModel.password.observeAsState("")
    val enableButton: Boolean by viewModel.loginEnable.observeAsState(false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(false)
    val stateModal: Boolean by viewModel.isSuccessFullLogin.observeAsState(true)
    val msgLoginInfo: String by viewModel.msgLogin.observeAsState("")
    val rememberCoroutine = rememberCoroutineScope()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        return
    }
    if (stateModal) {
        ModalSessionInfo(modifier = modifier, msgLoginInfo) { viewModel.closeModal() }
    }
    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = modifier.padding(26.dp))
        EmailField(email) { viewModel.onLoginChange(it, password) }
        Spacer(modifier = modifier.padding(10.dp))
        PasswordField(password) { viewModel.onLoginChange(email, it) }
        Spacer(modifier = modifier.padding(8.dp))
        ForgotPassword(modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = modifier.padding(16.dp))
        LoginButton(enableButton) {
            rememberCoroutine.launch {
                viewModel.onLogin()
            }
        }
        Spacer(modifier = modifier.padding(5.dp))
        Text(
            "ó",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 18.sp,
            color = Color(0xFFFF5816)
        )
        Spacer(modifier = modifier.padding(5.dp))
        GuestButton()
        Spacer(modifier = modifier.padding(5.dp))
        GoogleButton(modifier, viewModel)
        Spacer(modifier = modifier.padding(16.dp))
        LogUpText(modifier)
    }
}

@Composable
fun GoogleButton(modifier: Modifier, viewModel: LoginViewModel) {
    Button(
        onClick = {
            viewModel.sigInWithGoogle()
        },
        shape = RoundedCornerShape(0.dp),
        colors = ButtonColors(
            contentColor = Color(0xFFFFFFFF),
            disabledContentColor = Color(0xFF111111),
            containerColor = Color(0xFF4889f4),
            disabledContainerColor = Color(0xFFeeeeee)
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.icons8_google_48),
                contentDescription = "google icon",
                modifier = modifier
                    .height(40.dp)
                    .padding(horizontal = 5.dp)
            )
            Text(
                text = "Iniciar con Google"
            )
        }
    }
}

@Composable
fun ModalSessionInfo(modifier: Modifier, msgLoginInfo: String, closeModal: () -> Unit) {
    Dialog(
        onDismissRequest = { closeModal() },
    ) {
        Column(
            modifier = modifier
                .size(250.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFFFFFF)),
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color(0xFFFF9090))
                    .padding(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = "error session",
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .height(70.dp)
                        .width(70.dp)
                )
            }
            Column {
                Text(
                    text = msgLoginInfo,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 20.dp)
                )

                Button(
                    onClick = { closeModal() },
                    modifier = modifier
                        .fillMaxWidth()
                        .absoluteOffset(y = 10.dp),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonColors(
                        containerColor = Color(0xFFFF9090),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFFF9090),
                        disabledContentColor = Color.White
                    )
                ) {
                    Text(text = "Cerrar")
                }
            }

        }
    }
}

@Composable
fun GuestButton() {
    Button(
        colors = ButtonColors(
            contentColor = Color(0xFF7A1CFF),
            containerColor = Color.Transparent,
            disabledContentColor = Color(0xFF7A1CFF),
            disabledContainerColor = Color.Transparent
        ),
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(0.dp),
        border = BorderStroke(1.dp, Color(0xFF7A1CFF))
    ) {
        Text(text = "Iniciar Como Invitado")
    }
}

@Composable
fun LoginButton(buttonEnable: Boolean, onLogin: () -> Unit) {
    Button(
        onClick = { onLogin() },
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        //.aspectRatio(ratio = 0.1f, matchHeightConstraintsFirst = false),
        colors = ButtonColors(
            containerColor = Color(0xFF7A1CFF),
            contentColor = Color(0xFFFFFFFF),
            disabledContentColor = Color(0xFF646464),
            disabledContainerColor = Color(0xFFEEEEEE),
        ),
        enabled = buttonEnable
    ) {
        Text(text = "Iniciar Sesión")
    }
}


@Composable
fun LogUpText(modifier: Modifier) {
    Text(
        text = "No tienes cuenta? Registrate",
        modifier = modifier
            .fillMaxWidth()
            .clickable { },
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        color = Color(0xFFFF5816),

        )
}


@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "¿Olvidaste la contraseña?",
        modifier = modifier
            .fillMaxWidth()
            .clickable { },
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        color = Color(0xFFFF5816),

        )
}

@Composable
fun EmailField(email: String, onTextFieldChange: (String) -> Unit) {
    //var email by remember { mutableStateOf("") } //basic mode
    TextField(
        value = email,
        onValueChange = { onTextFieldChange(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email o usuario") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFFFFFFF),
            focusedContainerColor = Color(0xFFFFFFFF),
            unfocusedTextColor = Color(0xFF111111),
            focusedTextColor = Color(0xFF111111),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedLabelColor = Color(0xFF111111)
        ),

        )
}

@Composable
fun PasswordField(password: String, onTextFieldChange: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = { onTextFieldChange(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Constraseña") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        visualTransformation = PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFFFFFFF),
            focusedContainerColor = Color(0xFFFFFFFF),
            unfocusedTextColor = Color(0xFF111111),
            focusedTextColor = Color(0xFF111111),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedLabelColor = Color(0xFF111111)
        ),
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.render_icon_beslwa),
        contentDescription = "Logo App",
        modifier = modifier
    )
}