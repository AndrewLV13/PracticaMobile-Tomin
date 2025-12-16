package com.example.compose_supabase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val email: String,
    val password: String
)


//data class User(
    //val email: String,
   // val password: String
//)
