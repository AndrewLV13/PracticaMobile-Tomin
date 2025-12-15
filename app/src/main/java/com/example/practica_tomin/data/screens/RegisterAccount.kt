package com.example.practice_mobile.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

// Создание экрана регистрации
@Composable
fun RegisterAccount(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onRegisterClick: (String, String, String) -> Unit = { _, _, _ -> }
) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val agreementChecked = remember { mutableStateOf(false) }


    val isFormValid =
        name.value.isNotBlank() &&
                email.value.isNotBlank() &&
                password.value.isNotBlank() &&
                agreementChecked.value

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Верхняя панель с кнопкой Back
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = "Назад",
                        tint = Color.Unspecified
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Регистрация",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 32.sp
            )
            Text(
                text = "Заполните Свои данные",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF707B81),
                fontSize = 16.sp
            )

            Spacer(Modifier.height(32.dp))

            // Имя
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Ваше имя",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,

                )
            }
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                placeholder = { Text("xxxxxxxx") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(16.dp))

// Email
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp
                )
            }
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                placeholder = { Text("xyz@gmail.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Пароль",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp
                )
            }
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                visualTransformation = if (passwordVisible.value)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible.value)
                                    R.drawable.eye_open     // иконка «глаз закрыт»
                                else
                                    R.drawable.union  // иконка «глаз открыт»
                            ),
                            contentDescription = if (passwordVisible.value)
                                "Скрыть пароль"
                            else
                                "Показать пароль"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(
                        id = if (agreementChecked.value)
                            R.drawable.frame_1000000814__1_
                        else
                            R.drawable.policy_check
                    ),
                    contentDescription = if (agreementChecked.value)
                        "Отмечено"
                    else
                        "Не отмечено",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            agreementChecked.value = !agreementChecked.value
                        }
                )

                Text(
                    fontSize = 18.sp,
                    text = "Даю согласие на обработку\nперсональных данных",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF48B2E7),
                    contentColor = Color.White,

                    disabledContainerColor = Color(0xFF2B6B8B),
                    disabledContentColor = Color.White
                ),
                onClick = { /* TODO: регистрация */ },
                enabled = agreementChecked.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Зарегистрироваться")
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Есть аккаунт? ")
                Text(
                    text = "Войти",
                    color = Color(0xFF000000),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { /* TODO: навигация к логину */ }
                )
            }
        }
    }
}

@Preview
@Composable
private fun RegisterAccountPreview() {
    MaterialTheme {
        RegisterAccount()
    }
}
