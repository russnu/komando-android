package org.russel.komandoandroid.data.repository

import org.russel.komandoandroid.data.model.User
import org.russel.komandoandroid.data.remote.UserApi

class UserRepository(private val api: UserApi) {
    //--------------------------------------------------------------------------------------//
    suspend fun getAllUsers(): List<User> {
        return api.getAll()
    }

}