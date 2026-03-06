package org.russel.komandoandroid.data.remote

import org.russel.komandoandroid.data.model.request.LoginRequest
import org.russel.komandoandroid.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}