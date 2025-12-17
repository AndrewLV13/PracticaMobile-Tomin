package com.example.practice_mobile.ui.screen

import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_tomin.R
import com.example.practica_tomin.ui.theme.PracticaTominTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


// ViewModel как в первом примере
class ForgotPasswordViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _isEmailValid = MutableStateFlow(false)
    val isEmailValid: StateFlow<Boolean> = _isEmailValid.asStateFlow()

    private val _passwordRecoveryState = MutableStateFlow<PasswordRecoveryState>(PasswordRecoveryState.Idle)
    val passwordRecoveryState: StateFlow<PasswordRecoveryState> = _passwordRecoveryState.asStateFlow()

    private val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        _isEmailValid.value = newEmail.isNotEmpty() && emailRegex.matches(newEmail)
    }

    fun recoverPassword() {
        if (_isEmailValid.value) {
            _passwordRecoveryState.update { PasswordRecoveryState.Loading }


            kotlinx.coroutines.MainScope().launch {
                kotlinx.coroutines.delay(2000)
                _passwordRecoveryState.update { PasswordRecoveryState.Success("Код отправлен!") }
            }
        }
    }
}

// Состояния как в первом примере
sealed class PasswordRecoveryState {
    object Idle : PasswordRecoveryState()
    object Loading : PasswordRecoveryState()
    data class Success(val message: String = "Код отправлен!") : PasswordRecoveryState()
    data class Error(val message: String) : PasswordRecoveryState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onOTPClick: (email: String) -> Unit = {},
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val uiState by viewModel.passwordRecoveryState.collectAsState()
    val email by viewModel.email.collectAsState()
    val isEmailValid by viewModel.isEmailValid.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Состояние для отображения AlertDialog
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Обработка состояний как в первом примере
    LaunchedEffect(uiState) {
        when (uiState) {
            is PasswordRecoveryState.Success -> {
                showSuccessDialog = true
            }
            is PasswordRecoveryState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        (uiState as PasswordRecoveryState.Error).message,
                        withDismissAction = true
                    )
                }
            }
            else -> {}
        }
    }

    // Диалог успеха как в первом примере
    if (showSuccessDialog && email.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Код отправлен!") },
            text = { Text("Проверьте вашу почту. Мы отправили вам код для сброса пароля.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        onOTPClick(email)
                    }
                ) {
                    Text("Продолжить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Круглая кнопка "назад"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedButton(
                    onClick = onBackClick,
                    shape = CircleShape,
                    modifier = Modifier.size(width = 44.dp, height = 44.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Назад",
                        tint = Color.Black
                    )
                }
            }

            // Заголовок
            Text(
                text = "Забыл пароль",
                fontSize = 32.sp,
                fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Введите свою учетную запись\nдля сброса пароля",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 54.dp)
            )

            // Поле email
            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.updateEmail(it) },
                placeholder = {
                    Text(
                        "xyz@gmail.com",
                        color = Color(0xFF999999)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = if (email.isNotEmpty() && !isEmailValid)
                        Color.Red
                    else
                        Color(0xFFE0E0E0),
                    focusedBorderColor = if (isEmailValid)
                        Color(0xFF48B2E7)
                    else
                        Color.Red,
                    cursorColor = Color(0xFF48B2E7),
                    focusedContainerColor = Color(0xFFF3F3F3),
                    unfocusedContainerColor = Color(0xFFF3F3F3)
                ),
                singleLine = true,
                isError = email.isNotEmpty() && !isEmailValid,
                supportingText = {
                    if (email.isNotEmpty() && !isEmailValid) {
                        Text(
                            text = "Введите корректный email адрес",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка отправки
            Button(
                onClick = { viewModel.recoverPassword() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isEmailValid && uiState !is PasswordRecoveryState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEmailValid && uiState !is PasswordRecoveryState.Loading)
                        Color(0xFF48B2E7)
                    else
                        Color(0xFF2B6B8B),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState is PasswordRecoveryState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Отправить", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ForgotPasswordScreenPreview() {
    PracticaTominTheme {
        ForgotPassword(
            onBackClick = {},
            onOTPClick = {}
        )
    }
}