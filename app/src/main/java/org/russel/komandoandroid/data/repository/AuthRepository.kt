package org.russel.komandoandroid.data.repository

import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.model.request.LoginRequest
import org.russel.komandoandroid.data.remote.AuthApi

class AuthRepository(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) {

    suspend fun login(username: String, password: String): Result<String> {
        val response = api.login(LoginRequest(username, password))

        if (response.isSuccessful) {
            val body = response.body()!!
            val userId = body.userId ?: 0
            val fullname = body.fullname ?: "Unknown"
            val username = body.username ?: "Unknown"

            sessionManager.saveCredentials(userId, fullname, username, password)

            return Result.success("Login successful")
        }

        return Result.failure(Exception("Invalid credentials"))
    }
}