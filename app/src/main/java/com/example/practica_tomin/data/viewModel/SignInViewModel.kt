package com.example.practica_tomin.data.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.compose_supabase.data.model.SignInRequest
import com.example.compose_supabase.data.model.UserDAO
import com.example.practica_tomin.data.RetrofitInstance
import kotlinx.coroutines.launch
import kotlin.let

class SignInViewModel(private val userDAO: UserDAO): ViewModel() {
    var email: String = ""
    var password: String = ""

    fun signIn(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val signInData = SignInRequest(email, password)
            val response = RetrofitInstance.userManagementService.signIn(signInData)

            if (response.isSuccessful){
                response.body()?.let {
                    Log.v("SignIn", "Пользователь успешно авторизован: ${it.email}")
                    onSuccess()
                }
            }
            else {
                Log.e("SignIn", "HTTP ошибка: ${response.code()} - ${response.message()}")

                val errorMessage = when
                                           (response.code())
                {
                    400 -> "Неверный email или пароль"
                    422 -> "Некорректные данные"
                    500 -> "Ошибка сервера"
                    else -> "Ошибка входа: ${response.message()}"
                }

                val errorBody = response.errorBody()?.string()
                Log.e("SignIn", "Тело ошибки: $errorBody")
                onError(errorMessage)
            }
        }
    }
}
