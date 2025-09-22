package com.caicoders.pruebasatet.view.login

import android.content.Context
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.caicoders.pruebasatet.R
import com.caicoders.pruebasatet.ui.theme.Naranja
import com.caicoders.pruebasatet.ui.theme.Purpura
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.math.log

@Composable
fun LoginScreen(navController: NavController, context: Context) {
    val loginModel = remember { LoginModel() }
    Login(loginModel, navController, context)
}

@Composable
fun Login(loginModel: LoginModel, navController: NavController, context: Context) {
    val scope = rememberCoroutineScope()
    val buttonColorCrear = remember { Animatable(Color.Black) }
    val buttonColorIniciar = remember { Animatable(Color.Black) }
    var showRegisterDialog = remember { mutableStateOf(false) }
    val showRecoveryPasswordDialog = loginModel.showResetPasswordDialog.collectAsState()
    val showLoginDialog = loginModel.showLoginDialog.collectAsState()
    val user = loginModel.username.collectAsState()
    val pass = loginModel.password.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondoatet),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .fillMaxHeight(0.6f)
                .clip(RoundedCornerShape(30.dp))
                .background(Purpura.copy(alpha = 0.81f))
                .align(Alignment.Center)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Usuario",
                    fontSize = 40.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    value = user.value,
                    onValueChange = { loginModel.setUsername(it) },
                    singleLine = true,
                    placeholder = { Text("Introduce tu usuario") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Text(
                    text = "Contraseña",
                    fontSize = 40.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    value = pass.value,
                    onValueChange = { loginModel.setPassword(it) },
                    placeholder = { Text("Introduce tu contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f) // 80% del ancho del Box
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                buttonColorCrear.animateTo(
                                    targetValue = Naranja,
                                    animationSpec = tween(durationMillis = 500)
                                )

                                showRegisterDialog.value = true

                                buttonColorCrear.animateTo(
                                    targetValue = Color.Black,
                                    animationSpec = tween(durationMillis = 500)
                                )
                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .weight(0.6f),
                        colors = buttonColors(
                            containerColor = buttonColorCrear.value,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = "Icono de cuenta",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = "Crear cuenta",
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.fillMaxWidth(0.1f))

                    Button(
                        onClick = {
                            scope.launch {
                                buttonColorIniciar.animateTo(
                                    targetValue = Naranja,
                                    animationSpec = tween(durationMillis = 500)
                                )

                                loginModel.setShowLoginDialog(true)

                                buttonColorIniciar.animateTo(
                                    targetValue = Color.Black,
                                    animationSpec = tween(durationMillis = 500)
                                )
                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .weight(0.6f),
                        colors = buttonColors(
                            containerColor = buttonColorIniciar.value, contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Iniciar Sesión", fontSize = 20.sp, fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        SnackbarHost(
            hostState = loginModel.snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (showRegisterDialog.value) {
            loginModel.RegisterUserDialog(showRegisterDialog, loginModel, scope)
        }

        if (showLoginDialog.value) {
            loginModel.CheckLogin(user, pass, loginModel.snackbarHostState, scope, navController)
        }

        if (showRecoveryPasswordDialog.value) {
            ResetPasswordDialog(
                loginModel,
                showRecoveryPasswordDialog.value,
                user.value,
                FirebaseAuth.getInstance()
            )
        }

    }
}

@Composable
fun ResetPasswordDialog(
    loginModel: LoginModel,
    showDialog: Boolean,
    email: String,
    auth: FirebaseAuth
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { loginModel.setShowResetPasswordDialog(false) },
            title = { Text("Recuperar contraseña") },
            text = {
                Text("Se enviará un correo electrónico a $email con instrucciones para restablecer tu contraseña.")
            },
            confirmButton = {
                Button(onClick = {
                    auth.sendPasswordResetEmail(email)
                    loginModel.setShowResetPasswordDialog(false)
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { loginModel.setShowResetPasswordDialog(false) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


