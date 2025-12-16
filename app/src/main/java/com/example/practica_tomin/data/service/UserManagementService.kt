package com.example.compose_supabase.data.service

import com.example.compose_supabase.data.model.SignInRequest
import com.example.compose_supabase.data.model.SignUpRequest
import com.example.compose_supabase.data.model.SignUpResponse
import com.example.compose_supabase.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

//const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNvdGN6d25rbGptZWRkZWJ0eHJhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwNjQsImV4cCI6MjA3NTMwMzA2NH0.OBWI0Kujg_IP1cvVOqOkp-qLakdUh0qpvWgXOsycArY"
const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRzdG96d3Rub3VvYXBneXFmcmx0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTkyNjA3ODUsImV4cCI6MjA3NDgzNjc4NX0.isfbU6tvNFBO2-WtFXd1HkUKMYK3AK-147PKLZGAUrs"

interface UserManagementService {
    // Sign UP
    @Headers("apikey: $SUPABASE_KEY")
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpRequest>

    // Sign IN
    @Headers("apikey: $SUPABASE_KEY")
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInRequest>
}