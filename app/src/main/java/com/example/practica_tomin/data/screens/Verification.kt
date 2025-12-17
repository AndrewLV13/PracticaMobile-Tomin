package com.example.practice_mobile.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.Surface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_tomin.ui.theme.PracticaTominTheme

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val emailRegex = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")

// ViewModel как в первом примере
class OtpVerificationViewModel : ViewModel() {
    private val _verificationState = MutableStateFlow<VerificationState>(VerificationState.Idle)
    val verificationState: StateFlow<VerificationState> = _verificationState.asStateFlow()

    private val _resetToken = MutableStateFlow("")
    val resetToken: StateFlow<String> = _resetToken.asStateFlow()

    fun verifyOtp(email: String, otpCode: String) {
        _verificationState.update { VerificationState.Loading }

        kotlinx.coroutines.MainScope().launch {
            delay(2000) // Имитация сети
            if (otpCode == "123456") { // Тестовый код
                _resetToken.value = "test_reset_token_123"
                _verificationState.value = VerificationState.Success("Код подтвержден!")
            } else {
                _verificationState.value = VerificationState.Error("Неверный код")
            }
        }
    }

    fun resetState() {
        _verificationState.value = VerificationState.Idle
    }

    fun getResetToken(): String = _resetToken.value
}

// Состояния как в первом примере
sealed class VerificationState {
    object Idle : VerificationState()
    object Loading : VerificationState()
    data class Success(val message: String) : VerificationState()
    data class Error(val message: String) : VerificationState()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Verification(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onResetPasswordClick: (resetToken: String) -> Unit = {},
    viewModel: OtpVerificationViewModel = viewModel()
) {
    var otpCode by remember { mutableStateOf("") }
    val context = LocalContext.current
    val verificationState by viewModel.verificationState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    var timeLeft by remember { mutableStateOf(30) }
    var isTimerRunning by remember { mutableStateOf(true) }

    // Focus Requesters для каждого поля
    val focusRequesters = remember {
        List(6) { FocusRequester() }
    }

    // Таймер
    LaunchedEffect(isTimerRunning) {
        if (!isTimerRunning) return@LaunchedEffect
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        isTimerRunning = false
    }

    // Авто-проверка при вводе 6 цифр
    LaunchedEffect(otpCode) {
        if (otpCode.length == 6) {
            keyboardController?.hide()
            focusManager.clearFocus()
            viewModel.verifyOtp("user@example.com", otpCode)
        }
    }

    // Обработка состояний
    LaunchedEffect(verificationState) {
        when (verificationState) {
            is VerificationState.Success -> {
                val resetToken = viewModel.getResetToken()
                if (resetToken.isNotEmpty()) {
                    onResetPasswordClick(resetToken)
                }
                viewModel.resetState()
            }
            is VerificationState.Error -> {
                showToast(context, (verificationState as VerificationState.Error).message)
                otpCode = ""
                scope.launch {
                    focusRequesters[0].requestFocus()
                }
                viewModel.resetState()
            }
            else -> {}
        }
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
            // Кнопка назад
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "← Назад",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.clickable { onBackClick() }
                )
            }

            Text(
                text = "OTP Проверка",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Пожалуйста, проверьте свою\nэлектронную почту, чтобы увидеть код\nподтверждения",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 54.dp)
            )

            // OTP поля
            Column {
                Text("OTP Код", fontSize = 16.sp, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(6) { index ->
                        OTPDigitBox(
                            index = index,
                            otpCode = otpCode,
                            onValueChange = { newValue ->
                                handleOtpInput(
                                    index = index,
                                    newValue = newValue,
                                    currentOtp = otpCode,
                                    onOtpChange = { otpCode = it },
                                    focusRequesters = focusRequesters
                                )
                            },
                            focusRequester = focusRequesters[index],
                            modifier = Modifier
                                .size(56.dp)
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!isTimerRunning) {
                        Text(
                            text = "Отправить заново",
                            color = Color(0xFF48B2E7),
                            fontSize = 14.sp,
                            modifier = Modifier.clickable {
                                timeLeft = 30
                                isTimerRunning = true
                            }
                        )
                    }
                    Text(
                        text = "00:${timeLeft.toString().padStart(2, '0')}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Индикатор загрузки
            if (verificationState is VerificationState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun OTPDigitBox(
    index: Int,
    otpCode: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val currentChar = if (index < otpCode.length) otpCode[index].toString() else ""

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                color = if (isFocused) Color(0xFF48B2E7) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color(0xFFF3F3F3))
            .clickable { focusRequester.requestFocus() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = currentChar,
            onValueChange = { newValue ->
                if (newValue.length <= 1 && (newValue.isEmpty() || newValue.all { it.isDigit() })) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .focusable()
                .onFocusChanged { isFocused = it.isFocused },
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            cursorBrush = SolidColor(Color(0xFF48B2E7)),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (currentChar.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .height(2.dp)
                                .background(Color.Gray.copy(alpha = 0.3f))
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun handleOtpInput(
    index: Int,
    newValue: String,
    currentOtp: String,
    onOtpChange: (String) -> Unit,
    focusRequesters: List<FocusRequester>
) {
    val newOtp = StringBuilder(currentOtp)

    if (newValue.isNotEmpty()) {
        if (index < newOtp.length) {
            newOtp[index] = newValue[0]
        } else {
            while (newOtp.length < index) newOtp.append(' ')
            newOtp.append(newValue[0])
        }
        onOtpChange(newOtp.toString())
        if (index < 5) focusRequesters[index + 1].requestFocus()
    } else {
        if (index < newOtp.length) {
            newOtp.deleteCharAt(index)
            onOtpChange(newOtp.toString())
        }
        if (index > 0) focusRequesters[index - 1].requestFocus()
    }
}

private fun showToast(context: android.content.Context, message: String) {
    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerificationScreenPreview() {
    PracticaTominTheme {
        Verification(
            onBackClick = {},
            onResetPasswordClick = {}
        )
    }
}