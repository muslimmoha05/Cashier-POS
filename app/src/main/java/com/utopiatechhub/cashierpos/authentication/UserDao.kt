package com.utopiatechhub.cashierpos.authentication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerUser(user: User)

    @Query("SELECT COUNT(*) FROM users WHERE LOWER(username) = LOWER(:username) AND role = :role")
    suspend fun isUsernameTaken(username: String, role: String): Int

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun loginUser(username: String, password: String): User?
}