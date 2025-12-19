package com.example.compose_supabase.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.practica_tomin.R

import com.example.up_piatnitskii.data.Model.CategoryEntity
import com.example.up_piatnitskii.data.Model.CategoryResponse

@Dao
interface UserDAO {
    @Query("SELECT * FROM users")
    suspend fun getUser(): List<User>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM users WHERE uid = :userId")
    suspend fun deleteUserById(userId: Long)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

}