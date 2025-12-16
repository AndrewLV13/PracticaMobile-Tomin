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
const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNmdW1za2ZxeHBoc3ZybmRnb25pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU4MzM1MDMsImV4cCI6MjA4MTQwOTUwM30.3d9Ea7su3iGJMy1pyocdzfn6-Vf1OspsWw8WrCZa5qE"

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