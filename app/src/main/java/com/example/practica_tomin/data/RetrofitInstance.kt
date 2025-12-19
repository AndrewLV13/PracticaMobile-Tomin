package com.example.practica_tomin.data

import android.util.Log
import com.example.compose_supabase.data.service.UserManagementService
import com.example.practica_tomin.data.service.CategoriesService
import com.example.practica_tomin.data.service.ProductsService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    // Ваши Supabase данные
    private const val SUPABASE_URL = "https://sfumskfqxphsvrndgoni.supabase.co"
    private const val REST_URL = "$SUPABASE_URL/rest/v1/"

    // Прокси настройки (как в shoeshop)
    private const val PROXY_HOST = "10.207.106.77"
    private const val PROXY_PORT = 3128
    private const val USE_PROXY = true

    // API ключ (вставьте свой из Supabase Dashboard)
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNmdW1za2ZxeHBoc3ZybmRnb25pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU4MzM1MDMsImV4cCI6MjA4MTQwOTUwM30.3d9Ea7su3iGJMy1pyocdzfn6-Vf1OspsWw8WrCZa5qE"

    // Основной интерцептор (как в shoeshop)
    private val supabaseInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("apikey", SUPABASE_ANON_KEY)
            .header("Authorization", "Bearer $SUPABASE_ANON_KEY")
            .header("Content-Type", "application/json")
            .method(original.method, original.body)

        // Для auth endpoints только apikey
        val url = original.url.toString()
        if (url.contains("/auth/")) {
            requestBuilder.removeHeader("Authorization")
        }

        val request = requestBuilder.build()
        Log.d("RetrofitInstance", "Запрос: ${request.url} | apikey=${SUPABASE_ANON_KEY.take(10)}...")
        chain.proceed(request)
    }

    // Логирование
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Клиент с прокси и таймаутами (как в shoeshop)
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .apply {
                if (USE_PROXY) {
                    proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(PROXY_HOST, PROXY_PORT)))
                }
            }
            .addInterceptor(supabaseInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit для Auth
    private val retrofitAuth = Retrofit.Builder()
        .baseUrl(SUPABASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userManagementService = retrofitAuth.create(UserManagementService::class.java)

    // Retrofit для REST API
    private val retrofitRest = Retrofit.Builder()
        .baseUrl(REST_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productsService = retrofitRest.create(ProductsService::class.java)
    val categoriesService = retrofitRest.create(CategoriesService::class.java)
    val favouriteService: FavouriteService = com.example.practica_tomin.data.RetrofitInstance.retrofitRest.create(FavouriteService::class.java)

}