package com.utopiatechhub.cashierpos.authentication

import android.util.Log

class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: User): Boolean {
        return try {
            userDao.registerUser(user)
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error registering user: ${e.message}")
            false
        }
    }

    suspend fun isUsernameTaken(username: String, role: String): Boolean {
        return userDao.isUsernameTaken(username, role) > 0
    }

    suspend fun loginUser(username: String, password: String): User? {
        return userDao.loginUser(username, password)
    }
}