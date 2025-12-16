package com.example.practice_mobile.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica_tomin.R
import com.example.practica_tomin.ui.theme.PracticaTominTheme

private val emailRegex = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")

@Composable
fun SignInScreen(
    viewModel: SignInViewModel? = null,
    onSignInSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var isEmptyError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Требование №12: Валидация email в реальном времени
    LaunchedEffect(email) {
        emailError = email.isNotBlank() && !emailRegex.matches(email)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = "Привет!",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 32.sp
            )
            Text(
                text = "Заполните свои данные",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(32.dp))

            // Email
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "Email", style = MaterialTheme.typography.bodyMedium)
            }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    isEmptyError = false
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("xyz@gmail.com") },
                isError = emailError || isEmptyError,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F3F3),
                    unfocusedContainerColor = Color(0xFFF3F3F3),
                    errorContainerColor = Color(0xFFFFE6E6)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Пароль
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "Пароль", style = MaterialTheme.typography.bodyMedium)
            }

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isEmptyError = false
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F3F3),
                    unfocusedContainerColor = Color(0xFFF3F3F3)
                ),
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else PasswordVisualTransformation(),
                // Требование №14: Возможность отображения пароля
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible)
                                    R.drawable.eye_open
                                else
                                    R.drawable.union
                            ),
                            contentDescription = if (passwordVisible)
                                "Скрыть пароль"
                            else
                                "Показать пароль",
                            tint = Color.Unspecified
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            // Ссылка "Восстановить"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Восстановить",
                    fontSize = 14.sp,
                    color = Color(0xFF48B2E7),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable { onForgotPasswordClick() } // Требование №16
                )
            }

            // Кнопка входа
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF48B2E7),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF2B6B8B)
                ),
                onClick = {
                    // Требования №13: Проверка пустоты полей
                    if (email.isBlank() || password.isBlank()) {
                        isEmptyError = true
                        errorMessage = "Заполните все поля"
                        showErrorDialog = true
                        return@Button
                    }

                    // Требование №12: Проверка email
                    if (emailError) {
                        errorMessage = "Некорректный формат email"
                        showErrorDialog = true
                        return@Button
                    }

                    // TODO: Требование №8: Отправка запроса на сервер
                    isLoading = true
                    // viewModel?.signIn(email, password) { success, error ->
                    //     isLoading = false
                    //     if (success) onSignInSuccess()
                    //     else {
                    //         errorMessage = error ?: "Ошибка сервера"
                    //         showErrorDialog = true
                    //     }
                    // }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank() && !emailError,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Войти")
                }
            }

            Spacer(Modifier.weight(1f))

            // Ссылка "Создать пользователя"
            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Вы здесь впервые? ")
                Text(
                    text = "Создать",
                    color = Color(0xFF000000),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onSignUpClick() } // Требование №17
                )
            }
        }
    }

    // Требования №9, №31: Диалог ошибок
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Ошибка") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    PracticaTominTheme {
        SignInScreen()
    }
}
