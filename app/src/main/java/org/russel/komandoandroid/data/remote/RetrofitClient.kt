package org.russel.komandoandroid.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import org.russel.komandoandroid.data.auth.AuthInterceptor
import org.russel.komandoandroid.data.auth.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    private var retrofit: Retrofit? = null
    fun create(context: Context): Retrofit {

        val sessionManager = SessionManager(context)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //===============================================================================//
    fun getInstance(context: Context): Retrofit {
        return retrofit ?: create(context.applicationContext).also {
            retrofit = it
        }
    }


    fun taskService(context: Context): TaskApi {
        return getInstance(context).create(TaskApi::class.java)
    }

    fun authService(context: Context): AuthApi {
        return getInstance(context).create(AuthApi::class.java)
    }

    fun groupService(context: Context): GroupApi {
        return getInstance(context).create(GroupApi::class.java)
    }

    fun deviceService(context: Context): DeviceApi {
        return getInstance(context).create(DeviceApi::class.java)
    }

    fun userService(context: Context): UserApi {
        return getInstance(context).create(UserApi::class.java)
    }
}