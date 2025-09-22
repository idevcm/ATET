package com.caicoders.pruebasatet.view.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.caicoders.pruebasatet.nav.Graft
import com.caicoders.pruebasatet.ui.theme.Purpura
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginModel : ViewModel() {
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    val _showResetPasswordDialog = MutableStateFlow(false)
    val showResetPasswordDialog = _showResetPasswordDialog.asStateFlow()

    fun setShowResetPasswordDialog(newValue: Boolean) {
        _showResetPasswordDialog.value = newValue
    }

    private val _showLoginDialog = MutableStateFlow(false)
    val showLoginDialog = _showLoginDialog.asStateFlow()

    fun setShowLoginDialog(newValue: Boolean) {
        _showLoginDialog.value = newValue
    }

    val snackbarHostState = SnackbarHostState()

    fun setUsername(newText: String) {
        _username.value = newText
    }

    fun setPassword(newText: String) {
        _password.value = newText
    }

    fun runLogin(navController: NavController) {
        if (username.value.trim() == "" && password.value.trim() == "") {
            viewModelScope.launch {
                navController.navigate(Graft.Home.route)
            }
        } else {
            viewModelScope.launch {
                snackbarHostState.showSnackbar("Login failed")
            }
        }
    }

    @Composable
    fun RegisterUserDialog(
        showDialog: MutableState<Boolean>,
        loginModel: LoginModel,
        scope: CoroutineScope
    ) {
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = "Crear cuenta") },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val password = remember { mutableStateOf("") }
                        val email = remember { mutableStateOf("") }

                        OutlinedTextField(
                            value = email.value,
                            onValueChange = { newValue -> email.value = newValue },
                            label = { Text("Correo electrónico") }
                        )

                        OutlinedTextField(
                            value = password.value,
                            onValueChange = { newValue -> password.value = newValue },
                            label = { Text("Contraseña") },
                            visualTransformation = PasswordVisualTransformation()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                onUserRegistered(
                                    password.value.trim(),
                                    email.value.trim(),
                                    loginModel.snackbarHostState,
                                    scope
                                )
                                showDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(Purpura)
                        ) {
                            Text(
                                "Quiero Registrarme",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }
                    }
                },
                confirmButton = { }
            )
        }
    }


    fun CheckLogin(
        user: State<String>,
        pass: State<String>,
        snackbarHostState: SnackbarHostState,
        scope: CoroutineScope,
        navController: NavController
    ) {
        val auth = FirebaseAuth.getInstance()

        if (user.value.isNullOrEmpty() || pass.value.isNullOrEmpty()) {
            scope.launch {
                setShowLoginDialog(false)
                snackbarHostState.showSnackbar("Introduce tu email y tu contraseña")
            }
        } else {
            auth.signInWithEmailAndPassword(user.value, pass.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        scope.launch {
                            setShowLoginDialog(false)
                            navController.navigate(Graft.Home.route)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            scope.launch {
                                setShowResetPasswordDialog(true)
                            }
                        }

                        else -> {
                            setShowLoginDialog(false)
                        }
                    }
                }
        }
    }

    fun onUserRegistered(
        password: String,
        email: String,
        snackbarHostState: SnackbarHostState,
        scope: CoroutineScope
    ) {
        val auth = FirebaseAuth.getInstance()

        if (password.length < 6) {
            scope.launch {
                snackbarHostState.showSnackbar("La contraseña debe tener al menos 6 caracteres")
            }
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Usuario creado")
                    }
                }
            }
            .addOnFailureListener { e ->
                when (e) {
                    is FirebaseAuthUserCollisionException -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("El usuario ya está registrado")
                        }
                    }

                    is FirebaseAuthInvalidCredentialsException -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("El email introducido es inválido")
                        }
                    }

                    else -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Error al registrar el usuario: ${e.message}")
                        }
                    }
                }
            }
    }

}