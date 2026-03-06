package org.russel.komandoandroid.data.auth

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val username = sessionManager.getUsername()
        val password = sessionManager.getPassword()

        val requestBuilder = chain.request().newBuilder()

        if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
            val credentials = Credentials.basic(username, password)
            requestBuilder.header("Authorization", credentials)
        }

        return chain.proceed(requestBuilder.build())
    }
}