package com.example.compose_supabase.data.model

import android.app.Application
import androidx.room.Room
import com.example.compose_supabase.data.model.AppDatabase

class StudentApplication: Application() {

    companion object{
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "user_database"
        ).build()
    }
}