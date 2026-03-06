package org.russel.komandoandroid.data.remote

import org.russel.komandoandroid.data.model.User
import retrofit2.http.GET

interface UserApi {
    @GET("/api/user")
    suspend fun getAll(): List<User>
}